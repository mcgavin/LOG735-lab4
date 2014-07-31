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
			if ( it < listServeurFichier.size()-1){
				it++;
			}else{ 
				it = 0;
			}

		return listServeurFichier.get(it);
	}
	
	public void serverHS(String ipServer, String portClient){
		
		for(int i=0 ; i<listServeurFichier.size();i++){
			if(ipServer.equals(listServeurFichier.get(i).getServeurIp()) && 
					(Integer.parseInt(portClient)==listServeurFichier.get(i).getClientPort())){
				
				listServeurFichier.remove(i);
				System.out.println("le serveur " + ipServer + ":" + portClient + " a ete enleve de la liste car HS");
				if(it>0){
					it = it - 1;
				}else{
					it = Integer.MAX_VALUE;
				}
				
			}
			
		}
	}
}
