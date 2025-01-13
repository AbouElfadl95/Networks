import java.io.DataOutputStream;


public class ClientFinder {
	String name;
	DataOutputStream dos;
	
	public ClientFinder(String name,DataOutputStream dos){
		this.name = name;
		this.dos = dos;
	}
	public String getName(){
		return this.name;
	}
	public DataOutputStream getDOS(){
		return this.dos;
	}
}
