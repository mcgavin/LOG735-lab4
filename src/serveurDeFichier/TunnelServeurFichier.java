package serveurDeFichier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class TunnelServeurFichier extends AbstractTunnel{
	
	
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
	public TunnelServeurFichier(Socket clientSocket, ServeurFichier serveurFichierLocal){
		
		ipLocal = clientSocket.getLocalAddress().getHostAddress();
		portLocal = clientSocket.getLocalPort();
		
		ipDistant = clientSocket.getInetAddress().getHostAddress();
		portDistant = clientSocket.getPort();
		
		this.serveurFichierLocal = serveurFichierLocal;

		writeList = new SyncedWriteList();
		
		outputTread = new SocketWriter(clientSocket,writeList );
		inputTread = new SocketListener(clientSocket,this );
		outputTread.start();
		inputTread.start();
			
	}
	
	public String toString(){
		
		return ""+ipLocal+":"+portLocal+" --> "+ipDistant+":"+portDistant;
	}
	
	public void close(){
		//inputTread.stop();
		outputTread.stop();
		serveurFichierLocal.removeTunnelServeurFichierFromList(this);
	}
	
	public String getIpLocal(){
		return ipLocal;
	}
	
}


