package Client;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Client.DataObject;

public class ModelDAO {
	
	private Document doc;
	private String filepath = "metadata.xml";
	
	public ModelDAO() {
		
		
		
		try {
			
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder;
		
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.parse(filepath);
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			
			System.err.println("Failed to parse "+filepath);
			//e.printStackTrace();
		}
		
	}
	
	
	public void AddNewFile(DataObject dataObject) {
		try {

			String filepath = "metadata.xml";
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			String[] repoPath = dataObject.getRepo().split("/");
			int nbSplits = repoPath.length;

			// Pour simplifer, on fait getElement by name au lieu de faire une
			// vrai recherche. PAS DE NOEUD AVEC NOM SIMILAIRE SINON CA MARCHE
			// PAS !
			Node nodeToPlaceNewFile = null;
			nodeToPlaceNewFile = doc.getElementsByTagName(
					repoPath[nbSplits - 1]).item(0);

			// Add new node file to nodeToPlaceNewFil
			Element fileTodAdd = doc.createElement("file");
			nodeToPlaceNewFile.appendChild(fileTodAdd);

			// update file attribute
			Attr attr = doc.createAttribute("id");
			attr.setValue(Integer.toString(dataObject.getId()));
			fileTodAdd.setAttributeNode(attr);

			// name elements
			Element name = doc.createElement("name");
			name.appendChild(doc.createTextNode(dataObject.getName()));
			fileTodAdd.appendChild(name);

			// server elements
			Element server = doc.createElement("server");
			server.appendChild(doc.createTextNode(dataObject.getServer()));
			fileTodAdd.appendChild(server);

			// port elements
			Element port = doc.createElement("port");
			port.appendChild(doc.createTextNode(Integer.toString((dataObject
					.getPort()))));
			fileTodAdd.appendChild(port);

			// relName elements
			Element absPath = doc.createElement("relname");
			absPath.appendChild(doc.createTextNode(dataObject.getRelName()));
			fileTodAdd.appendChild(absPath);

			// repo elements
			Element repo = doc.createElement("repo");
			repo.appendChild(doc.createTextNode(dataObject.getRepo()));
			fileTodAdd.appendChild(repo);

			// owner elements
			Element owner = doc.createElement("owner");
			owner.appendChild(doc.createTextNode(dataObject.getOwner()));
			fileTodAdd.appendChild(owner);

			// owner elements
			Element lastUpdated = doc.createElement("lastupdated");
			lastUpdated.appendChild(doc.createTextNode(dataObject.getLastUpdated()));
			fileTodAdd.appendChild(lastUpdated);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);

			System.out.println("Adding Done");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
	}

	@Deprecated
	public void ModifyFile(DataObject dataObject) {
		try {
			String filepath = "metadata.xml";
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			// Pour simplifer, on fait getElement by name au lieu de faire une
			// vrai recherche. PAS DE NOEUD AVEC NOM SIMILAIRE SINON CA MARCHE
			// PAS !
			NodeList fileList = doc.getElementsByTagName("file");
			Node file = null;
			for (int i = 0; i < fileList.getLength(); i++) {
				if (fileList.item(i).getAttributes().item(0).getTextContent()
						.equals(Integer.toString(dataObject.getId()))) {
					file = fileList.item(i);
				}
			}

			// Modify les children du file a modifier
			NodeList list = file.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);

				// update name value
				if ("name".equals(node.getNodeName())) {
					node.setTextContent(dataObject.getName());
				}

				// update server value
				if ("server".equals(node.getNodeName())) {
					node.setTextContent(dataObject.getServer());
				}

				// update port value
				if ("port".equals(node.getNodeName())) {
					node.setTextContent(Integer.toString(dataObject.getPort()));
				}

				// update absPath value
				if ("relname".equals(node.getNodeName())) {
					node.setTextContent(dataObject.getRelName());
				}

				// update repo value
				if ("repo".equals(node.getNodeName())) {
					node.setTextContent(dataObject.getRepo());
				}

				// update owner value
				if ("owner".equals(node.getNodeName())) {
					node.setTextContent(dataObject.getOwner());
				}
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);

			System.out.println("Modify Done");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
	}

	public void DeleteFile(DataObject dataObject) {
		try {
			String filepath = "metadata.xml";
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			// Find node to delete
			// Pour simplifer, on fait getElement by name au lieu de faire une
			// vrai recherche. PAS DE NOEUD AVEC NOM SIMILAIRE SINON CA MARCHE
			// PAS !
			NodeList fileList = doc.getElementsByTagName("file");
			Node file = null;
			for (int i = 0; i < fileList.getLength(); i++) {
				if (fileList.item(i).getAttributes().item(0).getTextContent()
						.equals(Integer.toString(dataObject.getId()))) {
					file = fileList.item(i);
				}
			}

			Node parent = file.getParentNode();
			parent.removeChild(file);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);

			System.out.println("Delete Done");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		} catch (NullPointerException e) {
			System.err.println("File does not exist.");
		}
	}
}
