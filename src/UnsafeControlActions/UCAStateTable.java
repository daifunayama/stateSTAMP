package UnsafeControlActions;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import DataType.AnalysisData;
import DataType.CAData;
import DataType.StateData;
import DataType.UCAData;

/*状態ありUCA抽出テーブル*/
public class UCAStateTable extends JPanel implements ActionListener{
	//JList list;
	JComboBox box;
	int focusedState = 0;
	private AnalysisData analysisData = AnalysisData.getInstance();
	private DefaultListModel<StateData> listModel = new DefaultListModel<StateData>();

	private JScrollPane Msp;
	private TablePanel[][] p;

	private JPopupMenu popup;
	private JMenu preState,nextState;
	private int mca;

	final int CELL_WIDTH = 132, CELL_HEIGHT = 80;
	int TABLE_X = 120, TABLE_Y = 30;

	final int UCANUM = 6;

	public UCAStateTable() {
		TABLE_Y = 50;

		analysisData.SynchronizeCAData();
		listModel = analysisData.getStateData();
		//list = new JList(listModel);

		popup = new JPopupMenu();
		preState = new JMenu("Previous State");
		nextState = new JMenu("Next State");
		popup.add(preState);
		popup.add(nextState);

		Vector<String> combodata = new Vector<String>();

		for(int i=0;i<listModel.getSize();i++)combodata.add(listModel.get(i).getId() + " " + listModel.get(i).getTitle());

		box = new JComboBox(combodata);
		box.addActionListener(this);
		box.setActionCommand("box");

		add(new JLabel("Please select state:"));
		add(box);

		DrawTable();

	}


