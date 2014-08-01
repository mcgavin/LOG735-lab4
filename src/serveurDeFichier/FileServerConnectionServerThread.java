package serveurDeFichier;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class FileServerConnectionServerThread extends Thread{

	private int listeningPort;
	private ServerSocket serverSocket;
	
	private ServeurFichier serveurFichierLocal;
	
	private boolean running; 

	public FileServerConnectionServerThread (int listeningPort, ServeurFichier serveurFichierLocal) throws IOException
	{
		this.listeningPort = listeningPort;
		this.serveurFichierLocal = serveurFichierLocal;
	}
	
	
	public void run() {
		running = true;
		try { 
			serverSocket = new ServerSocket(listeningPort); 
        } 
		catch (IOException e) 
        { 
			System.err.println("On ne peut pas écouter au port: " + Integer.toString(listeningPort) + "."); 
			System.exit(1); 
        }
		
		System.out.println ("En ecoute au port : " + listeningPort);
		while(running) {
			Socket clientSocket = null; 
			
			try { 
				clientSocket = serverSocket.accept(); 
				System.out.println("Connection serveur entrante réussi, port : " + clientSocket.getPort());
			} 
			catch (IOException e) 
		    { 
				System.err.println("Connection serveur entrante échoué, port : " + clientSocket.getPort()); 
				System.exit(1); 
		    } 
			
			TunnelServeurFichier tunnelServeurFichier = new TunnelServeurFichier(clientSocket,serveurFichierLocal);
			
			serveurFichierLocal.addTunnelServeurFichierToList(tunnelServeurFichier);
			
			serveurFichierLocal.printServerList();
		}
	}
	/**
	 * arrete les connection entrante
	 */
	public void stopServerThread(){
		running = false;
	}
}
