package pointEntree;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerThreadPointEntree extends Thread{

	private int port;
	private ServerSocket serverSocket;
	private PointEntree pointEntree;

	public ServerThreadPointEntree (int port, PointEntree pointEntree) throws IOException
	{
		this.port = port;
		this.pointEntree = pointEntree;
	}
	
	public void run() {
		
		try { 
			serverSocket = new ServerSocket(port); 
        } 
		catch (IOException e) 
        { 
			System.err.println("Erreur ServerSocket port : " +port);
			System.exit(1);
        }
		
		while(true) {
			
			Socket clientSocket = null; 
			System.out.println ("Ecoute au port : " + port );

			
			//connection
			try { 
				clientSocket = serverSocket.accept();
			} 
			catch (Exception e) 
		    { 
				System.err.println("Erreur Connection Client "); 
		    } 
			
			Communicator communicator = new Communicator(clientSocket, pointEntree);
			communicator.start();
			
		}
	}

}
