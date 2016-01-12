import java.awt.*;
import java.io.*;
import java.util.*;
import java.net.*;
public class Request {
	
	 byte[] msg = new byte[100], // message we send
             fn, // filename as an array of bytes
             md, // mode as an array of bytes
             data; // reply as array of bytes
      String filename, mode; // filename and mode as Strings
      
      int j, len,reqtype, sendPort;
      DatagramPacket packet;
	public Request (int type ,String file,int port) {
		
		reqtype = type;
		filename = file;
		sendPort = port;
	}
	public Request()
	{
		//empty constructor
	}
	

	// function used to create read, write request 
	// TODO add ability to write ACK and data packet 
	public DatagramPacket create(){
		msg[0]=0;
		if (reqtype==1) { 
			msg[1]=1; 
			
			}else if (reqtype ==2) {
		    msg[1]=2;
			}
			else { 
		return null;
			}
		  // building byte array
		fn = filename.getBytes();
		System.arraycopy(fn,0,msg,2,fn.length);
		msg[fn.length+2] = 0;
		mode = "octet";
		md = mode.getBytes();
        System.arraycopy(md,0,msg,fn.length+3,md.length);
        len = fn.length+md.length+4;
        msg[len-1] = 0;
       
        // create packet 
        
        try {
            packet = new DatagramPacket(msg, len,
                                InetAddress.getLocalHost(), sendPort);
         } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
         }
        return packet;
	}
	public int getOpcode (DatagramPacket dp) {
		 
	 byte [] ad= dp.getData();
	 
	 return ad[1];
		
	}
	public String getFile (DatagramPacket dp) {
		 
		byte [] ad= dp.getData();
		int ind=0;
		 String data= new String(ad);
		 
		
		for (int i=2; i<ad.length; i++) {
			
			if (ad[i]==0) { 
				
			ind=i;
			break;	
			}
			
			
		}
		 String result = data.substring(2, ind);
		
		return result;
		
		
	}
	

	
	
	
	// skeleton for check function , meant to check integrity of packet , 
	// TODO change / add more functionality ?
	/*public Boolean check(DatagramPacket pckt) {
		
		byte [] rcvdata = pckt.getData();
		
		if (rcvdata[0]!=0) {
		return false;
		}
		return true;
	}
*/
}
