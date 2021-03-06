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
import java.util.List;

import XMLtool.UpdateMetadata;
import Client.DataObject;

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
	// MAIN
	
	
	/**
	 * execute with
	 * 
	 * ServeurFichier ipPointEntrer portPointEntre portClient portServeur 
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("**** Serveur de Fichier ****");
		
		
		//default port
		int pointEntreePort = 12045;
		int portClient = 2015;
		int portServeur = (int) (7000 + (Math.random()* (10000-7000)));
		String pointEntreeIp = "127.0.0.1";
		
		try{
			
//			System.out.println("nb ARG = " +args.length);
			if (args.length==2) {
				pointEntreeIp = args[0];
				pointEntreePort = Integer.parseInt(args[1]);
				
			}else if (args.length==4) {
				pointEntreeIp = args[0];
				pointEntreePort = Integer.parseInt(args[1]);
				portClient = Integer.parseInt(args[2]);
				portServeur = Integer.parseInt(args[3]);
			}
		}catch (NumberFormatException e) {
			System.err.println("port Invalide");
		}
		
		new ServeurFichier(pointEntreeIp , pointEntreePort,portClient,portServeur);
	}
	
	
	/**
	 * Constructeur 
	 * 
	 * @param pointEntreeIP   : ip du point d<entrer du systeme de fichier
	 * @param pointEntreePort : port de connectino du point d<entrer du systeme de fichier
	 */
	public ServeurFichier(String pointEntreeIP, int pointEntreePort, int portClient , int portServeur) {
		

		listTunnelServeurFichier = new ArrayList<TunnelServeurFichier>();
		listTunnelClient = new ArrayList<TunnelClient>();
		
		try {
			serverFichierIP = InetAddress.getLocalHost().getHostAddress();
			this.portClient =portClient;
			this.portServeur =portServeur;
			servID = serverFichierIP.replace(".","");
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		
		//intervale random pour port entre 5000 et 10000
//		this.portClient = (int) (5000 + (Math.random()* (7000-5000)));

		
		
		System.out.println("Ecoute pour Client au port  : "+ portClient);
		System.out.println("Ecoute pour Serveur au port : "+ portServeur);
		
		
		
		try {
			
			//connection point entrer et au pair
			connectionPointEntrer(pointEntreeIP,pointEntreePort);
			
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
		//outils de ligne de commande
		CommandLineToolServeur cmd = new CommandLineToolServeur(this);
		cmd.start();
	}
	
	public void connectionPointEntrer(String pointEntreeIP, int pointEntreePort) throws UnknownHostException, IOException{
		
		Socket s = new Socket(pointEntreeIP, pointEntreePort);

		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

		
		//authentification
		oos.writeObject("serveur:"+portClient+":"+portServeur);
		
		
		//recoit la liste des serveurs a se connecter
		String listServeurFichierInfo = "";
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

	}
	
	
	/**
	 * Se Connecter au autres serveur de fichier selon le string qui represente une liste d'adresse serveur
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
				boolean xmlAsked = false;
				try{
					TunnelServeurFichier tunnel = new TunnelServeurFichier(
							new Socket(serverInfo[0], Integer.parseInt(serverInfo[1])),this);
					
					addTunnelServeurFichierToList(tunnel);
					
					//ask the xml to the first tunnel that is up
					if(tunnel.isTunnelUp() && !xmlAsked){
						tunnel.writeList.add("sendMeXML");
						xmlAsked = true;
					}
					
				}catch(IOException exception){
					
					System.err.println(serveurFichierInfo+ " est down, impossible de se connecter");
					
					//TODO
					//notify PointEntree a host is down
					//notify les autres serveur que le serveur en questin est down
				}

			}
		}else{
			System.err.println("Aucun autre serveur connecter au systeme");
		}
	}
	
	//***********************
	// SYNCHRO METHODS
	
	public synchronized int getUniqueFileId(){
	    return Integer.parseInt(""+servID + uniqueId++);
	}	
	
	public synchronized void addTunnelServeurFichierToList(TunnelServeurFichier tunnelServeurFichier) {
		listTunnelServeurFichier.add(tunnelServeurFichier);
    }
	
	public synchronized void removeTunnelServeurFichierFromList(TunnelServeurFichier tunnelServeurFichier) {
		listTunnelServeurFichier.remove(tunnelServeurFichier);
    }
	
	
	public synchronized void addTunnelClientToList(TunnelClient tunnelClient) {
		listTunnelClient.add(tunnelClient);
    }
	
	public synchronized void removeTunnelClientFromList(TunnelClient tunnelClient) {
		listTunnelClient.remove(tunnelClient);
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
	

	
	/**
	 * 
	 * @param newDataObject
	 * @param action	  {addFile |addRepo}
	 * @param destination {client |all}
	 */
	public void modifyXml(DataObject dataObject, String action,String destination){
		
		if (action.equals("addFile")){
			UpdateMetadata.AddNewFile(dataObject);
			
			if (destination.equals("client")){
				broacastToCLients(dataObject,action );
			}else{
				broacastToALL(dataObject,action );
			}
		}else if (action.equals("deleteFile")){
			
			UpdateMetadata.DeleteFile(dataObject);
			if (destination.equals("client")){
				broacastToCLients(dataObject,action );
			}else{
				broacastToALL(dataObject,action );
			}
		}else{
		System.err.println("ERROR");
		}
		
	}
	
	/**
	 * 
	 * @param repoPath
	 * @param action	  {|deleteRepo |addRepo}
	 * @param destination {client |all}
	 */
	public void modifyXmlFromRepo(String action, String repoPath, String name, String destination) {

		if (action.equals("addRepo")){
			
			UpdateMetadata.AddRepo(repoPath, name);
			if (destination.equals("client")){
				broacastToCLients(repoPath, name, action );
			}else{
				broacastToALL(repoPath, name, action );
			}
		}else if (action.equals("deleteRepo")){
			
			UpdateMetadata.DeleteRepo(repoPath);
			if (destination.equals("client")){
				broacastToCLients(repoPath, name, action );
			}else{
				broacastToALL(repoPath, name, action );
			}
		}
	}
	
	private void broacastToALL(DataObject newDataObject, String action) {
		for (TunnelServeurFichier tunnel : listTunnelServeurFichier) {
			tunnel.sendXmlModification(newDataObject, action);
		}
		broacastToCLients( newDataObject,action);
	}
	
	private void broacastToCLients(DataObject newDataObject,String action) {
		for (TunnelClient tunnel : listTunnelClient) {
			tunnel.sendXmlModification(newDataObject,action);
		}
	}
	
	
	
	private void broacastToALL(String repoPath,String name,String action) {
		for (TunnelServeurFichier tunnel : listTunnelServeurFichier) {
			tunnel.sendXmlModification(repoPath, name, action);
		}
		broacastToCLients(repoPath, name, action);
	}
	private void broacastToCLients(String repoPath,String name,String action) {
		for (TunnelClient tunnel : listTunnelClient) {
			tunnel.sendXmlModification( repoPath, name, action);
		}
	}

	
	
	public void modifyMetaData(  DataObject dataObject){
		
//		mySchemaXMLRootNode
		
	}
	
	/**
	 * stop every thread properly
	 * 
	 * @param tunnelServeurFichier
	 */
	public void stopServeurFichier() {
		
		if(clientServerThread.isAlive()){
			clientServerThread.stopServerThread();
		}
		if (!listTunnelClient.isEmpty()){
			for (TunnelClient tunnel : listTunnelClient) {
				tunnel.closeTunnel();
			}
		}
		
		
		if(fileServerThread.isAlive()){
			fileServerThread.stopServerThread();
		}
		if(!listTunnelServeurFichier.isEmpty()){
			for (TunnelServeurFichier tunnel : listTunnelServeurFichier) {
				tunnel.closeTunnel();
			}
		}

    }
	
	
	
	
	
	//***********************
	//PRINT METHOD
	/**
	 * printServerList
	 * imprime a la console la liste des serveur de fichier connecter
	 * @return
	 */
	public String printServerList(){
		
		String output = "***********************************\n"+
						"**  Liste de Serveur de fichier  **\n"+
						"***********************************";
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
		
		String output = "*********************************\n"+
						"*******  Liste de Client  *******\n"+
						"*********************************\n";
		for (TunnelClient tunnel : listTunnelClient) {
			output += " me -> "+tunnel+"\n";
		}
		System.out.println(output);
		return output;
	}


	public String printLocalInfo() {
		String s ="";
		
		s += serverFichierIP+":"+ portClient +":"+ portServeur+"\n";
				
		return s;
	}


	

	
}





