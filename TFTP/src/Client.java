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
	
	public static void main(String args[])
	{
		try {
			DatagramSocket clientSocket = new DatagramSocket();
		} catch (SocketException e) {
		}
		System.out.println("Welcome! This program utilizes the TFTP to do file transfer");
	}

}
