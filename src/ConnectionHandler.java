import java.io.*; 
import java.net.*; 
import java.util.Scanner; 
  
// Client class 
public class ConnectionHandler extends Thread   
{ 
    private LinkedTransferQueue<String> commands;
    public ConnectionHandler()
    {
        commands = new LinkedTransferQueue();
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
            while (true)  
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
                            scn.close(); 
                            dis.close(); 
                            dos.close(); 
                            return;
                        case "login":
                            break;
                        case ""
                    }
                } catch (Exception e) {
                    
                }
            } 
              
            // closing resources 
            scn.close(); 
            dis.close(); 
            dos.close(); 
        }catch(Exception e){ 
            e.printStackTrace(); 
        } 
    } 
    public void runCommand(String cmd) {
        commands.add(cmd);
    }
} 