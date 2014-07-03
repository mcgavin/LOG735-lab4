package Banque;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Succursale.Succursale;


public class Banque extends Thread{

	private ServerThreadBanque serverThread;
	private int total = 0 ; // init total
	private List<CommunicatorBanque> listSuccursale;
	
	public static void main(String[] args) throws IOException {
		System.out.println("**** BANQUE ****");
		Banque banque = new Banque(12045);
		banque.start();
	}
	
	public Banque(int port) throws IOException{
		listSuccursale = new ArrayList<CommunicatorBanque>();
		serverThread = new ServerThreadBanque(port, this);
		serverThread.start();
	}
	
	public void addSurccusale(CommunicatorBanque lienSucc) {
		listSuccursale.add(lienSucc);
    }
		
	public void addTotal(int montant){
		setTotal(getTotal() + montant);
	}
	
	public List<CommunicatorBanque> getListSucc(){
		return listSuccursale;
	}
	public void run() {
		while(true){
			
		}
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}
