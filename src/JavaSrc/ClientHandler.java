package JavaSrc;// Java implementation of  JavaSrc.Server side
// It contains two classes : JavaSrc.Server and JavaSrc.ClientHandler
// Save file as JavaSrc.Server.java

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

// JavaSrc.ClientHandler class
class ClientHandler extends Thread
{
	final DataInputStream dis;
	final DataOutputStream dos;
	final Socket s;
	
	
	// Constructor
	public ClientHandler(Socket _s, DataInputStream _dis, DataOutputStream _dos)
	{
		this.s = _s;
		this.dis = _dis;
		this.dos = _dos;
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
				switch(cmd[0].toLowerCase())
				{
					
					case "login":
						if(cmd.length < 3)
						{
							Util.error("Login attempted with < 3 args");
							dos.writeUTF("error");
							break;
						}
						System.out.println("Login attempted: " + cmd[1] + " " + cmd[2]);
						dos.writeUTF("login");
						break;
					
					case "exit":
						dos.writeUTF("exit");
						System.out.println("JavaSrc.Client " + this.s + " sends exit...");
						System.out.println("Closing this connection.");
						this.s.close();
						System.out.println("Connection closed");
						return;
					
					default:
						dos.writeUTF("error");
						break;
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
			// closing resources
			this.dis.close();
			this.dos.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
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
