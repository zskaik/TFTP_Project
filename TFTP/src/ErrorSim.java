import java.awt.*;
import java.io.*;
import java.util.*;
import java.net.*;

/*Your error simulation code must be a completely separate program (or programs) from your client and server code. 
The error simulator communicates with the client and the server using DatagramSocket objects. 
The number of threads for this code is your choice. 
Note that it should be possible to run two or more clients as well as the server in error simulation mode.
Added May 11th: Your error simulator may be single-threaded and is expected to work with one client only. 
To test multiple clients, send directly to port 69 (until we move to multiple machines)*/

public class ErrorSim 
{
private static DatagramSocket errsimSocket;
private static DatagramPacket recvPacket, sendPacket;
private static int clientPort;  // this is used to keep track of the client's port and will be passed into each thread that is created to handle each new client
private static Packet p = new Packet();
public ErrorSim()
{
	try {
		errsimSocket  = new DatagramSocket(70);
		
	} catch (SocketException e) {System.err.println("Socket failed to be created.") ;
	}
	
}
public static void main(String args[])
{
	new ErrorSim();
		for(;;)
		{
			byte buf[] = new byte[512];
			recvPacket = new DatagramPacket(buf, buf.length);
			try {
				errsimSocket.receive(recvPacket);
				clientPort = recvPacket.getPort();
				System.out.println("The client's port is " + clientPort);
			} catch (IOException e) {
				e.printStackTrace();
			}
			byte send[] =recvPacket.getData();
			try {
				sendPacket = new DatagramPacket(send,0,send.length,InetAddress.getByName("localhost"),69); //create new packet with specified server port number 69
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			try {
				errsimSocket.send(sendPacket); //packet received from client is forwarded to server 
			} catch (IOException e) {
		
				e.printStackTrace();
		}
			recvPacket = new DatagramPacket(buf, buf.length);
			try{
				
				errsimSocket.receive(recvPacket);
				byte send2[] =recvPacket.getData();
				try {
					sendPacket = new DatagramPacket(send2,0,send2.length,InetAddress.getByName("localhost"),clientPort); //create packet to send back to client
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} 
				
				errsimSocket.send(sendPacket);
			}
			
			 catch (IOException e) {
					e.printStackTrace();
				}
			if(p.getOpcode(recvPacket)==3) // if we receive a DATA packet back from the server then we know the client will send an ACK before the reading begins
			{
				recvPacket = new DatagramPacket(buf, buf.length);
				try {
					errsimSocket.receive(recvPacket);
					
					byte send3[] =recvPacket.getData();
					sendPacket = new DatagramPacket(send3,0,send3.length,InetAddress.getByName("localhost"),69);
					errsimSocket.send(sendPacket); // send ACK to server right before starting the read
				} catch (IOException e) {
				}
				
			}
			
			
			// wait for next data packet before starting thread
			int serverThreadPort = 0; 
			byte buf2[] = new byte[1000]; //make byte array for data packets to be received
			recvPacket = new DatagramPacket(buf2,buf2.length);
			try {
				errsimSocket.receive(recvPacket);
				serverThreadPort= recvPacket.getPort();
				byte send4[] =recvPacket.getData();
				sendPacket = new DatagramPacket(send4,0,send4.length,InetAddress.getByName("localhost"),clientPort);
			} catch (IOException e) {
				e.printStackTrace();
				
			}
			errSimThread t = new errSimThread(clientPort,serverThreadPort); 
			
			t.start();
	}	
}
}
class errSimThread extends Thread{
DatagramSocket threadSocket;
DatagramPacket receivePacket, sendPacket;
int clientPort =0;
int serverThreadPort=0;
public errSimThread(int clientPort, int serverThreadPort)
{
	try {
		threadSocket = new DatagramSocket();
	} catch (SocketException e) {
		e.printStackTrace();
	}
	byte buf[] = new byte[1000]; //make byte array for data packets to be received
	receivePacket = new DatagramPacket(buf,buf.length);
	
	this.clientPort=clientPort;
	this.serverThreadPort=serverThreadPort;
}
	public void run()
	{
	 	for(;;)
	 	{
	 		try {
				threadSocket.receive(receivePacket);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
	 		byte[] send = receivePacket.getData();
	 		if(receivePacket.getPort()==clientPort) // if we receive a packet from the client then we should send it to the server thread and vice versa  
	 		{
	 			try {
					sendPacket = new DatagramPacket(send,0,send.length,InetAddress.getByName("localhost"),serverThreadPort);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
	 			
	 		}
	 		else // otherwise if we receive a packet from the serverThread we need to forward the packet to the client
	 		{

	 			try {
					sendPacket = new DatagramPacket(send,0,send.length,InetAddress.getByName("localhost"),clientPort);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
	 		}
	 	}	
	}
}
