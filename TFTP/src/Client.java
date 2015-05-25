import java.awt.*;
import java.io.*;
import java.util.*;
import java.net.*;

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
