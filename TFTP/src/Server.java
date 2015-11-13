import java.awt.*;
import java.io.*;
import java.util.*;
import java.net.*;
/*The server will be implemented as a Java program that consists of multiple Java threads. 
 * The server must be capable of supporting multiple concurrent read and write connections with different clients.
To accomplish this, the server will have a multithreaded architecture. One thread will wait on port 69 for UDP datagrams containing WRQ and RRQ packets. 
This thread should:
verify that the received TFTP packet is a valid WRQ packet or RRQ packet
create another thread (call it the client connection thread), and pass it the TFTP packet; and
go back to waiting on port 69 for another request.
The newly created client connection will, as its name suggests, 
be responsible for communicating with the client to transfer a file.
Once started, the TFTP server will run until it receives a “shutdown” command from the server operator. 
Note that the server operator will type in this request in the server window. 
It is neither desirable nor acceptable for a client to request that the server shutdown.
After being told to shut down, the server should finish all file transfers that are currently in progress,
 but refuse to create new connections with clients. The server should then terminate*/
public class Server
{
	private static DatagramSocket serverSocket;
	private static DatagramPacket requestPacket,sendPacket;
	private static Request request = new Request();
	private static Packet p;
public Server()
{
	try {
		serverSocket = new DatagramSocket(69);
		System.out.println("The server's port is " + serverSocket.getLocalPort());
	} catch (SocketException e) {
		System.err.println("Socket failed to be created.");
	}
}
public static void handleRequest()
{
	
	for(;;)
		
	{
		byte buf[] = new byte[512]; 
		requestPacket = new DatagramPacket(buf, buf.length);
		try {
			serverSocket.receive(requestPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		 System.out.println("Packet received: ");
         System.out.println("Opcode: " + "0"+request.getOpcode(requestPacket));
         System.out.println("Data: "+ request.getFile(requestPacket));
         
		int oc = request.getOpcode(requestPacket);
		
		if(oc==1)
		{
			byte[] d = new byte[0];
			Packet data = new Packet((byte)3,(byte)1,d,requestPacket.getPort());
			try {
				serverSocket.send(data.create());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		else if(oc==2)
		{
			Packet ack = new Packet((byte)4,(byte)0,requestPacket.getPort());
			try {
				serverSocket.send(ack.create());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		Thread t = new Thread() {
		    public void run() {
		    	// This thread will handle a particular client's request and pass the packets to/from the server
		    }
		};
		t.start();
	}
	
/*	System.out.println(oc);
	String str = request.getFile(requestPacket);
	System.out.println(str);
	System.out.println(str.length());
*/
	/*try {
		BufferedReader br = new BufferedReader(new FileReader(str));
		  StringBuilder sb = new StringBuilder();
		    try {
				String line = br.readLine();
				
				while(line!=null)
				{
					sb.append(line);
			        sb.append(System.lineSeparator());
			        line = br.readLine();
				}
				System.out.println(sb.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}

	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}*/
	
	/*Writer writer = null;

	try {
	    writer = new BufferedWriter(new OutputStreamWriter(
	          new FileOutputStream(str), "utf-8"));
	    writer.write("Something");
	} catch (IOException ex) {
	  // report
	} finally {
	   try {writer.close();} catch (Exception ex) {System.err.println("ERROR");}
	}
	*/
  
}
public static void main(String args[])
{
	new Server();
	handleRequest();
	
}


}
