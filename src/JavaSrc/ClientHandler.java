package JavaSrc;// Java implementation of  JavaSrc.Server side
// It contains two classes : JavaSrc.Server and JavaSrc.ClientHandler
// Save file as JavaSrc.Server.java

import JavaSrc.Data.PatientInfo;
import JavaSrc.Data.Roles;
import JavaSrc.Data.SQLHandler;
import JavaSrc.Data.UserInfo;
import JavaSrc.Exceptions.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;

// JavaSrc.ClientHandler class
class ClientHandler extends Thread
{
	private final DataInputStream dis;
	private final DataOutputStream dos;
	private final Socket s;
	private Roles usrRole;
	private int usrID;
	private String usrname;
	
	// Constructor
	public ClientHandler(Socket _s, DataInputStream _dis, DataOutputStream _dos)
	{
		this.s = _s;
		this.dis = _dis;
		this.dos = _dos;
		logout();
	}
	
	@Override
	public void run()
	{
		// Set the socket timeout for ten minutes
		try
		{
			s.setSoTimeout(10 * 60 * 1000);
			//ten second socket timeout for testing purposes
			//s.setSoTimeout(10000);
		}
		catch(SocketException ignored) {}
		
		String received;
		String toreturn;
		while(true)
		{
			try
			{
				// Ask user what he wants
				
				// receive the answer from client
				received = dis.readUTF();
				System.out.println("Received: \"" + received + "\""); //TODO remove print
				String[] cmd = received.split(" ");
				
				// write on output stream based on the
				// answer from the client
				try
				{
					switch(cmd[0].toLowerCase())
					{
						case "login" -> {
							if(cmd.length < 3)
							{
								Util.error("Login attempted with < 3 args");
								throw new RPMError(ErrorCodes.INVALID_LOGIN, "Login message attempted with " + cmd.length + " args, needs 3 args.",
										false);
							}
							Connection c = SQLHandler.connect();
							UserInfo info = SQLHandler.login(c, cmd[1], cmd[2]);
							if(info != null)
							{
								usrRole = info.role;
								usrID = info.id;
								Util.msg("Login Successful! Role: %s, ID: %05d".formatted(usrRole, usrID));
								dos.writeUTF("login " + info.store());
							}
							else
							{
								Util.msg("Login Failed");
								throw new RPMError(ErrorCodes.INVALID_LOGIN, "Incorrect Username or Password!", true);
							}
						}
						case "exit" -> {
							dos.writeUTF("exit");
							Util.msg("Client " + this.s + " sends exit...");
							Util.msg("Closing this connection.");
							close();
							Util.msg("Connection closed");
							return;
						}
						case "createuser" -> {
							admin();
							String[] args = received.split(" ", 2)[1].split(";");
							SQLHandler.createUser(args);
							reloadACP();
						}
						
						case "edituser" -> {
							admin();
							int id = Integer.parseInt(cmd[1]);
							UserInfo info = UserInfo.loadUser(id);
							dos.writeUTF("edituser " + info.store());
						}
						case "deleteuser" -> {
							admin();
							int id = Integer.parseInt(cmd[1]);
							if(id == usrID)
								throw new RPMError(ErrorCodes.UNKNOWN_ERROR, "Cannot delete yourself!", false);
							SQLHandler.delUser(id);
							reloadACP();
						}
						case "loadacp" -> {
							admin();
							reloadACP();
						}
						case "createpat" -> {
							physician();
							String[] args = received.split(" ", 2)[1].split(";");
							SQLHandler.createPatient(args);
							reloadPatients();
						}
						case "editpat" -> {
							physician();
							int id = Integer.parseInt(cmd[1]);
							PatientInfo info = PatientInfo.loadPatient(id);
							dos.writeUTF("editpat " + info.store());
						}
						case "deletepat" -> {
							physician();
							int id = Integer.parseInt(cmd[1]);
							SQLHandler.delPatient(id);
							reloadPatients();
						}
						case "loadpat" -> {
							logged();
							reloadPatients();
						}
//						case "discharge" {
//							nurse();
//
//							reloadPatients();
//						}
						case "echo" -> {
							dos.writeUTF(received.substring(5));
							if(cmd[1].equals("error") || cmd[1].equals("over"))
								continue;
						}
						default -> throw new RPMError();
					}
					dos.writeUTF("over");
				}
				catch(RPMException | RPMRuntimeException e)
				{
					handle(e, true);
				}
				catch(IndexOutOfBoundsException e)
				{
					Util.error(e.getMessage());
					error(ErrorCodes.UNKNOWN_ERROR, "Unknown Index-Out-Of-Bounds Error!");
				}
				catch(NumberFormatException e)
				{
					Util.trace(e);
					error(ErrorCodes.UNKNOWN_ERROR, "Unknown NumberFormat Error!");
				}
			}
			catch(InterruptedIOException iioe)
			{
				try
				{
					dos.writeUTF("exit");
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				break;
			}
			
			catch(IOException ioe)
			{
				ioe.printStackTrace();
				break;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		try
		{
			close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void reloadACP() throws IOException
	{
		dos.writeUTF("clearacp");
		for(UserInfo info : SQLHandler.loadAllUserInfos())
		{
			dos.writeUTF("acp " + info.store());
		}
		dos.writeUTF("showacp");
	}
	
	private void reloadPatients() throws IOException
	{
		dos.writeUTF("clearpat");
		for(PatientInfo info : SQLHandler.loadAllPatientInfos())
		{
			dos.writeUTF("pat " + info.store());
		}
		dos.writeUTF("showpat");
	}
	
	private void handle(Exception e, boolean forceTerminal) throws IOException
	{
		if(!(e instanceof RPMException || e instanceof RPMRuntimeException)) return;
		if(((ErrorCodable) e).getWarn() && !forceTerminal) warning(((ErrorCodable) e).getCode(), e.getMessage());
		else error(((ErrorCodable) e).getCode(), e.getMessage());
	}
	
	private void error(ErrorCodes code, String msg) throws IOException
	{
		Util.error("E#%03d: %s".formatted(code.ordinal(), msg));
		dos.writeUTF(Util.formatError(false, code, msg));
	}
	
	private void warning(ErrorCodes code, String msg) throws IOException
	{
		Util.error("W#%03d: %s".formatted(code.ordinal(), msg));
		dos.writeUTF("warn " + code.ordinal() + " " + msg);
	}
	
	private void close() throws IOException
	{
		this.dis.close();
		this.dos.close();
		this.s.close();
	}
	
	private void logout()
	{
		usrRole = null;
		usrID = -1;
	}
	
	private void admin() throws RPMError
	{
		if(usrRole != Roles.Admin)
		{
			throw new RPMError(ErrorCodes.INSUFFICIENT_PERMS, "Insufficient Permissions", false);
		}
	}
	
	private void logged() throws RPMError
	{
		if(usrRole == null)
		{
			throw new RPMError(ErrorCodes.INSUFFICIENT_PERMS, "Insufficient Permissions", false);
		}
	}
	
	private void physician() throws RPMError
	{
		if(usrRole != Roles.Physician && usrRole != Roles.Admin)
		{
			throw new RPMError(ErrorCodes.INSUFFICIENT_PERMS, "Insufficient Permissions", false);
		}
	}
	
	private void nurse() throws RPMError
	{
		if(usrRole != Roles.Nurse)
		{
			throw new RPMError(ErrorCodes.INSUFFICIENT_PERMS, "Insufficient Permissions", false);
		}
	}
	
	private void billing() throws RPMError
	{
		if(usrRole != Roles.Billing)
		{
			throw new RPMError(ErrorCodes.INSUFFICIENT_PERMS, "Insufficient Permissions", false);
		}
	}
}
