package serveurDeFichier;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandLineToolServeur extends Thread {

	private ServeurFichier serveurFichier;
	
	public CommandLineToolServeur(ServeurFichier serveurFichier){
		this.serveurFichier = serveurFichier;
		
	}
	
	public void run(){

		try {
			//this is for console output
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

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
				
				if(commandArg[0].equals("print")){
					
					if(commandArg[1].equals("servers")){
						serveurFichier.printServerList();
						
					}else if (commandArg[1].equals("clients")){
							serveurFichier.printClientList();
						
					}else if(commandArg[1].equals("info")){
					
						System.out.println(serveurFichier.printLocalInfo());
				
					}else if(commandArg[1].equals("help")){
						printHelp();
					}
					
				}else if(commandArg[0].equals("connexion")){
				
					serveurFichier.connectionPointEntrer(commandArg[1], Integer.parseInt(commandArg[2]));
			
				}else if(commandArg[0].equals("help")){
					printHelp();
				}
			
			}catch (Exception e) {
				System.out.println("error trying to read your command!\n");
				printHelp();
			}
		}
	}
	
	public void printHelp(){
		System.out.println("Commande du serveur de fichier : \n" +
				"~~ COMMANDE ~~\n"+
				"connexion ip port      : connexion au point d'entrer\n"+
				"~~ INFORMATION ~~\n"+
				"print help             : print l'aide\n"+
				"print servers          : print la liste de serveurs\n"+
				"print clients          : print la liste de clients\n"+
				"print info             : print les info du serveur\n"
				);
	}
}
