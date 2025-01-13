import java.io.*;
import java.net.*;
import java.util.ArrayList;
public class Client{
	public Socket socket;
    DataOutputStream dos;
	BufferedReader serverResponse;
	BufferedReader userInput;
	public String userName;
	String currentDest;
	static int Server1Port = 9090;
	static int Server2Port = 1234;
	
	public Client(int Port) throws UnknownHostException, IOException{
		String ip = Inet4Address.getLocalHost().getHostAddress();
		socket = new Socket("localhost", Port);
		System.out.println("Connected to server!");
		dos = new DataOutputStream(socket.getOutputStream());
		serverResponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		userInput = new BufferedReader(new InputStreamReader(System.in));
		userName=null;
	}
	
	public String join(String name) throws UnknownHostException, IOException{
		dos.writeBytes("JOIN|"+name+'\n');
		String sr = serverResponse.readLine();
		while(sr.equals("||TAKEN||")){
			System.out.println("Username already taken. Please enter another." + '\n');
			name = userInput.readLine();
			dos.writeBytes("JOIN|"+name+'\n');
			sr = serverResponse.readLine();
		}
		System.out.println(sr);
		return name;
	}
	
	public void chat(String source, String destination, int ttl, String message) throws IOException{
		if (ttl == 0)
			return;
		else {
			if((ttl == 1) && (sameServer(source,destination)))
				return;
			else {
				dos.writeBytes(source + "|" + destination + "|"+ ttl + "|" + message + '\n');
			}
		}
		
		
	}
	
	public String getMemberList() throws IOException{
		dos.writeBytes("GetMemberList"+'\n');
		System.out.println("ALL Members:");
		return serverResponse.readLine();
	}
	
	public String getMemberList1() throws IOException{
		dos.writeBytes("GetMemberList1"+'\n');
		System.out.println("Members Of Server 1:" + '\n');
		return serverResponse.readLine();
	}
	
	public String getMemberList2() throws IOException{
		dos.writeBytes("GetMemberList2"+'\n');
		System.out.println("Members Of Server 2:" + '\n');
		return serverResponse.readLine();
	}
	
	public boolean sameServer(String X, String Y) {
		if((Server2.getMemberList().contains(X)) && (Server2.getMemberList().contains(Y)))
			return true;
		else {
			if((Server1.getMemberList().contains(X)) && (Server1.getMemberList().contains(Y)))
				return true;
			else
				return false;
		}
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Client client = new Client(Server1Port);
		String clientMessage;
		System.out.println("Please enter your username.");
		client.userName = client.join(client.userInput.readLine());
		System.out.println("To chat with someone, enter their username followed by a : and then your message. ");
		while(true){
			while(!client.serverResponse.ready()){
				if(client.userInput.ready()){
					clientMessage = client.userInput.readLine();
					String[] s = clientMessage.split(":");
					if(clientMessage.equals("GML"))
						System.out.println(client.getMemberList());
					else {
						if(clientMessage.equals("GML1"))
							System.out.println(client.getMemberList1());
						else {
							if(clientMessage.equals("GML2"))
								System.out.println(client.getMemberList2());
							else {
								if(clientMessage.equalsIgnoreCase("BYE") || clientMessage.equals("QUIT")){ 
									client.dos.writeBytes(clientMessage+'\n');
									client.socket.close();
									System.out.println("Disconnected from server.");
								}
								else{
									client.chat(client.userName, s[0], 1, s[1]);
								}
							}	
						}	
					}
				}	
			}
			String sr = client.serverResponse.readLine(); 
			System.out.println(sr);
		}
	}
}