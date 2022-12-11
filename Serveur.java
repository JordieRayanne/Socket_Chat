package projet;

import java.io.*;
import java.net.*;
import java.util.*;

public class Serveur{
	public ServerSocket serveur;
	public ServerSocket serveur2;
	public ServerSocket serveur3;
	public Socket sock;
	public Socket sock2;
	public Socket sock3;
	public int port;
	public int port2;
	public int port3;
	public String action;
	public String username;
	public String password;
	public DataInputStream dis;
	public DataOutputStream dout;
	public BufferedInputStream bis;
	public OutputStream os;
	public InputStream is;
	public FileInputStream fis;		///ampiasaina any @class traitement fichier
	public TraitementClient tc;
	public TraitementClientFichier tcf;
	public Vector<TraitementClient> place=new Vector<>();
	public Vector<TraitementClientFichier> placeF=new Vector<>();
	
	public void setPort(int nport){
		this.port=nport;
	}
	
	public void setPort2(int nport){
		this.port2=nport;
	}
	
	public void setPort3(int nport){
		this.port3=nport;
	}
	
	public Serveur(){}
	
	public Serveur(int port,int port2,int port3){
		setPort(port);
		setPort2(port2);
		setPort3(port3);
	}
	
	public int getPort(){return port;}
	public int getPort2(){return port2;}
	public int getPort3(){return port3;}
	
	public String[] val()throws Exception{
		Vector<String> kil=new Vector<>();
		BufferedReader reader = new BufferedReader(new FileReader("clients.txt"));
		String line;
        while((line=reader.readLine())!=null){
			kil.add(line);
		}
		String []valiny=new String[kil.size()];
		for(int a=0;a<kil.size();a++){
			valiny[a]=kil.get(a);
		}
		return valiny;
	}
	
	public void connexion()throws Exception{
		try{
			serveur=new ServerSocket(getPort());
			serveur2=new ServerSocket(getPort2());
			serveur3=new ServerSocket(getPort3());
			
			String []existingLogin=this.val();
			int a=1;
			while(true){
				sock3=serveur3.accept();//login
				System.out.println("New Login/Sign up request "+ sock3);
				sock=serveur.accept();//chat
				System.out.println("A client try to connect: "+ sock);
				sock2=serveur2.accept();//file
				System.out.println("A fileTransfer try to be processed: "+ sock2);
				
				//message_AND_files
				InputStream in=sock3.getInputStream();
				DataInputStream din=new DataInputStream(in);
				
				action=din.readUTF();
				username=din.readUTF();
				password=din.readUTF();
				
				if(action.compareTo("Login")==0){
					String all=username+"_"+password;
					System.out.println("existingLogin"+all);	
					String result=null;
					for(int b=0;b<existingLogin.length;b++){
						if(existingLogin[b].compareTo(all)==0){			
							result=existingLogin[b];break;
						}
					}
					System.out.println("existingLogin"+result);	
					
					if(result!=null){
					tc= new TraitementClient(result,sock,place);
					Thread t= new Thread(tc);
					tcf= new TraitementClientFichier(result,placeF,sock2);
					Thread tf=new Thread(tcf);
					System.out.println("Trying to add this client on the list");
				
					place.add(tc);
					placeF.add(tcf);
					t.start();
					tf.start();
					}//clode if("null")
				}//clode if("login")
				
				else if(action.compareTo("SignUp")==0){
					//creating storage for all clients
					File obj=new File("clients.txt");
					if(obj.createNewFile()){
						System.out.println("Clients list created: " + obj.getName());
					}
					else{
						System.out.println("Clients list already exists");
					}
					//save each client's name
					FileWriter soratra=new FileWriter("clients.txt",true);
					BufferedWriter out = new BufferedWriter(soratra);
					out.write(username+"_"+password);
					out.newLine();
					out.close();
					//creating this client's file directory
					File obj1=new File("Files/"+username+"_"+password);
					boolean bool=obj1.mkdirs();
					if(bool){
						System.out.println("Clients folder created: " + obj1.getName());
					}
					else{
						System.out.println("Clients folder already exists");
					}
					//generating thread to handle this new received client
					tc= new TraitementClient(username+"_"+password,sock,place);
					Thread t= new Thread(tc);
					tcf= new TraitementClientFichier(username+"_"+password,placeF,sock2);
					Thread tf=new Thread(tcf);
					System.out.println("Trying to add this client on the list");
				
					place.add(tc);
					placeF.add(tcf);
					t.start();
					tf.start();
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)throws Exception{
		Serveur connex=new Serveur(5555,5556,5557);
		connex.connexion();
	}
}
