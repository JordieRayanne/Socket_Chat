package projet;

import java.io.*;
import java.net.*;
import java.util.*;

public class TraitementClientFichier implements Runnable{
	
	public String clientName;
	public Socket socket;
	public boolean estConnecte;
	public DataOutputStream dout;
	public Vector<TraitementClientFichier> storeFichier=new Vector<>();
	public String nomClient_Fichier;
	public String fileName;
	public String recipient;
	public int bytesRead;
	
	public TraitementClientFichier(){}
	
	public TraitementClientFichier(String clientName,Vector storeFichier,Socket socket){
		this.clientName=clientName;
		this.storeFichier=storeFichier;
		this.socket=socket;
		this.estConnecte=true;
	}
	
	public Vector<TraitementClientFichier> getStoreFichier(){
		return storeFichier;
	}
	
	public void run(){
		while(true){
			try{
				
				int current=0;
				long start=0;
				InputStream in=socket.getInputStream();
				DataInputStream data=new DataInputStream(in);
				
				recipient=data.readUTF();
				System.out.println("recipient="+recipient);
				fileName=data.readUTF();
				System.out.println("fileName="+fileName);
				
				//storeClient = vector misy ny client active
				for(TraitementClientFichier tc : getStoreFichier()){
					if(tc.clientName.equals(recipient)&& tc.estConnecte==true){
						try{
							byte [] datas=new byte[10000000];
							OutputStream output=new FileOutputStream ("Files\\"+clientName+"\\"+fileName);
							long size=data.readLong();
							while(size>0 && (bytesRead=data.read(datas,0,(int)Math.min(datas.length,size)))!=-1){
								output.write(datas,0,bytesRead);
								size-=bytesRead;
								output.flush();
							}
						
						}catch(IOException e){e.printStackTrace();break;}
						finally{
							//resend to client
							File fichier=new File("Files\\"+clientName+"\\"+fileName);
							byte[] byteArray=new byte[(int)fichier.length()];
							FileInputStream fis=new FileInputStream(fichier);
							BufferedInputStream bis=new BufferedInputStream(fis);
							bis.read(byteArray,0,byteArray.length);
							
							OutputStream out=socket.getOutputStream();
							dout=new DataOutputStream(out);
							tc.dout.writeUTF(fileName);
							tc.dout.writeUTF(clientName);
							tc.dout.writeLong(byteArray.length);
							tc.dout.write(byteArray,0,byteArray.length);
							dout.flush();
						}
					}	
				}
			}
			catch(Exception e){e.printStackTrace();break;}
		}	
	}
}

