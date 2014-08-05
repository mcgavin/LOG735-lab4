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
					}else if(commandArg[1].equals("info")){
						System.out.println(this.pointEntree.printLocalInfo());
					}else{
						printHelp();
					}

					
				}else if(commandArg[0].equals("help")){
					printHelp();
				}
			
			
			}catch (Exception e) {
				System.out.println("error trying to read your command!");
			}
		}
	}
	
	public void printHelp(){
		System.out.println("\n" +
				"~~ INFORMATION ~~\n"+
				"print help          : print l'aide\n"+
				"print servers       : print la liste des serveur\n"+
//				"print clients       : print la liste de clients\n"+
				"print info          : print les info du point\n"

				);
	}
}
