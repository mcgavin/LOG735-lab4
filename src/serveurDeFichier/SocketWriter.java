package serveurDeFichier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class SocketWriter implements Runnable{
	
	private ObjectOutputStream oos;
	private SyncedWriteList stringToWrite;
	
	public SocketWriter(Socket clientSocket, SyncedWriteList stringToWrite){
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
					oos.writeObject(stringToWrite.get(0));
					stringToWrite.remove(0);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
