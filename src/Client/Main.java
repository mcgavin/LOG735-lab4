package Client;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.io.File;
import java.util.Arrays;

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
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;


public class Main {

	private JFrame frame;
	private JButton btnAddRepo;
	private JButton btnDelRepo;
	private JButton btnUpload;
	private JButton btnDownload;
	private JButton btnDelete;
	private JTree fileTree;
	
	private void initialize()  {
		// TODO Auto-generated method stub
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 700, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel listPanel = new JPanel();
		listPanel.setBounds(10, 11, 674, 369);
		frame.getContentPane().add(listPanel);
		listPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		
		TreeModel model = new FileTreeModel(new File("./Root Folder"));
		fileTree = new JTree(model);
		
		fileTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				File node = (File) e.getPath().getLastPathComponent();
				System.out.println("You selected " + node);
				//GestionBouton(node);
			}
		});
		
		JScrollPane scrollpane = new JScrollPane();
		scrollpane.getViewport().add(fileTree);
		listPanel.add(BorderLayout.CENTER, scrollpane);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
	}
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

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}
	class MyFile extends File {

		public MyFile(String pathname) {
			super(pathname);
			// TODO Auto-generated constructor stub
		}
		
		public String toString(){
			return this.getName();
			
		}
		
	}
	
	private static class FileTreeModel implements TreeModel {

	    private File root;

	    public FileTreeModel(File root) {
	        this.root = root;
	    }

	    @Override
	    public void addTreeModelListener(javax.swing.event.TreeModelListener l) {
	        //do nothing
	    }

	    @Override
	    public Object getChild(Object parent, int index) {
	        File f = (File) parent;
	        return f.listFiles()[index];
	    }

	    @Override
	    public int getChildCount(Object parent) {
	        File f = (File) parent;
	        if (!f.isDirectory()) {
	            return 0;
	        } else {
	            return f.list().length;
	        }
	    }

	    @Override
	    public int getIndexOfChild(Object parent, Object child) {
	        File par = (File) parent;
	        File ch = (File) child;
	        return Arrays.asList(par.listFiles()).indexOf(ch);
	    }

	    @Override
	    public Object getRoot() {
	        return root;
	    }

	    @Override
	    public boolean isLeaf(Object node) {
	        File f = (File) node;
	        return !f.isDirectory();
	    }

	    @Override
	    public void removeTreeModelListener(javax.swing.event.TreeModelListener l) {
	        //do nothing
	    }

	    @Override
	    public void valueForPathChanged(javax.swing.tree.TreePath path, Object newValue) {
	        //do nothing
	    }

	}
}
