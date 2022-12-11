package projet;
import java.io.*;
import java.io.OutputStream;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class SendFile implements Runnable {
	Socket socket;
	String pathAll;
	File file;
	String clientName;
	String fileName;
	BufferedInputStream bis;
	FileInputStream fis;
	
	public SendFile(){}
	
	public SendFile(Socket socket,DataOutputStream dout,String pathAll){
		this.socket=socket;
		this.pathAll=pathAll;
	}
	
	public String getPath(){return pathAll;}
	
	public void run(){
		while(true){
			try{
				StringTokenizer st=new StringTokenizer(pathAll, ">>>");
				String path=st.nextToken();
				String recipient=st.nextToken();
				String type=st.nextToken();
				String fileName=st.nextToken();
				System.out.println(socket);
				//fonction
				File fichier=new File(path);
				byte[] byteArray=new byte[(int)fichier.length()];
				fis=new FileInputStream(fichier);
				bis=new BufferedInputStream(fis);
				bis.read(byteArray,0,byteArray.length);
				
				OutputStream os=socket.getOutputStream();
				DataOutputStream data=new DataOutputStream(os);
				DataOutputStream fileNames=new DataOutputStream(os);
				data.writeUTF(recipient);
				data.flush();
				
				fileNames.writeUTF(fileName);
				fileNames.flush();
				System.out.println("Envoye...");
				System.out.println("Byte in server="+byteArray.length);
				
				DataOutputStream longs=new DataOutputStream(os);
				longs.writeLong(byteArray.length);
				longs.flush();
				
				DataOutputStream file=new DataOutputStream(os);
				file.write(byteArray,0,byteArray.length);
				os.write(byteArray,0,byteArray.length);
				os.flush();
				file.flush();
				System.out.println("Done...");
				break;
			}
			catch(Exception e){e.printStackTrace();break;}
		}
	}
}
