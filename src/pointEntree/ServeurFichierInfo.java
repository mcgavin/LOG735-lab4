package pointEntree;


public class ServeurFichierInfo {

	private int port;
	private String ip;

	public ServeurFichierInfo(String ip,int port){
		this.port=port;
		this.ip=ip;
	}

	public String getServeurIp() {
		return ip;
	}

	public int getServeurPort() {
		return port;
	}

	public String toString(){
		
		return ""+ip+":"+port;
		
	}

}
