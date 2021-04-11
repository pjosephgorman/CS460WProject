package JavaSrc;

import JavaSrc.Data.Roles;
import JavaSrc.Data.UserInfo;
import JavaSrc.Exceptions.RPMError;
import JavaSrc.Exceptions.RPMRuntimeException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.LinkedTransferQueue;

// JavaSrc.Client class
public class ConnectionHandler extends Thread
{
	private final Client client;
	private final LinkedTransferQueue<String> commands;
	private static final int RETRY_SECONDS = 30;
	private Roles role = null;
	private int id = -1;
	
	public ConnectionHandler(Client cl)
	{
		client = cl;
		commands = new LinkedTransferQueue<>();
	}
	
	//TODO remove test function
	public static void main(String[] args)
	{
		ConnectionHandler ch = new ConnectionHandler(null);
		ch.start();
		ch.runCommand("login x y");
	}
	
	@Override
	public void run()
	{
		try
		{
			// Scanner scn = new Scanner(System.in);
			// Read from queue ********
			
			// getting localhost ip
			InetAddress ip = InetAddress.getByName("localhost");
			
			// establish the connection with server port 5056
			Socket s = null;
			int i = 0;
			do
			{
				try
				{
					System.out.println("Attempting connection... #" + (i++));
					s = new Socket(ip, 5056);
				}
				catch(ConnectException e)
				{
					try
					{
						Thread.sleep(1000 * RETRY_SECONDS);
					}
					catch(InterruptedException ignored) {}
				}
			} while(s == null);
			client.setWelcome();
			// obtaining input and out streams
			DataInputStream dis = new DataInputStream(s.getInputStream());
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			
			// the following loop performs the exchange of
			// information between client and client handler
			boolean running = true;
			while(running)
			{
				try
				{
					String tosend = commands.take();
					//System.out.println("Sent: \"" + tosend + "\"");
					dos.writeUTF(tosend);
					boolean processing = true;
					while(processing)
					{
						// printing date or time as requested by client
						String received = dis.readUTF().trim();
						System.out.println("Received: \"" + received + "\""); //TODO remove print
						String[] cmd = received.split(" ");
						switch(cmd[0].toLowerCase())
						{
							case "exit" -> {
								dis.close();
								dos.close();
								return;
							}
							case "login" -> {
								if(cmd.length < 2) throw new RPMError();
								client.setMainMenu();
								Util.msg("Login successful!");
								UserInfo info = UserInfo.load(received.split(" ", 2)[1]);
								client.updateInfo(info);
								role = info.role;
								id = info.id;
							}
							case "clearacp" -> client.clearScene(Client.Scenes.ACP);
							case "acp" -> {
								if(cmd.length < 2) throw new RPMError();
								client.loadACP(UserInfo.load(received.split(" ", 2)[1]));
							}
							case "showacp" -> client.setACP();
							case "edituser" -> {
								if(cmd.length < 2) throw new RPMError();
								UserInfo info = UserInfo.load(received.split(" ", 2)[1]);
							}
							case "error" -> {
								processing = false;
								error(received);
							}
							case "warning" -> error(received);
							case "over" -> processing = false;
							default -> {
								Util.error("Unknown command '%s'\n".formatted(received));
								running = false;
							}
						}
					}
				}
				catch(InterruptedException ignored) {}
				catch(SocketException e)
				{
					client.timeout = true;
					running = false;
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
			// closing resources
			dis.close();
			dos.close();
		}
		catch(SocketException e)
		{
			client.timeout = true;
		}
		catch(RPMRuntimeException e)
		{
			error(Util.formatError(e));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			client.handler = null;
			Util.msg("Cleared connectionHandler!");
			client.handler = new ConnectionHandler(client);
			client.handler.start();
		}
	}
	
	public void runCommand(String cmd)
	{
		//System.out.println("Queued: \"" + cmd + "\"");
		commands.add(cmd);
	}
	
	public void kill()
	{
		commands.clear();
		runCommand("exit");
	}
	
	private void error(String command)
	{
		String[] cmd = command.split(" ", 3);
		ErrorCodes code = ErrorCodes.UNKNOWN_ERROR;
		try
		{
			code = ErrorCodes.values()[Integer.parseInt(cmd[1])];
		}
		catch(Exception ignored) {}
		String msg = ("%c#%03d").formatted(cmd[0].toUpperCase().charAt(0), code.ordinal()) + " " + (cmd.length < 3
		                                                                                            ? "Unknown Error"
		                                                                                            : cmd[2]);
		Util.error(msg);
		client.error(msg);
	}
	
	Roles getRole()
	{
		return role;
	}
	int getID()
	{
		return id;
	}
}
