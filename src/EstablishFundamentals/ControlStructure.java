package EstablishFundamentals;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import DataType.AnalysisData;
import DataType.ArrowComponent;
import DataType.ModelComponent;

public class ControlStructure extends JPanel implements ActionListener {
	private ControlStructure mcs;
	private JButton Controller, ControlledProcess, CommUnit, Actuator, Sensor, Other, Arrow, Delete, Name;
	private JTextArea nameText;
	private int componentMode = 0;
	private int focused = -1;
	private int selectedArrow;
	private JPopupMenu popup;
	private JMenu addCA,delCA;
	private JMenuItem delArrow;

	AnalysisData analysisData = AnalysisData.getInstance();
	ArrayList<ModelComponent> components = new ArrayList<ModelComponent>();
	ArrowComponent arrow = new ArrowComponent();;
	ArrayList<ArrowComponent> arrows = new ArrayList<ArrowComponent>();

	private static final Color clrHi = new Color(255, 229, 63);
    private static final Color clrLo = new Color(255, 105, 0);
    private static final Color clrGlowInnerHi = new Color(253, 255,255, 148);
    private static final Color clrGlowInnerLo = new Color(100,100,100);
    private static final Color clrGlowOuterHi = new Color(253, 255,255, 124);
    private static final Color clrGlowOuterLo = new Color(100,100,100);


