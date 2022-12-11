package Swing;

import java.net.*;
import java.io.*; 
import javax.swing.*; 
import java.awt.event.*; 
import javax.swing.filechooser.*; 
import java.util.*;

public class Fichier extends JFrame implements ActionListener {
	
	JFileChooser file=new JFileChooser();
	static JLabel l=new JLabel();
	JLabel fileName=new JLabel();
	static JLabel fileNameShow=new JLabel();
	JLabel path=new JLabel();
	static JLabel pathShow=new JLabel();
	Socket soc;

	JComboBox client=new JComboBox();
	
	public String[] val()throws Exception{
		Vector<String> kil=new Vector<>();
		BufferedReader reader = new BufferedReader(new FileReader("clients.txt"));
		String line;
        while((line=reader.readLine())!=null){
			kil.add(line);
		}
		String []valiny=new String[kil.size()];
		System.out.println(kil.size());
		for(int a=0;a<kil.size();a++){
			valiny[a]=kil.get(a);
		}
		return valiny;
	}
	
	public Fichier(Socket socket){
		try{
		this.soc=socket;
		// frame to contains GUI elements  
		this.setTitle("file chooser");
		
		// set the size of the frame 
		this.setSize(400, 400); 

		// set the frame's visibility  
		this.setVisible(true); 

		//~ f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		// button to open open dialog 
		JButton button2 = new JButton("Ouvrir gestionnaire de fichiers"); 
		button2.setBounds(100,20,200,30);
		
		// label for path and filename
		fileName=new JLabel("Nom du fichier:");
		fileName.setBounds(10,120,100,30);
		fileNameShow=new JLabel("Aucun fichier selectionne!");
		fileNameShow.setBounds(120,120,170,30);
		path=new JLabel("Chemin:");
		path.setBounds(10,170,100,30);
		pathShow=new JLabel("Aucun fichier selectionne!");
		pathShow.setBounds(120,170,170,30);
		
		//comboBox to choose client
		//~ String[] listeClients={"client1","client2"};
		String[] listeClients=val();
		client=new JComboBox(listeClients);
		client.setBounds(150,250,100,30);
		
		// button to send the file
		JButton button1 = new JButton("Envoyer");
		button1.setBounds(150,300,100,30);  
		
		// add action listener to the button to capture user 
		// response on buttons 
		button2.addActionListener(this); 
		button1.addActionListener((ActionEvent e)->{
			try{
			appending(pathShow.getText(),fileNameShow.getText(),client.getSelectedItem().toString());
			System.out.println("ok");}
			catch(Exception diso){diso.printStackTrace();} 
			}
		); 
		
		// make a panel to add the buttons and labels 
		JPanel p = new JPanel(); 
		
		// add buttons to the frame 
		p.add(button2); 
		p.add(fileName);
		p.add(fileNameShow);
		p.add(path);
		p.add(pathShow);
		p.add(client);
		p.add(button1); 
 		
		// add panel to the frame  
		p.setLayout(null);
		this.add(p); 
		
		this.show(); }
		catch(Exception e){e.printStackTrace();}
	}
	
	public void actionPerformed(ActionEvent evt) {
		// if the user presses the save button show the save dialog 
		String com = evt.getActionCommand(); 

		if (com.equals("Ouvrir gestionnaire de fichiers")) { 
			// create an object of JFileChooser class 
			JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); 

			// invoke the showsSaveDialog function to show the save dialog 
			int r = j.showSaveDialog(null); 

			// if the user selects a file 
			if (r == JFileChooser.APPROVE_OPTION) 
			{ 
				File file=new File(j.getSelectedFile().getAbsolutePath());
				// set the label to the path of the selected file 
				fileNameShow.setText(file.getName());
				pathShow.setText(j.getSelectedFile().getAbsolutePath()); 
			} 
			// if the user cancelled the operation 
			else{
				fileNameShow.setText("You cancelled the operation");
				pathShow.setText("You cancelled the operation"); }
		} 
	} 
	
	public void appending(String path,String fileName,String clientName) throws Exception{
		Social social=new Social(path,fileName,clientName);
		social.treatFile(soc);
	} 
	
	public static void main (String[] args) {
		//~ new Fichier();
	}
}

