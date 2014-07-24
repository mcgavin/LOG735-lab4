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
class SocketListener implements Runnable{
	
	private ObjectInputStream ois;
	private AbstractTunnel tunnel;
	Socket socket; 
	
	public SocketListener(Socket clientSocket,AbstractTunnel tunnel ){
		try {
			this.tunnel =  tunnel;
			this.socket = clientSocket;
			ois = new ObjectInputStream(clientSocket.getInputStream());

			
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
		while(true){
			try {
				
				String event = ois.readObject().toString();
				
				if (event.startsWith("file")){
					
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
					
					
				}else if(event.startsWith("transfer:")){
					
				}
				
			} catch (IOException | ClassNotFoundException e) {	
				
				e.printStackTrace();
				
				try {
					
					this.socket.close();
					System.exit(1);
				} catch (IOException e1) {

					e1.printStackTrace();
				}
			}

		}
		
	}
}
