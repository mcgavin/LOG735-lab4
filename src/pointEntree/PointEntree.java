package pointEntree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class PointEntree {

	private ServerThreadPointEntree serverThread;
	private ArrayList<ServeurFichierInfo> listServeurFichier;
	private Iterator<ServeurFichierInfo> it;
	
	public static void main(String[] args) throws IOException {
		
		//default port
		int port = 12045;
		try{
			if (args.length>0) {
				port = Integer.parseInt(args[0]);
			}
		}catch (NumberFormatException e) {
			System.err.println("port Invalide");
		}

		System.out.println("**** Point d'Entree ****");
		System.out.println("Port d'ecoute : "+port);
		
		PointEntree pointEntree = new PointEntree(port);
	}
	
	
	public PointEntree(int port) throws IOException{
		listServeurFichier = new ArrayList<ServeurFichierInfo>();
		it = listServeurFichier.iterator() ;
		
		
		serverThread = new ServerThreadPointEntree(port, this);
		serverThread.start();
	}
	
	public void addServeurFichier(ServeurFichierInfo lienSucc) {
		listServeurFichier.add(lienSucc);
    }
		
	public ArrayList<ServeurFichierInfo> getListSucc(){
		return listServeurFichier;
	}
	
	public ServeurFichierInfo getIteratorNext(){
		
		ServeurFichierInfo info = null;
		try {
			info = it.next();
		} catch (NoSuchElementException e) {
			System.err.println("NoSuchElementException");
		}
		
		return info;
	}
	
}
