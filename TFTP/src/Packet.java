import java.awt.*;
import java.io.*;
import java.util.*;
import java.net.*;
public class Packet {

	private int op=0;
	private int blk=0;
	private byte[] data;
	 DatagramPacket packet;
	public Packet (int op , int blk, byte[] data) {
	   
		this.op=op;
		this.blk=blk;
		this.data=data;
	
	}
	
	public Packet (int op , int blk) {
	
		this.op=op;
		this.blk=blk;
	}


public DatagramPacket create(){
 
    if (op==3) {
    
    	
    	
    	
    }else if (op==4) {
    	
    	
           	
    }   
    return packet; 
 }
}