package serveurDeFichier;

import XMLtool.SyncedWriteList;
import XMLtool.UpdateMetadata;
import XMLtool.xmlParser;
import Client.DataObject;

public abstract class AbstractTunnel {

	protected SyncedWriteList writeList;
	protected ServeurFichier serveurFichierLocal;
	private boolean tunnelup;
	
	
	public abstract void closeTunnel();
	public abstract void sendFile(DataObject dataObj);
	
	
	
	public AbstractTunnel(){
		writeList = new SyncedWriteList();
		tunnelup = true;
	}
	public boolean isTunnelUp(){
		return tunnelup;
	}
	
	public void setTunnelDown(){
		tunnelup = false;
	}
	
	
	//***********************************
	//modify function goes   from socket to serveur ( UP) 
	
	//abstract
	public abstract void modifyXML(String repoPath, String name,String action);
	public abstract void modifyXML(DataObject dataObject,String action);
		
	//These are implementation of child class
//	public void modifyXML(String repoPath, String name,String action){
//		serveurFichierLocal.modifyXmlFromRepo(action, repoPath , name,"client|all");
//	}
//	public void modifyXML(DataObject dataObject,String action){
//		serveurFichierLocal.modifyXml(dataObject, action, "client|all");
//	}
	
	
	
	
	
	//***********************************
	// SEND FUNCTION GOES TO WRITELIST  from serveur to socket (DOWN)
	
	public void sendXmlModification(DataObject newDataObject,String action) {
		writeList.add(action+":" +xmlParser.ObjectToXMLString(newDataObject));
	}
	public void sendXmlModification(String repoPath,String name, String action){
		if (action.equals("deleteRepo")){
			writeList.add(action+":"+repoPath);
		}else{
			writeList.add(action+":"+repoPath+":"+name);
		}
		
	}
	
	public void sendAllXML() {
		writeList.add("wholeXMl");
		writeList.addObject(UpdateMetadata.getXMLFile());
	}
	
}

