package UnsafeControlActions;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.QuadCurve2D;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;

import DataType.AnalysisData;
import DataType.ComponentData;
import DataType.StateData;

public class StateMachine extends JPanel implements ActionListener, ComponentListener{

	private AnalysisData analysisData = AnalysisData.getInstance();
	private DefaultListModel<StateData> stateData = analysisData.getStateData();
	JPopupMenu popup,statepopup;
	JScrollPane sp;
	JTextField textField;
	DefaultListModel<ComponentData> listModel;

	JButton StartButton;
	JButton EndButton;
	JButton ResetButton;
	JButton AddButton;
	JButton DeleteButton;
	JButton RightButton;
	JButton LeftButton;
	JButton RenameButton;
	JButton ExtendButton;
	JButton ShrinkButton;

	MousePaintcore mpc;

	private int focused = -1;
	private int popupedState = -1, popupedTrans = -1;
	private int INTERVAL = 200;
	private int YOFFSET = 600;
	private ActionListener myActionListener;

	public StateMachine(){
		this.addComponentListener(this);

		Draw();

	}

	void Draw() {
		removeAll();

		//データの読み込み
		// リストの初期化
		listModel = AnalysisData.getInstance().getComponentData();

		myActionListener = this;

		//コンポーネントの設置


		//for(int i=0;i<listModel.size();i++)
			//listModel.get(i).setTitle(listModel.get(i).getTitle() + " " + listModel.get(i).getState(0));
		for(int i=0;i<stateData.getSize();i++)stateData.get(i).resizeComponent(analysisData.getComponentData().getSize());

		//コンポーネントの設置
		setLayout(null);

		mpc = new MousePaintcore();
		mpc.setBounds(0, 0, INTERVAL * stateData.getSize() + 100, 1700);
		// setBackground(Color.WHITE);
		//mpc.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		mpc.setPreferredSize(new Dimension(1700,1700));

		sp = new JScrollPane();
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sp.getVerticalScrollBar().setUnitIncrement(25);
		sp.getHorizontalScrollBar().setUnitIncrement(25);

		sp.getViewport().setView(mpc);
		sp.getViewport().setViewPosition(new Point(0, 600));
		//sp.setBounds(0,0,940,500);

		sp.setBounds(0,0,(int)analysisData.getWindowSize().getWidth()-300,(int)analysisData.getWindowSize().getHeight()-250);

		StartButton = new JButton("Start state");
		EndButton = new JButton("End state");
		ResetButton = new JButton("Reset state");
		AddButton = new JButton("Add new state");
		DeleteButton = new JButton("Delete state");
		RightButton = new JButton("→");
		LeftButton = new JButton("←");
		RenameButton = new JButton("Rename");

		ExtendButton = new JButton("←→");
		ShrinkButton = new JButton("→←");

		textField = new JTextField();

		StartButton.setActionCommand("StartButton");
		EndButton.setActionCommand("EndButton");
		ResetButton.setActionCommand("ResetButton");
		AddButton.setActionCommand("AddButton");
		DeleteButton.setActionCommand("DeleteButton");
		RightButton.setActionCommand("RightButton");
		LeftButton.setActionCommand("LeftButton");
		RenameButton.setActionCommand("RenameButton");
		ExtendButton.setActionCommand("ExtendButton");
		ShrinkButton.setActionCommand("ShrinkButton");

		StartButton.addActionListener(this);
		EndButton.addActionListener(this);
		ResetButton.addActionListener(this);
		AddButton.addActionListener(this);
		DeleteButton.addActionListener(this);
		RightButton.addActionListener(this);
		LeftButton.addActionListener(this);
		RenameButton.addActionListener(this);
		ExtendButton.addActionListener(this);
		ShrinkButton.addActionListener(this);

		JLabel label1 = new JLabel("State option");

		//StartButton.setBounds(200, 500, 150, 40);
		//EndButton.setBounds(200, 550, 150, 40);
		//ResetButton.setBounds(200, 600, 150, 40);
		//label1.setBounds(80,550,100,40);

		JLabel label2 = new JLabel("Operation");
		AddButton.setBounds(685, (int)analysisData.getWindowSize().getHeight()-250, 150, 40);
		DeleteButton.setBounds(685, (int)analysisData.getWindowSize().getHeight()-250, 150, 40);
		RightButton.setBounds(685, (int)analysisData.getWindowSize().getHeight()-200, 150, 40);
		LeftButton.setBounds(525, (int)analysisData.getWindowSize().getHeight()-200, 150, 40);
		label2.setBounds(460,(int)analysisData.getWindowSize().getHeight()-250,100,40);
		RenameButton.setBounds(525,(int)analysisData.getWindowSize().getHeight()-150,150,40);
		textField.setBounds(525,(int)analysisData.getWindowSize().getHeight()-250,150,40);

		ExtendButton.setBounds(185, (int)analysisData.getWindowSize().getHeight()-250, 150, 40);
		ShrinkButton.setBounds(185, (int)analysisData.getWindowSize().getHeight()-150, 150, 40);

		popup = new JPopupMenu();
		JMenuItem item = new JMenuItem("Delete");
		item.addActionListener(this);
		popup.add(item);

		item = new JMenuItem("ε");
		item.addActionListener(this);
		popup.add(item);

		for (int i = 0; i < analysisData.getCAData().size(); i++) {
			item = new JMenuItem(analysisData.getCAData().get(i).getTitle());
			item.addActionListener(this);
			popup.add(item);
		}

		item.addActionListener(this);
		popup.add(item);

		add(sp);


		add(label1);
		add(label2);

		add(StartButton);
		add(EndButton);
		add(ResetButton);
		add(AddButton);
		add(DeleteButton);
		add(RenameButton);
		add(RightButton);
		add(LeftButton);
		add(textField);

		add(ExtendButton);
		add(ShrinkButton);

		this.repaint();
		this.invalidate();
		this.revalidate();

	}

