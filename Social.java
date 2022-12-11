package Swing;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import projet.*;
import java.io.*;
import java.awt.event.*;
import java.util.*;

public class Social extends JFrame {
	String clientName;
	JPanel all, soratra, menu;
	JButton message, fileSend, fileManager, media, upload;
	JTextField chat;
	JTextArea allChat;
	JComboBox clients;
	JScrollPane scroll;
	Socket soc;
	Socket soc2;
	InputStream in;
	OutputStream out;
	DataInputStream din;
	DataOutputStream dout;
	String path;
	String fileName;
	String anarana;
	Serveur vao = new Serveur();

	public Social() throws Exception {
	}

	public Socket getSocket() {
		return soc;
	}

	public String[] val() throws Exception {
		Vector<String> kil = new Vector<>();
		BufferedReader reader = new BufferedReader(new FileReader("clients.txt"));
		String line;
		while ((line = reader.readLine()) != null) {
			kil.add(line);
		}
		String[] valiny = new String[kil.size()];
		System.out.println(kil.size());
		for (int a = 0; a < kil.size(); a++) {
			valiny[a] = kil.get(a);
		}
		return valiny;
	}

	public Social(String path, String fileName, String clientName) throws Exception {
		this.path = path;
		this.fileName = fileName;
		this.clientName = clientName;
	}

	public String getPath() {
		return path;
	}

	public String getFileName() {
		return fileName;
	}

	public String getClientName() {
		return clientName;
	}

	public Social(Socket socket, Socket socket2, String anarana) {
		try {
			// ~ Icon
			ImageIcon img = new ImageIcon("assets/icon3.png");
			this.setIconImage(img.getImage());
			// DataInput/output stream
			this.soc = socket;
			this.soc2 = socket2;
			in = socket.getInputStream();
			out = socket.getOutputStream();
			din = new DataInputStream(in);
			dout = new DataOutputStream(out);
			// to put the actual client's name
			this.setTitle(anarana);
			// custom colors
			Color panAll = new Color(240, 237, 237);
			Color panSoratra = new Color(220, 237, 237);
			Color panMenu = new Color(251, 251, 251);
			// the panel to put all content
			all = new JPanel();
			all.setLayout(null);
			all.setBackground(panAll);
			this.setContentPane(all);
			// the left-side panel with the all messages and the textfield+combobox
			soratra = new JPanel();
			soratra.setLayout(null);
			soratra.setBounds(10, 10, 290, 340);
			soratra.setBackground(panSoratra);
			/// all received chat
			allChat = new JTextArea(" ");
			allChat.setBounds(2, 2, 286, 200);
			scroll = new JScrollPane(allChat);
			scroll.setBounds(2, 2, 286, 200);
			soratra.add(scroll);
			/// textField
			chat = new JTextField("Write your message here!");
			chat.setBounds(2, 210, 286, 50);
			soratra.add(chat);
			/// combobox
			String[] cli = val();
			String[] nameClient = new String[cli.length];
			String[] password = new String[cli.length];
			for (int x = 0; x < cli.length; x++) {
				nameClient[x] = cli[x].split("_")[0];
				password[x] = cli[x].split("_")[1];
			}
			clients = new JComboBox(nameClient);
			clients.setBounds(2, 270, 200, 50);
			soratra.add(clients);
			/// send button
			message = new JButton("Send");
			message.setBounds(204, 270, 82, 50);
			message.addActionListener((ActionEvent e) -> {
				treatSend(chat, clients);
			});
			soratra.add(message);

			all.add(soratra);
			// the rigth-side panel with the file listener
			menu = new JPanel();
			menu.setLayout(null);
			menu.setBounds(310, 10, 160, 340);
			menu.setBackground(panMenu);
			/// to allow the client to send file
			fileSend = new JButton("Send File");
			fileSend.setBounds(5, 150, 150, 50);
			fileSend.addActionListener((ActionEvent e) -> {
				new Fichier(soc2);
			});
			menu.add(fileSend);

			all.add(menu);

			Thread readMessage = new Thread(new ReadMessage(socket, din, allChat));
			readMessage.start();

			Thread readFile = new Thread(new ReadFile(socket, socket2, allChat));
			readFile.start();

			this.setResizable(false);
			this.setSize(500, 400);
			this.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void treatSend(JTextField message, JComboBox name) {
		try {
			String vraiMessage = message.getText() + ">>>" + name.getSelectedItem() + ">>>M";
			Thread sendMessage = new Thread(new SendMessage(soc, dout, vraiMessage));
			sendMessage.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void treatFile(Socket socs) {
		try {
			String[] name = getClientName().split("/");
			String vraiMessage = getPath() + ">>>" + name[0] + ">>>F" + ">>>" + getFileName();
			Thread sendMessage = new Thread(new SendFile(socs, dout, vraiMessage));
			sendMessage.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
