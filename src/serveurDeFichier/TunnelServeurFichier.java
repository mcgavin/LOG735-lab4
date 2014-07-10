package serveurDeFichier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class TunnelServeurFichier extends Thread{
	
	
	private String ipLocal;
	private int portLocal;
	
	private String ipDistant;
	private int portDistant;
	
	private socketWriter outputTread;
	private socketListener inputTread;
	
	private syncedWriteList writeList;
	//instance of the initiator
	private ServeurFichier serveurFichierLocal;
	private int succID;
	private boolean record;
	
	public TunnelServeurFichier(Socket clientSocket, ServeurFichier serveurFichierLocal){
		
		ipLocal = clientSocket.getLocalAddress().getHostAddress();
		portLocal = clientSocket.getLocalPort();
		
		ipDistant = clientSocket.getInetAddress().getHostAddress();
		portDistant = clientSocket.getPort();
		this.serveurFichierLocal = serveurFichierLocal;

		writeList = new syncedWriteList();
		outputTread = new socketWriter(clientSocket,writeList );
		inputTread = new socketListener(clientSocket,this );

	}
	
	public void run(){
		
		new Thread(outputTread).start();
		new Thread(inputTread).start();
	}
	
	public String toString(){
		
		return ""+ipLocal+":"+portLocal+" --> "+ipDistant+":"+portDistant;
	}
	
	
}




	/**
	 * socketWriter
	 * 
	 * classe runnable pour ecrire dans le socket
	 * 
	 */
	class socketWriter implements Runnable{
		
		private ObjectOutputStream oos;
		private syncedWriteList stringToWrite;
		
		public socketWriter(Socket clientSocket, syncedWriteList stringToWrite){
			try {
				oos = new ObjectOutputStream(clientSocket.getOutputStream());
				this.stringToWrite = stringToWrite;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void run() {
			//write whats in the list
			while(true){
				
				if (!stringToWrite.isEmpty()){
					try {
						if(stringToWrite.get(0).contains("transfert")){
							//Thread.sleep(5000);
						}
						oos.writeObject(stringToWrite.get(0));
						stringToWrite.remove(0);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * socketWriter
	 * 
	 * classe runnable pour ecouter le socket et declancher des evenements selon le message
	 * 
	 * @author AJ60940
	 *
	 */
	class socketListener implements Runnable{
		
		private ObjectInputStream ois;
		private TunnelServeurFichier tunnel;
		
		public socketListener(Socket clientSocket,TunnelServeurFichier tunnel ){
			try {
				this.tunnel =  tunnel;
				ois = new ObjectInputStream(clientSocket.getInputStream());

				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void run() {
			
			//Ecoute et traite les evenements
			while(true){
				try {
					
					//TODO
					
					String event = ois.readObject().toString();
					
					if (event.startsWith("chandy")){
						String[] message = event.split(":");
						
					}else if(event.startsWith("transfer:")){
						
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	
	//to sync the WriteList
	/**
	 * 
	 * arraylist with sync method 
	 * ( lock to acces the writeList)
	 *
	 */
	class syncedWriteList {
		
		private ArrayList<String> arraylist ;
		private Object lock = new Object();
		
		public syncedWriteList(){
			this.arraylist = new ArrayList<String>() ;
		}
		
		public void add(String s) {
		   synchronized(lock) {
			   this.arraylist.add(s);
	        }
		}

		public void remove(int index){
		   synchronized(lock) {
			   this.arraylist.remove(index);
	        }
		}
		
		/**
		 *  get by index
		 * @param index
		 * @return String at the index 
		 */
		public String get(int index){
			String s;
		    synchronized(lock) {
			   s = this.arraylist.get(index);
	        }
		    return s;
		}
		
		public boolean isEmpty(){
			boolean b;
			synchronized(lock) {
				 b = this.arraylist.isEmpty();
			}
			
			return b;
		}
		
	}
	
	
