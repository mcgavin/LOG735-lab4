package pointEntree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class PointEntree {

	private ServerThreadPointEntree serverThread;
	private ArrayList<ServeurFichierInfo> listServeurFichier;
//	private Iterator<ServeurFichierInfo> it;
	int it= Integer.MAX_VALUE;
	
	public static void main(String[] args) throws IOException {
		
		//default port
		int port = 12045;
		try{
			if (args.length>0) {
				port = Integer.parseInt(args[0]);
			}
		}catch (NumberFormatException e) {
			System.err.println("port Invalide, utilisation du port par default "+port);
		}

		System.out.println("**** Point d'Entree ****");
		System.out.println("Port d'ecoute : "+port);
		
		PointEntree pointEntree = new PointEntree(port);
	}
	
	
	public PointEntree(int port) throws IOException{
		
		CommandLineToolPointEntree cmd = new CommandLineToolPointEntree(this);
		cmd.start();
		
		listServeurFichier = new ArrayList<ServeurFichierInfo>();
//		it = listServeurFichier.iterator() ;
				
		serverThread = new ServerThreadPointEntree(port, this);
		serverThread.start();
	}
	
	public void addServeurFichier(ServeurFichierInfo serveurFichierInfo) {
		listServeurFichier.add(serveurFichierInfo);
    }
		
	public ArrayList<ServeurFichierInfo> getListServeurFichierInfo(){
		return listServeurFichier;
	}
	
	public String printAllServers() {
		String out= "";
		for (ServeurFichierInfo serveurFichierInfo : listServeurFichier) {
			out += " "+ serveurFichierInfo+"\n";
		}
		
		return out;
		
	}
	
	public ServeurFichierInfo getIteratorNext(){
		
		if ( it < listServeurFichier.size()){
			it++;
		}else{ 
			it = 0;
		}
		
		return listServeurFichier.get(it);
	}
	
}
