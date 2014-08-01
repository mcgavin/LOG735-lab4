package serveurDeFichier;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.file.Files;

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
				Object event = ois.readObject();
				
				//String event = ois.readObject().toString();
				
				if(true){
					String eventString  = ois.readObject().toString();
					
					
					if (eventString.startsWith("file")){
						
						//XXX delete ouput
						System.out.println("Oh shit a file...");
						
						//expect partially filled dataObject ( name, owner, repo)
						String xml = (String) ois.readObject();
						
						DataObject dataObject = xmlParser.xmlStringToObject(xml);
						
						//XXX delete ouput
						System.out.println(dataObject);
						
						//create file 
						File f = new File(dataObject.getName()) ;
						
						//get file from user
						byte[] content = (byte[]) ois.readObject();

							Files.write(f.toPath(), content);
						


						
						//XXX delete ouput
						System.out.println("file transfered");
						
						
						//fill dataObject
						tunnel.addAndFillDataObject(dataObject);
						
						
					}else if(eventString.startsWith("transfer:")){
						
					}else if(eventString.startsWith("close")){
						System.out.println("close");
						running = false;
						tunnel.closeTunnel();
					}
					
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
		
	}
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
