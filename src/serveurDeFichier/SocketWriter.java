package serveurDeFichier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import XMLtool.SyncedWriteList;

class SocketWriter extends Thread{
	
	private ObjectOutputStream oos;
	private SyncedWriteList thingsToWrite;
	private boolean running;
	
	public SocketWriter(Socket clientSocket, SyncedWriteList thingsToWrite){
		running = true;
		try {
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			this.thingsToWrite = thingsToWrite;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		//write whats in the list
		while(running){
			
			if (!thingsToWrite.isEmpty()){
				try {
					String string = (String) thingsToWrite.get(0);
					
					if(string.equals("file")){
					
						thingsToWrite.remove(0);

						Thread.sleep(1000);
						//write the file
						byte[] content  = ( byte[] )thingsToWrite.getNRemoveFirst();
						oos.writeObject(content);

						
						System.out.println("file Sent");
						
					}else if(string.startsWith("addfile")|string.startsWith("deleteFile")|string.startsWith("addrepo")|string.startsWith("deleteRepo")){
						
						oos.writeObject(string);
						thingsToWrite.remove(0);
						
					}else{
						oos.writeObject(string);
						thingsToWrite.remove(0);
					}
				
					
					
					
				} catch (IOException | InterruptedException e) {
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
