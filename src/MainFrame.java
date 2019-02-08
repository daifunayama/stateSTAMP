import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import CausalAnalysis.IdentifyHCF;
import CausalAnalysis.UCAsList;
import DataType.AnalysisData;
import EstablishFundamentals.Components;
import EstablishFundamentals.ControlStates;
import EstablishFundamentals.ControlStructure;
import EstablishFundamentals.Link;
import EstablishFundamentals.SafetyConstraints;
import EstablishFundamentals.SystemAccidents;
import EstablishFundamentals.SystemHazards;
import UnsafeControlActions.ControlActions;
import UnsafeControlActions.StateMachine;
import UnsafeControlActions.UCAStateTable;
import UnsafeControlActions.UCATable;
import Utility.TMGridBagConstraints;
import Utility.TMGridBagLayout;


public class MainFrame extends JFrame implements ActionListener,ComponentListener{
	AnalysisData analysisData;

	JLabel label;
	JPanel rp,rsp;
	TMGridBagLayout gbl;

	JMenu menu1,menu2,menu3;
	JMenuItem menuitem1,menuitem2,menuitem3,menuitem4;

    /**
     * @param args
     */
    public static void main(String[] args) {

        MainFrame frame = new MainFrame("StateSTAMP");
        frame.setVisible(true);
        //frame.setResizable(false);
    }

