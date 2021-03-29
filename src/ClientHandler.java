// Java implementation of  Server side 
// It contains two classes : Server and ClientHandler 
// Save file as Server.java 

import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;

// ClientHandler class 
class ClientHandler extends Thread
{
	final DataInputStream dis;
	final DataOutputStream dos;
	final Socket s;
	
	
	// Constructor
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)
	{
		this.s = s;
		this.dis = dis;
		this.dos = dos;
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
						System.out.println("Client " + this.s + " sends exit...");
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


// need to add:
// public static void main in this and have it create
// a client handler and run the server and have it connect
// and make you r test main type commands for it
// type commands and see how it works
// use some sort of delimiter on the string you receive to split it into fields