package XMLtool;

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

public class UpdateMetadata {
	public static void AddNewFile(DataObject dataObject) {
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

			// absPath elements
			Element absPath = doc.createElement("absPath");
			absPath.appendChild(doc.createTextNode(dataObject.getAbsPath()));
			fileTodAdd.appendChild(absPath);

			// repo elements
			Element repo = doc.createElement("repo");
			repo.appendChild(doc.createTextNode(dataObject.getRepo()));
			fileTodAdd.appendChild(repo);

			// owner elements
			Element owner = doc.createElement("owner");
			owner.appendChild(doc.createTextNode(dataObject.getOwner()));
			fileTodAdd.appendChild(owner);

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

	public static void ModifyFile(DataObject dataObject) {
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
			Node nodeToModifyFile = null;
			nodeToModifyFile = doc.getElementsByTagName(repoPath[nbSplits - 1])
					.item(0);

			// Find child "file" with same name as dataobject.getname

			Node nodeToModify = null;
			for (int i = 0; i < nodeToModifyFile.getChildNodes().getLength(); i++) {
				Node node = nodeToModifyFile.getChildNodes().item(i);

				for (int j = 0; j < node.getChildNodes().getLength(); j++) {
					if ("name".equals(node.getChildNodes().item(j)
							.getNodeName())
							&& node.getChildNodes().item(j).getTextContent()
									.equals(dataObject.getName())) {
						nodeToModify = node;
					}
				}

			}

			// Modify la nodeToModify
			NodeList list = nodeToModify.getChildNodes();
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
				if ("absPath".equals(node.getNodeName())) {
					node.setTextContent(dataObject.getAbsPath());
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
	

}
