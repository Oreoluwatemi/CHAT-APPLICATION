/*Oreoluwa Lawal
 * N01452264
 */
package client_server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class Client extends JFrame {
	private JTextField txt;
	private Socket socket = null;
	private DataOutputStream toServer;


	JTextArea txtArea;
	public Client(String title) {
		getContentPane().setLayout(null);
		setTitle(title);
		txtArea = new JTextArea();
		txtArea.setBounds(10, 11, 414, 228);
		getContentPane().add(txtArea);
		
		txt = new JTextField();
		txt.setBounds(22, 265, 283, 20);
		getContentPane().add(txt);
		txt.setColumns(10);
		
		JButton btn = new JButton("Save");
			
		btn.setBounds(315, 264, 89, 23);
		getContentPane().add(btn);
		
		try {		
			socket = new Socket("localhost", 8000);

			
			// Create an output stream to send data to the server
			toServer = new DataOutputStream(socket.getOutputStream());

			new ServerChat(socket);
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		
		btn.addActionListener(new TextFieldListener());
	}
	
	private class TextFieldListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			try {
				//get user input
				String message = txt.getText(); // 

				toServer.writeUTF(message);
				toServer.flush();
				
				txt.setText("");
			}
			catch (Exception ex) {
				System.err.println(ex);
			}
		}
	}
	
	class ServerChat implements Runnable {
		private Socket socket;

		public ServerChat(Socket socket) {
			this.socket = socket;

			// Create a thread
			Thread thread = new Thread(this);

			// start a thread
			thread.start();
		}

		// Run method
		public void run() {
			try {
				// receive message from the server
				DataInputStream fromServer = new DataInputStream(socket.getInputStream());
				while (true) {
					// read message coming from server
					String text = fromServer.readUTF();

					// Display message to the screen
					txtArea.append(text + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
		
	}
	
	public static void main(String[] args) {
		
		        // Creating a scanner object to prompt client names
				Scanner input = new Scanner(System.in);

				// Prompting user to enter name
				System.out.print("Enter your name: ");

				String name = input.nextLine();
				
				//instance of client class
				Client client = new Client(name);
				client.setSize(500, 400);
				client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				client.setVisible(true); 

	}
	
}