	private void DrawTable() {
		DefaultListModel<CAData> mCAData = new DefaultListModel<CAData>();
		mCAData = analysisData.getStateData().get(focusedState).getmCAData();

		//System.out.println(focusedState);


		if (Msp != null)
			remove(Msp);

		JPanel mp = new JPanel();
		mp.setLayout(null);
		mp.setBackground(Color.WHITE);

		JLabel CAlabel = new JLabel("Control Action");
		CAlabel.setBounds(20, TABLE_Y - 50, 100, 50);
		mp.add(CAlabel);

		JLabel gwlabel[] = new JLabel[UCANUM];

		gwlabel[0] = new JLabel("<html>Incorrect<br>Transition<html>");
		gwlabel[1] = new JLabel("<html>No transition <html>");
		gwlabel[2] = new JLabel("<html>Missing<br>neccesarry action<html>");
		gwlabel[3] = new JLabel("<html>Occuring<br>unneccesarry<br>action<html>");
		gwlabel[4] = new JLabel("<html>Too early/late <br>transition <html>");
		gwlabel[5] = new JLabel("<html>Incorrect <br> parameta<html>");

		for (int i = 0; i < gwlabel.length; i++) {
			//gwlabel[i] = new JLabel("Guide word" + String.valueOf((i + 1)));
			gwlabel[i].setBounds(TABLE_X + i * CELL_WIDTH + 10, TABLE_Y - 50, 100, 50);
			//gwlabel[i].setHorizontalTextPosition(JLabel.CENTER);
			mp.add(gwlabel[i]);
		}

		int columns = 0;

		JPanel[] cp = new JPanel[mCAData.size()];

		// CA列
		int panelCounter = 0;


		for (int i = 0; i < mCAData.size(); i++) {
			if(analysisData.hasCAinAllState(i)) {

				mca = i;

				int panelSize = mCAData.get(i).getMaxUCASize() + 1;

				cp[i] = new JPanel();
				cp[i].setName(String.valueOf(i));
				cp[i].setBounds(TABLE_X - 120, TABLE_Y + (i + panelCounter) * CELL_HEIGHT, 120, CELL_HEIGHT * panelSize);
				cp[i].setBorder(new LineBorder(Color.GRAY, 1, true));
				cp[i].setBackground(Color.LIGHT_GRAY);

				Boolean found = false;
				for(int j=0;j<analysisData.getStateData().get(focusedState).getTransitions().size();j++) {
					if(analysisData.getStateData().get(focusedState).getTransition(j).getCA() == i) {
						found = true;
						break;
					}
				}

				if(found)cp[i].setBackground(Color.LIGHT_GRAY);
				else cp[i].setBackground(new Color(255,100,100));

				if(found) {
					cp[i].addMouseListener(
						new MouseAdapter(){
							public void mouseClicked(MouseEvent e){
								if(e.getButton() == MouseEvent.BUTTON3) {
									preState.removeAll();
									nextState.removeAll();

									JMenuItem item;

									for(int j=0;j<analysisData.getStateData().get(focusedState).getPreviousStates().size();j++) {
										int stateId = analysisData.getStateData().get(focusedState).getPreviousStates().get(j);
										item = new JMenuItem(analysisData.getStateData().get(stateId).getTitle());
										item.setName("goto " + String.valueOf(stateId));
										item.addActionListener(new ActionListener() {
											@Override
											public void actionPerformed(ActionEvent e) {
												// TODO 自動生成されたメソッド・スタブ
												focusedState = stateId;
												DrawTable();
											}
										});

										preState.add(item);
									}

									int caId = Integer.valueOf(e.getComponent().getName());
									int stateId = analysisData.getStateData().get(focusedState).getTransitionFromCA(caId);
									item = new JMenuItem(analysisData.getStateData().get(stateId).getTitle());
									item.setName("goto " + String.valueOf(stateId));
									item.addActionListener(new ActionListener() {
										@Override
										public void actionPerformed(ActionEvent e) {
											// TODO 自動生成されたメソッド・スタブ
											focusedState = stateId;
											DrawTable();
										}
									});
									nextState.add(item);


									popup.show(e.getComponent(), e.getX(), e.getY());
								}
							}
						}
					);
				}

				// CAの名前データを取得してテーブルに入れる
				cp[i].add(new JLabel(mCAData.get(i).getTitle()));
				mp.add(cp[i]);

				columns += mCAData.get(i).getUCAArray(1).size() + 1;
				panelCounter += panelSize - 1;
			}
		}

		p = new TablePanel[100][UCANUM];
		int CAnum = 0, UCAnum = 0, c = 0;

		//分析列
		while (CAnum < mCAData.getSize()) {

			if(!analysisData.hasCAinAllState(CAnum)) {
				CAnum++;
			}
			else {
				Boolean found = false;
				for(int j=0;j<analysisData.getStateData().get(focusedState).getTransitions().size();j++) {
					if(analysisData.getStateData().get(focusedState).getTransition(j).getCA() == CAnum) {
						found = true;
						break;
					}
				}

				for (int r = 0; r < UCANUM; r++) {

					if(!found && r != 0)p[c][r] = new TablePanel(this,r, c, null,-1);

					//UCAデータがある場合コンストラクタに入れる
					else if(mCAData.get(CAnum).getUCAArray(r+1).size() > UCAnum)p[c][r] = new TablePanel(this,r, c, mCAData.get(CAnum).getUCA(r+1,UCAnum),2);

					//次の状態にUCAがある場合ピックアップ
					else if(analysisData.getStateData().get(focusedState).hasUCAinNextState(CAnum)) {
						p[c][r] = new TablePanel(this,r, c, null,3);
					}

					//ない場合空のブロックを作成
					else p[c][r] = new TablePanel(this,r, c, null,0);

					p[c][r].getPanel().setBounds(TABLE_X + r * CELL_WIDTH, TABLE_Y + c * CELL_HEIGHT, CELL_WIDTH,CELL_HEIGHT);
					p[c][r].getPanel().setBorder(new LineBorder(Color.GRAY, 1, true));
					p[c][r].getPanel().setBackground(Color.WHITE);

					mp.add(p[c][r].getPanel());
				}
				c++;
				UCAnum++;

				if (UCAnum >= mCAData.get(CAnum).getMaxUCASize()) {

					if (mCAData.get(CAnum).getMaxUCASize() > 0) {
						for (int r = 0; r < UCANUM; r++) {
							if(!found && r != 0)p[c][r] = new TablePanel(this,r, c, null,-1);
							else if(analysisData.getStateData().get(focusedState).hasUCAinNextState(CAnum)) {
								p[c][r] = new TablePanel(this,r, c, null,3);
							}
							else p[c][r] = new TablePanel(this, r, c, null,0);

							p[c][r].getPanel().setBounds(TABLE_X + r * CELL_WIDTH, TABLE_Y + c * CELL_HEIGHT, CELL_WIDTH,
									CELL_HEIGHT);
							p[c][r].getPanel().setBorder(new LineBorder(Color.GRAY, 1, true));
							p[c][r].getPanel().setBackground(Color.WHITE);
							mp.add(p[c][r].getPanel());
						}
						c++;
					}
					UCAnum = 0;
					CAnum++;
				}

			}


		}

		// 縦のサイズを可変にする
		mp.setPreferredSize(new Dimension(920, (c) * CELL_HEIGHT + 60));

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
	private class TablePanel extends JPanel implements ActionListener{
		private int row,column;
		JPanel panel;
		JTextArea ucaText;
		JLabel hazard;
		JPopupMenu popup;
		JButton AddButton,LinkButton,EditButton;
		int hazardid = -1;
		JScrollPane sp;

		private TablePanel(UCAStateTable ucaStateTable, int r, int c, UCAData ucaData, int panelType) {
			/*パネルタイプ
			 * 0 未入力、追加可能
			 * -1 未入力、追加不可
			 * 2 入力済み
			 * 3 未入力、ピックアップ
			 */
			row = r;
			column = c;

			panel = new JPanel();
			panel.setLayout(null);

			ucaText = new JTextArea("Please input scenario");
			ucaText.setForeground(new Color(150,150,150));

			hazard = new JLabel("Not Hazardous");

			ImageIcon imageIcon = new ImageIcon("./Resources/button_plus.png");
			Image image = imageIcon.getImage();
			Image newimg = image.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH);
			imageIcon = new ImageIcon(newimg);
			AddButton = new JButton(imageIcon);

			imageIcon = new ImageIcon("./Resources/button_hazard.png");
			image = imageIcon.getImage();
			newimg = image.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH);
			imageIcon = new ImageIcon(newimg);
			LinkButton = new JButton(imageIcon);

			imageIcon = new ImageIcon("./Resources/button_edit.png");
			image = imageIcon.getImage();
			newimg = image.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH);
			imageIcon = new ImageIcon(newimg);
			EditButton = new JButton(imageIcon);

