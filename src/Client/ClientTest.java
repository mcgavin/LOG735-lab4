package Client;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;

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
			
			System.out.println("connection au point entrer");
			oos.writeObject("client");
			
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
			
			DataObject dataOb =new DataObject(0,"Desert.jpg","0",0,"0","root/folder1/","Mathieu");
			
			oos.writeObject(xmlParser.ObjectToXMLString(dataOb));
			
			File f = new File("C:\\Users\\Public\\Pictures\\Sample Pictures\\Desert.jpg");
			byte[] content = Files.readAllBytes(f.toPath());
			oos.writeObject(content);
			System.out.println("envoie reussi");
			
			ois.close();
			oos.flush();
			oos.close();
			
			s.close();
			
		} catch (IOException | ClassNotFoundException e ) {

			e.printStackTrace();
		}
		
	}
}