package serveurDeFichier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class SocketWriter extends Thread{
	
	private ObjectOutputStream oos;
	private SyncedWriteList stringToWrite;
	private boolean running;
	
	public SocketWriter(Socket clientSocket, SyncedWriteList stringToWrite){
		running = true;
		try {
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			this.stringToWrite = stringToWrite;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		//write whats in the list
		while(running){
			
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

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
