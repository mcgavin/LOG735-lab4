package Client;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainClient {

	private GUIWindow window;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Socket socket;
	//private String infoServer;
	private String[] fileServerInfo;
	private int nbfois=0;
	private boolean isConnectedServeurFichier;
	
	private ModelDAO xmlHandler;
	private TunnelClientServeurFichier tunnelServeurFichier;
	
	public static void main(final String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					//default port
					int pointEntreePort = 12045;
					String pointEntreeIp = "127.0.0.1";
					try{
						System.out.println(args.length);
						if (args.length==2) {
							pointEntreeIp = args[0];
							pointEntreePort = Integer.parseInt(args[1]);
						}else{
							System.err.println("NO argument to connect to point entrer, taking default : "+pointEntreeIp+":"+pointEntreePort );
						}
					}catch (NumberFormatException e) {
						System.err.println("port Invalide");
					}
					
					
					ModelDAO xmlHandler = new ModelDAO();
					GUIWindow window = new GUIWindow(xmlHandler);
					MainClient mainclient = new MainClient(xmlHandler,window,pointEntreeIp,pointEntreePort );
					
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}
	
	public MainClient(ModelDAO xmlHandler,GUIWindow window, String pointEntreeIp, int pointEntreePort){
				
		window.addController(this);
		this.window = window;
		isConnectedServeurFichier = false;
		
		
		try {
			while ((!isConnectedServeurFichier) && nbfois<3  ){
				//connection au point d'entrer
				
				
				fileServerInfo = connexionPointEntrer(pointEntreeIp, pointEntreePort, nbfois++);
	
				isConnectedServeurFichier = connexionServeurFichier(fileServerInfo[0],Integer.parseInt(fileServerInfo[1]));
				
			}
		
		} catch (IOException | ClassNotFoundException e ){
			
			System.err.println("\nIncapable de se connecter au point d'entrer du systeme");
			
		}
		
		System.out.println("\n*****************************************************************************\n"
						   + "** Veuillez utiliser l'utilitaire de ligne de commande pour vous connecter si vous n'etes pas connecte **"
					      +"\n*****************************************************************************");
		
		CommandLineToolClient cmd = new CommandLineToolClient(this);
		cmd.start();
		
	}
	
	/**
	 * 
	 * @param serveurIP
	 * @param port : serveur port
	 */
	public boolean connexionServeurFichier(String serveurIP, int port){
		
//		boolean connectionSucceed = false;
		
		System.out.println("Connection au serveur de fichier");
		
		try{
			
//			this.fileServerInfo = infoServer.split(":");
			
			System.out.println("connexion au serveur "+ serveurIP+":"+port);
			socket = new Socket(serveurIP,port);
			
			isConnectedServeurFichier = true;
			System.out.println("connexion reussis");
			
			tunnelServeurFichier = new TunnelClientServeurFichier(socket, this);
			
		} catch (IOException  e ) {
			System.err.println("Impossible de ce connecter au serveur : " + serveurIP+":"+port);
		}
		
		return isConnectedServeurFichier;
	}
	
	/**
	 * Connection au point d'entrer
	 * @param pointEntrerIP
	 * @param port
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Deprecated
	public String connexionPointEntrer(String pointEntrerIP, int port) throws IOException, ClassNotFoundException, UnknownHostException{
		
		//connection au point d'entrer
		System.out.print("connexion au point entrer : ");
		
		socket = new Socket(pointEntrerIP, port);
		
		System.out.print(" ok\n");
			
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
		
		//specifie l'intention = { client | serveur | download }
		oos.writeObject("client");
		
		//information du serveur de fichier a se connecter
		String infoServer = (String) ois.readObject();
		
		//fermeture de la connection avec le point d'entrer
		socket.close();
		return infoServer;
	}
	

	/**
	 * 
	 * @param pointEntrerIP
	 * @param port
	 * @param nbFois
	 * @return
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public String[] connexionPointEntrer(String pointEntrerIP, int port,int nbFois) throws UnknownHostException, IOException, ClassNotFoundException{
		
			System.out.print("connexion au point entrer : ");
			socket = new Socket(pointEntrerIP, port);
			System.out.print(" ok \n");
			
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			if (nbFois>0){
				oos.writeObject("client:" + fileServerInfo[0]+":"+fileServerInfo[1]);
			}else{
				oos.writeObject("client");
			}
			
			String infoServer = (String) ois.readObject();
			
			fileServerInfo = infoServer.split(":");

			socket.close();

			return fileServerInfo;
	}
	
	
	/**
	 * 
	 */
	public void sendFile(DataObject dataObj, File fileToSend){
		
			System.out.println("envoie d'un fichier");
			dataObj.setServer(fileServerInfo[0]);
			dataObj.setPort(Integer.parseInt(fileServerInfo[1]));
			tunnelServeurFichier.sendFile( dataObj, fileToSend);
	}
	

	
	public void downloadFile(DataObject dataObj){
		
		Socket s;
		try {
			
			s = new Socket(dataObj.getServer(), dataObj.getPort());
			new TunnelDownloadFichier(s, dataObj);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	public void sendXMLModification(String repoPath, String name, String action) {
		tunnelServeurFichier.sendXMLModification(repoPath, name, action);
	}
	
	public void sendXMLModification(DataObject dataObject) {
		tunnelServeurFichier.sendXMLModification(dataObject);
	}

	public void refresh(){
		window.refreshTree();
	}

	public ModelDAO getXmlHandler() {
		return xmlHandler;
	}

	public boolean isConnectedServeurFichier() {
		return isConnectedServeurFichier;
	}

	public String printServeurInfo() {
		
		return fileServerInfo[0]+":"+fileServerInfo[1];
	}

	public String printLocalInfo() {
		
		String s ="";
		
		try {
			s += InetAddress.getLocalHost().getHostAddress() +"\n";
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
				
		return s;
	}
}