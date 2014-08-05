package serveurDeFichier;

import java.io.IOException;
import java.net.Socket;

import Client.DataObject;

public class TunnelServeurFichier extends AbstractTunnel{
	
	
	private String ipLocal;
	private int portLocal;
	
	private String ipDistant;
	private int portDistant;
	
	private SocketWriter outputTread;
	private SocketListener inputTread;
	
	
	//instancier a partire de 0
	public TunnelServeurFichier(Socket clientSocket, ServeurFichier serveurFichierLocal){
		
			super();
			ipLocal = clientSocket.getLocalAddress().getHostAddress();
			portLocal = clientSocket.getLocalPort();
			
			ipDistant = clientSocket.getInetAddress().getHostAddress();
			portDistant = clientSocket.getPort();
			
			this.serveurFichierLocal = serveurFichierLocal;
	
			try {
				outputTread = new SocketWriter(clientSocket,writeList);
				inputTread = new SocketListener(clientSocket,this );
				outputTread.start();
				inputTread.start();
			} catch (IOException e) {

				closeTunnel();
			}
			
	}
	

			
			
	public void closeTunnel(){
		outputTread.setRunning(false);
		inputTread.setRunning(false);
		super.setTunnelDown();
		this.serveurFichierLocal.removeTunnelServeurFichierFromList(this);
		
	}
	
	public String toString(){
		
		return ""+ipLocal+":"+portLocal+" --> "+ipDistant+":"+portDistant;
	}

	public void modifyXML(String repoPath, String name,String action){
		serveurFichierLocal.modifyXmlFromRepo(action, repoPath , name,"client");
	}
	public void modifyXML(DataObject dataObject,String action){
		serveurFichierLocal.modifyXml(dataObject, action, "client");
	}

	@Override
	public void sendFile(DataObject dataObj) {
		// TODO Auto-generated method stub
		
	}





	
	
}


