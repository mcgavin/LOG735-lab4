package Client;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import XMLtool.xmlParser;

public class TunnelDownloadFichier{

	/**
	 * 
	 * download the file DataObject form the pre Opend socket
	 * 
	 * connection need to be made
	 * @param clientSocket
	 * @param dataObj
	 */
	public TunnelDownloadFichier(Socket clientSocket, DataObject dataObj){
		
		try {
			
			ObjectInputStream  ois = new ObjectInputStream(clientSocket.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
			
			//say i wanna download
			oos.writeObject("download:"+xmlParser.ObjectToXMLString(dataObj));
			
			//say i wanna download this file
//			oos.writeObject(xmlParser.ObjectToXMLString(dataObj));
			
			//getting file sent by the server
			byte[] content = (byte[]) ois.readObject();
			
			File theDir = new File(dataObj.getRepo());
			// if the directory does not exist, create it
			if (!theDir.exists()) {
				System.out.println("creating directory: " + dataObj.getRepo());
				boolean result = false;
				
				try{
					theDir.mkdirs();
					result = true;
				} catch(SecurityException se){
					   System.err.println("Cannot create the directory");
				}        
				if(result) {    
					System.out.println("DIR created");  
				}
			}
			Path path = Paths.get(dataObj.getRepo()+"/"+dataObj.getName());
			Files.write(path, content);
			oos.writeObject("close");
		
		} catch (IOException | ClassNotFoundException e) {

			e.printStackTrace();
		}
	}

}


