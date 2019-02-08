package DataType;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

public class ArrowComponent {
	private class data{
		int id;
		int pos;
	}

	//矢印に格納されるCAのID
	private ArrayList<Integer> CAs;

	//4辺のどこから出ているか記憶する
	private data start,end;
	private Point point;

	public ArrowComponent() {
		start = new data();
		end = new data();
		point = new Point();

		CAs = new ArrayList<Integer>();
	}

	public ArrowComponent(int sid,int spos,int eid,int epos) {
		start = new data();
		end = new data();
		point = new Point();

		setStart(sid);
		setStartType(spos);
		setEnd(eid);
		setEndType(epos);

		CAs = new ArrayList<Integer>();
	}

	public ArrowComponent(int pos) {
		start = new data();
		end = new data();
		start.pos = pos;
		point = new Point();

		CAs = new ArrayList<Integer>();
	}

	public void setStart(int s) {start.id = s;}
	public void setEnd(int e) {end.id = e;}
	public void setStartType(int p) {start.pos = p;}
	public void setEndType(int p) {end.pos = p;}
	public int getStart() {return start.id;}
	public int getEnd() {return end.id;}
	public int getStartType() {return start.pos;}
	public int getEndType() {return end.pos;}
	public Point getPoint() {return point;}
	public void setPoint(Point p) {point = p;}

	public void addCA(int id) {
		Boolean found = false;
		for(Integer c:CAs) {
			if(c == id)found = true;
		}
		if(!found)CAs.add(id);
	}
	public void removeCA(int id) {CAs.remove(id);}
	public void sortCAs() {Collections.sort(CAs);}
	public int getCA(int n) {return CAs.get(n);}
	public ArrayList<Integer> getCAs(){return CAs;}

}
