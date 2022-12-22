package Swing;

import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import projet.*;

public class Input extends JFrame {

	JButton login;
	JButton signUp;
	JPanel all;
	Socket socket;

	public Input() {
		try {
			socket = new Socket("localhost", 5557);
			this.setTitle("Login or Sign up");

			all = new JPanel();
			this.setContentPane(all);
			all.setLayout(null);

			login = new JButton("Login");
			login.setBounds(50, 40, 100, 50);
			login.addActionListener((ActionEvent e) -> {
				this.login();
			});

			signUp = new JButton("Sign up");
			signUp.setBounds(230, 40, 100, 50);
			signUp.addActionListener((ActionEvent e) -> {
				this.signUp();
			});

			all.add(login);
			all.add(signUp);

			this.setLayout(null);
			this.setSize(400, 200);
			this.setVisible(true);
		} catch (Exception e) {
			System.out.println("Could not connect!");
		}
	}

	public void login() {
		this.setSize(400, 400);
		JPanel all = new JPanel();
		all.setLayout(null);
		this.setContentPane(all);

		JTextField username = new JTextField("Username");
		username.setBounds(100, 80, 200, 40);
		JTextField password = new JTextField("Password");
		password.setBounds(100, 140, 200, 40);

		JButton submit = new JButton("Log in");
		submit.setBounds(150, 200, 100, 50);
		submit.addActionListener((ActionEvent e) -> {
			try {
				this.getLogin(username.getText(), password.getText());
			} catch (Exception diso) {
				diso.printStackTrace();
			} finally {
				this.dispose();
			}
		});

		all.add(username);
		all.add(password);
		all.add(submit);
		this.setVisible(true);
	}

	public void signUp() {
		this.setSize(400, 400);
		JPanel all = new JPanel();
		all.setLayout(null);
		this.setContentPane(all);

		JTextField username = new JTextField("Username");
		username.setBounds(100, 80, 200, 40);
		JTextField password = new JTextField("Password");
		password.setBounds(100, 140, 200, 40);

		JButton submit = new JButton("Sign up");
		submit.setBounds(150, 200, 100, 50);
		submit.addActionListener((ActionEvent e) -> {
			try {
				this.getSignUp(username.getText(), password.getText());
			} catch (Exception diso) {
				diso.printStackTrace();
			} finally {
				this.dispose();
			}
		});

		all.add(username);
		all.add(password);
		all.add(submit);
		this.setVisible(true);
	}

	public void getLogin(String username, String password) throws Exception {
		OutputStream out = socket.getOutputStream();
		DataOutputStream output = new DataOutputStream(out);
		output.writeUTF("Login");
		output.writeUTF(username);
		output.writeUTF(password);
		Client vao = new Client("localhost", 5555, 5556, username);
		vao.connecting();
		// vao.receiveFile();
	}

	public void getSignUp(String username, String password) throws Exception {
		OutputStream out = socket.getOutputStream();
		DataOutputStream output = new DataOutputStream(out);
		output.writeUTF("SignUp");
		output.writeUTF(username);
		output.writeUTF(password);
		// save each client's name
		FileWriter soratra = new FileWriter("clients.txt", true);
		BufferedWriter out1 = new BufferedWriter(soratra);
		out1.write(username + "_" + password);
		out1.newLine();
		out1.close();
		// open client
		Client vao = new Client("localhost", 5555, 5556, username);
		vao.connecting();
	}

	public static void main(String[] args) {
		new Input();
	}
}
