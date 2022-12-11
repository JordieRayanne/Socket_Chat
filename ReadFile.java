package projet;
import java.io.*;
import java.io.InputStream;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class ReadFile implements Runnable {
	Socket socket;
	Socket socket2;
	DataInputStream din;
	JTextArea allMessage;
	BufferedOutputStream out;
	int bytesRead;
	String fileName;
	String clientName;
	JTextArea all;
	
	public ReadFile(Socket soc,Socket soc2,JTextArea all){
		this.socket=soc;
		this.socket2=soc2;
		this.all=all;
	}
	
	public DataInputStream getStream(){return din;}
	
	
	public void run(){
	while(true){
			try{
				//mamaky fichier avy any
				InputStream in=socket2.getInputStream();
				DataInputStream data=new DataInputStream(in);
				
				fileName=data.readUTF();
				clientName=data.readUTF();
				
				byte [] datas=new byte[10000000];
				OutputStream output=new FileOutputStream("YourFiles\\"+fileName);
				long size=data.readLong();
				while(size>0 && (bytesRead=data.read(datas,0,(int)Math.min(datas.length,size)))!=-1){
					output.write(datas,0,bytesRead);
					size-=bytesRead;
				}
				output.flush();
				
				all.append("File sent"+"\n");
						
			}
			catch(IOException e){
				e.printStackTrace();break;
			}
		}
	}
}
