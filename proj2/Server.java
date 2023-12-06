// A Java program for a Server
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.io.*;

public class Server
{
	//initialize socket and input stream
	private Socket		 socket = null;
	private ServerSocket server = null;
	private DataInputStream in	 = null;
	ArrayList<Socket> client_list = new ArrayList<>();
	DataOutputStream out = null;
	BufferedReader br = null;

	// constructor with port
	public Server(int port)
	{
		// starts server and waits for a connection
		try
		{
			server = new ServerSocket(port);
			System.out.println("Server started");

			System.out.println("Waiting for a client ...");
			while (true) {
				// Accept an incoming connection
				socket = server.accept();
				System.out.println("Client accepted: ");

				// Create a new thread to handle the connection
				Thread t = new EchoThread(socket);
				client_list.add(socket);
				t.start();
				System.out.println("New Thread: "+t.getName()+", Thread ID: "+t.getId());
			}
		}
		catch(IOException i)
		{
			System.out.println(i);
		}
		// close connection
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	public static void main(String args[])
	{
		Server server = new Server(5000);
	}
}

class EchoThread extends Thread {
    protected Socket socket;
	private Scanner server_input = null;
	private DataInputStream client_input = null;
	private DataOutputStream server_output = null;
	private static List<Socket> socketList = new ArrayList<>();

    public EchoThread(Socket clientSocket) {
        this.socket = clientSocket;
		socketList.add(socket);
    }

    public void run() {
		try {
			// takes input from terminal
			server_input = new Scanner(System.in);

			//input from Client
			client_input = new DataInputStream(socket.getInputStream());

			// sends output to the server socket
			server_output = new DataOutputStream(socket.getOutputStream());
		} catch (UnknownHostException u) {
			System.out.println(u);
			return;
		}
		catch (IOException i) {
			System.out.println(i);
			return;
		}
		

		String line = "", line2="";

		// reads message from client until "Over" is sent
		while (!line.equals("Over"))
		{
			try
			{
				line = client_input.readUTF();
				sendMessageToClients("Client "+getId()+": "+line);
				
			}
			catch(IOException i)
			{
				System.out.println(i);
			}
		}
		System.out.println("Closing connection");

		// close connection
		try {
			socket.close();
			client_input.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
    }

	private void sendMessageToClients(String line) throws IOException {
		for (Socket other : socketList) {
			if (other != socket) {
				DataOutputStream output = new DataOutputStream(other.getOutputStream());
				output.writeUTF(line);
			}
		}
	}
}