	public ControlStructure() {
		mcs = this;

		//データの読み込み
		components = analysisData.getModelComponentData();
		arrows = analysisData.getArrowData();


		//コンポーネントの設置
		setLayout(null);

		add(new JLabel("Please input System Hazard title and note."));

		MousePaintcore mpc = new MousePaintcore();
		mpc.setBounds(0, 0, 700, 700);
		// setBackground(Color.WHITE);
		mpc.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));

		JPanel rp = new JPanel();
		rp.setBounds(720, 0, 200, 600);
		rp.setBackground(Color.WHITE);
		rp.setLayout(new GridLayout(15, 1));

		Controller = new JButton("Controller");
		ControlledProcess = new JButton("Controlled Process");
		CommUnit = new JButton("Communication Unit");
		Actuator = new JButton("Actuator");
		Sensor = new JButton("Sensor");
		Other = new JButton("Other");
		Arrow = new JButton("Arrow");
		Delete = new JButton("Delete");
		Name = new JButton("Add Name");

		nameText = new JTextArea();

		Controller.setForeground(Color.RED);

		Controller.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
		ControlledProcess.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
		CommUnit.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
		Actuator.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
		Sensor.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
		Other.setBorder(new LineBorder(Color.LIGHT_GRAY, 2, true));
		Arrow.setBackground(Color.LIGHT_GRAY);
		Delete.setBackground(new Color(255, 150, 150));
		nameText.setBorder(new LineBorder(Color.black));

		Controller.addActionListener(this);
		ControlledProcess.addActionListener(this);
		CommUnit.addActionListener(this);
		Actuator.addActionListener(this);
		Sensor.addActionListener(this);
		Other.addActionListener(this);
		Arrow.addActionListener(this);
		Delete.addActionListener(this);
		Name.addActionListener(this);

		Controller.setActionCommand("Controller");
		ControlledProcess.setActionCommand("ControlledProcess");
		CommUnit.setActionCommand("CommUnit");
		Actuator.setActionCommand("Actuator");
		Sensor.setActionCommand("Sensor");
		Other.setActionCommand("Other");
		Arrow.setActionCommand("Arrow");
		Delete.setActionCommand("Delete");
		Name.setActionCommand("Name");
		nameText.setFont(new Font("Arial", Font.PLAIN, 14));

		popup = new JPopupMenu();
		delArrow = new JMenuItem("Delete Arrow");
		addCA = new JMenu("Add CA");
		delCA = new JMenu("Delete CA");

		delArrow.addActionListener(this);
		delArrow.setName("delArrow");
		addCA.addActionListener(this);
		addCA.setName("addCA");
		delCA.addActionListener(this);

		addCA.removeAll();

		for(int i=0;i<analysisData.getCAData().size();i++) {
			JMenuItem item = new JMenuItem(analysisData.getCAData().get(i).getString());
			item.addActionListener(this);
			item.setName("addCA " + i);
			addCA.add(item);
		}

		popup.add(delArrow);
		popup.add(addCA);
		popup.add(delCA);

		rp.add(new JLabel("Palette"), BorderLayout.CENTER);
		rp.add(Controller);
		rp.add(ControlledProcess);
		rp.add(CommUnit);
		rp.add(Actuator);
		rp.add(Sensor);
		rp.add(Other);
		rp.add(Arrow);
		rp.add(Delete);
		rp.add(new JLabel(" "));
		rp.add(nameText);
		rp.add(Name);

		add(mpc);
		add(rp, BorderLayout.EAST);
	}

	/*ボタンが押された時の処理*/
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		Controller.setForeground(Color.BLACK);
		ControlledProcess.setForeground(Color.BLACK);
		CommUnit.setForeground(Color.BLACK);
		Actuator.setForeground(Color.BLACK);
		Sensor.setForeground(Color.BLACK);
		Other.setForeground(Color.BLACK);
		Arrow.setForeground(Color.BLACK);
		Delete.setForeground(Color.BLACK);

		if (actionCommand.equals("Controller")) {
			Controller.setForeground(Color.RED);
			componentMode = 0;
		}
		else if (actionCommand.equals("ControlledProcess")) {
			ControlledProcess.setForeground(Color.RED);
			componentMode = 1;
		}
		else if (actionCommand.equals("CommUnit")) {
			CommUnit.setForeground(Color.RED);
			componentMode = 2;
		}
		else if (actionCommand.equals("Actuator")) {
			Actuator.setForeground(Color.RED);
			componentMode = 3;
		}
		else if (actionCommand.equals("Sensor")) {
			Sensor.setForeground(Color.RED);
			componentMode = 4;
		}
		else if (actionCommand.equals("Other")) {
			Other.setForeground(Color.RED);
			componentMode = 5;
		}

		else if (actionCommand.equals("Arrow")) {
			Arrow.setForeground(Color.RED);
			componentMode = 6;
		}

		else if (actionCommand.equals("Delete")) {
			Delete.setForeground(Color.RED);
			componentMode = -1;
		}

		else if (actionCommand.equals("Name")) {
			if (focused >= 0) {
				components.get(focused).setName(nameText.getText());
				nameText.setText("");
				repaint();
			}
		}

		//ポップアップが選択された場合の処理
		else {

			Component c = (Component) e.getSource();
			String name = c.getName();

			String strlist[] = name.split(" ");

			if(name.equals("delArrow")) {
				arrows.remove(selectedArrow);
			}

			else if(strlist[0].equals("addCA")) {
				JMenuItem i = (JMenuItem)e.getSource();
				System.out.println(strlist[1]);

				int ca = Integer.valueOf(strlist[1]);

				arrows.get(selectedArrow).addCA(ca);
				arrows.get(selectedArrow).sortCAs();
			}

			else if(strlist[0].equals("delCA")) {
				JMenuItem i = (JMenuItem)e.getSource();
				System.out.println(strlist[1]);

				int ca = Integer.valueOf(strlist[1]);

				arrows.get(selectedArrow).removeCA(ca);
				arrows.get(selectedArrow).sortCAs();
			}
			repaint();
		}
	}

	/*マウスによる描画処理*/
	class MousePaintcore extends JPanel implements MouseMotionListener, MouseListener {
		private Point start = new Point(0, 0);
		private Point end = new Point(0, 0);
		private int x, y, w, h;
		private int gapX, gapY;
		private int catchId;
		private Boolean drawingModel = false, drawingArrow = false, moving = false;

		public MousePaintcore() {
			addMouseListener(this);
			addMouseMotionListener(this);
		}

		public void paint(Graphics g) {
			Color color = Color.BLACK;

			g.clearRect(0, 0, getWidth(), getHeight());

			g.setFont(new Font("Arial", Font.PLAIN, 14));

			// コンポーネントの描画
			for (int i = 0; i < components.size(); i++) {
				g.setColor(new Color(220,220,220));
				g.fillRect(components.get(i).getX(), components.get(i).getY(), components.get(i).getWidth(),
						components.get(i).getHeight());

				if (components.get(i).getId() == 0)
					color = Color.BLUE;
				if (components.get(i).getId() == 1)
					color = Color.RED;
				if (components.get(i).getId() == 2)
					color = Color.ORANGE;
				if (components.get(i).getId() == 3)
					color = Color.YELLOW;
				if (components.get(i).getId() == 4)
					color = Color.GREEN;
				if (components.get(i).getId() == 5)
					color = Color.LIGHT_GRAY;

				/*
				g.drawRect(components.get(i).getX(), components.get(i).getY(), components.get(i).getWidth(),
						components.get(i).getHeight());
				if (focused == i)
					g.drawRect(components.get(i).getX() + 1, components.get(i).getY() + 1,
							components.get(i).getWidth() - 2, components.get(i).getHeight() - 2);
				 */

				x = components.get(i).getX();
				y = components.get(i).getY();
				w =components.get(i).getWidth();
				h = components.get(i).getHeight();

				Shape clipShape = new Rectangle(0,0,w,h);
				BufferedImage clipImage = createClipImage(clipShape, (Graphics2D) g, w, h);
		        Graphics2D g2 = clipImage.createGraphics();

		        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		        g2.setComposite(AlphaComposite.SrcAtop);
		        g2.setPaint(new GradientPaint(0, 0, new Color(180,180,180), 0, h, new Color(255,255,255)));
		        g2.fill(clipShape);

		        paintBorderGlow(clipShape, g2, 8, w, h);

		        if (focused == i)paintBorderShadow(clipShape, g2, 4,color);
		        else paintBorderShadow(clipShape, g2, 2,color);

		        g2.dispose();
		        g.drawImage(clipImage, x,y, null);

		        g.setColor(Color.BLACK);
				g.drawString(components.get(i).getName(), components.get(i).getX() + 10,
						components.get(i).getY() + components.get(i).getHeight() / 2 - 2);
			}
			// System.out.println("focus:" + focused);

			// 矢印の描画
			for (int i = 0; i < arrows.size(); i++) {
				DrawArrow(g, arrows.get(i));
				//System.out.println(i + " " + arrows.get(i).getStart() + " " + arrows.get(i).getEnd());
			}

			// モデルの描画中
			if (drawingModel) {
				x = start.x < end.x ? start.x : end.x;
				y = start.y < end.y ? start.y : end.y;
				w = Math.abs(end.x - start.x);
				h = Math.abs(end.y - start.y);

				g.setColor(Color.WHITE);

				g.fillRect(x, y, w, h);

				if (componentMode == 0)
					color = Color.BLUE;
				if (componentMode == 1)
					color = Color.RED;
				if (componentMode == 2)
					color = Color.ORANGE;
				if (componentMode == 3)
					color = Color.YELLOW;
				if (componentMode == 4)
					color = Color.GREEN;
				if (componentMode == 5)
					color = Color.LIGHT_GRAY;

				//g.drawRect(x, y, w, h);
				//Shape clipShape = createClipShape(w, h);

				Shape clipShape = new Rectangle(0,0,w,h);
				BufferedImage clipImage = createClipImage(clipShape, (Graphics2D) g, w, h);
		        Graphics2D g2 = clipImage.createGraphics();

		        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		        g2.setComposite(AlphaComposite.SrcAtop);
		        g2.setPaint(new GradientPaint(0, 0, new Color(255,255,255), 0, h, new Color(235,235,235)));
		        g2.fill(clipShape);

		        paintBorderGlow(clipShape, g2, 8, w, h);
		        paintBorderShadow(clipShape, g2, 4,color);
		        g2.dispose();
		        g.drawImage(clipImage, x,y, null);
			}

			// 矢印の描画中
			if (drawingArrow) {
				DrawArrow(g, arrow.getStartType(), arrow.getStart());
			}
		}

		//矢印の描画（配置中）
		private void DrawArrow(Graphics g, int type, int s) {
			g.setColor(Color.BLACK);

			if (type == 0)g.drawLine(components.get(s).getXD2(), components.get(s).getY(), end.x,end.y);
			if (type == 1)g.drawLine(components.get(s).getRight(), components.get(s).getYD2(), end.x,end.y);
			if (type == 2)g.drawLine(components.get(s).getXD2(), components.get(s).getBottom(), end.x,end.y);
			if (type == 3)g.drawLine(components.get(s).getX(), components.get(s).getYD2(),end.x, end.y);

		}

		//矢印の描画（配置済み）
		private void DrawArrow(Graphics g, ArrowComponent a) {
			g.setColor(Color.BLACK);
			int s = a.getStart();
			int e = a.getEnd();
			ModelComponent cs = components.get(s);
			ModelComponent ce = components.get(e);

			//↑
			if (a.getStartType() == 0) {
				if(a.getEndType() == 0) {
					int y = cs.getY() < ce.getY() ? cs.getY() - 50: ce.getY() - 50;
					g.drawLine(cs.getXD2(),cs.getY(),cs.getXD2(),y);
					g.drawLine(ce.getXD2(),ce.getY(),ce.getXD2(),y);
					g.drawLine(cs.getXD2(),y,ce.getXD2(),y);
					DrawArrowhead(g,0,ce.getXD2(),ce.getY());
					a.setPoint(new Point((cs.getXD2() + ce.getXD2())/2,y));
				}

				if(a.getEndType() == 1) {
					g.drawLine(cs.getXD2(), cs.getY(),cs.getXD2(), ce.getYD2());
					g.drawLine(cs.getXD2(),ce.getYD2(),ce.getRight(),ce.getYD2());
					DrawArrowhead(g,1,ce.getRight(),ce.getYD2());
					a.setPoint(new Point(cs.getXD2(),ce.getYD2()));
				}

				if(a.getEndType() == 2){
					g.drawLine(cs.getXD2(),cs.getY(),ce.getXD2(),ce.getBottom());
					DrawArrowhead(g,2,ce.getXD2(),ce.getBottom());
					a.setPoint(new Point((cs.getXD2() + ce.getXD2())/2,(cs.getY() + ce.getBottom())/2));
				}

				if(a.getEndType() == 3) {
					g.drawLine(cs.getXD2(),cs.getY(),cs.getXD2(),ce.getYD2());
					g.drawLine(cs.getXD2(),ce.getYD2(),ce.getX(),ce.getYD2());
					DrawArrowhead(g,3,ce.getX(),ce.getYD2());
					a.setPoint(new Point(cs.getXD2(),ce.getYD2()));
				}
			}

			//→
			if (a.getStartType() == 1) {
				if(a.getEndType() == 0) {
					g.drawLine(cs.getRight(),cs.getYD2(),ce.getXD2(),cs.getYD2());
					g.drawLine(ce.getXD2(),cs.getYD2(),ce.getXD2(),ce.getY());
					DrawArrowhead(g,0,ce.getXD2(),ce.getY());
					a.setPoint(new Point(cs.getYD2(),ce.getXD2()));
				}
				if(a.getEndType() == 1) {
					int x = cs.getRight() > ce.getRight() ? cs.getRight() + 50: ce.getRight() + 50;
					g.drawLine(cs.getRight(),cs.getYD2(),x,cs.getYD2());
					g.drawLine(ce.getRight(),ce.getYD2(),x,ce.getYD2());
					g.drawLine(x,cs.getYD2(),x,ce.getYD2());
					DrawArrowhead(g,1,ce.getRight(),ce.getYD2());
					a.setPoint(new Point(x,(cs.getYD2()+ce.getYD2())/2));
				}
				if(a.getEndType() == 2) {
					g.drawLine(cs.getRight(),cs.getYD2(),ce.getXD2(),cs.getYD2());
					g.drawLine(ce.getXD2(),cs.getYD2(),ce.getXD2(),ce.getBottom());
					DrawArrowhead(g,2,ce.getXD2(),ce.getBottom());
					a.setPoint(new Point(ce.getXD2(),cs.getYD2()));
				}
				if(a.getEndType() == 3) {
					g.drawLine(cs.getRight(),cs.getYD2(),ce.getX(),ce.getYD2());
					DrawArrowhead(g,3,ce.getX(),ce.getYD2());
					a.setPoint(new Point((cs.getRight() + ce.getX())/2,(cs.getYD2()+ce.getYD2())/2));
				}
			}

			//↓
			if (a.getStartType() == 2) {
				if(a.getEndType() == 0) {
					g.drawLine(cs.getXD2(),cs.getBottom(),ce.getXD2(),ce.getY());
					DrawArrowhead(g,0,ce.getXD2(),ce.getY());
					a.setPoint(new Point((cs.getXD2()+ce.getXD2())/2,(cs.getBottom()+ce.getY())/2));
				}
				if(a.getEndType() == 1) {
					g.drawLine(cs.getXD2(),cs.getBottom(),cs.getXD2(),ce.getYD2());
					g.drawLine(cs.getXD2(),ce.getYD2(),ce.getRight(),ce.getYD2());
					DrawArrowhead(g,1,ce.getRight(),ce.getYD2());
					a.setPoint(new Point(cs.getXD2(),ce.getYD2()));
				}
				if(a.getEndType() == 2) {
					int y = cs.getBottom() > ce.getBottom() ? cs.getBottom() + 50: ce.getBottom() + 50;
					g.drawLine(cs.getXD2(),cs.getBottom(),cs.getXD2(),y);
					g.drawLine(ce.getXD2(),ce.getBottom(),ce.getXD2(),y);
					g.drawLine(cs.getXD2(),y,ce.getXD2(),y);
					DrawArrowhead(g,2,ce.getXD2(),ce.getBottom());
					a.setPoint(new Point((cs.getXD2()+ce.getXD2())/2,y));
				}
				if(a.getEndType() == 3){
					g.drawLine(cs.getXD2(),cs.getBottom(),cs.getXD2(),ce.getYD2());
					g.drawLine(cs.getXD2(),ce.getYD2(),ce.getX(),ce.getYD2());
					DrawArrowhead(g,3,ce.getX(),ce.getYD2());
					a.setPoint(new Point(cs.getXD2(),ce.getYD2()));
				}
			}

			//←
			if (a.getStartType() == 3) {
				if(a.getEndType() == 0) {
					g.drawLine(cs.getX(),cs.getYD2(),ce.getXD2(),cs.getYD2());
					g.drawLine(ce.getXD2(),cs.getYD2(),ce.getXD2(), ce.getY());
					DrawArrowhead(g,0,ce.getXD2(),ce.getY());
					a.setPoint(new Point(ce.getXD2(),cs.getYD2()));
				}
				if(a.getEndType() == 1) {
					g.drawLine(cs.getX(),cs.getYD2(),ce.getRight(),ce.getYD2());
					DrawArrowhead(g,1,ce.getRight(),ce.getYD2());
					a.setPoint(new Point((cs.getX()+ce.getRight())/2,(cs.getYD2()+ce.getYD2())/2));
				}
				if(a.getEndType() == 2) {
					g.drawLine(cs.getX(),cs.getYD2(),ce.getXD2(),cs.getYD2());
					g.drawLine(ce.getXD2(),cs.getYD2(),ce.getXD2(),ce.getBottom());
					DrawArrowhead(g,2,ce.getXD2(),ce.getBottom());
					a.setPoint(new Point(ce.getXD2(),cs.getYD2()));
				}
				if(a.getEndType() == 3) {
					int x = cs.getX() < ce.getX() ? cs.getX() - 50: ce.getX() - 50;
					g.drawLine(cs.getX(),cs.getYD2(),x,cs.getYD2());
					g.drawLine(ce.getX(),ce.getYD2(),x,ce.getYD2());
					g.drawLine(x,cs.getYD2(),x,ce.getYD2());
					DrawArrowhead(g,3,ce.getX(),ce.getYD2());
					a.setPoint(new Point(x,(cs.getYD2()+ce.getYD2())/2));
				}
			}

			g.setColor(Color.BLACK);
			g.fillOval(a.getPoint().x-5, a.getPoint().y-5, 10, 10);

			for(int i=0;i<a.getCAs().size();i++) {
				g.setColor(Color.WHITE);
				g.fillRect(a.getPoint().x+10, a.getPoint().y+i*14-2,analysisData.getCAData().get(a.getCA(i)).getString().length()*7,14);
				g.setColor(Color.BLACK);
				g.drawRect(a.getPoint().x+10, a.getPoint().y+i*14-2,analysisData.getCAData().get(a.getCA(i)).getString().length()*7,14);
				g.drawString(analysisData.getCAData().get(a.getCA(i)).getString(),a.getPoint().x+10, a.getPoint().y+10+i*14);
			}
		}

		//矢印の先端の描画
		private void DrawArrowhead(Graphics g, int type, int x, int y) {
			if(type == 2) {
				g.drawLine(x, y, x-5, y+5);
				g.drawLine(x, y, x+5, y+5);
			}
			if(type == 3) {
				g.drawLine(x, y, x-5, y-5);
				g.drawLine(x, y, x-5, y+5);
			}
			if(type == 0) {
				g.drawLine(x, y, x-5, y-5);
				g.drawLine(x, y, x+5, y-5);
			}
			if(type == 1) {
				g.drawLine(x, y, x+5, y-5);
				g.drawLine(x, y, x+5, y+5);
			}
		}


		// マウスクリック時
		@Override
 		public void mousePressed(MouseEvent e) {
			drawingModel = true;
			drawingArrow = true;
			moving = false;

			if(e.getButton() == MouseEvent.BUTTON3) {
				drawingModel = false;
				drawingArrow = false;

				for(int i=0;i<arrows.size();i++) {

					if(Math.sqrt(Math.pow(arrows.get(i).getPoint().x - e.getX(),2) + Math.pow(arrows.get(i).getPoint().y - e.getY(),2)) < 10) {

						delCA.removeAll();

						for(int j=0;j<arrows.get(i).getCAs().size();j++) {
							JMenuItem item = new JMenuItem(analysisData.getCAData().get(arrows.get(i).getCA(j)).getString());
							item.addActionListener(mcs);
							item.setName("delCA " + j);
							delCA.add(item);
						}
						popup.show(e.getComponent(), e.getX(), e.getY());
						selectedArrow = i;

						break;
					}
				}
			}

			// 削除
			else if (componentMode == -1) {
				drawingModel = false;
				drawingArrow = false;
				for (int i = 0; i < components.size(); i++) {
					if (e.getX() > components.get(i).getX()
							&& components.get(i).getX() + components.get(i).getWidth() > e.getX()
							&& e.getY() > components.get(i).getY()
							&& components.get(i).getY() + components.get(i).getHeight() > e.getY()) {

						// 矢印の削除
						Boolean cs,ce;
						for (int j = 0; j < arrows.size(); j++) {
							cs = false;
							ce = false;
							if(arrows.get(j).getStart() > i) {
								arrows.get(j).setStart(arrows.get(j).getStart()-1);
								cs = true;
							}
							if(arrows.get(j).getEnd() > i) {
								arrows.get(j).setEnd(arrows.get(j).getEnd()-1);
								ce = true;
							}

							if ((arrows.get(j).getStart() == i && !cs) || (arrows.get(j).getEnd() == i && !ce)) {
								arrows.remove(j);
								j--;
							}
						}

						components.remove(i);

						break;
					}
				}
				repaint();
			}

			// 矢印
			else if (componentMode == 6) {
				drawingModel = false;
				drawingArrow = false;
				arrow = new ArrowComponent();

				for (int i = 0; i < components.size(); i++) {
					// 上側
					if (e.getX() > components.get(i).getX()
							&& components.get(i).getX() + components.get(i).getWidth() > e.getX()
							&& e.getY() > components.get(i).getY() - 10 && components.get(i).getY() + 10 > e.getY()) {
						arrow.setStartType(0);
						arrow.setStart(i);
						drawingArrow = true;
						break;
					}

					// 右側
					if (e.getX() > components.get(i).getX() + components.get(i).getWidth() - 10
							&& components.get(i).getX() + components.get(i).getWidth() + 10 > e.getX()
							&& e.getY() > components.get(i).getY()
							&& components.get(i).getY() + components.get(i).getHeight() > e.getY()) {
						arrow.setStartType(1);
						arrow.setStart(i);
						drawingArrow = true;
						break;
					}

					// 下側
					if (e.getX() > components.get(i).getX()
							&& components.get(i).getX() + components.get(i).getWidth() > e.getX()
							&& e.getY() > components.get(i).getY() + components.get(i).getHeight() - 10
							&& components.get(i).getY() + components.get(i).getHeight() + 10 > e.getY()) {
						arrow.setStartType(2);
						arrow.setStart(i);
						drawingArrow = true;
						break;
					}

					// 左側
					if (e.getX() > components.get(i).getX() - 10 && components.get(i).getX() + 10 > e.getX()
							&& e.getY() > components.get(i).getY()
							&& components.get(i).getY() + components.get(i).getHeight() > e.getY()) {
						arrow.setStartType(3);
						arrow.setStart(i);
						drawingArrow = true;
						break;
					}
				}
			}

			else {
				drawingArrow = false;

				// 移動
				for (int i = 0; i < components.size(); i++) {
					if (e.getX() > components.get(i).getX()
							&& components.get(i).getX() + components.get(i).getWidth() > e.getX()
							&& e.getY() > components.get(i).getY()
							&& components.get(i).getY() + components.get(i).getHeight() > e.getY()) {
						catchId = i;
						gapX = e.getX() - components.get(i).getX();
						gapY = e.getY() - components.get(i).getY();
						drawingModel = false;
						moving = true;
						focused = i;
						repaint();
						break;
					}
				}

				if (!moving)
					focused = -1;
			}
			// 作成
			if (drawingModel)
				start = e.getPoint();
			end = e.getPoint();
			w = 0;
			h = 0;
		}

		// マウスドラッグ時
		public void mouseDragged(MouseEvent e) {
			if (drawingModel || drawingArrow) {
				end = e.getPoint();
				repaint();
			}
			if (moving) {
				components.get(catchId).setPoint(new Point(e.getX() - gapX, e.getY() - gapY));
				repaint();
			}
		}

		// マウス移動時
		public void mouseMoved(MouseEvent e) {

		}

		// マウス解放時
		public void mouseReleased(MouseEvent e) {
			// モデルの描画中
			if (drawingModel) {
				if (w > 30 && h > 30) {
					ModelComponent mc = new ModelComponent(componentMode, new Point(x, y), w, h);
					components.add(mc);
					focused = components.size() - 1;
				}
				repaint();
			}
			// 矢印の描画中
			if (drawingArrow) {
				for (int i = 0; i < components.size(); i++) {
					// 上側
					if (e.getX() > components.get(i).getX()
							&& components.get(i).getX() + components.get(i).getWidth() > e.getX()
							&& e.getY() > components.get(i).getY() - 10 && components.get(i).getY() + 10 > e.getY()) {
						arrow.setEnd(i);
						arrow.setEndType(0);
						arrows.add(arrow);
						repaint();
						break;
					}

					// 右側
					if (e.getX() > components.get(i).getX() + components.get(i).getWidth() - 10
							&& components.get(i).getX() + components.get(i).getWidth() + 10 > e.getX()
							&& e.getY() > components.get(i).getY()
							&& components.get(i).getY() + components.get(i).getHeight() > e.getY()) {
						arrow.setEnd(i);
						arrow.setEndType(1);
						arrows.add(arrow);
						repaint();
						break;
					}

					// 下側
					if (e.getX() > components.get(i).getX()
							&& components.get(i).getX() + components.get(i).getWidth() > e.getX()
							&& e.getY() > components.get(i).getY() + components.get(i).getHeight() - 10
							&& components.get(i).getY() + components.get(i).getHeight() + 10 > e.getY()) {
						arrow.setEnd(i);
						arrow.setEndType(2);
						arrows.add(arrow);
						repaint();
						break;
					}

					// 左側
					if (e.getX() > components.get(i).getX() - 10 && components.get(i).getX() + 10 > e.getX()
							&& e.getY() > components.get(i).getY()
							&& components.get(i).getY() + components.get(i).getHeight() > e.getY()) {
						arrow.setEnd(i);
						arrow.setEndType(3);
						arrows.add(arrow);
						repaint();
						break;
					}

				}

				arrow = new ArrowComponent();
				repaint();
			}

			drawingModel = false;
			drawingArrow = false;

			//データの保存
			analysisData.setModelComponentData(components);
			analysisData.setArrowData(arrows);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO 自動生成されたメソッド・スタブ

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

    private Shape createClipShape(int width, int height) {

        float border = 20.0f;
        float x1 = border;
        float y1 = border;
        float x2 = width - border;
        float y2 = height - border;
        float adj = 3.0f; // helps round out the sharp corners
        float arc = 8.0f;
        float dcx = 0.18f * width;
        float cx1 = x1-dcx;
        float cy1 = 0.40f * height;
        float cx2 = x1+dcx;
        float cy2 = 0.50f * height;
        GeneralPath gp = new GeneralPath();
        gp.moveTo(x1-adj, y1+adj);
        gp.quadTo(x1, y1, x1+adj, y1);
        gp.lineTo(x2-arc, y1);
        gp.quadTo(x2, y1, x2, y1+arc);
        gp.lineTo(x2, y2-arc);
        gp.quadTo(x2, y2, x2-arc, y2);
        gp.lineTo(x1+adj, y2);
        gp.quadTo(x1, y2, x1, y2-adj);
        gp.curveTo(cx2, cy2, cx1, cy1, x1-adj, y1+adj);
        gp.closePath();
        return gp;
    }

    private BufferedImage createClipImage(Shape s, Graphics2D g, int width, int height) {
        // Create a translucent intermediate image in which we can perform
        // the soft clipping
        GraphicsConfiguration gc = g.getDeviceConfiguration();
        BufferedImage img = gc.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        Graphics2D g2 = img.createGraphics();
        // Clear the image so all pixels have zero alpha
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0, 0, width, height);
        // Render our clip shape into the image. Note that we enable
        // antialiasing to achieve the soft clipping effect. Try
        // commenting out the line that enables antialiasing, and
        // you will see that you end up with the usual hard clipping.
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(s);
        g2.dispose();
        return img;
    }

    private Color getMixedColor(Color c1, float pct1, Color c2, float pct2) {
        float[] clr1 = c1.getComponents(null);
        float[] clr2 = c2.getComponents(null);
        for (int i = 0; i < clr1.length; i++) {
            clr1[i] = (clr1[i] * pct1) + (clr2[i] * pct2);
        }
        return new Color(clr1[0], clr1[1], clr1[2], clr1[3]);
    }

    private void paintBorderGlow(Shape clipShape, Graphics2D g2, int glowWidth, int width, int height) {
        int gw = glowWidth*2;
        for (int i=gw; i >= 2; i-=2) {
            float pct = (float)(gw - i) / (gw - 1);
            Color mixHi = getMixedColor(clrGlowInnerHi, pct,
                                        clrGlowOuterHi, 1.0f - pct);
            Color mixLo = getMixedColor(clrGlowInnerLo, pct,
                                        clrGlowOuterLo, 1.0f - pct);
            g2.setPaint(new GradientPaint(0.0f, height*0.25f, mixHi,
                                         0.0f, height, mixLo));
            //g2.setColor(Color.WHITE);
            // See my "Java 2D Trickery: Soft Clipping" entry for more
            // on why we use SRC_ATOP here
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, pct));
            g2.setStroke(new BasicStroke(i));
            g2.draw(clipShape);
        }
    }



    private void paintBorderShadow(Shape clipShape, Graphics2D g2, int shadowWidth,Color color) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        int sw = shadowWidth*2;
        for (int i=sw; i >= 2; i-=2) {
            float pct = (float)(sw - i) / (sw - 1);
            g2.setColor(getMixedColor(Color.LIGHT_GRAY, pct,
                                     color, 1.0f-pct));
            g2.setStroke(new BasicStroke(i));
            g2.draw(clipShape);
        }
    }



}