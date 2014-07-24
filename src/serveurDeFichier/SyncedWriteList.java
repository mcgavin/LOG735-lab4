package serveurDeFichier;

import java.util.ArrayList;

//to sync the WriteList
/**
 * 
 * arraylist with sync method 
 * ( lock to acces the writeList)
 *
 */
class SyncedWriteList {
	
	private ArrayList<String> arraylist ;
	private Object lock = new Object();
	
	public SyncedWriteList(){
		this.arraylist = new ArrayList<String>() ;
	}
	
	public void add(String s) {
	   synchronized(lock) {
		   this.arraylist.add(s);
        }
	}

	public void remove(int index){
	   synchronized(lock) {
		   this.arraylist.remove(index);
        }
	}
	
	/**
	 *  get by index
	 * @param index
	 * @return String at the index 
	 */
	public String get(int index){
		String s;
	    synchronized(lock) {
		   s = this.arraylist.get(index);
        }
	    return s;
	}
	
	public boolean isEmpty(){
		boolean b;
		synchronized(lock) {
			 b = this.arraylist.isEmpty();
		}
		
		return b;
	}
	
}