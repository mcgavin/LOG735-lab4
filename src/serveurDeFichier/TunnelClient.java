package serveurDeFichier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import Client.DataObject;

public class TunnelClient extends AbstractTunnel{
	
	
	private String ipLocal;
	private int portLocal;
	
	private String ipDistant;
	private int portDistant;
	
	private SocketWriter outputTread;
	private SocketListener inputTread;
	
	private SyncedWriteList writeList;
	//instance of the initiator
	private ServeurFichier serveurFichierLocal;
	private int succID;
	private boolean record;
	
	//instancier a partire de 0
	public TunnelClient(Socket clientSocket, ServeurFichier serveurFichierLocal){
		
		ipLocal = clientSocket.getLocalAddress().getHostAddress();
		portLocal = clientSocket.getLocalPort();
		
		ipDistant = clientSocket.getInetAddress().getHostAddress();
		portDistant = clientSocket.getPort();
		this.serveurFichierLocal = serveurFichierLocal;

		writeList = new SyncedWriteList();
		
		try {
			ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
			
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


	public void addAndFillDataObject(DataObject dataObject) {
		this.serveurFichierLocal.addAndFillDataObject(dataObject);
	}
	
	public void closeTunnel(){
		inputTread.setRunning(false);
		outputTread.setRunning(false);
	}
}

	
	
