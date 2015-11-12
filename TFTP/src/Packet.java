import java.awt.*;
import java.io.*;
import java.util.*;
import java.net.*;
public class Packet {

	private int op=0;
	private int blk=0;
	private int port=0;
	private int len=0;
	private byte[] data;
	private byte[] msg;
	 DatagramPacket packet;
	public Packet (int op , int blk, byte[] data,int port) {
	   
		this.op=op;
		this.blk=blk;
		this.data=data;
		this.port=port;
	
	}
	
	public Packet (int op , int blk,int port) {
	
		this.op=op;
		this.blk=blk;
		this.port=port;
	}


public DatagramPacket create(){
    msg[0]=0;
    if (op==3) {
    
    	msg[1]=3;
    	msg[2]=0;
    	System.arraycopy(blk,0,msg,3,1);
    	System.arraycopy(data,0,msg,4,data.length);
    	
    	
    }else if (op==4) {
    	
    	msg[1]=4;
    	msg[2]=0;
    	System.arraycopy(blk,0,msg,3,1);
    	
           	
    } 
    try {
    	len=msg.length;
        packet = new DatagramPacket(msg, len,
                            InetAddress.getLocalHost(), port);
     } catch (UnknownHostException e) {
        e.printStackTrace();
        System.exit(1);
     }
    return packet;
 }
}