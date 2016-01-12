import java.awt.*;
import java.io.*;
import java.util.*;
import java.net.*;
public class Packet {

	private byte op=0;
	private byte blk[]= new byte[1];
	private int port=0;
	private int len=0;
	private byte[] data;
	private byte[] msg = new byte[100];
	 DatagramPacket packet;
	public Packet (byte op , byte blk, byte[] data,int port) {
	   
		this.op=op;
		this.blk[0]=blk;
		this.data=data;
		this.port=port;
	
	}
	
	public Packet (byte op , byte blk,int port) {
	
		this.op=op;
		this.blk[0]=blk;
		this.port=port;
	}
	public Packet()
	{
		
	}


public DatagramPacket create(){
    msg[0]=0;
    if (op==3) {
    
    	msg[1]=3;
    	msg[2]=0;
    	System.out.println("msg array: " + msg[0]+msg[1]+msg[2]);
    	System.arraycopy(blk,0,msg,3,1);
    	//System.out.println("after array copy" + new String(msg));
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

public byte getOpcode (DatagramPacket dp) {
	 
	 byte [] pd= dp.getData();
	 
	 return pd[1];
		
	}

public byte getBlk (DatagramPacket dp) {
	 
	 byte [] pd= dp.getData();
	 
	 return pd[3];
		
	}

public String getData (DatagramPacket dp) {
	 
	 byte [] pd= dp.getData();
	 int ind=0;		 
	 String data= new String(pd);
	String result = data.substring(4, pd.length);
		
		return result;
		
		
		
	}



}