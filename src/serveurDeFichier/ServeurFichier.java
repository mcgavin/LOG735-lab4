package serveurDeFichier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import pointEntree.ServeurFichierInfo;

public class ServeurFichier {

	private ServerThreadServeurFichier serverThread;
	private int ip;
	private int port;
	
	private static List<TunnelServeurFichier> listTunnelServeurFichier;
	
	

	public synchronized void addTunnelServeurFichierToList(TunnelServeurFichier tunnelServeurFichier) {
		listTunnelServeurFichier.add(tunnelServeurFichier);
    }
	
	
	public static void main(String[] args) throws IOException {
		System.out.println("**** Serveur de Fichier ****");
		
		//default port
		int pointEntreePort = 10000;
		String pointEntreeIp = "127.0.0.1";
		try{
			if (args.length==1) {
				pointEntreeIp = args[0];
				pointEntreePort = Integer.parseInt(args[1]);
			}
		}catch (NumberFormatException e) {
			System.err.println("port Invalide");
		}
		
		ServeurFichier serveurFichier = new ServeurFichier(pointEntreeIp , pointEntreePort);
	}
	
	
	
	public ServeurFichier(String pointEntreeIP, int pointEntreePort) {
		
		//default
		//pointEntreeIP = "127.0.0.1";
		//pointEntreePort = 12045;
		
		listTunnelServeurFichier = new ArrayList<TunnelServeurFichier>();
		
		//intervale random pour port entre 5000 et 10000
		this.port = (int) (5000 + (Math.random()* (10000-5000)));

		System.out.println("Mine Porte IST : "+ port);
		
		try {
			Socket s = new Socket(pointEntreeIP, pointEntreePort);
			
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

			oos.writeObject("serveur:"+port);
			
			String listServeurFichierInfo = (String) ois.readObject();
			
			String[] serveurConnection = listServeurFichierInfo.split(";");
			
			System.out.println(serveurConnection.length);
			
			if(listServeurFichierInfo.startsWith(";")){
				for (String serveurFichierInfo : serveurConnection) {
					
					System.out.println("serveurFichierInfo=" + serveurFichierInfo);
					
					String[] serverInfo = serveurFichierInfo.split(":");
					
					if(serverInfo.length >1){
						System.out.println(serverInfo[0]+ " : "+serverInfo[1]);
					}
					
					TunnelServeurFichier tunnel = new TunnelServeurFichier(
							new Socket(serverInfo[0], Integer.parseInt(serverInfo[1])),this);
					listTunnelServeurFichier.add(tunnel);
				} 
			}
			printAllTunnel();
			
			//thread qui ecoute les connection entrante
			serverThread = new ServerThreadServeurFichier(port, this);
			serverThread.start();
			
		} catch (Exception e) {
			e.printStackTrace();
//			System.err.println("ERREUR serveurThreadINIT : \n"+ e.toString());
		}
	}
	

	public int getPort() {
		return this.port;
	}
	
	public String printAllTunnel(){
		
		String output = "";
		for (TunnelServeurFichier tunnel : listTunnelServeurFichier) {
			
			//TODO add money to tunnel print
			output += "\n me -> "+tunnel;
			
		}
		return output;
	}

}





