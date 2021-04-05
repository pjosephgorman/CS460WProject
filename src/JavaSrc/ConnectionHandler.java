package JavaSrc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.LinkedTransferQueue;

// JavaSrc.Client class
public class ConnectionHandler extends Thread
{
	private Client client;
	private final LinkedTransferQueue<String> commands;
	
	public ConnectionHandler(Client cl)
	{
		client = cl;
		commands = new LinkedTransferQueue<>();
	}
	
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
			Socket s = new Socket(ip, 5056);
			
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
					dos.writeUTF(tosend);
					
					// printing date or time as requested by client
					String received = dis.readUTF().trim();
					String[] cmd = received.split(" ");
					switch(cmd[0].toLowerCase())
					{
						case "exit":
							//terminate GUI, and then return
							dis.close();
							dos.close();
							return;
						case "login":
							break;
						case "error":
							break;
						default:
							//unexpected command
							running = false;
							break;
					}
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
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void runCommand(String cmd)
	{
		commands.add(cmd);
	}
}


// need to add:
// unnecessary comment
// magical fix

// need to add:
// public static void main in this and have it create
// a client handler and run the server and have it connect
// and make you r test main type commands for it
// type commands and see how it works
// use some sort of delimiter on the string you receive to split it into fields