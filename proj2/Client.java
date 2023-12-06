// A Java program for a Client
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
	// initialize socket and input output streams
	private Socket socket = null;
	private Scanner client_input = null;
	private DataInputStream server_input = null;
	private DataOutputStream client_output = null;

	// constructor to put ip address and port
	public Client(String address, int port)
	{
		// establish a connection
		try {
			socket = new Socket(address, port);
			System.out.println("Connected");

			// takes input from terminal
			client_input = new Scanner(System.in);

			//input from server
			server_input = new DataInputStream(socket.getInputStream());

			// sends output to the socket
			client_output = new DataOutputStream(socket.getOutputStream());

			// sendMessage thread 
			Thread sendMessage = new Thread(new Runnable()  
			{ 
				@Override
				public void run() { 
					synchronized (this){
						while (true) { 
							try { 
								// read the message to deliver. 
								String msg = client_input.nextLine(); 
								// write on the output stream 
								client_output.writeUTF(msg); 
							} catch (IOException e) { 
								e.printStackTrace(); 
							} 
						} 
					}
					
				} 
			}); 
			  
			// readMessage thread 
			Thread readMessage = new Thread(new Runnable()  
			{ 
				@Override
				public void run() { 
					synchronized(this){
						while (true) { 
							try { 
								// read the message sent to this client 
								String msg = server_input.readUTF(); 
								System.out.println(msg); 
							} catch (IOException e) { 
		
								e.printStackTrace(); 
							} 
						} 
					}
					
				} 
			}); 
	  
			sendMessage.start(); 
			readMessage.start(); 

		}
		catch (UnknownHostException u) {
			System.out.println(u);
			return;
		}
		catch (IOException i) {
			System.out.println(i);
			return;
		}
	}
	public static void main(String args[])
	{
		Client client = new Client("127.0.0.1", 5000);
	}
}