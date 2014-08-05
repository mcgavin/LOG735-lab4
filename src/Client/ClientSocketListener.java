package Client;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import XMLtool.xmlParser;
import Client.DataObject;

/**
 * socketWriter
 * 
 * classe runnable pour ecouter le socket et declancher des evenements selon le message
 * 
 * @author AJ60940
 *
 */
class ClientSocketListener extends Thread{
	
	private ObjectInputStream ois;
	private TunnelClientServeurFichier tunnel;
	private boolean running;
	Socket socket; 
	
	public ClientSocketListener(Socket clientSocket,TunnelClientServeurFichier tunnel ){
		try {
			this.tunnel =  tunnel;
			this.socket = clientSocket;
			ois = new ObjectInputStream(clientSocket.getInputStream());
			running = true;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		
		//Ecoute et traite les evenements
		while(running){
			try {
				
				String eventString  = ois.readObject().toString();
				if (eventString.startsWith("addRepo")){
					String[] s = eventString.split(":");
					//s[1]= repoPath, s[2]=name
					tunnel.modifyXML(s[1], s[2], "addRepo");
					
				}else if(eventString.startsWith("deleteRepo")){
					String[] s = eventString.split(":");
					//s[1]= repoPath, s[2]=name
					tunnel.modifyXML(s[1], null, "deleteRepo");
					
				}else if(eventString.startsWith("addFile")){
					String[] s = eventString.split(":");
					DataObject dataobj = xmlParser.xmlStringToObject(s[1]);
					tunnel.modifyXML(dataobj, "addFile");
				}
				else if(eventString.startsWith("deleteFile")){
					String[] s = eventString.split(":");
					DataObject dataobj = xmlParser.xmlStringToObject(s[1]);
					tunnel.modifyXML(dataobj, "deleteFile");
				}
				else if(eventString.startsWith("wholeXMl")){
					System.out.println("receiving new XML");
					byte[] content = (byte[]) ois.readObject();
					Path p = Paths.get("Client/metadata.xml");
					
					Files.delete(p);
					
					Files.write(Paths.get("Client/metadata.xml"), content);
					tunnel.updateClient();
					System.out.println("!!! OK !!!");
				}
				
			} catch (IOException | ClassNotFoundException e) {	
				
				//e.printStackTrace();
				
				try {
					
					this.socket.close();
					System.out.println("arf");
					running = false;
					
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		}
		this.tunnel.close();
		
	}
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
