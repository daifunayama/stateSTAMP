package EstablishFundamentals;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import DataType.AnalysisData;
import DataType.BasicData;

public class Link extends JPanel implements ActionListener {
	JList Alist, Hlist, Slist;
	DefaultListModel<BasicData> Alm, Hlm, Slm;
	JPanel AHPanel, HSPanel;
	JScrollPane Msp;
	int linkMode = 0;

	AnalysisData analysisData = AnalysisData.getInstance();

	public Link() {
		// リストの初期化
		Alm = AnalysisData.getInstance().getAccidentData();
		Alist = new JList(Alm);
		Alist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		Hlm = AnalysisData.getInstance().getHazardData();
		Hlist = new JList(Hlm);
		Hlist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		Slm = AnalysisData.getInstance().getSafetyData();
		Slist = new JList(Slm);
		Slist.setEnabled(false);

		// コンポーネント設置
		setLayout(null);

		JLabel Alabel = new JLabel("Accident list");
		Alabel.setBounds(20, 300, 300, 10);

		JScrollPane Asp = new JScrollPane();
		Asp.getViewport().setView(Alist);
		Asp.setBounds(10, 320, 300, 250);

		JLabel Hlabel = new JLabel("Hazard list");
		Hlabel.setBounds(330, 300, 300, 10);

		JScrollPane Hsp = new JScrollPane();
		Hsp.getViewport().setView(Hlist);
		Hsp.setBounds(320, 320, 300, 250);

		JLabel Slabel = new JLabel("Safety Constraint list");
		Slabel.setBounds(640, 300, 300, 10);

		JScrollPane Ssp = new JScrollPane();
		Ssp.getViewport().setView(Slist);
		Ssp.setBounds(630, 320, 300, 250);

		AHPanel = new JPanel();
		AHPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
		AHPanel.setBackground(Color.ORANGE);
		AHPanel.setBounds(0, 270, 630, 310);

		HSPanel = new JPanel();
		HSPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
		HSPanel.setBackground(Color.ORANGE);
		HSPanel.setBounds(310, 270, 630, 310);
		HSPanel.setVisible(false);

		JButton AHButton = new JButton("Linking Accident and Hazards");
		AHButton.setBounds(195, 260, 250, 30);
		AHButton.addActionListener(this);
		AHButton.setActionCommand("AHButton");

		JButton HSButton = new JButton("Linking hazard and SC");
		HSButton.setBounds(495, 260, 250, 30);
		HSButton.addActionListener(this);
		HSButton.setActionCommand("HSButton");

		JButton LinkButton = new JButton("Linking");
		LinkButton.setBounds(345, 600, 250, 30);
		LinkButton.addActionListener(this);
		LinkButton.setActionCommand("LinkButton");

		DrawLinks();

		add(Alabel);
		add(Asp);
		add(Hlabel);
		add(Hsp);
		add(Slabel);
		add(Ssp);

		add(AHButton);
		add(HSButton);
		add(LinkButton);

		add(AHPanel);
		add(HSPanel);

		// リストがフォーカスされたときの処理
		Alist.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					// The user is still manipulating the selection.
					return;
				}
				Object obj = Alist.getSelectedValue();
				if (obj != null) {
					if (linkMode == 0) {
						Hlist.clearSelection();
					}
				}
			}
		});

		// リストがフォーカスされたときの処理
		Hlist.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					// The user is still manipulating the selection.
					return;
				}
				Object obj = Hlist.getSelectedValue();
				if (obj != null) {
					if (linkMode == 1) {
						Slist.clearSelection();
					}
				}
			}
		});
	}

	public void DrawLinks() {
		int INTERVAL = 100;
		int CELL_HEIGHT = 80;

		if(Msp != null)remove(Msp);

		JPanel mp = new JPanel();
		mp.setLayout(null);
		mp.setBackground(Color.WHITE);

		int hCounter = 0,sCounter = 0;
		JPanel p;
		JLabel l;
		JTextArea t;

		for(int i=0;i<Alm.size();i++) {
			p = new JPanel();
			p.setBorder(new BevelBorder(BevelBorder.RAISED));

			t = new JTextArea(Alm.getElementAt(i).getId() + "\n"  + Alm.getElementAt(i).getTitle());

			t.setOpaque(false);
			t.setEditable(false);
			t.setFocusable(false);
			t.setLineWrap(true);
			t.setWrapStyleWord(true);
			t.setBounds(0,0,200,CELL_HEIGHT);

			p.add(t);
			p.setBounds(10,10 + INTERVAL*(hCounter+sCounter) + i*INTERVAL,200,CELL_HEIGHT);
			p.setBackground(new Color(255,150,100));
			mp.add(p);

			for(int j=0;j<Alm.getElementAt(i).getLinks().size();j++) {
				if(j == 0) {
					l = new JLabel("caused by");
					l.setBounds(250,30 + INTERVAL*(hCounter+sCounter) + i*INTERVAL,200,30);
					mp.add(l);
				}

				p = new JPanel();
				p.setBorder(new BevelBorder(BevelBorder.RAISED));
				t = new JTextArea(Hlm.getElementAt(Alm.getElementAt(i).getLink(j)).getId() + "\n"  + Hlm.getElementAt(Alm.getElementAt(i).getLink(j)).getTitle());

				t.setOpaque(false);
				t.setEditable(false);
				t.setFocusable(false);
				t.setLineWrap(true);
				t.setWrapStyleWord(true);
				t.setBounds(0,0,200,CELL_HEIGHT);

				p.add(t);
				p.setBounds(350,10 + INTERVAL*(hCounter+sCounter) + i*INTERVAL,200,CELL_HEIGHT);
				p.setBackground(new Color(255,255,100));
				mp.add(p);

				for(int k=0;k<Hlm.getElementAt(Alm.getElementAt(i).getLink(j)).getLinks().size();k++) {
					if(k == 0) {
						l = new JLabel("protected by");
						l.setBounds(580,30 + INTERVAL*(hCounter+sCounter) + i*INTERVAL,200,30);
						mp.add(l);
					}

					p = new JPanel();
					p.setBorder(new BevelBorder(BevelBorder.RAISED));

					t = new JTextArea(Slm.getElementAt(Hlm.getElementAt(Alm.getElementAt(i).getLink(j)).getLink(k)).getId() + "\n"
							+ Slm.getElementAt(Hlm.getElementAt(Alm.getElementAt(i).getLink(j)).getLink(k)).getTitle());

					t.setOpaque(false);
					t.setEditable(false);
					t.setFocusable(false);
					t.setLineWrap(true);
					t.setWrapStyleWord(true);
					t.setBounds(100,100,200,CELL_HEIGHT);

					p.add(t);

					p.setBounds(700,10 + INTERVAL*(hCounter+sCounter) + i*INTERVAL,200,CELL_HEIGHT);
					p.setBackground(Color.CYAN);
					mp.add(p);

					if(k<Hlm.getElementAt(Alm.getElementAt(i).getLink(j)).getLinks().size()-1)sCounter++;
				}

				if(j<Alm.getElementAt(i).getLinks().size()-1)hCounter++;
			}
		}

		mp.setPreferredSize(new Dimension(920,Alm.size()*60+(hCounter+sCounter)*INTERVAL + 10));

		Msp = new JScrollPane();
		Msp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		Msp.getViewport().setView(mp);
		Msp.setBounds(0,0,940,250);

		add(Msp);

		//リフレッシュ
		revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();

		if (actionCommand == "AHButton") {
			linkMode = 0;
			Alist.clearSelection();
			Hlist.clearSelection();
			Slist.clearSelection();
			Alist.setEnabled(true);
			Slist.setEnabled(false);
			Hlist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			Slist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			AHPanel.setVisible(true);
			HSPanel.setVisible(false);

		}
		if (actionCommand == "HSButton") {
			linkMode = 1;
			Alist.clearSelection();
			Hlist.clearSelection();
			Slist.clearSelection();
			Alist.setEnabled(false);
			Slist.setEnabled(true);
			Hlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			Slist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			AHPanel.setVisible(false);
			HSPanel.setVisible(true);

		}

		if (actionCommand == "LinkButton") {
			if (linkMode == 0) {
				int selected = Alist.getSelectedIndex();
				int[] selectedList = Hlist.getSelectedIndices();

				if (selected >= 0) {
					Alm.getElementAt(selected).clearLink();

					for (int i = 0; i < selectedList.length; i++) {
						Alm.getElementAt(selected).addLink(selectedList[i]);
					}

					DrawLinks();

					analysisData.setAccidentData(Alm);
				}
			}

			if (linkMode == 1) {
				int selected =Hlist.getSelectedIndex();
				int[] selectedList = Slist.getSelectedIndices();

				if (selected >= 0) {
					Hlm.getElementAt(selected).clearLink();

					for (int i = 0; i < selectedList.length; i++) {
						Hlm.getElementAt(selected).addLink(selectedList[i]);
					}

					DrawLinks();

					analysisData.setHazardData(Hlm);
				}
			}

		}
	}
}
