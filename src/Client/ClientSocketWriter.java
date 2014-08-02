package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import XMLtool.SyncedWriteList;
import XMLtool.xmlParser;

class ClientSocketWriter extends Thread{
	
	private ObjectOutputStream oos;
	private SyncedWriteList thingsToWrite;
	private boolean running;
	
	public ClientSocketWriter(Socket clientSocket, SyncedWriteList thingsToWrite){
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
					
					String s = (String) thingsToWrite.get(0);
					if(s.equals("file")){
						
						//write "files"
						oos.writeObject(thingsToWrite.getNRemoveFirst());
						
						//need to wait for synchro //HARDCODED,  ideal : 1 command
						Thread.sleep(1000);
						
						//write the dataObject
						oos.writeObject(thingsToWrite.getNRemoveFirst());

						//need to wait for synchro //HARDCODED,  ideal : 1 command
						Thread.sleep(1000);
						//write the file
						byte[] content  = ( byte[] )thingsToWrite.getNRemoveFirst();
						oos.writeObject(content);

						
						System.out.println("file Sent");
						
					}else if(s.startsWith("addRepo")){
						
						oos.writeObject(s);
						thingsToWrite.remove(0);
					}else{
					
						oos.writeObject(s);
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
