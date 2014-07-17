package serveurDeFichier;


public class FichierInfo {

	private String nomfichier;
	private boolean isOpen;
	
	private int port;
	private String ip;

	public FichierInfo(String ip,int port){
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
