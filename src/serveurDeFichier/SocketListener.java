package serveurDeFichier;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.UUID;

import XMLtool.UpdateMetadata;
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
class SocketListener extends Thread{
	
	private ObjectInputStream ois;
	private AbstractTunnel tunnel;
	private boolean running;
	Socket socket; 
	
	public SocketListener(Socket clientSocket,AbstractTunnel tunnel ){
		try {
			this.tunnel =  tunnel;
			this.socket = clientSocket;
			ois = new ObjectInputStream(clientSocket.getInputStream());
			running = true;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public SocketListener(ObjectInputStream objectInputStream,AbstractTunnel tunnel ){
//		
//			this.tunnel =  tunnel;
//			
//			ois = objectInputStream;
//	}
	
	public void run() {
		
		//Ecoute et traite les evenements
		while(running){
			try {
				if(true){
					String eventString  = ois.readObject().toString();
					
					
					if (eventString.startsWith("file")){
						
						System.out.println("Receiving file");
						
						//expect partially filled dataObject ( name, owner, repo)
						String xml = (String) ois.readObject();
						
						DataObject dataObject = xmlParser.xmlStringToObject(xml);
						String uniqueId = UUID.randomUUID().toString();
						dataObject.setId(uniqueId);
						dataObject.setRelName(Integer.toString(dataObject.hashCode()));
						
						//XXX delete ouput
						System.out.println(dataObject);
						
						//create file 
						File f = new File(dataObject.getRelName()) ;
						
						//get file from user
						byte[] content = (byte[]) ois.readObject();

						Files.write(f.toPath(), content);
						
						//XXX delete ouput
						System.out.println("file transfered");
						
						//UPDATE XML AND BROADCAST
						tunnel.modifyXML(dataObject, "addFile");
						
						
					}else if(eventString.startsWith("download")){
						String[] s = eventString.split(":");
						DataObject dataobj = xmlParser.xmlStringToObject(s[1]);
						
						tunnel.sendFile(dataobj);
						
					}else if(eventString.startsWith("close")){
						System.out.println("close");
						running = false;
						tunnel.closeTunnel();
						
						
					}else if(eventString.startsWith("addFile")){
						String[] s = eventString.split(":");
						DataObject dataobj = xmlParser.xmlStringToObject(s[1]);
						//Aviser mes clients
						tunnel.modifyXML(dataobj, "addFile");
						
					}else if(eventString.startsWith("deleteFile")){
						String[] s = eventString.split(":");
						DataObject dataobj = xmlParser.xmlStringToObject(s[1]);
						//Aviser mes clients
						tunnel.modifyXML(dataobj, "deleteFile");
						
					}else if(eventString.startsWith("addRepo")){
						
						String[] s = eventString.split(":");
						//s[1]= repoPath, s[2]=name
						tunnel.modifyXML(s[1], s[2], "addRepo");
						
					}else if(eventString.startsWith("deleteRepo")){
						
						String[] s = eventString.split(":");
						//s[1]= repoPath
						tunnel.modifyXML(s[1], null, "deleteRepo");
					}
					
				}
				
				
				
			} catch (IOException | ClassNotFoundException e) {	
				
				//e.printStackTrace();
				
				try {
					
					this.socket.close();
//					System.out.println("arf");
					setRunning(false);
					tunnel.closeTunnel();
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
