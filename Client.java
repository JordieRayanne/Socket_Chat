package projet;
import java.io.*;
import java.net.*;
import java.util.*;
import Swing.*;


public class Client {
	
	Socket socks;
	Socket socks2;
	String host;
	String username;
	int port;
	int port2;
	int filesize;
	long start;
	
	public Client(){}
	
	public void setHost(String host) throws Exception{
		this.host=host;
	}
	
	public void setSocket(int port) throws Exception{ 
		this.port=port;
	}
	
	public void setSocket2(int port2) throws Exception{ 
		this.port2=port2;
	}
	
	public void setFileSize(){filesize=10000000;}
	
	public Client(String host,int port,int port2,String username) throws Exception{
		setHost(host);
		setSocket(port);
		setSocket2(port2);
		this.username=username;
	} 
	
	public int getPort(){return port;}
	public int getPort2(){return port2;}
	public String getHost(){return host;}
	public int getFileSize(){return 10000000;}
	public long getStart(){return System.currentTimeMillis();}
	
	public void connecting() throws Exception{
		try{
		Scanner scan=new Scanner(System.in);
		socks=new Socket(getHost(),getPort());
		socks2=new Socket(getHost(),getPort2());
		
		new Social(socks,socks2,username); //The whole content of the client side
		
		} catch(Exception e){System.out.println("Unable to connect");};
	}
	
	public void receiveFile() throws Exception{
		try{
		int bytesRead;
		int current=0;
		socks=new Socket(getHost(),getPort());
		
		///Mandray file
		byte [] datas=new byte[getFileSize()];
		InputStream is=socks.getInputStream();
		FileOutputStream fos=new FileOutputStream("C:\\File\\capture.PNG");
		BufferedOutputStream bos=new BufferedOutputStream(fos);
		bytesRead=is.read(datas,0,datas.length);
		current=bytesRead;

		do{
			bytesRead=is.read(datas,current,(datas.length-current));
			if(bytesRead>=0){current+=bytesRead;}
		}while (bytesRead>-1);
		
		bos.write(datas,0,current);
		bos.flush();
		long end = System.currentTimeMillis();
		System.out.println(end-start);
		bos.close();
		socks.close();
		}catch(Exception e){System.out.println("An error occured");}
	}
	
	public static void main (String[] args) throws Exception  {
		Client vao=new Client("127.0.0.1",5555,5556,"Koto");
		vao.connecting();
		//~ vao.receiveFile();
	}
}

