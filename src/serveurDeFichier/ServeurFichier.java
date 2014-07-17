package serveurDeFichier;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import pointEntree.ServeurFichierInfo;

public class ServeurFichier {

	private ServerThreadServeurFichier serverThread;
	private int ip;
	private int port;
	
	private static ArrayList<File> listFichier;
	
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

		System.out.println("Mon port est : "+ port);
		
		try {
			Socket s = new Socket(pointEntreeIP, pointEntreePort);

			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

			oos.writeObject("serveur:"+port);
			
			
			//recoit la liste des serveurs a se connecter
			String listServeurFichierInfo;
			try {
				listServeurFichierInfo = (String) ois.readObject();
				


			
				//if not empty
				if(listServeurFichierInfo.length() > 0){
					
					//les serveurs sont separer par des "ip:port;ip:port"
					String[] serveurConnection = listServeurFichierInfo.split(";");
					
					
					for (String serveurFichierInfo : serveurConnection) {
						
						System.out.println("connection a " + serveurFichierInfo);
						
						String[] serverInfo = serveurFichierInfo.split(":");
						
						try{
							TunnelServeurFichier tunnel = new TunnelServeurFichier(
									new Socket(serverInfo[0], Integer.parseInt(serverInfo[1])),this,"serveur");
							listTunnelServeurFichier.add(tunnel);
							
						}catch(SocketException exception){
							
							System.err.println(serveurFichierInfo+ " est down, impossible de se connecter");
							
							//TODO
							//notify PointEntree a host is down
							//notify les autres serveur que le serveur en questin est down
						}
		
					}
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			printAllTunnel();
			
			//thread qui ecoute les connection entrante
				serverThread = new ServerThreadServeurFichier(port, this);

			serverThread.start();
			

		}catch(SocketException e ){
			System.err.println("pointEntree est down, check ip: "+pointEntreeIP+":"+pointEntreePort );
			
		}catch (IOException e) {
			//impossible de creer une classe
			e.printStackTrace();
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





