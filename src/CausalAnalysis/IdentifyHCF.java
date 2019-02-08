package CausalAnalysis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import DataType.AnalysisData;
import DataType.HCFData;
import DataType.HCFWords;
import DataType.StateData;
import DataType.UCAData;

/*HCF特定テーブル*/
public class IdentifyHCF extends JPanel implements ActionListener{

	private AnalysisData analysisData = AnalysisData.getInstance();
	private DefaultListModel<StateData> listModel = new DefaultListModel<StateData>();

	private JScrollPane Msp;
	private HCFPanel[] p;

	final int CELL_WIDTH =760, CELL_HEIGHT = 120;
	int TABLE_X = 130, TABLE_Y = 30;

	public IdentifyHCF() {
		TABLE_Y = 10;

		analysisData.SynchronizeCAData();
		listModel = analysisData.getStateData();

		DrawTable();

	}

	private void DrawTable() {
		DefaultListModel<StateData> stateData = new DefaultListModel<StateData>();
		stateData = analysisData.getStateData();

		//System.out.println(focusedState);


		if (Msp != null)
			remove(Msp);

		JPanel mp = new JPanel();
		mp.setLayout(null);
		mp.setBackground(Color.WHITE);

		JLabel label = new JLabel("   UCA-ID        State-ID       Hazard-ID        UCA");
		label.setBounds(10,20,600,50);
		//mp.add(label);

		int columns = 0;

		int ucatotal = 0;
		for(int i=0;i<stateData.size();i++)ucatotal += stateData.get(0).getAllUCAnum();

		JPanel[] cp = new JPanel[100];

		// UCA列
		int panelCounter = 0;
		int idCounter = 0;

		for (int i = 0; i < stateData.size(); i++) {

			//System.out.println(stateData.get(i).getAllUCAnum());

			for(int j=0;j<stateData.get(i).getAllUCAnum();j++) {
				int panelSize = stateData.get(i).getUCAfromIndex(j).getHCFArray().size() + 1;

				System.out.println(i);

				cp[idCounter] = new JPanel();
				cp[idCounter].setBounds(TABLE_X - 120, TABLE_Y + (idCounter + panelCounter) * CELL_HEIGHT, 120, CELL_HEIGHT * panelSize);
				cp[idCounter].setBorder(new LineBorder(Color.GRAY, 1, true));
				cp[idCounter].setBackground(new Color(220,220,255));


				// CAの名前データを取得してテーブルに入れる
				cp[idCounter].add(new JLabel("UCA-" + (idCounter+1)));
				mp.add(cp[idCounter]);

				//columns += mCAData.get(i).getUCAArray(1).size() + 1;
				panelCounter += panelSize - 1;

				idCounter++;
			}
		}

		p = new HCFPanel[100];
		int statenum = 0,ucanum = 0, hcfnum = 0, c = 0;

/*
		//分析列
		while (statenum < stateData.getSize()) {

			//UCAデータがある場合コンストラクタに入れる
			if(stateData.get(statenum).getUCAfromIndex(ucanum).getHCFArray().size() > hcfnum)
				p[c] = new TablePanel(this,0, c, stateData.get(statenum).getUCAfromIndex(ucanum).getHCF(hcfnum),2);

			//ない場合空のブロックを作成
			else p[c] = new TablePanel(this,0, c, null,0);

			p[c].getPanel().setBounds(TABLE_X * CELL_WIDTH, TABLE_Y + c * CELL_HEIGHT, CELL_WIDTH,CELL_HEIGHT);
			p[c].getPanel().setBorder(new LineBorder(Color.GRAY, 1, true));
			p[c].getPanel().setBackground(Color.WHITE);

			mp.add(p[c].getPanel());

			c++;
			ucanum++;

			if (HCFnum >= mCAData.get(CAnum).getMaxUCASize()) {

				if (mCAData.get(CAnum).getMaxUCASize() > 0) {
					for (int r = 0; r < UCANUM; r++) {
						p[c] = new TablePanel(this, r, c, null,0);

						p[c].getPanel().setBounds(TABLE_X * CELL_WIDTH, TABLE_Y + c * CELL_HEIGHT, CELL_WIDTH,CELL_HEIGHT);
						p[c].getPanel().setBorder(new LineBorder(Color.GRAY, 1, true));
						p[c].getPanel().setBackground(Color.WHITE);
						mp.add(p[c].getPanel());
					}
					c++;
				}
				HCFnum = 0;
				statenum++;
			}
		}
*/
		//System.out.println(stateData.get(0).getUCAfromIndex(0).getHCFArray().size());

		//全状態
		for(int i=0;i<stateData.getSize();i++) {

			//全UCA
			for(int j=0;j<stateData.get(i).getAllUCAnum();j++) {

				for(int k=0;k<stateData.get(i).getUCAfromIndex(j).getHCFArray().size()+1;k++) {

					if(k<stateData.get(i).getUCAfromIndex(j).getHCFArray().size()) {
						p[c] = new HCFPanel(this,c, i,j,k, stateData.get(i).getUCAfromIndex(j), stateData.get(i).getUCAfromIndex(j).getHCF(k) ,1);
					}
					else p[c] = new HCFPanel(this,c, i,j,k, stateData.get(i).getUCAfromIndex(j), null,0);

					p[c].getPanel().setBounds(TABLE_X, TABLE_Y + c * CELL_HEIGHT, CELL_WIDTH,CELL_HEIGHT);
					p[c].getPanel().setBorder(new LineBorder(Color.BLACK, 1, true));
					p[c].getPanel().setBackground(Color.WHITE);

					mp.add(p[c].getPanel());

					c++;
				}

				/*
				p[c] = new TablePanel(this, 0, c, null,0);

				p[c].getPanel().setBounds(TABLE_X * CELL_WIDTH, TABLE_Y + c * CELL_HEIGHT, CELL_WIDTH,CELL_HEIGHT);
				p[c].getPanel().setBorder(new LineBorder(Color.GRAY, 1, true));
				p[c].getPanel().setBackground(Color.WHITE);
				mp.add(p[c].getPanel());

				c++;
				 */
			}
		}


		// 縦のサイズを可変にする
		mp.setPreferredSize(new Dimension(920, c * CELL_HEIGHT + 100));

		Msp = new JScrollPane();
		Msp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		Msp.getViewport().setView(mp);
		// Msp.setBounds(0, 0, 940, 600);
		Msp.setPreferredSize(new Dimension(940, 600));
		Msp.getVerticalScrollBar().setUnitIncrement(25);

		add(Msp);

		// リフレッシュ
		revalidate();

	}

