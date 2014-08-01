package Client;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;

import XMLtool.xmlParser;

public class TunnelClientServeurFichier{
	
	
	private String ipLocal;
	private int portLocal;
	
	private String ipDistant;
	private int portDistant;
	
	private ClientSocketWriter outputTread;
	private ClientSocketListener inputTread;
	
	private SyncedWriteList writeList;
	//instance of the initiator
	private MainClient mainClient;
	private int succID;
	private boolean record;
	
	//instancier a partire de 0
	public TunnelClientServeurFichier(Socket clientSocket, MainClient mainClient){
		
		ipLocal = clientSocket.getLocalAddress().getHostAddress();
		portLocal = clientSocket.getLocalPort();
		
		ipDistant = clientSocket.getInetAddress().getHostAddress();
		portDistant = clientSocket.getPort();
		
		this.mainClient = mainClient;

		writeList = new SyncedWriteList();
		
		outputTread = new ClientSocketWriter(clientSocket,writeList );
		inputTread = new ClientSocketListener(clientSocket,this );
		outputTread.start();
		inputTread.start();
			
	}
	
	/**
	 * RAW command 
	 * 
	 * send a string
	 * Mostly debug purpose
	 * 
	 * @param s
	 */
	public void sendCommand(String s){
		
		writeList.add(s);
		
	}
	
	public void sendFile(DataObject dataObj, File fileToSend){
		
		writeList.add("file");
		
		writeList.add(xmlParser.ObjectToXMLString(dataObj));
		
		byte[] content;
		try {
			content = Files.readAllBytes(fileToSend.toPath());
			writeList.addObject(content);
		} catch (IOException e) {
			System.err.println("error getting file");
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * close connection between client and serveur
	 */
	public void close(){
		outputTread.setRunning(false);
		inputTread.setRunning(false);
	}
	
	
	
	
	
	public String toString(){
		
		return ""+ipLocal+":"+portLocal+" --> "+ipDistant+":"+portDistant;
	}
	
	
}


