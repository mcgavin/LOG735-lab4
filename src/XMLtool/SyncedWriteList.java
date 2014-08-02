package XMLtool;

import java.util.ArrayList;

//to sync the WriteList
/**
 * 
 * arraylist with sync method 
 * ( lock to acces the writeList)
 *
 */
public class SyncedWriteList {
	
	private ArrayList<Object> arraylist ;
	private Object lock = new Object();
	
	public SyncedWriteList(){
		this.arraylist = new ArrayList<Object>() ;
	}
	
	public void add(String s) {
	   synchronized(lock) {
		   this.arraylist.add(s);
        }
	}
	
	//this is for file transfer
	public void addObject(byte[] content) {
		   synchronized(lock) {
			   this.arraylist.add(content);
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
	 * @return Object at the index 
	 */
	public Object get(int index){
		Object s;
	    synchronized(lock) {
		   s = this.arraylist.get(index);
        }
	    return s;
	}
	
	public Object getNRemoveFirst(){
		Object s;
	    synchronized(lock) {
		   s = this.arraylist.get(0);
		   this.arraylist.remove(0);
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