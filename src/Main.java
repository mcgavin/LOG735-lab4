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
import javax.swing.tree.DefaultMutableTreeNode;


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
		
		
		DefaultMutableTreeNode dmt = new DefaultMutableTreeNode();
		MyFile f =  new MyFile("./Root Folder");
		MyFile f2 =  new MyFile("./Root Folder/Folder3/Doc5.txt");
		
		DefaultMutableTreeNode dmt2 = new DefaultMutableTreeNode();
		dmt2.add(new DefaultMutableTreeNode(f2));
		
		dmt.add(new DefaultMutableTreeNode(f));
		dmt.add(dmt2);
		
		fileTree = new JTree(dmt);
		
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

}