    MainFrame(String title){
    	System.setProperty("file.encoding", "UTF-8");

    	File file = new File("./safety.txt");
		AnalysisData.getInstance().ReadData(file);

		analysisData = AnalysisData.getInstance();

		setTitle(title);
		setBounds(0, 0, 1280, 720);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		analysisData.setWindowsSize(new Dimension(1280, 720));

		JMenuBar menubar = new JMenuBar();

		menu1 = new JMenu("File");
		menu2 = new JMenu("Edit");
		menu3 = new JMenu("Help");

		menubar.add(menu1);
		menubar.add(menu2);
		menubar.add(menu3);


		menuitem1 = new JMenuItem("New");
		menuitem2 = new JMenuItem("Open");
		menuitem3 = new JMenuItem("Save");
		menuitem4 = new JMenuItem("Help");

		menu1.add(menuitem1);
		menu1.add(menuitem2);
		menu1.add(menuitem3);
		menu3.add(menuitem4);

		menuitem1.addActionListener(this);
		menuitem2.addActionListener(this);
		menuitem3.addActionListener(this);
		menuitem4.addActionListener(this);

		setJMenuBar(menubar);

		JPanel p = new JPanel();

		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");

		DefaultMutableTreeNode ef = new DefaultMutableTreeNode("Establish Fundamentals");
		ef.add(new DefaultMutableTreeNode("System Accidents"));
		ef.add(new DefaultMutableTreeNode("System Hazards"));
		ef.add(new DefaultMutableTreeNode("Safety Constraints"));
		ef.add(new DefaultMutableTreeNode("Link"));
		ef.add(new DefaultMutableTreeNode("Control Structure"));
		ef.add(new DefaultMutableTreeNode("Control States"));
		//ef.add(new DefaultMutableTreeNode("Components"));

		DefaultMutableTreeNode uca = new DefaultMutableTreeNode("Unsafe Control Actions");
		uca.add(new DefaultMutableTreeNode("Control Actions"));
		uca.add(new DefaultMutableTreeNode("State Machine"));
		//uca.add(new DefaultMutableTreeNode("Unsafe Control Actions"));
		uca.add(new DefaultMutableTreeNode("Unsafe Control Actions with State"));

		DefaultMutableTreeNode ca = new DefaultMutableTreeNode("Causal Analysis");
		ca.add(new DefaultMutableTreeNode("UCAs List"));
		ca.add(new DefaultMutableTreeNode("Identify HCF"));

		root.add(ef);
		root.add(uca);
		root.add(ca);

		JTree tree = new JTree(root);
		tree.setRootVisible(false);

		this.addComponentListener(this);

		//ツリーアイテムの選択
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
		    public void valueChanged(TreeSelectionEvent e) {

				analysisData.setWindowsSize(getSize());

		        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		        if(node.isLeaf()) {
		        	label.setText((String)node.getUserObject());

		        	if((String)node.getUserObject() == "System Accidents") {
		        		if(rsp != null)rp.remove(rsp);
		        		rsp = new SystemAccidents();
		        		rsp.setPreferredSize(new Dimension(600, 500));

		        		rp.add(rsp,new TMGridBagConstraints(0,1,1,1));
		        	}
		        	else if((String)node.getUserObject() == "System Hazards") {
		        		if(rsp != null)rp.remove(rsp);
		        		rsp = new SystemHazards();
		        		rsp.setPreferredSize(new Dimension(600, 500));
		        		rp.add(rsp,new TMGridBagConstraints(0,1,1,1));
		        	}
		          	else if((String)node.getUserObject() == "Safety Constraints") {
		        		if(rsp != null)rp.remove(rsp);
		        		rsp = new SafetyConstraints();
		        		rsp.setPreferredSize(new Dimension(600, 500));
		        		rp.add(rsp,new TMGridBagConstraints(0,1,1,1));
		        	}

		          	else if((String)node.getUserObject() == "Control Structure") {
		        		if(rsp != null)rp.remove(rsp);
		        		rsp = new ControlStructure();
		        		rsp.setPreferredSize(new Dimension(600, 500));
		        		rp.add(rsp,new TMGridBagConstraints(0,1,1,1));
		        	}

		          	else if((String)node.getUserObject() == "Control States") {
		        		if(rsp != null)rp.remove(rsp);
		        		rsp = new ControlStates();
		        		rsp.setPreferredSize(new Dimension(600, 500));
		        		rp.add(rsp,new TMGridBagConstraints(0,1,1,1));
		        	}

		          	else if((String)node.getUserObject() == "Components") {
			        		if(rsp != null)rp.remove(rsp);
			        		rsp = new Components();
			        		rsp.setPreferredSize(new Dimension(600, 500));
			        		rp.add(rsp,new TMGridBagConstraints(0,1,1,1));
			        	}

		          	else if((String)node.getUserObject() == "Link") {
		        		if(rsp != null)rp.remove(rsp);
		        		rsp = new Link();
		        		rsp.setPreferredSize(new Dimension(600, 500));
		        		rp.add(rsp,new TMGridBagConstraints(0,1,1,1));
		        	}

		          	else if((String)node.getUserObject() == "Control Actions") {
		        		if(rsp != null)rp.remove(rsp);
		        		rsp = new ControlActions();
		        		rsp.setPreferredSize(new Dimension(600, 500));
		        		rp.add(rsp,new TMGridBagConstraints(0,1,1,1));
		        	}

		        	else if((String)node.getUserObject() == "State Machine") {
		        		if(rsp != null)rp.remove(rsp);
		        		rsp = new StateMachine();
		        		rsp.setPreferredSize(new Dimension(600, 500));
		        		rp.add(rsp,new TMGridBagConstraints(0,1,1,1));
		        	}

		          	else if((String)node.getUserObject() == "Unsafe Control Actions") {
		        		if(rsp != null)rp.remove(rsp);
		        		rsp = new UCATable();
		        		rsp.setPreferredSize(new Dimension(600, 500));
		        		rp.add(rsp,new TMGridBagConstraints(0,1,1,1));
		        	}

		          	else if((String)node.getUserObject() == "Unsafe Control Actions with State") {
		        		if(rsp != null)rp.remove(rsp);
		        		rsp = new UCAStateTable();
		        		rsp.setPreferredSize(new Dimension(600, 500));
		        		rp.add(rsp,new TMGridBagConstraints(0,1,1,1));
		        	}

		            else if((String)node.getUserObject() == "UCAs List") {
		        		if(rsp != null)rp.remove(rsp);
		        		rsp = new UCAsList();
		        		rsp.setPreferredSize(new Dimension(600, 500));
		        		rp.add(rsp,new TMGridBagConstraints(0,1,1,1));
		        	}

		            else if((String)node.getUserObject() == "Identify HCF") {
		        		if(rsp != null)rp.remove(rsp);
		        		rsp = new IdentifyHCF();
		        		rsp.setPreferredSize(new Dimension(600, 500));
		        		rp.add(rsp,new TMGridBagConstraints(0,1,1,1));
		        	}

		        	else {
		        		if(rsp != null)rp.remove(rsp);
		        		rsp = new JPanel();
		        		rsp.setPreferredSize(new Dimension(600, 500));
		        		rp.add(rsp,new TMGridBagConstraints(0,1,1,1));
		        	}

		        	repaint();

		        }
			}
		});


		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView(tree);
		scrollPane.setPreferredSize(new Dimension(280, 500));

		p.add(scrollPane);

		rp = new JPanel();

		gbl = new TMGridBagLayout(3,2,0,1);

		rp.setPreferredSize(new Dimension(500, 500));
		rp.setLayout(gbl);
		//rp.setBackground(Color.YELLOW);

		label = new JLabel("Menu");

		rsp = new JPanel();
		rsp.setPreferredSize(new Dimension(600, 500));
		//rsp.setBackground(Color.CYAN);

		rp.add(label,new TMGridBagConstraints(0, 0, 1, 1));
		rp.add(rsp,new TMGridBagConstraints(0,1,1,1));

		Container contentPane = getContentPane();
		contentPane.add(p, BorderLayout.WEST);
		contentPane.add(rp,BorderLayout.CENTER);
    }

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		JFileChooser filechooser = new JFileChooser();

		//新規
		if(obj == menuitem1) {
			AnalysisData.getInstance().ClearData();
		}

		//オープン
		if(obj == menuitem2) {
			 if (filechooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION){
			        return;
			      }
			 File file = filechooser.getSelectedFile();
			 AnalysisData.getInstance().ReadData(file);
		}

		//セーブ
		if(obj == menuitem3) {
		    int selected = filechooser.showSaveDialog(this);
		    if (selected == JFileChooser.APPROVE_OPTION){
		      File file = filechooser.getSelectedFile();
		       AnalysisData.getInstance().SaveData(file);

		    }else if (selected == JFileChooser.CANCEL_OPTION){

		    }else if (selected == JFileChooser.ERROR_OPTION){

		    }
		}

		//ヘルプ
		if(obj == menuitem4) {
			label.setText("Help");
			if(rsp != null)rp.remove(rsp);
    		rsp = new Help();
    		rsp.setPreferredSize(new Dimension(600, 500));

    		rp.add(rsp,new TMGridBagConstraints(0,1,1,1));
    		repaint();
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		//System.out.println(getSize().width);
		analysisData.setWindowsSize(getSize());
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("move!");
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}


}