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
private static int clientPort,serverThreadPort;  // this is used to keep track of the client's port and will be passed into each thread that is created to handle each new client
private static Packet p = new Packet();
public ErrorSim()
{
	try {
		errsimSocket  = new DatagramSocket(70);
		
	} catch (SocketException e) {
		System.err.println("Socket failed to be created.");
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
				errsimSocket.receive(recvPacket); // Received request packet from client
				clientPort = recvPacket.getPort();
				System.out.println("The client's port is " + clientPort);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			byte buf2[] = new byte[512];
			recvPacket = new DatagramPacket(buf2, buf2.length);
			try {
				errsimSocket.receive(recvPacket);
				serverThreadPort = recvPacket.getPort();
				System.out.println("The server thread's port is " + serverThreadPort);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(p.getOpcode(recvPacket)==3) // if we receive a DATA packet from the server then we know it's a RRQ 
			{
				
				try {
					sendPacket = new DatagramPacket(recvPacket.getData(),recvPacket.getData().length,InetAddress.getByName("localhost"),clientPort);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					errsimSocket.send(sendPacket);  // send DATA BLK 1 packet to client 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				recvPacket = new DatagramPacket(buf, buf.length); 
				try {
					errsimSocket.receive(recvPacket);  // received ACK BLK 1 
					serverThreadPort = recvPacket.getPort();
					
				} catch (IOException e) {
				}
				
				try {
					sendPacket = new DatagramPacket(recvPacket.getData(),recvPacket.getData().length,InetAddress.getByName("localhost"),69); //create new packet with specified server port number 69
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				try {
					errsimSocket.send(sendPacket); //packet received from client is forwarded to server 
				} catch (IOException e) {
			
					e.printStackTrace();
			}
					
			}
			else if(p.getOpcode(recvPacket)==4) // otherwise if we receive an ACK packet then we know it's WRQ
			{
				try {
					errsimSocket.receive(recvPacket);
					
				} catch (IOException e) {
				}
			}
	}	
}
class errSimThread extends Thread{
	private DatagramSocket threadSocket;
	private DatagramPacket requestPacket,receivedPacket, sendPacket;
	private int clientPort, serverThreadPort;

	
	public errSimThread(int clientPort,DatagramPacket requestPacket)
	{
		try {
			this.threadSocket= new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.clientPort=clientPort;
		this.requestPacket= requestPacket;
	}
	public void run()
	{
		byte send[] =this.requestPacket.getData();
		try {
			this.sendPacket = new DatagramPacket(send,0,send.length,InetAddress.getByName("localhost"),69); //create new packet with specified server port number 69
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		try {
			threadSocket.send(this.sendPacket); //packet received from client is forwarded to server 
		} catch (IOException e) {
	
			e.printStackTrace();
	}
		
			
			byte buf[] = new byte[512];
			receivedPacket = new DatagramPacket(buf, buf.length);
			try{
				
				threadSocket.receive(receivedPacket);
			}
			
			 catch (IOException e) {
					e.printStackTrace();
				}
			
			this.serverThreadPort=receivedPacket.getPort();
			
			
			
			send=this.receivedPacket.getData();
			try {
				this.sendPacket = new DatagramPacket(send,0,send.length,InetAddress.getByName("localhost"),this.clientPort); 
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			try {
				threadSocket.send(this.sendPacket); 
			} catch (IOException e) {
		
				e.printStackTrace();
		}
			
			for(;;)
			{
				 buf = new byte[512];
				receivedPacket = new DatagramPacket(buf, buf.length);
				try{
					
					threadSocket.receive(receivedPacket);
				}
				
				 catch (IOException e) {
						e.printStackTrace();
					}
				
				if(receivedPacket.getPort()==clientPort)
				{
						
						 send =this.requestPacket.getData();
						try {
							this.sendPacket = new DatagramPacket(send,0,send.length,InetAddress.getByName("localhost"),serverThreadPort);
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
						
				}
				else if(receivedPacket.getPort()==serverThreadPort)
				{
					 send =this.requestPacket.getData();
						try {
							this.sendPacket = new DatagramPacket(send,0,send.length,InetAddress.getByName("localhost"),clientPort);
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
				
				}
				try {
					threadSocket.send(this.sendPacket); 
				} catch (IOException e) {
			
					e.printStackTrace();
			}
			
			
	
	}
	
	
}
}
}