			ucaText.setLineWrap(true);
			ucaText.setWrapStyleWord(true);

			sp = new JScrollPane();
			sp.getViewport().setView(ucaText);

			if(panelType == -1) {
				JPanel p = new JPanel();
				p.setBackground(Color.LIGHT_GRAY);
				p.setBounds(0,0,CELL_WIDTH,CELL_HEIGHT);
				p.setBorder(new LineBorder(Color.GRAY, 1, true));
				panel.add(p);
			}
			else {

				//すでにデータがある場合
				if(ucaData != null) {
					ucaText.setText(ucaData.getTitle());
					ucaText.setForeground(new Color(0,0,0));
					ucaText.setBackground(Color.CYAN);

					if(ucaData.getHazard() != -1) {
						hazard.setText(analysisData.getHazardData().get(ucaData.getHazard()).getId());
						hazardid = ucaData.getHazard();
					}

					imageIcon = new ImageIcon("./Resources/button_minus.png");
					image = imageIcon.getImage();
					newimg = image.getScaledInstance(32, 32,  java.awt.Image.SCALE_SMOOTH);
					imageIcon = new ImageIcon(newimg);
					AddButton = new JButton(imageIcon);
					AddButton.setActionCommand(String.valueOf(r) + " " + String.valueOf(c) + " -");
					AddButton.addActionListener(ucaStateTable);

					EditButton.setActionCommand(String.valueOf(r) + " " + String.valueOf(c) + " E");
					EditButton.addActionListener(ucaStateTable);
				}


				//データが未入力の場合
				else {
					AddButton.setActionCommand(String.valueOf(r) + " " + String.valueOf(c) + " +");
					AddButton.addActionListener(ucaStateTable);
				}

				//ピックアップ時
				if(panelType == 3)ucaText.setBackground(new Color(255,255,200));

				sp.setBounds(0, 0, (int) (CELL_WIDTH * 0.8), (int) (CELL_HEIGHT * 0.7));
				sp.setBorder(new LineBorder(Color.GRAY, 1, true));

				hazard.setBounds(0, (int) (CELL_HEIGHT * 0.7), (int) (CELL_WIDTH * 0.8), (int) (CELL_HEIGHT * 0.3));

				AddButton.setBounds((int) (CELL_WIDTH * 0.8), 0, (int) (CELL_WIDTH * 0.2), (int) (CELL_HEIGHT * 0.35));
				EditButton.setBounds((int) (CELL_WIDTH * 0.8), (int)(CELL_HEIGHT * 0.35), (int) (CELL_WIDTH * 0.2), (int) (CELL_HEIGHT * 0.35));
				LinkButton.setBounds((int) (CELL_WIDTH * 0.8), (int) (CELL_HEIGHT * 0.7), (int) (CELL_WIDTH * 0.2), (int) (CELL_HEIGHT * 0.3));


				popup = new JPopupMenu();
				JMenuItem item = new JMenuItem("Not Hazardous");
				item.addActionListener(this);
				popup.add(item);

				for (int i = 0; i < analysisData.getHazardData().size(); i++) {
					item = new JMenuItem(analysisData.getHazardData().get(i).getId());
					item.addActionListener(this);
					popup.add(item);
				}

				panel.add(sp);
				panel.add(AddButton);
				panel.add(hazard);
				panel.add(LinkButton);
				panel.add(EditButton);

			}

