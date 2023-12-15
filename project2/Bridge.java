// A Java program for a Server
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import java.io.*;
public class Bridge
{
	//initialize socket and input stream
	private Socket		 socket  = null;
	private ServerSocket server  = null;
	private DataInputStream in	 = null;
	private static ArrayList<String> bridge_names = new ArrayList<>();
	DataOutputStream out = null;
	BufferedReader br = null;
	Scanner server_input = new Scanner(System.in);
	static String bridge_name;
	static int num_ports=0;

	// constructor with port
	public Bridge(int port)
	{
		// starts server and waits for a connection
		try
		{
			server = new ServerSocket(port);
			System.out.println("Server started");
			System.out.println(Inet4Address.getLocalHost().getHostAddress().toString());
			System.out.println(Inet4Address.getLocalHost().getLocalHost().toString());
			FileHandling.createSymbolicFiles(bridge_name, Inet4Address.getLocalHost().getHostAddress().toString(), server.getLocalPort());
            System.out.println("Port: "+server.getLocalPort());
			System.out.println("Waiting for a client ...");
			int count_clients = 0;
			while (true) {
				
				// Accept an incoming connection
				socket = server.accept();
				count_clients++;
				if (count_clients <= num_ports) {
					// Create a new thread to handle the connection
					Thread t = new EchoThread(socket);
					t.start();
					System.out.println("accepted");
					System.out.println(socket);
					// System.out.println("New Thread: "+t.getName()+", Thread ID: "+t.getId());
				}
				else{
					System.out.println("rejected");
					System.out.println("No port available, All ports are in use.");
				}
				
			}
		}
		catch(IOException i){
			System.out.println(i);
		}
		// close connection
		try {
			socket.close();
			// server.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	public static void main(String args[])
	{
		bridge_name = args[0];
		num_ports = Integer.valueOf(args[1]);

		if (!bridge_names.contains(bridge_name)) {
			bridge_names.add(bridge_name);
			Bridge server = new Bridge(0);
		}
		else
			System.out.println("Try with another bridge name, "+bridge_name+" already exists.");
		
	}
}

class EchoThread extends Thread {
    protected Socket socket;
	private Scanner server_input = null;
	private ObjectInputStream client_input = null;
	private ObjectOutputStream server_output = null;
	private static List<Socket> socketList = new ArrayList<>();
	private static HashMap<Socket,ObjectOutputStream> socket_out_stream_mapping = new HashMap<>();
	private static HashMap<String,Socket> mac_socket_mapping = new HashMap<>();


    public EchoThread(Socket clientSocket) {
        this.socket = clientSocket;
		socketList.add(socket);
    }

    public void run() {
		try {
			// takes input from terminal
			server_input = new Scanner(System.in);

			//input from Client
			client_input = new ObjectInputStream(socket.getInputStream());

			// sends output to the server socket
			server_output = new ObjectOutputStream(socket.getOutputStream());
			socket_out_stream_mapping.put(socket,server_output);
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
			try{
				DataFrame dFrame = (DataFrame)client_input.readObject();
				System.out.println(dFrame);
				for (String key : mac_socket_mapping.keySet()) {
					System.out.println(key + " : " + mac_socket_mapping.get(key));
				}
				
				if (dFrame.getType().equalsIgnoreCase("arpreply")) {
					if (!mac_socket_mapping.containsKey(dFrame.getSrcMAC())) {
						mac_socket_mapping.put(dFrame.getSrcMAC(), socket);
					}
					Socket s = mac_socket_mapping.get(dFrame.getDesMAC());
					sendDframeToClient(s,dFrame);
				}
				else if(dFrame.getType().equalsIgnoreCase("arprequest")){
					if (!mac_socket_mapping.containsKey(dFrame.getSrcMAC())) {
						mac_socket_mapping.put(dFrame.getSrcMAC(), socket);
					}
					sendDFrameToClients(dFrame);
				}
				else if(dFrame.getType().equalsIgnoreCase("message")){
					line = dFrame.getMsgPayload();
					// // message dframe doesn't have mac socket mapping
					// if (dFrame.getDesMAC().equalsIgnoreCase("FF:FF:FF:FF:FF")) {
					// 	mac_socket_mapping.put(dFrame.getSrcMAC(), socket);	
					// 	dFrame.setType("arpRequest");
					// 	sendMessageToClients("Client "+dFrame.getSrcStationName()+": "+line);
					// }
					// else{
						// message dframe has mac socket mapping
						System.out.println("Herereee...");
						Socket s = mac_socket_mapping.get(dFrame.getDesMAC());
						sendDframeToClient(s,dFrame);
					// }
				}
				System.out.println(mac_socket_mapping);
			}
		
			catch(IOException i){
				System.out.println(i);
			}
			catch(ClassNotFoundException c){
				System.out.println(c);
			}
			catch(Exception e){
				e.printStackTrace();
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
	private void sendMessageToClient(Socket s, String line) throws IOException {
		for (Socket other : socketList) {
			if (other == s) {
				DataOutputStream output = new DataOutputStream(other.getOutputStream());
				output.writeUTF(line);
			}
		}
	}

	private void sendDFrameToClients(DataFrame dFrame) throws IOException {
		for (Socket other : socketList) {
			if (other != socket) {
				// ObjectOutputStream output = new ObjectOutputStream(other.getOutputStream());
				socket_out_stream_mapping.get(other).flush();
				socket_out_stream_mapping.get(other).reset();
				socket_out_stream_mapping.get(other).writeObject(dFrame);
				socket_out_stream_mapping.get(other).flush();
				socket_out_stream_mapping.get(other).reset();
			}
		}
	}
	private void sendDframeToClient(Socket s, DataFrame dFrame) throws IOException {
		for (Socket other : socketList) {
			if (other == s) {
				// ObjectOutputStream output = new ObjectOutputStream(other.getOutputStream());
				socket_out_stream_mapping.get(other).flush();
				socket_out_stream_mapping.get(other).reset();
				socket_out_stream_mapping.get(other).writeObject(dFrame);
				socket_out_stream_mapping.get(other).reset();
				socket_out_stream_mapping.get(other).flush();
			}
		}
	}
}