	/*ボタンが押された時の処理*/
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();

		//開始状態ボタン
		if (actionCommand.equals("StartButton")) {
			if(focused >= 0) {
				stateData.get(focused).setType(1);
			}
		}
		//終了状態ボタン
		else if (actionCommand.equals("EndButton")) {
			if(focused >= 0) {
				stateData.get(focused).setType(2);
			}
		}
		//状態リセットボタン
		else if (actionCommand.equals("ResetButton")) {
			if(focused >= 0) {
				stateData.get(focused).setType(0);
			}
		}
		//状態追加ボタン
		else if(actionCommand.equals("AddButton")) {
			if(!textField.getText().equals(""))stateData.addElement(new StateData("S-" + (stateData.getSize()),textField.getText(),""));
		}

		else if(actionCommand.equals("DeleteButton")) {
			if(focused >= 0 && stateData.size() > 0) {
				//状態に伸びている遷移をすべて消去
				for(int i=0;i<stateData.getSize();i++) {
					for(int j=0;j<stateData.get(i).getTransitions().size();j++) {
						if(stateData.get(i).getTransition(j).getNextState() == focused)stateData.get(i).eraceTransition(j);
					}
				}

				stateData.remove(focused);

				//遷移のIDを修正
				for(int i=0;i<stateData.getSize();i++) {
					for(int j=0;j<stateData.get(i).getTransitions().size();j++) {
						if(stateData.get(i).getTransition(j).getNextState() >= focused)stateData.get(i).getTransition(j).setNextState(stateData.get(i).getTransition(j).getNextState()-1);
					}
				}
			}
		}

		else if(actionCommand.equals("RenameButton")) {
			if(!textField.getText().equals("") && focused >= 0 && focused < stateData.getSize())
				stateData.get(focused).setTitle(textField.getText());
		}


		//→ボタン
		else if(actionCommand.equals("RightButton")) {
			if(focused >= 0 && focused < stateData.getSize()-1) {

				StateData tmp = stateData.get(focused);
				stateData.setElementAt(stateData.get(focused+1), focused);
				stateData.setElementAt(tmp, focused+1);

				//全遷移中の該当遷移を入れ替える
				for(int i=0;i<stateData.getSize();i++) {
					for(int j=0;j<stateData.get(i).getTransitions().size();j++) {
						if(stateData.get(i).getTransition(j).getNextState() == focused)stateData.get(i).getTransition(j).setNextState(focused+1);
						else if(stateData.get(i).getTransition(j).getNextState() == focused+1)stateData.get(i).getTransition(j).setNextState(focused);
					}
				}

				focused++;
			}
		}

