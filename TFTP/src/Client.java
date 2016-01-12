import java.awt.*;
import java.io.*;
import java.util.*;
import java.net.*;
/*The client will be implemented as a Java program that consists of one or more Java threads. 
 * The client will provide a simple user interface (a GUI is neither required nor recommended) that allows the user to input:
the file transfer operation (read file from server, write file to server)
the name of the file that is to be transferred
The client will then attempt to establish the appropriate connection with the server and transfer the file. 
After the current connection has been terminated (either because the file was transferred successfully or because an unrecoverable error occurred), 
the client should permit the user to initiate another file transfer.
 When the user indicates that no more files are to be transferred, the client should terminate.
The client should not support concurrent file transfers; for example, 
the client will not be able to concurrently transfer multiple files to and from one or more servers.*/
public class Client {
	
	   private DatagramPacket sendPacket,sendPacket2, receivePacket;
	   private static DatagramSocket clientSocket;
	   private Request request,request2;
	   private static Packet p = new Packet();
	   private int errSimPort = 70;
	private static int errSimThreadPort;
	private static int acknum, datanum;
	private int datablock;
	   private static String filename1, filename2;
	   public Client() { // create connection as soon as new client is created
		   
		   try {
				clientSocket = new DatagramSocket();
			
			} catch (SocketException e) {
				System.err.println("Socket failed to be created.");
				e.printStackTrace();
				System.exit(1);
			}
	   }
	
	 public void rqst() {
		 
		 // read request for example 
		 filename1 = "filename.txt";
		 filename2 = "filename.txt";
		 request = new Request(1,filename1,errSimPort); // read request packet created to send to error simulator connected to port 70
		 request2 = new Request(2,filename2,errSimPort);  // write request packet created to send to error simulator connected to port 70
		 sendPacket =  request.create();
		 sendPacket2 = request2.create();
		 readRequest(sendPacket,filename1);
		// writeRequest(sendPacket2);
		 
		 
		// Send the datagram packet to the server via the send/receive socket.

	      
	 }   
	 public void readRequest(DatagramPacket requestpacket,String filename)
	 {
		
		 try {
	           clientSocket.send(requestpacket);  //request sent to server 
	       	System.out.println("The client's port is: " + clientSocket.getLocalPort());
	        } catch (IOException e) {
	           e.printStackTrace();
	           System.exit(1);
	        }
	        
	        byte []data = new byte[100];
	        receivePacket = new DatagramPacket(data, data.length);

	        //	`System.out.println("Client: Waiting for packet.");
	        try {
	           // Block until a datagram is received via sendReceiveSocket.
	           clientSocket.receive(receivePacket);
	           
	           this.errSimThreadPort=receivePacket.getPort();
	          
	           
	           printReceivedPacket(receivePacket);
	        } catch(IOException e) {
	           e.printStackTrace();
	           System.exit(1);
	        }
	    
	        	  // need to send ACK before reading
	        	acknum = 1; // initial ack block number is 1
		        
		        sendAck();
		        BufferedWriter output = null;
		        File file = new File(filename);
		        for(;;)
		        {
		        	byte b[] = new byte[1000];
		        	
		        	DatagramPacket filedata = new DatagramPacket(b,b.length);
		        	
		        	
		        	try {
						clientSocket.receive(filedata);
						
						printReceivedPacket(filedata);
						String d = p.getData(filedata);
						
				        try {
				          
				            output = new BufferedWriter(new FileWriter(file));
				            output.write(d);
				        } catch ( IOException e ) {
				            e.printStackTrace();
				        } finally {
				            if ( output != null ) output.close();
				        }
					
					} catch (IOException e) {
						System.err.println("error in receiving file data packet");
						System.exit(1);
						e.printStackTrace();
					}
		        	
		        	
		        	acknum++;
		        	sendAck();
		        	// loop used to handle steady state file transfer 
		        }
		 
	 }
	 public void writeRequest(DatagramPacket requestpacket)
	 {
		  try {
	           clientSocket.send(requestpacket);  //request sent to server 
	       	//System.out.println("The client's port is: " + requestSocket.getLocalPort());
	        } catch (IOException e) {
	           e.printStackTrace();
	           System.exit(1);
	        }
	        
	        byte []data = new byte[100];
	        receivePacket = new DatagramPacket(data, data.length);

	        //	`System.out.println("Client: Waiting for packet.");
	        try {
	           // Block until a datagram is received via sendReceiveSocket.
	           clientSocket.receive(receivePacket);
	           System.out.println("Packet received: ");
	        // System.out.println(receivePacket.getData());
	           System.out.println("Opcode: " + "0"+p.getOpcode(receivePacket));
	           System.out.println("Block number: " + "0"+p.getBlk(receivePacket));
	         //  System.out.println("Data: "+ p.getData(receivePacket));
	           
	        } catch(IOException e) {
	           e.printStackTrace();
	           System.exit(1);
	        }
	        for(;;)
	        {
	        	
	        	// loop used to handle steady state file transfer 
	        }
		 
	 }
	 public static void printReceivedPacket(DatagramPacket packet)
	 {
		  System.out.println("Packet received: ");
	        // System.out.println(receivePacket.getData());
	           System.out.println("Opcode: " + "0"+p.getOpcode(packet));
	           System.out.println("Block number: " + "0"+p.getBlk(packet));
	         //  System.out.println("Data: "+ p.getData(receivePacket));
	           
	 }
	 public static void sendAck()
	 {
		   Packet ackPacket = new Packet((byte)4,(byte)acknum,errSimThreadPort);
	        DatagramPacket ack = ackPacket.create();
	        try {
				clientSocket.send(ack);
			} catch (IOException e) {
				e.printStackTrace();
			}
	 }
	public static void main(String args[])
	{
		Client client = new Client();
		client.rqst();   //sending request to server
	}

}
