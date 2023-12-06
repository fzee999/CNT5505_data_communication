// A Java program for a Client
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.xml.crypto.Data;

public class Station {
	// initialize socket and input output streams
	private Socket socket = null;
	private Scanner client_input = null;
	private ObjectInputStream server_input = null;
	private ObjectOutputStream client_output = null;
    List<Hostname> hostnames;
    List<RoutingTable> routingTable;
    List<Interface> interfaces;
    private static HashMap<String,String> arpCache = new HashMap<>();
    // constructor to put ip address and port
	public Station(String type, String ifaceFileName, String rtableFileName, String hostFileName)
	{
		// establish a connection
		try {
            //load and store three files
            hostnames = FileHandling.loadHostnames();
            routingTable = FileHandling.loadRoutingTable(rtableFileName);
            interfaces = FileHandling.loadInterface(ifaceFileName);

            List<Socket> sockets = new ArrayList<>();

            //Queue of DATA Packets
            List<DataFrame> dFrameQueue = new ArrayList<>();

            //loading interface data structure and sockets
            for(Interface iface:interfaces){
                String bridgeName = iface.getBridgeName();
                String ipAddress = FileHandling.getBridgeIP("bridge-"+bridgeName+"-ip");
                String port = FileHandling.getBridgeIP("bridge-"+bridgeName+"-port");
                socket = new Socket(ipAddress,Integer.valueOf(port));
                sockets.add(socket);
            }
			// socket = new Socket(address, port);
			System.out.println("Connected");

			// takes input from terminal
			client_input = new Scanner(System.in);

            // sends output to the socket
			client_output = new ObjectOutputStream(socket.getOutputStream());

            //input from server
			server_input = new ObjectInputStream(socket.getInputStream());	

			// sendMessage thread 
			Thread sendMessage = new Thread(new Runnable()  
			{ 
				@Override
				public void run() { 
					synchronized (this){
						while (true) { 
							try { 
                                // System.out.println("msg: ");
								// read the message to deliver. 
								String msg = client_input.nextLine();
                                System.out.println("msg: "+msg);
                                if(msg.split(" ")[0].equalsIgnoreCase("send")){

                                    // Setting up Data Frame to send
                                    DataFrame dFrame = new DataFrame();
                                    dFrame.setType("message");
                                    dFrame.setMsgPayload(msg.substring(7));

                                    String srcStationName = interfaces.get(0).getName().substring(0, 1);
                                    dFrame.setSrcStationName(srcStationName);
                                    
                                    String desStationName = msg.split(" ")[1];
                                    dFrame.setDesStationName(desStationName);

                                    //set sender's IP address
                                    String srcIPAddress = null;
                                    for (Hostname hostname : hostnames) {
                                        if (hostname.getName().equalsIgnoreCase(srcStationName)) {
                                            srcIPAddress = hostname.getIpAddress();
                                        }
                                    }
                                    dFrame.setSrcIPAddress(srcIPAddress);

                                    //set destination's IP address
                                    String desIPAddress = null;
                                    for (Hostname hostname : hostnames) {
                                        if (hostname.getName().equalsIgnoreCase(desStationName)) {
                                            desIPAddress = hostname.getIpAddress();
                                        }
                                    }
                                    dFrame.setDesIPAddress(desIPAddress);
                                    
                                    //set source MAC address
                                    String srcMACAddress=interfaces.get(0).getEthernetAddress();
                                    dFrame.setSrcMAC(srcMACAddress);

                                    //set destination MAC Address
                                        // first search for next hop ip from routing table
                                    //.....need to be added
                                    System.out.println(dFrame);
                                    System.out.println(arpCache);
                                    if (arpCache.size()>0 && arpCache.containsKey(dFrame.getDesIPAddress())) {
                                        dFrame.setDesMAC(arpCache.get(dFrame.getDesIPAddress()));
                                        client_output.writeObject(dFrame);
                                    }
                                    else{
                                        dFrameQueue.add(dFrame);
                                        client_output.reset();
                                        DataFrame arpPacket = new DataFrame();
                                        arpPacket.setType("arprequest");
                                        arpPacket.setDesIPAddress(dFrame.getDesIPAddress());
                                        arpPacket.setDesMAC("FF:FF:FF:FF:FF");
                                        arpPacket.setSrcIPAddress(dFrame.getSrcIPAddress());
                                        arpPacket.setSrcMAC(dFrame.getSrcMAC());
                                        System.out.println(arpPacket);
                                        client_output.writeObject(arpPacket); 
                                    }
                                    
                                }
                                else{
                                    System.out.println("Please use 'send stationName msg' command");
                                }
								
							} catch (IOException e) { 
								e.printStackTrace(); 
							} catch (Exception e){
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
								DataFrame dFrame = (DataFrame)server_input.readObject();
                                System.out.println(dFrame);
                                //get type of the packet
                                String type = dFrame.getType();
                                if (dFrame.getDesIPAddress().equals(interfaces.get(0).getIpAddress())) {
                                    //Add src mac address to ARPCache
                                    if(!arpCache.containsKey(dFrame.getSrcIPAddress())){
                                        arpCache.put(dFrame.getSrcIPAddress(), dFrame.getSrcMAC());
                                    }

                                    if (type.equalsIgnoreCase("message")) {
                                        System.out.println("Client "+dFrame.getSrcStationName()+": "+dFrame.getMsgPayload()); 
                                    }
                                    else if (type.equalsIgnoreCase("arprequest")) {
                                        DataFrame arpPacket = new DataFrame();
                                        arpPacket.setSrcIPAddress(dFrame.getDesIPAddress());
                                        arpPacket.setDesIPAddress(dFrame.getSrcIPAddress());
                                        arpPacket.setDesMAC(dFrame.getSrcMAC());
                                        //get mac address from interface file
                                        arpPacket.setSrcMAC(interfaces.get(0).getEthernetAddress());
                                        arpPacket.setType("arpreply");
                                        client_output.writeObject(arpPacket);
                                    }
                                    else if (type.equalsIgnoreCase("arpreply")){
                                        DataFrame dFrame2 = dFrameQueue.remove(0);
                                        dFrame2.setDesMAC(dFrame.getSrcMAC());
                                        client_output.flush();
                                        client_output.reset();
                                        client_output.writeObject(dFrame2);
                                    }
                                }
                                else{
                                    System.out.println("packet discarded");
                                }
								
							} catch (IOException e) { 
		
								e.printStackTrace(); 
							} catch(ClassNotFoundException e){
                                e.printStackTrace();
                            } catch(Exception e){
                                e.printStackTrace();
                            }
						} 
					}
					
				} 
			}); 
            System.out.println("asd");
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

    public static DataFrame createDataFrame(DataFrame dFrame){
        
        return dFrame;
    }

	public static void main(String args[])
	{
        String type = args[0];
        String ifaceFileName = args[1];
        String rtableFileName = args[2];
        String hostFileName = args[3];
		Station client = new Station(type,ifaceFileName, rtableFileName, hostFileName);
	}

}
