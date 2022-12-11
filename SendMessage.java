
package projet;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.io.*;
import java.lang.*;

public class SendMessage implements Runnable {
	
	Socket sock;
	OutputStream out;
	DataOutputStream dout;
	String soc;

	public SendMessage(Socket sock,OutputStream out,String soc){
		this.sock=sock;
		this.out=out;
		this.soc=soc;
	}
		
	public SendMessage(Socket sock,DataOutputStream dout,String soc){
		this.sock=sock;
		this.dout=dout;
		this.soc=soc;
	}
	
	public DataOutputStream getStream(){return dout;}
	public OutputStream getStreamO(){return out;}
	public String getMessages(){return soc;}
	
	public void run(){
		while(true){
			try{
				StringTokenizer st=new StringTokenizer(getMessages(), ">>>");
				String messageAEnvoye=st.nextToken();
				String recipient=st.nextToken();
				String type=st.nextToken();
				OutputStream fout=sock.getOutputStream();
				
				if(type.equals("M")){
					//manoratra eo @ output Stream
					DataOutputStream dout1=new DataOutputStream(sock.getOutputStream());
					dout1.writeUTF(this.getMessages());
					System.out.println("Message to be sent="+getMessages());
					dout1.flush();
					break;
				}
			}catch(IOException e){ e.printStackTrace();}
		}
	}
}

