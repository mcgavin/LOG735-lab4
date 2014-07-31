package Client;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;

import XMLtool.xmlParser;

public class ClientTest {

	public static void main(String[] args) {

		new ClientTest();
	}

	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Socket socket;
	private String infoServer;
	private String[] fileServerInfo;
	
	
	public ClientTest(){
		try {
			connection();

		} catch (IOException | ClassNotFoundException  e ){
			e.printStackTrace();
		}
	
		instruction();
	}



	public void instruction(){
		System.out.println("lancement des instructions");
		try{
		socket = new Socket(fileServerInfo[0],Integer.parseInt(fileServerInfo[1]));
		
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
		
		System.out.println("connection reussis");
		
		System.out.println("envoie d<un fichier");
		oos.writeObject("file");
		
		DataObject dataOb =new DataObject(0,"Desert.jpg","0",0,"0","root/folder1/","Mathieu","");
		
		oos.writeObject(xmlParser.ObjectToXMLString(dataOb));
		
		File f = new File("C:\\Users\\Public\\Pictures\\Sample Pictures\\Desert.jpg");
		byte[] content = Files.readAllBytes(f.toPath());
		oos.writeObject(content);
		System.out.println("envoie reussi");
		System.out.println("close");
		oos.writeObject("close");
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
	public void connection() throws IOException, ClassNotFoundException{
		
		socket = new Socket("127.0.0.1", 12045);
		
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
		
		System.out.println("connection au point entrer");
		oos.writeObject("client");
		
		infoServer = (String) ois.readObject();
		
		System.out.println("connection au serveur "+ infoServer);
		
		fileServerInfo = infoServer.split(":");
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
			
			fileServerInfo = infoServer.split(":");
			socket.close();
			instruction();

		
	}
}