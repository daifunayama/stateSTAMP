package CausalAnalysis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import DataType.AnalysisData;
import DataType.CAData;
import DataType.UCAData;
import DataType.UCAlistData;
import Utility.TMGridBagConstraints;
import Utility.TMGridBagLayout;

public class UCAsList extends JPanel implements ActionListener {
	JTextField titleField, noteField;
	JLabel exp;
	JList list;
	
	private JScrollPane Msp;
	final int CELL_WIDTH = 900, CELL_HEIGHT = 30;

	AnalysisData analysisData = AnalysisData.getInstance();
	DefaultListModel<UCAData> listModel;
	CAData myData;

	public UCAsList() {

		setLayout(null);
		
		//Scrollpaneの作成
		if (Msp != null)
			remove(Msp);
	
		JPanel mp = new JPanel();
		mp.setLayout(null);
		mp.setBackground(Color.WHITE);
		
		//listModel = new DefaultListModel<UCAData>();
		
		JLabel label = new JLabel("   UCA-ID        State-ID       Hazard-ID        UCA");
		label.setBounds(10,20,600,50);
		mp.add(label);
		
		ArrayList<TablePanel> tableList = new ArrayList<>();
		TablePanel tablePanel;
		
		for(int s=0;s<analysisData.getStateData().getSize();s++) {
		
			//全CAを探索
			for(int i=0;i<analysisData.getStateData().get(s).getmCAData().getSize();i++) {
				for(int r = 1;r<=6;r++) {
					for(int j=0;j<analysisData.getStateData().get(s).getmCAData().get(i).getUCAArray(r).size();j++) {
					
						UCAData data = new UCAData();
						data = analysisData.getStateData().get(s).getmCAData().get(i).getUCA(r, j);
						
						if(data.getHazard() != -1) {

							tablePanel = new TablePanel(tableList.size()+1,s,data);
							tableList.add(tablePanel);
							
							tableList.get(tableList.size()-1).getPanel().setBounds(10,50 + CELL_HEIGHT * tableList.size()-1,CELL_WIDTH,CELL_HEIGHT);
							//tableList.get(tableList.size()-1).getPanel().setBackground(Color.WHITE);
							
							mp.add(tableList.get(tableList.size()-1).getPanel());
						}
					}
				}
			}
		}
		
		for(int i=0;i<10;i++) {
			//UCAData data = new UCAData();
			// = analysisData.getStateData().get(s).getmCAData().get(i).getUCA(r, j);
			

		}
		
		//コンポーネントの設置
		
		mp.setPreferredSize(new Dimension(920, 600));

		Msp = new JScrollPane();
		Msp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		Msp.getViewport().setView(mp);
		Msp.setBounds(0, 0, 940, 600);
		Msp.setPreferredSize(new Dimension(940, 600));

		add(Msp);

		 //*/
		
		// リフレッシュ
		revalidate();
	
		
		/*
		// リストがフォーカスされたときの処理
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					// The user is still manipulating the selection.
					return;
				}
				Object obj = list.getSelectedValue();
				if (obj != null) {
					exp.setText(listModel.getElementAt(list.getSelectedIndex()).getString());
				}
			}
		});
		*/
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();

	
	}
	
	//セルに表示するPanel
	private class TablePanel{
		
		JPanel panel;
		JLabel id,state,hazard,text;
		
		private TablePanel(int ucaId, int ucaState, UCAData data) {
			
			panel = new JPanel();
			panel.setLayout(null);
			
			//panel.setBackground(Color.LIGHT_GRAY);
			
			//id.setBounds(0, 0, (int) (CELL_WIDTH * 0.75), (int) (CELL_HEIGHT * 0.7));
			id = new JLabel();
			state = new JLabel();
			hazard = new JLabel();
			text = new JLabel();
			
			id.setText("UCA-" + String.valueOf(ucaId));
			state.setText("S-" + String.valueOf(ucaState));
			hazard.setText("H-" + String.valueOf(data.getHazard()+1));
			text.setText(data.getString());
			
			
			id.setBackground(Color.CYAN);
			state.setBackground(Color.YELLOW);
			hazard.setBackground(Color.ORANGE);
			text.setBackground(Color.WHITE);

			id.setBounds(0,0,(int)(CELL_WIDTH*0.1),(int)CELL_HEIGHT);
			state.setBounds((int)(CELL_WIDTH*0.1),0,(int)(CELL_WIDTH*0.1),(int)CELL_HEIGHT);
			hazard.setBounds((int)(CELL_WIDTH*0.2),0,(int)(CELL_WIDTH*0.1),(int)CELL_HEIGHT);
			text.setBounds((int)(CELL_WIDTH*0.3),0,(int)(CELL_WIDTH*0.7),(int)CELL_HEIGHT);
			
			id.setHorizontalAlignment(JLabel.CENTER);
			state.setHorizontalAlignment(JLabel.CENTER);
			hazard.setHorizontalAlignment(JLabel.CENTER);
			text.setHorizontalAlignment(JLabel.LEFT);
			
			id.setBorder(new LineBorder(Color.GRAY, 1, true));
			state.setBorder(new LineBorder(Color.GRAY, 1, true));
			hazard.setBorder(new LineBorder(Color.GRAY, 1, true));
			text.setBorder(new LineBorder(Color.GRAY, 1, true));
			
			panel.add(id);
			panel.add(state);
			panel.add(hazard);
			panel.add(text);
		}
		
		public JPanel getPanel() {
			return panel;
		}
	}
	
	
}











