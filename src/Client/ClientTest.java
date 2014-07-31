package Client;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;

import XMLtool.UpdateMetadata;
import XMLtool.xmlParser;

public class ClientTest {

	public static void main(String[] args) {

		new ClientTest();
	}

	public ClientTest(){
		
		
		ObjectOutputStream oos;
		ObjectInputStream ois;
		try {
			Socket s = new Socket("127.0.0.1", 12045);
			
			
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			
			//point entrée, identification
			System.out.println("connection au point entrer");
			oos.writeObject("client");
			
			//attente d'info d'un serveur a se connecter
			String string = (String) ois.readObject();
			
			System.out.println("connection au serveur "+ string);
			
			String[] fileServerInfo = string.split(":");
			s.close();
			
			
			//new socket to new server
			s = new Socket(fileServerInfo[0],Integer.parseInt(fileServerInfo[1]));
			
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			
			System.out.println("connection reussis");
			
			System.out.println("envoie d<un fichier");
			oos.writeObject("file");
			
			DataObject dataOb =new DataObject(0,"Desert.jpg","0",0,"0","root/folder1/","Mathieu", "23452");
			
			oos.writeObject(xmlParser.ObjectToXMLString(dataOb));
			
			File f = new File("C:\\Users\\Public\\Pictures\\Sample Pictures\\Desert.jpg");
			byte[] content = Files.readAllBytes(f.toPath());
			oos.writeObject(content);
			System.out.println("envoie reussi");
			System.out.println("close");
			oos.writeObject("close");
		/*	ois.close();
			oos.flush();
			oos.close();
			
			s.close();*/
			
		} catch (IOException | ClassNotFoundException e ) {

			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * upload a file 
	 * @parameter String path to the file to upload to the server
	 */
	public boolean uploadFile(String pathToFile, Socket s){
		boolean isComplete = false;
		
		try {
			ObjectOutputStream oos;
			ObjectInputStream ois;
		
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
		
			
			System.out.println("envoie dun fichier");
			//notifie le serveur d'un envoie de fichier
			oos.writeObject("file");
			
			
			//create file 
			//DataObject dataObj = UpdateMetadata.getDataObjectFromFile(pathToFile);
			
			
			//get file from user
			//byte[] content = (byte[]) ois.readObject();
			//Files.write(f.toPath(), content);
			
			
			//creer un DataObject par rapport au fichier a uploader
			DataObject dataOb =new DataObject(0,"Desert.jpg","0",0,"0","root/folder1/","Mathieu", "tete");
			
			oos.writeObject(xmlParser.ObjectToXMLString(dataOb));
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return isComplete;
	}
	
	
	/**
	 * client side
	 * 
	 * @param dataObject
	 */
	public static DataObject getDataObjectFromFile(String pathToFile) {
		
		DataObject dataObj = new DataObject();
		
		File f = new File(pathToFile) ;
		dataObj.setName(f.getName());
		
		return dataObj;
	}
}