	//セルに表示するPanel
	private class HCFPanel extends JPanel implements ActionListener{
		private int state,ucaindex,hcfindex;
		JPanel panel;
		JPanel ucapanel,scenariopanel, hintpanel;
		JTextArea ucaText,hcfText,hint;
		JPopupMenu popup;
		JButton AddButton,EditButton,LinkButton;
		int hintId = -1;

		private HCFPanel(IdentifyHCF identifyHCF, int c, int state, int ucaindex, int hcfindex, UCAData ucaData, HCFData hcfData, int panelType) {
			/*パネルタイプ
			 * 0 未入力、追加可能
			 * 1 入力済み
			 */

			HCFWords.get();

			this.state = state;
			this.ucaindex = ucaindex;
			this.hcfindex = hcfindex;

			panel = new JPanel();
			panel.setLayout(null);

			ucapanel = new JPanel();
			ucapanel.setBackground(new Color(255,255,100));
			ucapanel.add(new JLabel("UCA"));

			ucapanel = new JPanel();
			ucapanel.setBackground(new Color(255,200,200));
			ucapanel.add(new JLabel("UCA"));

			scenariopanel = new JPanel();
			scenariopanel.setBackground(new Color(255,255,150));
			scenariopanel.add(new JLabel("Scenario"));

			hintpanel = new JPanel();
			hintpanel.setBackground(new Color(200,200,200));
			hintpanel.add(new JLabel("Hint word"));

			ucaText = new JTextArea(ucaData.getTitle());
			ucaText.setBackground(new Color(230,230,230));

			hcfText = new JTextArea("Please input scenario");

			hint = new JTextArea("None");
			hint.setBackground(new Color(230,230,230));

			ucaText.setLineWrap(true);
			ucaText.setWrapStyleWord(true);
			ucaText.setEditable(false);

			hcfText.setLineWrap(true);
			hcfText.setWrapStyleWord(true);

			hint.setLineWrap(true);
			hint.setWrapStyleWord(true);
			hint.setEditable(false);

			ImageIcon imageIcon = new ImageIcon("./Resources/button_plus.png");
			Image image = imageIcon.getImage();
			Image newimg = image.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH);
			imageIcon = new ImageIcon(newimg);
			AddButton = new JButton(imageIcon);

			imageIcon = new ImageIcon("./Resources/button_edit.png");
			image = imageIcon.getImage();
			newimg = image.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH);
			imageIcon = new ImageIcon(newimg);
			EditButton = new JButton(imageIcon);

