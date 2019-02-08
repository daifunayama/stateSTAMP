package DataType;

import java.awt.Point;
import java.util.ArrayList;

/*
 * 描画用コンポーネント
 * */
public class ModelComponent {
	private Point point;
	private int width;
	private int height;
	private int id;
	private String name;
	private ArrayList<Integer> CAlist;
	
	public ModelComponent(int i, String n, int x, int y, int w, int h) {
		id = i;
		point = new Point(x,y);
		width = w;
		height = h;
		name = n;
		CAlist = new ArrayList<Integer>();
	}
	
	public ModelComponent(int i,Point p, int w, int h) {
		id = i;
		point = p;
		width = w;
		height = h;
		name = "";
		CAlist = new ArrayList<Integer>();
	}
	
	public int getX() {return point.x;}
	public int getY() {return point.y;}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public int getRight() {return point.x + width;}
	public int getBottom() {return point.y + height;}
	public int getXD2() {return point.x + width / 2;}
	public int getYD2() {return point.y + height / 2;}
	public int getId() {return id;}
	public String getName() {return name;}
	public void setPoint(Point p) {point = p;}
	public void setName(String n) {name = n;}
	public void addCA(int id) {CAlist.add(id);}
}
