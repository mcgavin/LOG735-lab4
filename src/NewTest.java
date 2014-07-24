import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NewTest {

	private JFrame frame;
	private JButton btnAddRepo;
	private JButton btnDelRepo;
	private JButton btnUpload;
	private JButton btnDownload;
	private JButton btnDelete;
	private JTree fileTree;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewTest window = new NewTest();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public NewTest() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 700, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel listPanel = new JPanel();
		listPanel.setBounds(10, 11, 674, 369);
		frame.getContentPane().add(listPanel);
		listPanel.setLayout(new GridLayout(0, 1, 0, 0));

		DefaultMutableTreeNode top = new DefaultMutableTreeNode(
				"The Java Series");
		createNodes(top);
		fileTree = new JTree(top);

		ReadXmlFile();

		fileTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
						.getPath().getLastPathComponent();
				System.out.println("You selected " + node);
				if (node == null)
					// Nothing is selected.
					return;

				Object nodeInfo = node.getUserObject();
				if (node.isLeaf()) {
					DataObject data = (DataObject) nodeInfo;
					System.out.println(data.getName());
					System.out.println(data.getId());
				} else {
					System.out.println("une repo");
				}
				// GestionBouton(node);
			}
		});

		// Lastly, put the JTree into a JScrollPane.
		JScrollPane scrollpane = new JScrollPane();
		scrollpane.getViewport().add(fileTree);
		listPanel.add(BorderLayout.CENTER, scrollpane);

		JPanel btnPanel = new JPanel();
		btnPanel.setBounds(10, 391, 674, 49);
		frame.getContentPane().add(btnPanel);
		btnPanel.setLayout(new GridLayout(1, 0, 0, 0));

		btnAddRepo = new JButton("Add repository");
		btnAddRepo.setEnabled(false);
		btnPanel.add(btnAddRepo);

		btnDelRepo = new JButton("Delete Repository");
		btnDelRepo.setEnabled(false);
		btnPanel.add(btnDelRepo);

		btnUpload = new JButton("Upload");
		btnUpload.setEnabled(false);
		btnPanel.add(btnUpload);

		btnDownload = new JButton("Download");
		btnDownload.setEnabled(false);
		btnPanel.add(btnDownload);

		btnDelete = new JButton("Delete");
		btnDelete.setEnabled(false);
		btnPanel.add(btnDelete);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
	}

	private void createNodes(DefaultMutableTreeNode top) {
		DefaultMutableTreeNode category = null;
		DefaultMutableTreeNode data = null;

		category = new DefaultMutableTreeNode("Books for Java Programmers");
		top.add(category);

		// original Tutorial
		data = new DefaultMutableTreeNode(new DataObject(1001, "allo2.txt",
				"192.100.100.100", 1335, "absPath", "leRepo", "Alex"));
		category.add(data);

		// Tutorial Continued
		data = new DefaultMutableTreeNode(new DataObject(1000, "allo.txt",
				"192.100.100.100", 1335, "absPath", "leRepo", "Alex"));
		category.add(data);

	}

	public void ReadXmlFile() {
		try {

			File fXmlFile = new File("metadata.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			System.out.println("Root element :"
					+ doc.getDocumentElement().getNodeName());

			parcourir(doc.getFirstChild());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void parcourir(Node node) {
		if (node != null) {
			if (node.getNodeName().equals("file")) {
				// do something with file
				if (node.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) node;
					DataObject data = new DataObject();
					data.setId(Integer.parseInt(eElement.getAttribute("id")));
					data.setName(eElement.getElementsByTagName("name").item(0)
							.getTextContent());
					data.setServer(eElement.getElementsByTagName("server")
							.item(0).getTextContent());
					data.setPort(Integer.parseInt(eElement
							.getElementsByTagName("port").item(0)
							.getTextContent()));
					data.setAbsPath(eElement.getElementsByTagName("absPath")
							.item(0).getTextContent());
					data.setRepo(eElement.getElementsByTagName("repo").item(0)
							.getTextContent());
					data.setOwner(eElement.getElementsByTagName("owner")
							.item(0).getTextContent());

				}
			} else {
				for (int i = 0; i < node.getChildNodes().getLength(); i++) {
					// not a file ? make it a repo in jTREE
					
					parcourir(node.getChildNodes().item(i));
				}
			}
		}

	}
}
