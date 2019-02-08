package UnsafeControlActions;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import DataType.BasicData;
import DataType.UCAData;

/*UCA抽出テーブル*/
public class UCATable extends JPanel implements ActionListener{
	private JScrollPane Msp;
	private TablePanel[][] p;
	private AnalysisData analysisData = AnalysisData.getInstance();

	final int CELL_WIDTH = 180, CELL_HEIGHT = 80;
	int TABLE_X = 200, TABLE_Y = 30;

	public UCATable() {

		DrawTable();

	}

	// JTableではセル結合などが困難なためJPanelを並べてTableを実装する
	private void DrawTable() {

		if (Msp != null)remove(Msp);

		JPanel mp = new JPanel();
		mp.setLayout(null);
		mp.setBackground(Color.WHITE);

		JLabel CAlabel = new JLabel("Control Action");
		CAlabel.setBounds(20, TABLE_Y-30, 100, 30);
		mp.add(CAlabel);

		JLabel gwlabel[] = new JLabel[4];
		
		gwlabel[0] = new JLabel("<html>No provided<html>");
		gwlabel[1] = new JLabel("<html>Provided<html>");
		gwlabel[2] = new JLabel("<html>Too early/late<html>");
		gwlabel[3] = new JLabel("<html>Stop too soon/<br>Apply too long<html>");

		for (int i = 0; i < gwlabel.length; i++) {
			//gwlabel[i] = new JLabel("Guide word" + String.valueOf((i + 1)));
			gwlabel[i].setBounds(TABLE_X + i * CELL_WIDTH + 10, TABLE_Y-30, 100, 30);
			mp.add(gwlabel[i]);
		}

		int columns = 0;

		JPanel[] cp = new JPanel[analysisData.getCAData().size()];

		// CA列
		int panelCounter = 0;
		
		for (int i = 0; i < analysisData.getCAData().size(); i++) {
			int panelSize = analysisData.getCAData().get(i).getMaxUCASize()+1;
			
			cp[i] = new JPanel();
			cp[i].setBounds(TABLE_X - 200, TABLE_Y + (i+panelCounter) * CELL_HEIGHT, 200, CELL_HEIGHT * panelSize);
			cp[i].setBorder(new LineBorder(Color.GRAY, 1, true));
			cp[i].setBackground(Color.LIGHT_GRAY);

			// CAの名前データを取得してテーブルに入れる
			cp[i].add(new JLabel(analysisData.getCAData().get(i).getTitle()));
			mp.add(cp[i]);

			columns += analysisData.getCAData().get(i).getMaxUCASize() + 1;
			panelCounter += panelSize-1;
		}

		p = new TablePanel[columns][4];
		int CAnum = 0,UCAnum = 0,c = 0;

		//テーブルを作成
		while(CAnum < analysisData.getCAData().getSize()) {
			for (int r = 0; r < 4; r++) {
				
				System.out.println("CAnum " + CAnum + " UCAnum " + UCAnum + " r " + r + " arraysize " + analysisData.getCAData().get(CAnum).getUCAArray(r+1).size());
				
				//UCAデータがある場合コンストラクタに入れる
				if(analysisData.getCAData().get(CAnum).getUCAArray(r+1).size() > UCAnum)p[c][r] = new TablePanel(this,r, c, analysisData.getCAData().get(CAnum).getUCA(r+1,UCAnum),2);
				else p[c][r] = new TablePanel(this,r, c, null,0);
				
				p[c][r].getPanel().setBounds(TABLE_X + r * CELL_WIDTH, TABLE_Y + c * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
				p[c][r].getPanel().setBorder(new LineBorder(Color.GRAY, 1, true));
				p[c][r].getPanel().setBackground(Color.WHITE);
				mp.add(p[c][r].getPanel());
			}
			c++;
			UCAnum++;
			
			if(UCAnum >= analysisData.getCAData().get(CAnum).getMaxUCASize()) {
				
				if(analysisData.getCAData().get(CAnum).getMaxUCASize() > 0) {
					for (int r = 0; r < 4; r++) {	
						p[c][r] = new TablePanel(this,r, c, null,0);
						p[c][r].getPanel().setBounds(TABLE_X + r * CELL_WIDTH, TABLE_Y + c * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
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

		//縦のサイズを可変にする
		mp.setPreferredSize(new Dimension(920, (analysisData.getCAData().size() + panelCounter) * 80+40));

		Msp = new JScrollPane();
		Msp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		Msp.getViewport().setView(mp);
		//Msp.setBounds(0, 0, 940, 600);
		Msp.setPreferredSize(new Dimension(940,600));

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
		JButton AddButton,LinkButton;
		int hazardid = -1;
		
		private TablePanel(UCATable com, int r, int c, UCAData ucaData, int panelType) {
			/*パネルタイプ
			 * 0 未入力、追加可能
			 * 1 未入力、追加不可
			 * 2 入力済み 
			 */
			row = r;
			column = c;
			
			panel = new JPanel();
			panel.setLayout(null);

			ucaText = new JTextArea("Please input UCA");
			AddButton = new JButton("+");
			hazard = new JLabel("Not Hazardous");
			LinkButton = new JButton("H");
			
			ucaText.setLineWrap(true);
			ucaText.setWrapStyleWord(true);
			
			//すでにデータがある場合
			if(ucaData != null) {
				ucaText.setText(ucaData.getTitle());
				
				if(ucaData.getHazard() != -1)hazard.setText(analysisData.getHazardData().get(ucaData.getHazard()).getId());
				
				ucaText.setBackground(Color.CYAN);
				
				AddButton.setText("-");
				AddButton.setActionCommand(String.valueOf(r) + " " + String.valueOf(c) + " -");
			}
			
			//データが未入力の場合
			else {
				AddButton.setActionCommand(String.valueOf(r) + " " + String.valueOf(c) + " +");
			}

			ucaText.setBounds(0, 0, (int) (CELL_WIDTH * 0.75), (int) (CELL_HEIGHT * 0.7));
			ucaText.setBorder(new LineBorder(Color.GRAY, 1, true));

			hazard.setBounds(0, (int) (CELL_HEIGHT * 0.7), (int) (CELL_WIDTH * 0.75), (int) (CELL_HEIGHT * 0.3));

			AddButton.setBounds((int) (CELL_WIDTH * 0.75), 0, (int) (CELL_WIDTH * 0.25), (int) (CELL_HEIGHT * 0.7));
			LinkButton.setBounds((int) (CELL_WIDTH * 0.75), (int) (CELL_HEIGHT * 0.7), (int) (CELL_WIDTH * 0.25), (int) (CELL_HEIGHT * 0.3));
			
			popup = new JPopupMenu();
			JMenuItem item = new JMenuItem("Not Hazardous");
			item.addActionListener(this);
			popup.add(item);

			for (int i = 0; i < analysisData.getHazardData().size(); i++) {
				item = new JMenuItem(analysisData.getHazardData().get(i).getId());
				item.addActionListener(this);
				popup.add(item);
			}

			AddButton.addActionListener(com);
			
			// テキストエリアフォーカス時の利便性を上げる
			ucaText.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					if (ucaText.getText().equals("Please input UCA"))
						ucaText.setText("");
				}

				@Override
				public void focusLost(FocusEvent e) {
					if (ucaText.getText().equals(""))
						ucaText.setText("Please input UCA");
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

			panel.add(ucaText);
			panel.add(AddButton);
			panel.add(hazard);
			panel.add(LinkButton);

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

	//追加ボタンが押された時の処理
	@Override
	public void actionPerformed(ActionEvent e) {
		String[] str = e.getActionCommand().split(" ");
		int r = Integer.valueOf(str[0]);
		int c = Integer.valueOf(str[1]);
		//System.out.println(r + " " + c + " " + analysisData.getCAColumnNumber(c));
		if(str[2].equals("+")) {
			UCAData data = new UCAData("",p[c][r].getUCAText(),"");
			data.setHazard(p[c][r].getHazard());
			analysisData.getCAData().get(analysisData.getCAColumnNumber(c)).addUCA(r+1, data);
		}
		else {
			analysisData.getCAData().get(analysisData.getCAColumnNumber(c)).removeUCA(r+1,analysisData.getUCAColumnNumber(c));
		}
		
		DrawTable();
	}

}