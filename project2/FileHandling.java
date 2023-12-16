import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandling {

    public static ArrayList<String> getBridgeList() throws IOException {
        ArrayList<String> result = new ArrayList<>();
        BufferedReader br = null;

        try {

            br = new BufferedReader(new FileReader("bridges"));

            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return result;
    }

    public static void addBridgeName(String name) {
        try {
            FileWriter fwrite = new FileWriter("bridges",true);
            
            // writing the content into the FileOperationExample.txt file
            fwrite.write(name+"\n");

            // Closing the stream
            fwrite.close();
        } catch (IOException e) {
            System.out.println("Unexpected error occurred");
            e.printStackTrace();
        }

    }

    public static void createSymbolicFiles(String name, String ipAddress, int portNumber){

        try{
            // Creating new ip address symbolic link file
            File file = new File("bridge-"+name+"-ip");   
            if (file.createNewFile()) {  
                System.out.println("Symbolic link File " + file.getName() + " is created successfully.");  
            } 
            else {  
                file.delete();
                file.createNewFile();
                System.out.println("Symbolic link File " + file.getName() + " is created successfully.");  
            }
            FileWriter fwrite = new FileWriter(file.getName());
            fwrite.write(ipAddress+"\n");
            fwrite.close();

            // Creating new port number symbolic link file
            file = new File("bridge-"+name+"-port");   
            if (file.createNewFile()) {  
                System.out.println("Symbolic link File " + file.getName() + " is created successfully.");  
            } 
            else {  
                file.delete();
                file.createNewFile();
                System.out.println("Symbolic link File " + file.getName() + " is created successfully.");  
            }
            fwrite = new FileWriter(file.getName());
            fwrite.write(portNumber+"\n");
            fwrite.close();
        } 
        catch (IOException exception) {  
            System.out.println("An unexpected error is occurred.");  
            exception.printStackTrace();  
        }   
    }
    
    public static String getBridgeIP(String fileName){
        String ipAddress = null;
        BufferedReader br = null;

        try {

            br = new BufferedReader(new FileReader(fileName));

            String line;
            while ((line = br.readLine()) != null) {
                ipAddress = line;
                break;
            }

            if (br != null) {
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ipAddress;
    }

    public static String getBridgePort(String fileName){
        String port = null;
        BufferedReader br = null;

        try {

            br = new BufferedReader(new FileReader(fileName));

            String line;
            while ((line = br.readLine()) != null) {
                port = line;
                break;
            }

            if (br != null) {
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return port;
    }

    public static List<Hostname> loadHostnames(){
        List<Hostname> hostnames = new ArrayList<>();
        BufferedReader br = null;

        try {

            br = new BufferedReader(new FileReader("hosts"));

            String line;
            while ((line = br.readLine()) != null) {
                String[] arr = line.split("[\t| ]+"); 
                Hostname hostname = new Hostname(arr[0],arr[1]);
                hostnames.add(hostname);
            }

            if (br != null) {
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hostnames;
    }

    public static List<RoutingTable> loadRoutingTable(String fileName){
        List<RoutingTable> routingTable = new ArrayList<>();
        BufferedReader br = null;

        try {

            br = new BufferedReader(new FileReader("rtables/"+fileName));

            String line;
            while ((line = br.readLine()) != null) {
                String[] arr = line.strip().split("[\s]+"); 
                RoutingTable routingTableEntry = new RoutingTable(arr[0],arr[1],arr[2],arr[3]);
                routingTable.add(routingTableEntry);
            }

            if (br != null) {
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return routingTable;
    }

    public static List<Interface> loadInterface(String fileName){
        List<Interface> interfaces = new ArrayList<>();
        BufferedReader br = null;

        try {

            br = new BufferedReader(new FileReader("ifaces/"+fileName));

            String line;
            while ((line = br.readLine()) != null) {
                String[] arr = line.strip().split("[\s]+"); 
                System.out.println(arr[0]);
                Interface interface1 = new Interface(arr[0],arr[1],arr[2],arr[3],arr[4]);
                System.out.println(interface1);
                interfaces.add(interface1);
            }

            if (br != null) {
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return interfaces;
    }
}
