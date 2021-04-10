package JavaSrc;// Java implementation of  JavaSrc.Server side
// It contains two classes : JavaSrc.Server and JavaSrc.ClientHandler
// Save file as JavaSrc.Server.java

import JavaSrc.Data.Roles;
import JavaSrc.Data.SQLHandler;
import JavaSrc.Data.UserInfo;
import JavaSrc.Exceptions.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;

// JavaSrc.ClientHandler class
class ClientHandler extends Thread
{
	private final DataInputStream dis;
	private final DataOutputStream dos;
	private final Socket s;
	private Roles usrRole;
	private int usrID;
	
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
								Util.msg("Login Successful! Role: %s, ID: %05d".formatted(usrRole,usrID));
								dos.writeUTF("login");
							}
							else
							{
								Util.msg("Login Failed");
								throw new RPMError(ErrorCodes.INVALID_LOGIN, "Incorrect Username or Password!",true);
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
						case "createuser" -> throw new DuplicateUsernameException(); //TODO Implement user creation
						default -> throw new RPMError();
					}
					dos.writeUTF("over");
				}
				catch(RPMException|RPMRuntimeException e)
				{
					handle(e, true);
				}
				catch(IndexOutOfBoundsException e)
				{
					Util.error(e.getMessage());
					error(ErrorCodes.UNKNOWN_ERROR, "Unknown Index-Out-Of-Bounds Error!");
				}
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
	
	private void handle(Exception e, boolean forceTerminal) throws IOException
	{
		if(! (e instanceof RPMException || e instanceof RPMRuntimeException)) return;
		if(((ErrorCodable) e).getWarn() && !forceTerminal)
			warning(((ErrorCodable) e).getCode(), e.getMessage());
		else error(((ErrorCodable) e).getCode(), e.getMessage());
	}
	private void error(ErrorCodes code, String msg) throws IOException
	{
		Util.error("E#%03d: %s".formatted(code.ordinal(), msg));
		dos.writeUTF("error " + code.ordinal() + " " + msg);
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
}


// // create a timeout. start a timer, and update on every action. If it has been longer than an hour since last action, terminate connection and send message

// // Set the socket timeout for ten seconds
// connection.setSoTimeout (10000);
// try
// {
//    // Create a DataInputStream for reading from socket
//    DataInputStream dis = new DataInputStream (connection.getInputStream());
//    // Read data until end of data
//    for (;;)
//    {
//       String line = dis.readLine();
//       if (line != null)
//          System.out.println (line);
//       else
//          break;
//    }
// }
// // Exception thrown when network timeout occurs
// catch (InterruptedIOException iioe)
// {
//    System.err.println ("Remote host timed out during read operation");
// }
// // Exception thrown when general network I/O error occurs
// catch (IOException ioe)
// {
//    System.err.println ("Network I/O error - " + ioe);
// }
