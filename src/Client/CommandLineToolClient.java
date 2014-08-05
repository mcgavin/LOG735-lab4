package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandLineToolClient extends Thread {

	private MainClient mainClient;
	
	public CommandLineToolClient(MainClient mainClient){
		this.mainClient = mainClient;
		
	}
	
	public void run(){

		//  open up standard input
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String command = null;

		//  read the username from the command-line; need to use try/catch with the
		//  readLine() method
		while(true){
			
			try {
				System.out.print("Enter your command : ");
				command = br.readLine(); 
				String[] commandArg = command.split(" ");
				
				if(commandArg[0].equals("connexion")){
					if(commandArg[1].equals("point")){
						try	{
							String[] s = mainClient.connexionPointEntrer(commandArg[2], Integer.parseInt(commandArg[3]),0);
							System.out.println("information du point entrer recueillis");
							System.out.println(s[0]+" "+s[1]);
						}catch(IOException| ClassNotFoundException e ){
							System.err.println("ERROR Connecting, please retry");
							printHelp();
						}
						
					}else if(commandArg[1].equals("serveur")){
						if(mainClient.connexionServeurFichier(commandArg[2], Integer.parseInt(commandArg[3]))){
							System.out.println("Connexion Reussis");
						}else{
							System.err.println("Connexion echouer, reessayer");
						}
					
						
					}else{
						
						printHelp();
					}
					
	
				}else if(commandArg[0].equals("print")){
					
					if(commandArg[1].equals("ifconnected")){
						System.out.println(mainClient.isConnectedServeurFichier());
						
					}else if(commandArg[1].equals("infoConnexion")){
						System.out.println(mainClient.printServeurInfo());
					}else if(commandArg[1].equals("infoLocal")){
						System.out.println(mainClient.printLocalInfo());
					
					}else{
						printHelp();
					}
					
				}else if(commandArg[0].equals("help")){
					printHelp();
				
				}else if(commandArg[0].equals("updateXML")){
					mainClient.refresh();
				}
					
			
			}catch (Exception e) {
				System.out.println("Error trying to read your command!\n");
				printHelp();
			}
		}
	}
	
	public void printHelp(){
		System.out.println("\n" +
				"~~ COMMANDE ~~\n"+
				"connexion point ip port      : connexion au point d'entrer\n"+
				"connexion serveur ip port    : connexion a un serveur de fichier\n"+
				"~~ INFORMATION ~~\n"+
				"print ifconnected            : affiche si connecter a un serveur de fichier\n"+
				"print infoConnexion          : affiche si connecter a un serveur de fichier\n"+
				"print infoLocal              : print les info du point\n"

				);
	}
}
