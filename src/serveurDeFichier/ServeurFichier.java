package serveurDeFichier;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import Client.DataObject;
import pointEntree.ServeurFichierInfo;

public class ServeurFichier {

	private FileServerConnectionServerThread fileServerThread;
	private ClientConnectionServerThread clientServerThread;
	private String serverFichierIP;
	
	//port de connection des clients
	private int portClient;
	//port de connection des serveurs
	private int portServeur;
	
	private static ArrayList<File> listFichier;
	
	private static List<TunnelServeurFichier> listTunnelServeurFichier;
	private static List<TunnelClient> listTunnelClient;
	
	private int uniqueId = 0;
	private String servID = "0";

	
	//***********************
	// SYNCHRO METHODS
	
	public synchronized int getUniqueFileId(){
	    return Integer.parseInt(""+servID + uniqueId++);
	}	
	
	public synchronized void addTunnelServeurFichierToList(TunnelServeurFichier tunnelServeurFichier) {
		listTunnelServeurFichier.add(tunnelServeurFichier);
    }
	
	
	//***********************
	// MAiN
	
	public static void main(String[] args) throws IOException {
		System.out.println("**** Serveur de Fichier ****");
		
		//default port
		int pointEntreePort = 12045;
		String pointEntreeIp = "127.0.0.1";
		try{
			if (args.length==1) {
				pointEntreeIp = args[0];
				pointEntreePort = Integer.parseInt(args[1]);
			}
		}catch (NumberFormatException e) {
			System.err.println("port Invalide");
		}
		
		new ServeurFichier(pointEntreeIp , pointEntreePort);
	}
	
	
	/**
	 * Contructeur 
	 * 
	 * 
	 * @param pointEntreeIP   : ip du point d<entrer du systeme de fichier
	 * @param pointEntreePort : port de connectino du point d<entrer du systeme de fichier
	 */
	public ServeurFichier(String pointEntreeIP, int pointEntreePort) {
		
		//default
		//pointEntreeIP = "127.0.0.1";
		//pointEntreePort = 12045;
		
		//outils de ligne de commande
		CommandLineTool cmd = new CommandLineTool(this);
		cmd.start();
		

		listTunnelServeurFichier = new ArrayList<TunnelServeurFichier>();
		listTunnelClient = new ArrayList<TunnelClient>();
		
		try {
			serverFichierIP = InetAddress.getLocalHost().getHostAddress();
			servID = serverFichierIP.replace(".","");
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		
		//intervale random pour port entre 5000 et 10000
		this.portClient = (int) (5000 + (Math.random()* (7000-5000)));
		this.portServeur = (int) (7000 + (Math.random()* (10000-7000)));
		
		
		System.out.println("Ecoute pour Client au port  : "+ portClient);
		System.out.println("Ecoute pour Serveur au port : "+ portServeur);
		
		
		
		try {
			Socket s = new Socket(pointEntreeIP, pointEntreePort);

			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

			
			//authentification
			oos.writeObject("serveur:"+portClient+":"+portServeur);
			
			
			//recoit la liste des serveurs a se connecter
			String listServeurFichierInfo;
			try {
				
				//READ FROM SOCKETS
				listServeurFichierInfo = (String) ois.readObject();
			
				System.out.println("list serveur : "+listServeurFichierInfo);
			
				//se connecte au autre serveur de fichier
				connectionAuPair(listServeurFichierInfo);
			
				printServerList();
				
			} catch (ClassNotFoundException e) {
					e.printStackTrace();
			}
	
			s.close();
			
			
			// SPAWN SERVER THREAD
			
			//thread qui ecoute les connection entrante des client
			clientServerThread = new ClientConnectionServerThread(portClient, this);
			clientServerThread.start();
			
			fileServerThread = new FileServerConnectionServerThread(portServeur, this);
			fileServerThread.start();
			

			
		}catch(SocketException e ){
			System.err.println("pointEntree est down, check ip: "+pointEntreeIP+":"+pointEntreePort );
			
		}catch (IOException e) {
			//impossible de creer une classe
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Se Connecter au autres serveur de fichier selon le string qui represente une liste d<adresse serveur
	 * 
	 * @param listServeurFichierInfo
	 */
	public void connectionAuPair(String listServeurFichierInfo){
		
		if(listServeurFichierInfo.length() > 0){
			
			//les serveurs sont separer par des "ip:port;ip:port"
			String[] serveurConnection = listServeurFichierInfo.split(";");
			
			
			//se connecte a chacun des serveur
			for (String serveurFichierInfo : serveurConnection) {
				
				System.out.println("connection a " + serveurFichierInfo);
				
				String[] serverInfo = serveurFichierInfo.split(":");
				
				try{
					TunnelServeurFichier tunnel = new TunnelServeurFichier(
							new Socket(serverInfo[0], Integer.parseInt(serverInfo[1])),this);
					
					addTunnelServeurFichierToList(tunnel);
					
				}catch(IOException exception){
					
					System.err.println(serveurFichierInfo+ " est down, impossible de se connecter");
					
					//TODO
					//notify PointEntree a host is down
					//notify les autres serveur que le serveur en questin est down
				}

			}
		}else{
			System.out.println("Aucun autre serveur connecter au systeme");
		}
	}
	
	
	//***********************
	// GET METHODE
	
	public String getIpAdress() {
		return this.serverFichierIP;
	}
	
	public int getClientConnectionPort() {
		return this.portClient;
	}
	
	public int getServeurConnectionPort() {
		return this.portServeur;
	}
	

	
	
	public void addAndFillDataObject(DataObject newDataObject){
		newDataObject.setServer(getIpAdress());
		newDataObject.setPort(getClientConnectionPort());
		newDataObject.setId(getUniqueFileId());

		//modifyMetaData( new action(add), newDataObject.getrepo());
	}
	
	public void modifyMetaData(  DataObject dataObject){
		
//		mySchemaXMLRootNode
		
	}
	
	
	//***********************
	//PRINT METHOD
	/**
	 * printServerList
	 * imprime a la console la liste des serveur de fichier connecter
	 * @return
	 */
	public String printServerList(){
		
		String output = "***********************************\n**  Liste de Serveur de fichier  ** \n***********************************";
		for (TunnelServeurFichier tunnel : listTunnelServeurFichier) {
			output += "\n"+tunnel;
		}
		
		System.out.println(output+"\n");
		return output;
	}

	/**
	 * printClientList
	 * imprime a la console la liste des client connecter a ce serveur de fichier
	 * @return
	 */
	public String printClientList(){
		
		String output = "*********************************\n**  Liste de Client  ** \n*********************************\n";
		for (TunnelClient tunnel : listTunnelClient) {
			output += " me -> "+tunnel+"\n";
		}
		System.out.println(output);
		return output;
	}

	
}





