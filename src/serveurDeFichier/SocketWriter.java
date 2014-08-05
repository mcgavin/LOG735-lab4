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
	
	public SocketWriter(Socket clientSocket, SyncedWriteList thingsToWrite) throws IOException {
		
		running = true;
		oos = new ObjectOutputStream(clientSocket.getOutputStream());
		this.thingsToWrite = thingsToWrite;
	}
	
	public void run() {
		//write whats in the list
		while(running){
			try{
				if (!thingsToWrite.isEmpty()){
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
						
					}else if(string.startsWith("wholeXMl")){
						oos.writeObject("wholeXMl");
						thingsToWrite.remove(0);
						System.out.println("sending whole XML");
						Thread.sleep(1000);
						//write the file
						byte[] content  = ( byte[] )thingsToWrite.getNRemoveFirst();
						oos.writeObject(content);
						System.out.println("!!! SENT !!!");
						
						
					}else{
						oos.writeObject(string);
						thingsToWrite.remove(0);
					}
				}

			} catch (IOException | InterruptedException e) {
				System.err.println("OHoH");
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
