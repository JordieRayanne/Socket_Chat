package projet;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class ReadMessage implements Runnable {
	
	Socket socket;
	InputStream in;
	DataInputStream din;
	JTextArea allMessage;
	BufferedOutputStream out;
	
	public ReadMessage(Socket socket,DataInputStream din,JTextArea allMessage){
		this.socket=socket;
		this.din=din;
		this.allMessage=allMessage;
	}
	
	public DataInputStream getStream(){return din;}
	
	
	public void run(){
		while(true){
			try{
				//mamaky message avy any
				StringTokenizer st=new StringTokenizer(getStream().readUTF(), ">>>");
				String recipient=st.nextToken();
				String messageAEnvoye=st.nextToken();
				String type=st.nextToken();
				
				if(type.equals("M")){
				String msg = recipient+" : "+messageAEnvoye;
				System.out.println("Message to be sent="+msg);
				String []tenaExp=msg.split("_");
				String []tenaAlefa=tenaExp[1].split(":");
				allMessage.append(tenaExp[0]+":"+tenaAlefa[1]+"\n");
				}
				
			}
			catch(IOException e){
				e.printStackTrace();break;
			}
		}
	}
}

