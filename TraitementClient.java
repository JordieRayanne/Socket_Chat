package projet;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.io.*;
import java.lang.*;

public class TraitementClient implements Runnable{
	
	public Scanner scan=new Scanner(System.in);
	public String clientName;
	public DataOutputStream dout;
	public OutputStream streamFiles;
	public Socket socket;
	public boolean estConnecte;
	public Vector<TraitementClient> storeClient=new Vector<>();
	
	public TraitementClient(String clientName,Socket socket,Vector storeClient){
		this.clientName=clientName;
		this.socket=socket;
		this.storeClient=storeClient;
		this.estConnecte=true;
	}
	
	public Vector<TraitementClient> getStoreClient(){return storeClient;}
	
	public void run(){
		
		String receivedMessage;
		
		while(true){
			try{
				InputStream in=socket.getInputStream();
				DataInputStream din=new DataInputStream(in);
				//receive message and choice
				receivedMessage=din.readUTF();
				
				System.out.println("File/Message destination="+receivedMessage);
				System.out.println(clientName);
				
				if(receivedMessage.equals("logout")){
					this.estConnecte=false;
					this.socket.close();  
					break;					
				}							
											
				//separation du String en message et pour chaque recipient
				StringTokenizer st=new StringTokenizer(receivedMessage, ">>>");
				String messageAEnvoye=st.nextToken();
				//~ String []recip=st.nextToken().split("_");
				//~ String recipient=recip[0];  
				String recipient=st.nextToken();  
				String type=st.nextToken(); 
				System.out.println(receivedMessage);
				System.out.println("recipientSize="+getStoreClient().size());
				//mitady recipient @ zay client connected
				//storeClient = vector misy ny client active
				for(TraitementClient tc : getStoreClient()){
					if(tc.clientName.split("_")[0].equals(recipient)&& tc.estConnecte==true){
						if(type.equals("M")){
						System.out.println(clientName);
						dout=new DataOutputStream(socket.getOutputStream());
						tc.dout.writeUTF(this.clientName+">>>"+messageAEnvoye+">>>M");
						break;
						}
					}					
				}	
			}
			catch(Exception e){e.printStackTrace();}
		}
	}
}

