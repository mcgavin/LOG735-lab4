package serveurDeFichier;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;

import XMLtool.SyncedWriteList;
import XMLtool.xmlParser;
import Client.DataObject;

public class TunnelClient extends AbstractTunnel{
	
	
	private String ipLocal;
	private int portLocal;
	
	private String ipDistant;
	private int portDistant;
	
	private SocketWriter outputTread;
	private SocketListener inputTread;
	
//	private SyncedWriteList writeList;
	//instance of the initiator
	//private ServeurFichier serveurFichierLocal;
	private int succID;
	private boolean record;
	
	//instancier a partire de 0
	public TunnelClient(Socket clientSocket, ServeurFichier serveurFichierLocal){
		super();
		
		ipLocal = clientSocket.getLocalAddress().getHostAddress();
		portLocal = clientSocket.getLocalPort();
		
		ipDistant = clientSocket.getInetAddress().getHostAddress();
		portDistant = clientSocket.getPort();
		this.serveurFichierLocal = serveurFichierLocal;

//		writeList = new SyncedWriteList();
		
		try {
			
			outputTread = new SocketWriter(clientSocket,writeList );
			inputTread = new SocketListener(clientSocket,this );
			inputTread.start();
			outputTread.start();
			
			
			
		} catch (Exception e) {
			System.err.println("failed to create Object or cast readObject");
			e.printStackTrace();
		}
	}
	
		
	public String toString(){
		
		return ""+ipLocal+":"+portLocal+" --> "+ipDistant+":"+portDistant;
	}
	
	public void sendFile(DataObject dataObj){
		
		byte[] content;
		try {
			File fileToSend = new File(dataObj.getRelName());
			
			content = Files.readAllBytes(fileToSend.toPath());
			writeList.add("file");
			writeList.addObject(content);
			
		} catch (IOException e) {
			System.err.println("error getting file");
			e.printStackTrace();
		}
		
		
	}

//	public void addNewFileToXml(DataObject dataObject) {
//		this.serveurFichierLocal.addNewFileToXml(dataObject);
//		this.serveurFichierLocal.mo
//	}
	
	public void modifyXML(String repoPath, String name,String action){
		serveurFichierLocal.modifyXmlFromRepo(action, repoPath , name,"all");
	}
	
	public void modifyXML(DataObject dataObject,String action){
		serveurFichierLocal.modifyXml(dataObject, action, "all");
	}
	
	public void closeTunnel(){
		inputTread.setRunning(false);
		outputTread.setRunning(false);
	}



}

	
	
