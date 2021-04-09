package JavaSrc;// Java implementation of  JavaSrc.Server side
// It contains two classes : JavaSrc.Server and JavaSrc.ClientHandler
// Save file as JavaSrc.Server.java

import JavaSrc.Data.SQLHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;

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
					case "login" -> {
						if(cmd.length < 3)
						{
							Util.error("Login attempted with < 3 args");
							error(ErrorCodes.INVALID_LOGIN, "Login message attempted with " + cmd.length + " args, needs 3 args.");
							break;
						}
						Connection c = SQLHandler.connect();
						Util.msg("Login attempted: " + cmd[1] + " " + cmd[2]);
						if(SQLHandler.login(c, cmd[1], cmd[2]))
						{
							Util.msg("Login Successful");
							dos.writeUTF("login");
						}
						else
						{
							Util.msg("Login Failed");
							error(ErrorCodes.INVALID_LOGIN, "Incorrect Username or Password!");
						}
					}
					case "exit" -> {
						dos.writeUTF("exit");
						System.out.println("JavaSrc.Client " + this.s + " sends exit...");
						System.out.println("Closing this connection.");
						this.s.close();
						System.out.println("Connection closed");
						return;
					}
					default -> error(ErrorCodes.UNKNOWN_ERROR, "Unknown Error");
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
	
	private void error(ErrorCodes code, String msg) throws IOException
	{
		dos.writeUTF("error " + code.ordinal() + " " + msg);
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
