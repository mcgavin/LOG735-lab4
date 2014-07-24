package Client;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class DataObject {
	private int id;
	private String name;
	private String server;
	private int port;
	private String absPath;
	private String repo;
	private String owner;

	public DataObject(){}
	
	
	
	public DataObject(int id, String name, String server, int port, String absPath, String repo, String owner) {
		this.id = id;
		this.name = name;
		this.server = server;
		this.port = port;
		this.absPath = absPath;
		this.repo = repo;
		this.owner = owner;
	}

	/**
	 * Constructor from XML
	 * 
	 * @param xml as string
	 */
	public DataObject(String xml) {
		
		this.id = id;
		this.name = name;
		this.server = server;
		this.port = port;
		this.absPath = absPath;
		this.repo = repo;
		this.owner = owner;
	}
	
	public int getId() {
		return id;
	}
	
	@XmlElement
	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	@XmlElement
	public void setName(String name) {
		this.name = name;
	}

	public String getServer() {
		return server;
	}
	
	@XmlElement
	public void setServer(String server) {
		this.server = server;
	}

	public int getPort() {
		return port;
	}

	@XmlElement
	public void setPort(int port) {
		this.port = port;
	}

	public String getAbsPath() {
		return absPath;
	}

	@XmlElement
	public void setAbsPath(String absPath) {
		this.absPath = absPath;
	}

	public String getRepo() {
		return repo;
	}

	@XmlElement
	public void setRepo(String repo) {
		this.repo = repo;
	}

	public String getOwner() {
		return owner;
	}

	@XmlElement
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String toXML(){
		
		String out = "";
		
		out+=	"<file id=\""+id+"\">"+
					"<name>"+name+"</name>"+
					"<server>"+server+"</server>"+
					"<port>"+port+"</port>"+
					"<absPath>"+absPath+"</absPath>"+
					"<repo>"+repo+"</repo>"+
					"<owner>"+owner+"</owner>"+
				"</file>";
		return out;		
	}
	
	@Override
	public String toString(){
		return this.name;
	}
	
}
