package serveurDeFichier;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;

import Client.DataObject;

public class TunnelClient extends AbstractTunnel{
	
	
	private String ipLocal;
	private int portLocal;
	
	private String ipDistant;
	private int portDistant;
	
	private SocketWriter outputTread;
	private SocketListener inputTread;
	
	public TunnelClient(Socket clientSocket, ServeurFichier serveurFichierLocal){
		super();
		
		ipLocal = clientSocket.getLocalAddress().getHostAddress();
		portLocal = clientSocket.getLocalPort();
		
		ipDistant = clientSocket.getInetAddress().getHostAddress();
		portDistant = clientSocket.getPort();
		this.serveurFichierLocal = serveurFichierLocal;

		
		try {
			
			outputTread = new SocketWriter(clientSocket,writeList );
			inputTread = new SocketListener(clientSocket,this );
			inputTread.start();
			outputTread.start();
			
			
			
		} catch (Exception e) {
			System.err.println("failed to create Object or cast readObject");
			closeTunnel();
		}
	}
	
	public void sendFile(DataObject dataObj){
		byte[] content;
		try {
			File fileToSend = new File(dataObj.getRelName());
			
			content = Files.readAllBytes(fileToSend.toPath());
			writeList.add("file");
			writeList.addObject(content);
			
		} catch (IOException e) {
			System.err.println("error getting file");
			e.printStackTrace();
		}

	}

	public void modifyXML(String repoPath, String name,String action){
		serveurFichierLocal.modifyXmlFromRepo(action, repoPath , name,"all");
	}
	
	public void modifyXML(DataObject dataObject,String action){
		serveurFichierLocal.modifyXml(dataObject, action, "all");
	}
	
	public void closeTunnel(){
		inputTread.setRunning(false);
		outputTread.setRunning(false);
		this.serveurFichierLocal.removeTunnelClientFromList(this);
	}
	
	public String toString(){
		
		return ""+ipLocal+":"+portLocal+" --> "+ipDistant+":"+portDistant;
	}


}

	
	
