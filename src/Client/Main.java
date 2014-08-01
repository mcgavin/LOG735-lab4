package Client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import XMLtool.xmlParser;

public class Main {

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
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main() {
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

		// Load all the tree when 1rst time opening
		DefaultMutableTreeNode top = LoadAllXmlIntoTree();
		fileTree = new JTree(top);
		fileTree.setCellRenderer(new MyTreeCellRenderer());

		for (int i = 0; i < fileTree.getRowCount(); i++) {
			fileTree.expandRow(i);
		}

		// Selection event
		fileTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
						.getPath().getLastPathComponent();
				System.out.println("You selected " + node);
				if (node == null)
					// Nothing is selected.
					return;

				Object nodeInfo = node.getUserObject();
				if (node.getUserObject() instanceof DataObject) {
					DataObject data = (DataObject) nodeInfo;
					System.out.println(data.getName());
					System.out.println(data.getId());
				} else {
					System.out.println("une repo");
				}
				GestionBouton(node);
			}
		});

		//
		fileTree.addTreeExpansionListener(new TreeExpansionListener() {

			@Override
			public void treeCollapsed(TreeExpansionEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void treeExpanded(TreeExpansionEvent arg0) {
				// TODO Auto-generated method stub
				arg0.getPath();
				arg0.getSource();
			}

		});

		// put the JTree into a JScrollPane.
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
		btnAddRepo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});

		btnDelRepo = new JButton("Delete Repository");
		btnDelRepo.setEnabled(false);
		btnPanel.add(btnDelRepo);
		btnDelRepo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});

		btnUpload = new JButton("Upload");
		btnUpload.setEnabled(false);
		btnPanel.add(btnUpload);
		btnUpload.addActionListener(new ActionListener() {
			JFileChooser _fileChooser = new JFileChooser();
			@Override
			public void actionPerformed(ActionEvent ae) {
	            //... Open a file dialog.
	            int retval = _fileChooser.showOpenDialog(frame);
	            if (retval == JFileChooser.APPROVE_OPTION) {
	                //... The user selected a file, get it, use it.
	                File file = _fileChooser.getSelectedFile();
	                
	                String repoPath = fileTree.getSelectionPath().toString().replace("[Master Root","").replace("]", "").replace(", ", "/");

	                //... Update user interface.
	                //CLIENTTEST METHOD HERE XXX
	                DataObject dataObject = new DataObject();
	                dataObject.setName(file.getName());
	                //dataObject.setServer(fileServerInfo[0]);
	                //dataObject.setPort(fileServerInfo[1]);
	                dataObject.setOwner("Client");
	                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	                Date date = new Date();
	                dataObject.setLastUpdated(dateFormat.format(date));
	                dataObject.setRepo(repoPath);
	                //Id et relname fabriquer par serveur
	                //On envoit le dataobject a moitier construit au serveur
	                String dataObjectToSend = xmlParser.ObjectToXMLString(dataObject);
	                //Send dataObjectToSend et le file au serveur
	                //somecode
	                
	            }
	        }
		});

		btnDownload = new JButton("Download");
		btnDownload.setEnabled(false);
		btnPanel.add(btnDownload);
		btnDownload.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});

		btnDelete = new JButton("Delete");
		btnDelete.setEnabled(false);
		btnPanel.add(btnDelete);
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
	}

	private DefaultMutableTreeNode LoadAllXmlIntoTree() {
		DefaultMutableTreeNode top = null;
		DefaultMutableTreeNode completedTree = null;
		try {

			File fXmlFile = new File("metadata.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			doc.getDocumentElement().normalize();

			System.out.println("Root element :"
					+ doc.getDocumentElement().getNodeName());

			top = new DefaultMutableTreeNode(doc.getFirstChild().getNodeName());
			DefaultMutableTreeNode start = new DefaultMutableTreeNode(
					"Master Root");
			completedTree = parcourir(doc.getFirstChild(), start);
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
					data.setId(Integer.parseInt(eElement.getAttribute("id")));
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

					// not a file ? make it a repo in jTREE
					parcourir(node.getChildNodes().item(i), item);

				}
				top.add(item);
			}
		}
		return top;
	}

	private static class MyTreeCellRenderer extends DefaultTreeCellRenderer {
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, sel, expanded,
					leaf, row, hasFocus);

			// decide what icons you want by examining the node
			if (value instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
				if (node.getUserObject() instanceof DataObject) {
					setIcon(UIManager.getIcon("FileView.fileIcon"));
				} else {
					setIcon(UIManager.getIcon("FileView.directoryIcon"));
				}
			}
			return this;
		}
	}

	public void GestionBouton(DefaultMutableTreeNode node) {
		if (node.getUserObject() instanceof DataObject) {
			btnDownload.setEnabled(true);
			btnDelete.setEnabled(true);
			btnAddRepo.setEnabled(false);
			btnDelRepo.setEnabled(false);
			btnUpload.setEnabled(false);
		} else {
			btnDownload.setEnabled(false);
			btnDelete.setEnabled(false);
			btnAddRepo.setEnabled(true);
			btnDelRepo.setEnabled(true);
			btnUpload.setEnabled(true);
		}
	}
}
