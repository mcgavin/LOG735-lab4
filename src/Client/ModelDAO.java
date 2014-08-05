package Client;

import java.io.File;
import java.io.IOException;

import javax.swing.tree.DefaultMutableTreeNode;
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
	private String filepath = "Client/metadata.xml";

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
			attr.setValue((dataObject.getId()));
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

	public void DeleteFile(DataObject dataObject) {
		try {
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
						.equals((dataObject.getId()))) {
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

	public void AddRepo(String repo, String name) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			String[] repoPath = repo.split("/");
			int nbSplits = repoPath.length;
			Node nodeToPlaceNewRepo = null;
			nodeToPlaceNewRepo = doc.getElementsByTagName(
					repoPath[nbSplits - 1]).item(0);

			Element repoToAdd = doc.createElement(name);
			nodeToPlaceNewRepo.appendChild(repoToAdd);

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);

			System.out.println("Adding Repo Done");

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

	public void DeleteRepo(String repo) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(filepath);

			// Find node to delete
			// Pour simplifer, on fait getElement by name au lieu de faire une
			// vrai recherche. PAS DE NOEUD AVEC NOM SIMILAIRE SINON CA MARCHE
			// PAS !
			String[] repoPath = repo.split("/");
			int nbSplits = repoPath.length;
			Node nodeToDelete = doc.getElementsByTagName(
					repoPath[nbSplits - 1]).item(0);

			Node parent = nodeToDelete.getParentNode();
			parent.removeChild(nodeToDelete);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filepath));
			transformer.transform(source, result);

			System.out.println("Delete Repo Done");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		} catch (NullPointerException e) {
			System.err.println("Repo does not exist.");
		}
	}

	public DefaultMutableTreeNode loadAllXmlIntoTree() {
		DefaultMutableTreeNode top = null;
		DefaultMutableTreeNode completedTree = null;
		try {

			//Reload doc for refresh
			File fXmlFile = new File(filepath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document docum = dBuilder.parse(fXmlFile);

			docum.getDocumentElement().normalize();

			System.out.println("Root element :"
					+ docum.getDocumentElement().getNodeName());

			top = new DefaultMutableTreeNode(docum.getFirstChild().getNodeName());
			DefaultMutableTreeNode start = new DefaultMutableTreeNode(
					"Master Root");
			completedTree = parcourir(docum.getFirstChild(), start);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return completedTree;
	}

	public DefaultMutableTreeNode parcourir(Node node,
			DefaultMutableTreeNode top) {
		if (node != null) {
			if (node.getNodeName().equals("file")) {
				// do something with file
				if (node.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) node;
					DataObject data = new DataObject();
					data.setId((eElement.getAttribute("id")));
					data.setName(eElement.getElementsByTagName("name").item(0)
							.getTextContent());
					data.setServer(eElement.getElementsByTagName("server")
							.item(0).getTextContent());
					data.setPort(Integer.parseInt(eElement
							.getElementsByTagName("port").item(0)
							.getTextContent()));
					data.setRelName(eElement.getElementsByTagName("relname")
							.item(0).getTextContent());
					data.setRepo(eElement.getElementsByTagName("repo").item(0)
							.getTextContent());
					data.setOwner(eElement.getElementsByTagName("owner")
							.item(0).getTextContent());
					data.setOwner(eElement.getElementsByTagName("lastupdated")
							.item(0).getTextContent());
					DefaultMutableTreeNode item = new DefaultMutableTreeNode(
							data);
					top.add(item);
				}
			} else {
				DefaultMutableTreeNode item = new DefaultMutableTreeNode(
						node.getNodeName());
				for (int i = 0; i < node.getChildNodes().getLength(); i++) {
					// not a file ? make it a repo in jTREE
					parcourir(node.getChildNodes().item(i), item);
				}
				top.add(item);
			}
		}
		return top;
	}
}