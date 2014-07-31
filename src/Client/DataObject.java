package Client;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



@XmlRootElement(name = "file")
@XmlType(propOrder = {"name", "server", "port", "absPath", "repo","owner","lastUpdated"})
public class DataObject {
	private int id;
	private String name;
	private String server;
	private int port;
	private String absPath;
	private String repo;
	private String owner;
	private String lastUpdated;

	public DataObject(){}
	
	
	
	public DataObject(int id, String name, String server, int port, String absPath, String repo, String owner,String lastUpdated) {
		this.id = id;
		this.name = name;
		this.server = server;
		this.port = port;
		this.absPath = absPath;
		this.repo = repo;
		this.owner = owner;
		this.lastUpdated = lastUpdated;
	}
	
	@XmlAttribute
	public int getId() {
		return id;
	}
	
	
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServer() {
		return server;
	}
	
	public void setServer(String server) {
		this.server = server;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getAbsPath() {
		return absPath;
	}

	public void setAbsPath(String absPath) {
		this.absPath = absPath;
	}

	public String getRepo() {
		return repo;
	}

	public void setRepo(String repo) {
		this.repo = repo;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getLastUpdated() {
		return this.lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	
	@Override
	public String toString(){
		return this.name + " (" + this.lastUpdated + ")";
	}
	
}
