package pointEntree;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class Communicator extends Thread {

	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	private PointEntree pointEntree;
	
	private int portDistant;
	private String ipDistant;

	public Communicator(Socket clientSocket, PointEntree pointEntree){

		try {
			
			this.pointEntree = pointEntree;
			
			ipDistant = clientSocket.getInetAddress().getHostAddress();
			portDistant = clientSocket.getPort();
			
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			ois = new ObjectInputStream(clientSocket.getInputStream());
			
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void run(){
		try {
			
			// Authentification
			// eventuellement processus authentification
			
			//type connection
			// if client or serveur
				//if client, give nextServeur| 
				//else if new ServeurFichier, send Liste ServeurFichier
			
			String typeConnection = (String) ois.readObject();
			System.out.println("Nouvelle Connection : typeConnection = " +typeConnection);
			
			
			String[] typeConnectionsplit = typeConnection.split(":");
			
			if(typeConnectionsplit[0].equals("client")){
				//for load balancing
				System.out.println("Nouveau client ");
				oos.writeObject(this.pointEntree.getIteratorNext().toStringClient());
				
			}else if (typeConnectionsplit[0].equals("serveur")) {
				
				//send all connection then add the new one
				String out = "";
				
				//if not empty
				if (! ( this.pointEntree.getListServeurFichierInfo().isEmpty()) ){
					for (ServeurFichierInfo fichierInfo : this.pointEntree.getListServeurFichierInfo()) {
						out+=fichierInfo.toStringServeur()+";";
					}
				}
				
				System.out.println("la liste envoye est : "+out);
				
				//send all connection
				
				oos.writeObject(out);
				this.pointEntree.addServeurFichier(new ServeurFichierInfo(
																	ipDistant,
																	Integer.parseInt(typeConnectionsplit[1]),//port client
																	Integer.parseInt(typeConnectionsplit[2]) //port serveur
																	)
													);
				
				
			} 
			
						
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Erreur "+e);
		}
	}

}
