package pointEntree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandLineToolPointEntree extends Thread {

	private PointEntree pointEntree;
	
	public CommandLineToolPointEntree(PointEntree pointEntree){
		this.pointEntree = pointEntree;
		
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
						System.out.println(this.pointEntree.printAllServers());
					}
				}else if(commandArg[0].equals("erreur")){
					
				}
			
			
			}catch (Exception e) {
				System.out.println("error trying to read your command!");
			}
		}
	}
	
	public void printHelp(){
		System.out.println("Commande de la Succursale\n" +
				"~~ COMMANDE ~~\n"+
				"chandy                     : Demarre chandy-lamport\n"+
				"envoie %succ% %montant%    : envoie le %montant% a la succursale %succ% (entier )\n" +
				"erreur %montant%           : introduit un erreur en enlevant le montant a la succursale\n"+
				"\n"+
				"~~ INFORMATION ~~\n"+
				"print help/aide            : print l'aide\n"+
				"print ID/id                : print le Id de la succursale presente\n"+
				"print montant/argent       : print l'argent succursale presente\n"+
				"print list/tunnel          : print toute les connections vers les autres succursales\n" 
				);
	}
}
