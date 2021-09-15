/*Oreoluwa Lawal
 * N01452264
 */
package client_server;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings("serial")
public class Server extends JFrame {
	JTextArea textArea;
	ArrayList<Socket> clientList = new ArrayList<Socket>();
	String message;
	

	public static void main(String[] args) {
		new Server();
	}

	@SuppressWarnings("resource")
	public Server() {
		getContentPane().setLayout(new BorderLayout(0, 0));

		textArea = new JTextArea();
		getContentPane().add(textArea);
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		try { // surround with try and catch to get any error

			ServerSocket serverSocket = new ServerSocket(8000);
			textArea.append("MultiThreadServer started at " + new Date() + '\n');

			// Number a client
			int clientNo = 1;

			while (true) {
				// Listen for a new connection request
				Socket socket = serverSocket.accept();

				clientList.add(socket);
				// Display the client number
				textArea.append("Starting thread for client " + clientNo + " at " + new Date() + '\n');

				textArea.append("Client " + clientNo + " has joined ");

				ClientChat task = new ClientChat(socket, clientNo); // this is the Task class in multithread that should
																	// have been implemented from runnable interface
				// Start the new thread
				new Thread(task).start();

				// Increment clientNo
				clientNo++;

				if (clientNo > 10)
					break;

			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	class ClientChat implements Runnable {
		private Socket socket; // A connected socket
		int clientNo;

		/** Construct a thread */
		public ClientChat(Socket socket, int clientNo) {
			this.socket = socket;
			this.clientNo = clientNo;
		}

		@Override /** Run a thread */
		public void run() {
			try {
				// Continuously serve the client
				while (true) {
					message = new DataInputStream(socket.getInputStream()).readUTF();
					// Receive message from the client
                   
					if (message.equals("bye")) {
						textArea.append("\n" + new Date() + " Chat Ended!");
						break;
					} else {

						for (Socket s : clientList) {
							// Send message back to the client
							new DataOutputStream(s.getOutputStream()).writeUTF(("User " + clientNo + ": " + message));
						}

						textArea.append("message received from client: " + clientNo + ": " + message + '\n');
					}
				}
				this.socket.close();
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}

}