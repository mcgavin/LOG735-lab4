package pointEntree;


public class ServeurFichierInfo {

	private int portClient;
	private int portServeur;
	private String ip;

	public ServeurFichierInfo(String ip,int portClient, int portServeur){
		this.portClient=portClient;
		this.portServeur=portServeur;
		this.ip=ip;
	}

	public String getServeurIp() {
		return ip;
	}

	public int getClientPort() {
		return portClient;
	}
	public int getServeurPort() {
		return portServeur;
	}
	
	public String toStringClient(){
		
		return ""+ip+":"+portClient;
		
	}
	public String toStringServeur(){
		
		return ""+ip+":"+portServeur;
		
	}

	public String toString(){
		
		return ""+ip+":"+portClient+":"+portServeur;
		
	}

}
