package serveurDeFichier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerThreadServeurFichier extends Thread{

	private int listeningPort;
	private ServerSocket serverSocket;
	
	private Socket clientSocket;
	private ServeurFichier serveurLocal;
	

	public ServerThreadServeurFichier (int listeningPort, ServeurFichier succursaleLocal) throws IOException
	{
		this.listeningPort = listeningPort;
		this.serveurLocal = succursaleLocal;
	}
	
	
	public void run() {
		
		try { 
			serverSocket = new ServerSocket(listeningPort); 
        } 
		catch (IOException e) 
        { 
			System.err.println("On ne peut pas écouter au port: " + Integer.toString(listeningPort) + "."); 
			System.exit(1); 
        }
		
		System.out.println ("En ecoute au port : " + listeningPort);
		while(true) {
			Socket clientSocket = null; 
			
			try { 
				clientSocket = serverSocket.accept(); 
				System.out.println("Connection réussi, port : " + clientSocket.getPort());
			} 
			catch (IOException e) 
		    { 
				System.err.println("Connection échoué, port : " + clientSocket.getPort()); 
				System.exit(1); 
		    } 
			
			
			TunnelServeurFichier tunnelServeurFichier = new TunnelServeurFichier(clientSocket,serveurLocal);
			serveurLocal.addTunnelServeurFichierToList(tunnelServeurFichier);
			
			serveurLocal.printAllTunnel();

		}
	}
}
