package DataType;

import java.util.ArrayList;

public class BasicData {
	protected String id;
	protected String title;
	protected String note;
	protected ArrayList<Integer> link;

	public BasicData() {
		this.id = "";
		this.title = "";
		this.note = "";
		link = new ArrayList<Integer>();
	}

	public BasicData(String id, String title, String note) {
		this.id = id;
		this.title = title;
		this.note = note;
		link = new ArrayList<Integer>();
	}

	public void set(BasicData data) {
		this.id = data.id;
		this.title = data.title;
		this.note = data.note;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getId() {
		return id;
	}
	
	public int getIntId() {
		int p = id.indexOf("-");
		System.out.println(Integer.valueOf(id.substring(p+1)));
		return Integer.valueOf(id.substring(p+1));
	}

	public String getTitle() {
		return title;
	}

	public String getString() {
		return id + "  " + title + "  " + note;
	}

	@Override
	public String toString() {
		return id + " " + title;
	}
	
	public void addLink(int l) {
		link.add(l);
	}
	public int getLink(int n) {return link.get(n);}
	
	public ArrayList<Integer> getLinks() {return link;}
	
	public void clearLink() {
		link.clear();
	}

}