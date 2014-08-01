package Client;

import java.awt.EventQueue;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;

import javax.sound.sampled.Port;

import com.sun.java.swing.plaf.windows.resources.windows;

import XMLtool.xmlParser;

public class MainClient {

	public static void main(String[] args) {

	//	new ClientTest();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					ModelDAO xmlHandler = new ModelDAO();
					GUIWindow window = new GUIWindow(xmlHandler);
					MainClient mainclient = new MainClient(xmlHandler,window );
					
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}

	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Socket socket;
	private String infoServer;
	private String[] fileServerInfo;
	
	ModelDAO xmlHandler;
	TunnelClientServeurFichier tunnelServeurFichier;
	
	public MainClient(ModelDAO xmlHandler,GUIWindow window){
				
		window.addController(this);
		this.xmlHandler = xmlHandler;
		
		try {
			//connection au point d'entrer
			connection("127.0.0.1", 12045);

		} catch (IOException | ClassNotFoundException  e ){
			
			System.err.println("Incapable de se connecter au point d'entrer du systeme");
			//System.out.println("Veuillez entrer utiliser l<utilitaire de ligne de connande pour vous connecter");
			//TODO lauch commandLineTools and add a function thathelp connect directly to a file server
			//e.printStackTrace();
		}
	
		//instruction de connection au server de fichier
		instruction();
		
		
	}

	public void instruction(){
		
		System.out.println("Connection au serveur de fichier");
		
		try{
			
			this.fileServerInfo = infoServer.split(":");
			
			System.out.println("connection au serveur "+ infoServer);
			socket = new Socket(fileServerInfo[0],Integer.parseInt(fileServerInfo[1]));
			System.out.println("connection reussis");
			
			tunnelServeurFichier = new TunnelClientServeurFichier(socket, this);
			
			
//			oos = new ObjectOutputStream(socket.getOutputStream());
//			ois = new ObjectInputStream(socket.getInputStream());
			
			
			
			
			
//			System.out.println("envoie d<un fichier");
//			oos.writeObject("file");
//			
//			DataObject dataOb =new DataObject(0,"Desert.jpg","0",0,"0","root/folder1/","Mathieu","");
//			
//			oos.writeObject(xmlParser.ObjectToXMLString(dataOb));
//			
//			File f = new File("C:\\Users\\Public\\Pictures\\Sample Pictures\\Desert.jpg");
//			byte[] content = Files.readAllBytes(f.toPath());
//			oos.writeObject(content);
//			System.out.println("envoie reussi");
//			System.out.println("close");
//			oos.writeObject("close");
			
			
		} catch (IOException  e ) {
			System.out.println("impossible de ce connecter au serveur : " + infoServer);
			try {
				reConnection();
			}catch (ClassNotFoundException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//e.printStackTrace();
		}
		
	}
	
	/**
	 * Connection au point d,entrer
	 * @param pointEntrerIP
	 * @param port
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void connection(String pointEntrerIP, int port) throws IOException, ClassNotFoundException{
		
		//connection au point d'entrer
		System.out.print("connection au point entrer : ");
		
		socket = new Socket(pointEntrerIP, port);
		
		System.out.print(" ok\n");
			
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
		
		//specifie l'intention = { client | serveur | download }
		oos.writeObject("client");
		
		//information du serveur de fichier a se connecter
		this.infoServer = (String) ois.readObject();
		
		//fermeture de la connection avec le point d'entrer
		socket.close();
	}
	
	public void reConnection() throws UnknownHostException, IOException, ClassNotFoundException{
		
		
			socket = new Socket("127.0.0.1", 12045);
			
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			System.out.println("re-connection au point entrer");
			//String message = "client:" + infoServer;
			oos.writeObject("client:" + infoServer);
			
			infoServer = (String) ois.readObject();
			
			System.out.println("connection au serveur "+ infoServer);

			socket.close();
			instruction();

	}
	
	
	/**
	 * 
	 */
	public void sendFile(DataObject dataObj, File fileToSend){
		
		
			System.out.println("envoie d'un fichier");
		
			
			tunnelServeurFichier.sendFile( dataObj, fileToSend);
			//alert server that a file is comming
//			oos.writeObject("file");
		
			///XXX DUMMY dataObject and file
			//DataObject dataOb =new DataObject(0,"Desert.jpg","0",0,"0","root/folder1/","Mathieu","");
			//File f = new File("C:\\Users\\Public\\Pictures\\Sample Pictures\\Desert.jpg");
			
			
			//serialise and send dataObject( object that represent the file ) 
//			oos.writeObject(xmlParser.ObjectToXMLString(dataObj));
			
			//get the file Byte
//			byte[] content = Files.readAllBytes(fileToSend.toPath());
			
			//send the file
//			oos.writeObject(content);
//			System.out.println("envoie reussi");
			
			//say close to server
//			System.out.println("close");
//			oos.writeObject("close");
			

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
	
}