			imageIcon = new ImageIcon("./Resources/button_hazard.png");
			image = imageIcon.getImage();
			newimg = image.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH);
			imageIcon = new ImageIcon(newimg);
			LinkButton = new JButton(imageIcon);

			//すでにデータがある場合
			if(hcfData != null) {
				hcfText.setText(hcfData.getTitle());

				imageIcon = new ImageIcon("./Resources/button_minus.png");
				image = imageIcon.getImage();
				newimg = image.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH);
				imageIcon = new ImageIcon(newimg);
				AddButton = new JButton(imageIcon);
				AddButton.setActionCommand(String.valueOf(c) + " -");
				AddButton.addActionListener(identifyHCF);

				EditButton.setActionCommand(String.valueOf(c) + " E");
				EditButton.addActionListener(identifyHCF);

				if(hcfData.getHCFId() != -1) {
					hintId = hcfData.getHCFId();
					hint.setText("(" + (hintId+1) + ") " + HCFWords.getHCFWord(hintId));
				}
			}


			//データが未入力の場合
			else {
				AddButton.setActionCommand(String.valueOf(c) + " +");
				AddButton.addActionListener(identifyHCF);
			}

			ucapanel.setBounds(0,0,80,40);
			ucapanel.setBorder(new LineBorder(Color.GRAY, 1, true));

			scenariopanel.setBounds(0,40,80,40);
			scenariopanel.setBorder(new LineBorder(Color.GRAY, 1, true));

			hintpanel.setBounds(0,80,80,40);
			hintpanel.setBorder(new LineBorder(Color.GRAY, 1, true));

			ucaText.setBounds(80, 0, (int) (CELL_WIDTH -120), (int) (40));
			ucaText.setBorder(new LineBorder(Color.GRAY, 1, true));

			hcfText.setBounds(80, (int)(40), (int) (CELL_WIDTH -120), (int) (40));
			hcfText.setBorder(new LineBorder(Color.GRAY, 1, true));

			hint.setBounds(80, (int)(40*2), (int) (CELL_WIDTH -120), (int) (40));
			hint.setBorder(new LineBorder(Color.GRAY, 1, true));

			AddButton.setBounds((int) (CELL_WIDTH -40), 0, (int) (40), (int) (40));
			EditButton.setBounds((int) (CELL_WIDTH -40), (int)(40), (int) (40), (int) (40));
			LinkButton.setBounds((int) (CELL_WIDTH -40), (int) (40*2), (int) (40), (int) (40));


			popup = new JPopupMenu();
			JMenuItem item = new JMenuItem("None");
			item.addActionListener(this);
			popup.add(item);


			for (int i = 0; i < HCFWords.getHCFWorsSize(); i++) {
				item = new JMenuItem("(" + (i+1) + ") " + HCFWords.getHCFWord(i));
				item.addActionListener(this);
				popup.add(item);
			}

			panel.add(ucapanel);
			panel.add(scenariopanel);
			panel.add(hintpanel);

			panel.add(ucaText);
			panel.add(hcfText);
			panel.add(hint);

			panel.add(AddButton);
			panel.add(LinkButton);
			panel.add(EditButton);


			// テキストエリアフォーカス時の利便性を上げる
			hcfText.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					if (hcfText.getText().equals("Please input scenario"))
						hcfText.setText("");
					hcfText.setForeground(new Color(0,0,0));
				}

				@Override
				public void focusLost(FocusEvent e) {
					if (hcfText.getText().equals(""))
						hcfText.setText("Please input scenario");
					hcfText.setForeground(new Color(150,150,150));
				}
			});

			// リンクボタンが押されたらポップアップメニューを出す
			LinkButton.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
				}

				@Override
				public void mouseEntered(MouseEvent e) {
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseReleased(MouseEvent e) {
				}

				@Override
				public void mousePressed(MouseEvent e) {

					popup.show(e.getComponent(), e.getX(), e.getY());

				}

			});

		}

		private JPanel getPanel() {return panel;}
		private String getHCFText() {return hcfText.getText();}
		private int getHint() {return hintId;}
		private int getState() {return state;}
		private int getUCAIndex() {return ucaindex;}
		private int getHCFIndex() {return hcfindex;}


		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem i = (JMenuItem)e.getSource();

			hint.setText(i.getText());
			hintId = popup.getComponentIndex(i)-1;

		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {

		String[] str = e.getActionCommand().split(" ");
		int c = Integer.valueOf(str[0]);

		//System.out.println(analysisData.getCAData().get(0).getMaxUCASize());

		if(str[1].equals("+") && !p[c].getHCFText().equals("Please input scenario") && !p[c].getHCFText().equals("")) {
			HCFData data = new HCFData("",p[c].getHCFText(),"");
			data.setHCFId(p[c].getHint());
			analysisData.getStateData().get(p[c].getState()).getUCAfromIndex(p[c].getUCAIndex()).addHCF(data);
		}
		else if(str[1].equals("-")){
			analysisData.getStateData().get(p[c].getState()).getUCAfromIndex(p[c].getUCAIndex()).removeHCF(p[c].getHCFIndex());
		}
		else if(str[1].equals("E")){
			HCFData data = new HCFData("",p[c].getHCFText(),"");
			data.setHCFId(p[c].getHint());
			analysisData.getStateData().get(p[c].getState()).getUCAfromIndex(p[c].getUCAIndex()).setHCF(p[c].getHCFIndex(), data);
		}

		DrawTable();
	}



}