			// テキストエリアフォーカス時の利便性を上げる
			ucaText.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					if (ucaText.getText().equals("Please input scenario"))
						ucaText.setText("");
					ucaText.setForeground(new Color(0,0,0));
				}

				@Override
				public void focusLost(FocusEvent e) {
					if (ucaText.getText().equals(""))
						ucaText.setText("Please input scenario");
					ucaText.setForeground(new Color(150,150,150));
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
		private String getUCAText() {return ucaText.getText();}
		private int getHazard() {return hazardid;}

		@Override
		public void actionPerformed(ActionEvent e) {
			JMenuItem i = (JMenuItem)e.getSource();

			hazard.setText(i.getText());
			hazardid = popup.getComponentIndex(i)-1;

		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("box")) {
			focusedState = box.getSelectedIndex();
			//System.out.println(box.getSelectedItem().toString() + " " + focusedState);
		}
		else {
			String[] str = e.getActionCommand().split(" ");

			if(str[0].equals("cp")) {
				System.out.println(str[1]);

				//if

			}

			else {
				int r = Integer.valueOf(str[0]);
				int c = Integer.valueOf(str[1]);

				//System.out.println(analysisData.getCAData().get(0).getMaxUCASize());

				if(str[2].equals("+")) {
					UCAData data = new UCAData("",p[c][r].getUCAText(),"");
					data.setHazard(p[c][r].getHazard());
					//analysisData.getCAData().get(analysisData.getCAColumnNumber(c)).addUCA(r+1, data);
					analysisData.getStateData().get(focusedState).getmCAData().get(analysisData.getCAColumnNumber(focusedState,c)).addUCA(r+1, data);
				}
				else if(str[2].equals("-")){
					analysisData.getStateData().get(focusedState).getmCAData().get(analysisData.getCAColumnNumber(focusedState,c)).removeUCA(r+1,analysisData.getUCAColumnNumber(focusedState,c));
				}
				else if(str[2].equals("E")){
					UCAData data = new UCAData("",p[c][r].getUCAText(),"");
					data.setHazard(p[c][r].getHazard());
					analysisData.getStateData().get(focusedState).getmCAData().get(analysisData.getCAColumnNumber(focusedState,c)).setUCA(r+1,c, data);
				}
			}
			//System.out.println(analysisData.getCAData().get(0).getMaxUCASize());
		}
		DrawTable();
	}



}