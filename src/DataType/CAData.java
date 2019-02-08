package DataType;

import java.util.ArrayList;

public class CAData extends BasicData{
	private ArrayList<UCAData> uca1,uca2,uca3,uca4,uca5,uca6;

	public CAData() {
		super();
		uca1 = new ArrayList<UCAData>();
		uca2 = new ArrayList<UCAData>();
		uca3 = new ArrayList<UCAData>();
		uca4 = new ArrayList<UCAData>();
		uca5 = new ArrayList<UCAData>();
		uca6 = new ArrayList<UCAData>();
	}

	public CAData(String id, String title, String note) {
		super(id,title,note);
		uca1 = new ArrayList<UCAData>();
		uca2 = new ArrayList<UCAData>();
		uca3 = new ArrayList<UCAData>();
		uca4 = new ArrayList<UCAData>();
		uca5 = new ArrayList<UCAData>();
		uca6 = new ArrayList<UCAData>();
	}

	public CAData(String id, String title, String note, ArrayList<UCAData> u1, ArrayList<UCAData> u2, ArrayList<UCAData> u3, ArrayList<UCAData> u4, ArrayList<UCAData> u5, ArrayList<UCAData> u6) {
		super(id,title,note);
		uca1 = u1;
		uca2 = u2;
		uca3 = u3;
		uca4 = u4;
		uca5 = u5;
		uca6 = u6;
	}

	public ArrayList<UCAData> getUCAArray(int id) {
		if(id == 1)return uca1;
		if(id == 2)return uca2;
		if(id == 3)return uca3;
		if(id == 4)return uca4;
		if(id == 5)return uca5;
		if(id == 6)return uca6;

		return null;
	}

	public UCAData getUCA(int id, int n) {
		if(id == 1)return uca1.get(n);
		if(id == 2)return uca2.get(n);
		if(id == 3)return uca3.get(n);
		if(id == 4)return uca4.get(n);
		if(id == 5)return uca5.get(n);
		if(id == 6)return uca6.get(n);

		return null;
	}

	public void addUCA(int id, UCAData data) {
		if(id == 1)uca1.add(data);
		if(id == 2)uca2.add(data);
		if(id == 3)uca3.add(data);
		if(id == 4)uca4.add(data);
		if(id == 5)uca5.add(data);
		if(id == 6)uca6.add(data);
	}

	public void setUCA(int id, int pos, UCAData data) {
		if(id == 1)uca1.get(pos).setUCAData(data.getTitle(), data.getHazard());
		if(id == 2)uca2.get(pos).setUCAData(data.getTitle(), data.getHazard());
		if(id == 3)uca3.get(pos).setUCAData(data.getTitle(), data.getHazard());
		if(id == 4)uca4.get(pos).setUCAData(data.getTitle(), data.getHazard());
		if(id == 5)uca5.get(pos).setUCAData(data.getTitle(), data.getHazard());
		if(id == 6)uca6.get(pos).setUCAData(data.getTitle(), data.getHazard());
	}

	public void removeUCA(int id, int n) {
		if(id == 1)uca1.remove(n);
		if(id == 2)uca2.remove(n);
		if(id == 3)uca3.remove(n);
		if(id == 4)uca4.remove(n);
		if(id == 5)uca5.remove(n);
		if(id == 6)uca6.remove(n);
	}

	public int getMaxUCASize() {
		int s = uca1.size();

		if(s < uca2.size())s = uca2.size();
		if(s < uca3.size())s = uca3.size();
		if(s < uca4.size())s = uca4.size();
		if(s < uca5.size())s = uca5.size();
		if(s < uca6.size())s = uca6.size();
		return s;
	}

	public int getAllUCAnum() {
		int s = uca1.size();
		s += uca2.size();
		s += uca3.size();
		s += uca4.size();
		s += uca5.size();
		s += uca6.size();

		return s;
	}

}
