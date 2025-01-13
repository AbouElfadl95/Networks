import java.io.*;
import java.net.*;
import java.util.ArrayList;
public class Server2 extends Thread{
	static ServerSocket serverSocket;
	static Socket server1;
	static ArrayList<String> server1MemberList = new ArrayList<String>();
	static ArrayList<String> memberList = new ArrayList<String>();
	
	
	public static ArrayList<String> getMemberList() {
		return memberList;
	}

	static ArrayList<ClientFinder> clientList = new ArrayList<ClientFinder>();
	DataOutputStream dos;
	BufferedReader fromClient;
	static DataOutputStream server1DOS;
	static BufferedReader fromServer1;
	
	public Server2(DataOutputStream dos, BufferedReader fromClient) throws IOException{
		this.dos=dos;
		this.fromClient=fromClient;
	}
	
	public String JoinResponse(String userName) throws IOException{
		for(int i=0; i<server1MemberList.size(); i++){
			if(server1MemberList.get(i).equals(userName)){
				dos.writeBytes("||TAKEN||"+'\n');
				return "";
			}
		}
		for(int i=0; i<memberList.size(); i++){
			if(memberList.get(i).equals(userName)){
				dos.writeBytes("||TAKEN||"+'\n');
				return "";
			}
		}
		server1DOS.writeBytes("NEW2|"+userName+'\n');
		dos.writeBytes("Welcome "+userName+"!"+'\n');
		memberList.add(userName);
		clientList.add(new ClientFinder(userName, this.dos));
		return userName;
	}
	
	public void remove(String userName) throws IOException{
		for(int i=0; i<memberList.size(); i++){
			if(memberList.get(i).equals(userName)){
				server1DOS.writeBytes("REMOVE2|"+userName+'\n');
				memberList.remove(i);
				break;
			}
		}
		for(int i=0; i<clientList.size(); i++){
			if(clientList.get(i).getName().equals(userName)){
				clientList.remove(i);
				return;
			}
		}
	}
	
	public void removeFromServer1(String userName){
		for(int i=0; i<server1MemberList.size(); i++){
			if(server1MemberList.get(i).equals(userName)){
				server1MemberList.remove(i);
				return;
			}
		}
	}
	
	public void MemberListResponse() throws IOException{
		ArrayList<String> allMembers = new ArrayList<String>();
		allMembers.addAll(server1MemberList);
		allMembers.addAll(memberList);
		dos.writeBytes(allMembers.toString()+'\n');
	}
	
	public void MemberListResponse1() throws IOException{
		dos.writeBytes(server1MemberList.toString()+'\n');
	}
	
	public void MemberListResponse2() throws IOException{
		dos.writeBytes(memberList.toString()+'\n');
	}
	
	public void run(){
		try{
			String clientName = null;
			while(true){
				if(fromClient.ready()){
					String clientMessage = fromClient.readLine();
					String[] s = clientMessage.split("\\|");
					String[] t = clientMessage.split("\\^");
					if(s[0].equals("JOIN")){
						clientName = JoinResponse(s[1]);
					}
					else {
						if(clientMessage.equals("GetMemberList")){
							MemberListResponse();
						}
						else {
							if(clientMessage.equals("GetMemberList1")){
								MemberListResponse1();
							}
							else {
								if(clientMessage.equals("GetMemberList2")){
									MemberListResponse2();
								}
								else {
									if(clientMessage.equals("BYE")||clientMessage.equals("QUIT")){
										remove(clientName);
										System.out.println(clientName + " disconnected.");
										break;
									}
									else {
										if(s[0].equals("NEW1")){
											server1MemberList.add(s[1]);
										}
										else {
											if(s[0].equals("REMOVE1")){
												removeFromServer1(s[1]);
											}
											else {
												if(t[0].equals("REDIRECT")){
													Route(t[1]+"|"+t[2],t[3]);
												}
												else{
													Route(s[0]+"|"+s[3],s[1]);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public void Route(String Message, String Destination) throws IOException{
		String[] s =Message.split("\\|");
		for(int i=0; i<clientList.size(); i++){
			if(clientList.get(i).getName().equals(Destination)){
				clientList.get(i).getDOS().writeBytes("From "+s[0]+": "+s[1]+'\n');
				return;
			}
		}
		for(int i=0; i<server1MemberList.size(); i++){
			if(server1MemberList.get(i).equals(Destination)){
				server1DOS.writeBytes("REDIRECT^"+s[0]+"^"+s[1]+"^"+Destination+'\n');
				return;
			}
		}
		dos.writeBytes("user doesn't exist"+'\n');
	}
	
	public static void main(String[] args) throws Exception {
		serverSocket = new ServerSocket(1234); 
		System.out.println("Server is running.");
		String ip = Inet4Address.getLocalHost().getHostAddress();
		
		server1 = new Socket("localhost", 9090);
		server1DOS = new DataOutputStream(server1.getOutputStream());
		fromServer1 = new BufferedReader(new InputStreamReader(server1.getInputStream()));
		(new Server2(server1DOS, fromServer1)).start();
		
		while(true){
			Socket connectionSocket = serverSocket.accept();
			System.out.println("A client is trying to connect.");
			BufferedReader fromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream dos = new DataOutputStream(connectionSocket.getOutputStream());
			(new Server2(dos, fromClient)).start();
		}
	}
}
