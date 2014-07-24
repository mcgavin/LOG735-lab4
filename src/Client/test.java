package Client;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.BorderLayout;

import javax.swing.JTextArea;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
//
//import com.jgoodies.forms.layout.FormLayout;
//import com.jgoodies.forms.layout.ColumnSpec;
//import com.jgoodies.forms.layout.RowSpec;
//
//import java.awt.FlowLayout;
//import java.awt.CardLayout;
//
//import net.miginfocom.swing.MigLayout;

import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

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
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
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

		fileTree = new JTree(addNodes(null, new File("./Root Folder")));
		fileTree.setCellRenderer(new MyTreeCellRenderer());
		// Add a listener
		fileTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
						.getPath().getLastPathComponent();
				System.out.println("You selected " + node);
				GestionBouton(node);
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

	public void GestionBouton(DefaultMutableTreeNode node) {
		File f = new File(( node.getUserObject()).toString());
		if(f.isFile()){
			btnDownload.setEnabled(true);
			btnDelete.setEnabled(true);
			btnAddRepo.setEnabled(false);
			btnDelRepo.setEnabled(false);
			btnUpload.setEnabled(false);
		} else if(f.isDirectory()) {
			btnDownload.setEnabled(false);
			btnDelete.setEnabled(false);
			btnAddRepo.setEnabled(true);
			btnDelRepo.setEnabled(true);
			btnUpload.setEnabled(true);
		}
	}

	DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curTop, File dir) {
		String curPath = dir.getPath();
		DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(curPath);
		if (curTop != null) { // should only be null at root
			curTop.add(curDir);
		}
		Vector ol = new Vector();
		String[] tmp = dir.list();
		for (int i = 0; i < tmp.length; i++)
			ol.addElement(tmp[i]);
		Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
		File f;
		List<CustomClass> files = new ArrayList<CustomClass>();

		// Make two passes, one for Dirs and one for Files. This is #1.
		for (int i = 0; i < ol.size(); i++) {
			String thisObject = (String) ol.elementAt(i);
			String newPath;
			if (curPath.equals("."))
				newPath = thisObject;
			else
				newPath = curPath + File.separator + thisObject;
			if ((f = new File(newPath)).isDirectory())
				addNodes(curDir, f);
			else
				files.add(new CustomClass(curPath + File.separator + thisObject));
		}
		// Pass two: for files.
		for (int fnum = 0; fnum < files.size(); fnum++)
			curDir.add(new DefaultMutableTreeNode(files.get(fnum)));
		return curDir;
	}
	private static class MyTreeCellRenderer extends DefaultTreeCellRenderer {
		@Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            // decide what icons you want by examining the node
            if (value instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                File file = new File(node.getUserObject().toString());
                if (file.isDirectory()) {
                    // your root node, since you just put a String as a user obj                    
                    setIcon(UIManager.getIcon("FileView.directoryIcon"));
                }
                else {
                	setIcon(UIManager.getIcon("FileView.fileIcon"));
                }
            }

            return this;
        }
	}
	
	private class CustomClass extends File{

		public CustomClass(String pathname) {
			super(pathname);
			// TODO Auto-generated constructor stub
		}
		
		public String toString(){
			return this.getName();
		}
	}
}