		//←ボタン
		else if(actionCommand.equals("LeftButton")) {
			if(focused >= 1) {

				StateData tmp = stateData.get(focused);
				stateData.setElementAt(stateData.get(focused-1), focused);
				stateData.setElementAt(tmp, focused-1);

				//全遷移中の該当遷移を入れ替える
				for(int i=0;i<stateData.getSize();i++) {
					for(int j=0;j<stateData.get(i).getTransitions().size();j++) {
						if(stateData.get(i).getTransition(j).getNextState() == focused)stateData.get(i).getTransition(j).setNextState(focused-1);
						else if(stateData.get(i).getTransition(j).getNextState() == focused-1)stateData.get(i).getTransition(j).setNextState(focused);
					}
				}

				focused--;
			}
		}

		else if(actionCommand.equals("ExtendButton"))INTERVAL += 50;
		else if(actionCommand.equals("ShrinkButton") && INTERVAL >= 100)INTERVAL -= 50;

		else {
			//ポップアップが選択された場合の処理
			JMenuItem i = (JMenuItem)e.getSource();

			//ポップアップがCAの場合
			if(popup.getComponentIndex(i) >= 0) {
				int id = popup.getComponentIndex(i)-2;
				System.out.println("CA " + id);

				//遷移を削除する
				if(id == -2) {
					stateData.get(popupedState).eraceTransition(popupedTrans);
				}
				//遷移にCAを紐づける
				else stateData.get(popupedState).getTransition(popupedTrans).setCA(id);
			}

		}

		repaint();

		//データの同期
		analysisData.setStateData(stateData);

