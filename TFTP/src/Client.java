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
	   private DatagramSocket requestSocket;
	   private Request request,request2;
	   public Client() { // create connection as soon as new client is created
		   
		   try {
				requestSocket = new DatagramSocket();
			
			} catch (SocketException e) {
				System.err.println("Socket failed to be created.");
				e.printStackTrace();
				System.exit(1);
			}
	   }
	
	 public void rqst() {
		 
		 // read request for example 
		 
		 request = new Request(1,"filename.txt",70); // read request packet created to send to error simulator connected to port 70
		 request2 = new Request(2,"filename.txt",70);  // write request packet created to send to error simulator connected to port 70
		 sendPacket =  request.create();
		 sendPacket2 = request2.create();
		 
		// Send the datagram packet to the server via the send/receive socket.

	        try {
	           requestSocket.send(sendPacket);  //request sent to server 
	       	System.out.println("The client's port is: " + requestSocket.getLocalPort());
	        } catch (IOException e) {
	           e.printStackTrace();
	           System.exit(1);
	        }
	        
	        byte []data = new byte[100];
	        receivePacket = new DatagramPacket(data, data.length);

	        //	`System.out.println("Client: Waiting for packet.");
	        try {
	           // Block until a datagram is received via sendReceiveSocket.
	           requestSocket.receive(receivePacket);
	        } catch(IOException e) {
	           e.printStackTrace();
	           System.exit(1);
	        }
		 
	 }   
	public static void main(String args[])
	{
		Client client = new Client();
		client.rqst();   //sending request to server
	}

}
