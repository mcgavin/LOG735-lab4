import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.BorderLayout;

import javax.swing.JTextArea;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import java.awt.FlowLayout;
import java.awt.CardLayout;

import net.miginfocom.swing.MigLayout;

import java.awt.GridLayout;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultTreeModel;


public class test {

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
					test window = new test();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public test() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
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
		
		fileTree = new JTree();
		fileTree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("JTree") {
				{
					DefaultMutableTreeNode node_1;
					node_1 = new DefaultMutableTreeNode("colors");
						node_1.add(new DefaultMutableTreeNode(new File("blue")));
						node_1.add(new DefaultMutableTreeNode("violet"));
						node_1.add(new DefaultMutableTreeNode("red"));
						node_1.add(new DefaultMutableTreeNode("yellow"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("sports");
						node_1.add(new DefaultMutableTreeNode("basketball"));
						node_1.add(new DefaultMutableTreeNode("soccer"));
						node_1.add(new DefaultMutableTreeNode("football"));
						node_1.add(new DefaultMutableTreeNode("hockey"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("food");
					add(node_1);
				}
			}
		));
		fileTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent arg0) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
				GestionBouton(node);
			}
		});
		listPanel.add(fileTree);
		
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
	public void GestionBouton(DefaultMutableTreeNode node){
		if (node == null)
		    //Nothing is selected.  
		    return;
		if (node.isLeaf()){
			btnAddRepo.setEnabled(true);
		}
		else
			btnAddRepo.setEnabled(false);
	}
}
