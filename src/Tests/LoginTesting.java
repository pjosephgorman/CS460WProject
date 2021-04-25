package Tests;

import JavaSrc.Data.PatientInfo;
import JavaSrc.Data.SQLHandler;
import JavaSrc.Data.UserInfo;
import JavaSrc.Exceptions.RPMError;
import JavaSrc.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.LinkedTransferQueue;

public class LoginTesting
{
	public static void main(String[] args) throws InterruptedException
	{
		try
		{
			SQLHandler.main(null);
		}
		catch(Exception ignored) {}
		boolean success = false;
		LinkedTransferQueue<String> commands = new LinkedTransferQueue<>();
		DataInputStream dis = null;
		DataOutputStream dos = null;
		Socket s = null;
		try
		{
			// Scanner scn = new Scanner(System.in);
			// Read from queue ********
			
			// getting localhost ip
			InetAddress ip = InetAddress.getByName("localhost");
			
			// establish the connection with server port 5056
			s = null;
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
						Thread.sleep(1000 * 5);
					}
					catch(InterruptedException ignored) {}
				}
			} while(s == null);
			// obtaining input and out streams
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
			commands.add("login admin admin");
			
			while(!commands.isEmpty())
			{
				String next = commands.take();
				String[] cmd = next.split(" ");
				switch(cmd[0].toLowerCase())
				{
					case "exit" -> {
						dos.writeUTF("exit");
						dis.close();
						dos.close();
						s.close();
						return;
					}
					case "login" -> {
						if(cmd.length != 3)
						{
							next = "echo error 0 Must include 3 parameters!";
							break;
						}
						next = "%s %s %s".formatted(cmd[0], cmd[1], Util.hash(cmd[2]));
					}
				}
				dos.writeUTF(next);
				boolean processing = true;
				
				while(processing)
				{
					String received = dis.readUTF().trim();
					String[] scmd = received.split(" ");
					switch(scmd[0])
					{
						case "login" -> {
							UserInfo info = UserInfo.load(received.split(" ", 2)[1]);
							System.out.println("Logged in as:\n" + info);
							success = true;
						}
						case "pat", "editpat" -> {
							PatientInfo info = PatientInfo.load(received.split(" ", 2)[1]);
							System.out.println(info);
						}
						case "acp", "edituser" -> {
							UserInfo info = UserInfo.load(received.split(" ", 2)[1]);
							System.out.println(info);
						}
						case "error" -> {
							System.err.printf("Error (%d): %s%n", Integer.parseInt(scmd[1]), received.split(" ", 3)[2]);
							throw new RPMError();
						}
						case "over" -> processing = false;
						case "log" -> System.out.println(received.split(" ", 2)[1]);
						default -> System.out.println("Received: " + received);
					}
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			success = false;
		}
		catch(RPMError e)
		{
			success = false;
		}
		try
		{
			if(dis != null) dis.close();
		}
		catch(IOException ignored) {}
		try
		{
			if(dos != null) dos.close();
		}
		catch(IOException ignored) {}
		try
		{
			if(s != null) s.close();
		}
		catch(IOException ignored) {}
		
		System.out.printf("Test Status: %s%n", success ? "Success" : "Failure");
		if(!success) System.exit(1);
	}
}