		Draw();
	}

	/*マウスによる描画処理*/
	class MousePaintcore extends JPanel implements MouseMotionListener, MouseListener {
		//final int INTERVAL = 200;
		final int SIZE = 50;
		private Point start = new Point(0, 0);
		private Point end = new Point(0, 0);
		int pressed = -1;
		JLabel label;

		public MousePaintcore() {
			addMouseListener(this);
			addMouseMotionListener(this);

			label = new JLabel("test");
			label.setBounds(600, 100, 200, 200);
			label.setBackground(Color.RED);
			add(label);
		}

		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D)g;

			g2.setStroke(new BasicStroke(2.0f));

			g2.clearRect(0, 0, getWidth(), getHeight());

			g2.setFont(new Font("Arial", Font.PLAIN, 14));

			DrawState(g2);
			DrawArrows(g2);

			if(pressed >= 0)DrawArrow(g2);

		}

		//状態の描画
		private void DrawState(Graphics g) {
			for(int i=0;i<stateData.getSize();i++) {
				//通常状態
				if(stateData.get(i).getType() == 0)g.setColor(new Color(200,200,200));
				//初期状態
				if(stateData.get(i).getType() == 1)g.setColor(new Color(255,255,255));
				//終了状態
				if(stateData.get(i).getType() == 2)g.setColor(new Color(128,128,128));

				//((Graphics2D) g).setComposite(AlphaComposite.Clear);
				((Graphics2D) g).setComposite(AlphaComposite.Src);
				((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				g.fillOval(50+i*INTERVAL, 200+YOFFSET, SIZE, SIZE);

				//フォーカスされている場合赤く表示
				if(focused >= 0 && focused == i)g.setColor(Color.RED);
				else g.setColor(Color.GRAY);
				g.drawOval(50+i*INTERVAL, 200+YOFFSET, SIZE, SIZE);

				g.setColor(Color.BLACK);
				g.drawString("S-"+(i), SIZE+10+i*INTERVAL, 230+YOFFSET);
				g.drawString(stateData.get(i).getTitle(), SIZE+10+i*INTERVAL, 300+YOFFSET);

				for(int j=0;j<analysisData.getComponentData().getSize();j++) {
					g.drawString(analysisData.getComponentData().get(j).getTitle() + "  " + analysisData.getComponentData().get(j).getState(stateData.get(i).getComponent(j)),
							SIZE+10+i*INTERVAL, 320+j*20+YOFFSET);
				}
			}
		}

		//矢印の描画（配置中）
		private void DrawArrow(Graphics g) {
			g.setColor(Color.BLACK);

			if(start.x < end.x)g.drawLine(50+pressed*INTERVAL+SIZE/2,200+YOFFSET, end.x, end.y);
			else g.drawLine(50+pressed*INTERVAL+SIZE/2,200+SIZE+YOFFSET, end.x, end.y);
			repaint();
		}

		//矢印の描画（配置済み）
		private void DrawArrows(Graphics g) {
			Graphics2D g2 = (Graphics2D)g;
			QuadCurve2D.Double q = null;

			int arcHeight = 40;

			//全状態を探索
			for(int i=0;i<stateData.getSize();i++) {
				//遷移があれば矢印を作成
				for(int j=0;j<stateData.get(i).getTransitions().size();j++) {

					if(stateData.get(i).getTransition(j).getNextState() != i) {

						int x1 = 50+i*INTERVAL+SIZE/2;
						int x2 = 50+stateData.get(i).getTransition(j).getNextState() *INTERVAL+SIZE/2;
						if(x1 < x2) {
							q =  new QuadCurve2D.Double(x1,200+YOFFSET,(x1+x2)/2,250-(Math.abs(x1-x2)/INTERVAL)*60+YOFFSET,x2,200+YOFFSET);
							g.drawLine((x1+x2)/2, (200 + (250-(Math.abs(x1-x2)/INTERVAL)*60)) / 2+YOFFSET, (x1+x2)/2-5, (200 + (250-(Math.abs(x1-x2)/INTERVAL)*60)) / 2 -5+YOFFSET);
							g.drawLine((x1+x2)/2, (200 + (250-(Math.abs(x1-x2)/INTERVAL)*60)) / 2+YOFFSET, (x1+x2)/2-5, (200 + (250-(Math.abs(x1-x2)/INTERVAL)*60)) / 2 +5+YOFFSET);

							if(stateData.get(i).getTransition(j).getCA() == -1)g.drawString("ε", (x1+x2)/2-5, (200 + (250-(Math.abs(x1-x2)/INTERVAL)*60)) / 2+20+YOFFSET);
							else g.drawString(analysisData.getCAData().get(stateData.get(i).getTransition(j).getCA()).getTitle(), (x1+x2)/2-5, (200 + (250-(Math.abs(x1-x2)/INTERVAL)*60)) / 2+20+YOFFSET);
						}
						else {
							q =  new QuadCurve2D.Double(x1,200+SIZE+YOFFSET,(x1+x2)/2,200+(Math.abs(x1-x2)/INTERVAL)*60+YOFFSET,x2,200+SIZE+YOFFSET);
							g.drawLine((x1+x2)/2, (200 + (250+(Math.abs(x1-x2)/INTERVAL)*60)) / 2+YOFFSET, (x1+x2)/2+5, (200 + (250+(Math.abs(x1-x2)/INTERVAL)*60)) / 2-5+YOFFSET);
							g.drawLine((x1+x2)/2, (200 + (250+(Math.abs(x1-x2)/INTERVAL)*60)) / 2+YOFFSET, (x1+x2)/2+5, (200 + (250+(Math.abs(x1-x2)/INTERVAL)*60)) / 2+5+YOFFSET);

							if(stateData.get(i).getTransition(j).getCA() == -1)g.drawString("ε", (x1+x2)/2-5, (200 + (250+(Math.abs(x1-x2)/INTERVAL)*60)) / 2+20+YOFFSET);
							else g.drawString(analysisData.getCAData().get(stateData.get(i).getTransition(j).getCA()).getTitle(), (x1+x2)/2-5, (200 + (250+(Math.abs(x1-x2)/INTERVAL)*60)) / 2+20+YOFFSET);
						}

						g2.draw(q);

					}
					else {
						g2.drawArc(50+i*INTERVAL+SIZE-5, 210+YOFFSET, 51, 30, 225, 270+YOFFSET);
						g2.drawLine(50+i*INTERVAL+SIZE-5+50, 230+YOFFSET, 50+i*INTERVAL+SIZE+50, 225+YOFFSET);
						g2.drawLine(50+i*INTERVAL+SIZE-5+50, 230+YOFFSET, 50+i*INTERVAL+SIZE+40, 225+YOFFSET);

						if(stateData.get(i).getTransition(j).getCA() == -1)g.drawString("ε", 110+i*INTERVAL+SIZE, 230+YOFFSET);
						else g.drawString(analysisData.getCAData().get(stateData.get(i).getTransition(j).getCA()).getTitle(), 110+i*INTERVAL+SIZE-5, 230+YOFFSET);
					}
				}
			}
		}


		// マウスクリック時
		@Override
 		public void mousePressed(MouseEvent e) {
			start = e.getPoint();
			end = e.getPoint();

			//矢印を伸ばす
			for(int i=0;i<stateData.getSize();i++) {
				if(start.x > 50+i*INTERVAL && start.x < 50+i*INTERVAL+SIZE && start.y > 200+YOFFSET && start.y < 200+SIZE+YOFFSET) {
					pressed = i;
				}
			}
		}

		// マウスドラッグ時
		public void mouseDragged(MouseEvent e) {
			end = e.getPoint();
			repaint();

			JScrollBar vBar = sp.getHorizontalScrollBar();
			JViewport view = sp.getViewport();

			if(e.getPoint().getX() > 700) {
		        vBar.setValue((int)view.getViewPosition().getX()+10);
		        //System.out.println((int)view.getViewPosition().getX());
			}
			if(e.getPoint().getX()- (int)view.getViewPosition().getX() < 700) {
		        vBar.setValue((int)view.getViewPosition().getX()-10);
		        //System.out.println((int)view.getViewPosition().getX());
			}

			System.out.println(end.x);
		}

		// マウス移動時
		public void mouseMoved(MouseEvent e) {
		}

		// マウス解放時
		public void mouseReleased(MouseEvent e) {
			Boolean found = false;

			//遷移を作成する
			for(int i=0;i<stateData.getSize();i++) {
				if(end.x > 50+i*INTERVAL && end.x < 50+i*INTERVAL+SIZE && end.y > 200+YOFFSET && end.y < 200+SIZE+YOFFSET) {

					//矢印の先が自身以外の状態の場合
					if(pressed != i && pressed >= 0) {
						found = false;

						//既に同じ状態への遷移がある場合は作成しない
						for(int j=0;j<stateData.get(pressed).getTransitions().size();j++) {
							if(stateData.get(pressed).getTransition(j).getNextState() == i)found = true;
						}
						if(!found)stateData.get(pressed).addTransition(i);
						//System.out.println(1);
					}

					//矢印の先が自身の場合
					else if(pressed == i && pressed >= 0){
						if(e.getButton() == MouseEvent.BUTTON1) {
							focused = pressed;

						}

						if(e.getButton() == MouseEvent.BUTTON3) {
							found = false;

							//既に同じ状態への遷移がある場合は作成しない
							for(int j=0;j<stateData.get(pressed).getTransitions().size();j++) {
								if(stateData.get(pressed).getTransition(j).getNextState() == i)found = true;
							}
							if(!found)stateData.get(pressed).addTransition(i);
						}
					}
					break;
				}
			}
			repaint();
			//System.out.println(pressed + " " + focused);

			pressed = -1;

			//データの同期
			analysisData.setStateData(stateData);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ
			//popup.show(e.getComponent(), e.getX(), e.getY());

			//全状態を探索
			for(int i=0;i<stateData.getSize();i++) {
				//遷移があれば矢印を作成
				for(int j=0;j<stateData.get(i).getTransitions().size();j++) {
					int x1 = 50+i*INTERVAL+SIZE/2;
					int x2 = 50+stateData.get(i).getTransition(j).getNextState() *INTERVAL+SIZE/2;

					//異なる状態への遷移
					if(stateData.get(i).getTransition(j).getNextState() != i) {

						if(x1 < x2) {
							if(e.getX() > (x1+x2)/2 - 20 && e.getX() < (x1+x2)/2 + 10 &&
									e.getY() > (200 + (250-(Math.abs(x1-x2)/INTERVAL)*60)) / 2-15+YOFFSET && e.getY() < (200 + (250-(Math.abs(x1-x2)/INTERVAL)*60)) / 2+15+YOFFSET) {
								popup.show(e.getComponent(), e.getX(), e.getY());
								popupedState = i;
								popupedTrans = j;
							}
						}
						else {
							if(e.getX() > (x1+x2)/2 - 20 && e.getX() < (x1+x2)/2 + 10 &&
									e.getY() > (200 + (250+(Math.abs(x1-x2)/INTERVAL)*60)) / 2-15+YOFFSET && e.getY() < (200 + (250+(Math.abs(x1-x2)/INTERVAL)*60)) / 2+15+YOFFSET) {
								popup.show(e.getComponent(), e.getX(), e.getY());
								popupedState = i;
								popupedTrans = j;
							}
						}
					}

					//自身への遷移
					else {
						//g2.drawLine(50+i*INTERVAL+SIZE-5+50, 230, 50+i*INTERVAL+SIZE+50, 225);
						if(e.getX() > 50+i*INTERVAL+SIZE+30 && e.getX() < 50+i*INTERVAL+SIZE+60 &&
								e.getY() > 200+YOFFSET && e.getY() < 250+YOFFSET) {
							popup.show(e.getComponent(), e.getX(), e.getY());
							popupedState = i;
							popupedTrans = j;
						}

					}
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ

		}



	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		//System.out.println(getSize().width);
		Draw();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		System.out.println("move");
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
