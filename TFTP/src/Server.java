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
	private static DatagramPacket requestPacket,sendPacket,ack,data;
	private static Request request = new Request();
	private static Packet p= new Packet(),d= new Packet();
	private static int errSimThreadPort;
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
			errSimThreadPort = requestPacket.getPort();
			System.out.println("The error simulator thread's port is " + errSimThreadPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	printReceivedPacket(requestPacket);
		
		int oc = request.getOpcode(requestPacket);
		
		String filename = request.getFile(requestPacket);
		serverThread s = new serverThread(oc,filename,errSimThreadPort);
		s.start();
		

	}
}
public static void printReceivedPacket(DatagramPacket packet)
{
	  System.out.println("Packet received: ");
        System.out.println(new String(packet.getData()));
          System.out.println("Opcode: " + "0"+p.getOpcode(packet));
          System.out.println("Block number: " + "0"+p.getBlk(packet));
        //  System.out.println("Data: "+ p.getData(receivePacket));
          
}
public static void main(String args[])
{
	new Server();
	handleRequest();
	
}

}
class serverThread extends Thread{
DatagramSocket threadSocket;
DatagramPacket receivePacket, sendPacket;
private static Packet d, p;
private static int datanum, acknum, errSimThreadPort;
int op;
String s;

public serverThread(int x, String file,int errSimThreadPort)
{
	 op = x;
	 s=file;
	;
	try {
		threadSocket = new DatagramSocket();
	
	} catch (SocketException e) {
		e.printStackTrace();
	}
	byte buf[] = new byte[1000];
	receivePacket = new DatagramPacket(buf,buf.length);
	this.errSimThreadPort=errSimThreadPort;
	
}
	public void run()
	{
		 if (op==1) {
			 
			 

				byte[] d0= new byte[0];
				Packet data0 = new Packet((byte)3,(byte)1,d0,this.errSimThreadPort);
				try {
					threadSocket.send(data0.create());
				} catch (IOException e) {
					e.printStackTrace();
				}
			 datanum=1;
			 
			 
			 
			 InputStream reader = null;
			 System.out.println("The file name to be read is"+ s);
   			File file = new File(s);
			 
   			try {
  				reader = new FileInputStream(file);
  				 byte[] data = new byte[512];
  				while ( (reader.read(data))!=-1) {
  					//byte op , byte blk, byte[] data,int port
					d = new Packet ((byte)op,(byte)datanum,data,this.errSimThreadPort);
  					sendPacket = d.create();
  					threadSocket.send(sendPacket);
  					
  					byte[] ack = new byte[512];
  					receivePacket = new DatagramPacket(ack,ack.length);
  				    threadSocket.receive(receivePacket);
  				}	 
  				 
  			} catch (FileNotFoundException e1) {
				
  				System.err.println("Error! File not found.");
  				System.exit(1);
  				//e1.printStackTrace();
  			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		 }
	 	
		
	}
	/**
	 * Method to check the Ack number and verify its correctness
	 */
public void checkAckNumber()
{
	
}
/**
 * Method to check the Data block number to verify its correctness
 */
public void checkDataNumber(){
}  
}
