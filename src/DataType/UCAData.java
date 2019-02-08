package DataType;

import java.util.ArrayList;

public class UCAData extends BasicData{
	private int hazard;
	private ArrayList<HCFData> hcfdata;

	public UCAData(String id, String title, String note) {
		super(id,title,note);
		hazard = -1;
		hcfdata = new ArrayList<HCFData>();
	}

	public UCAData() {
		super();
		hazard = -1;
		hcfdata = new ArrayList<HCFData>();
	}

	public void setUCAData(String title, int hazard) {
		this.title = title;
		this.hazard = hazard;
	}

	public void setHazard(int h) {hazard = h;}
	public int getHazard() {return hazard;}

	public void addHCF(HCFData data) {
		hcfdata.add(data);
	}
	public void setHCF(int n, HCFData data){
		hcfdata.get(n).setHCFData(data.getTitle(),data.getHCFId());
	}

	public void removeHCF(int n) {
		hcfdata.remove(n);
	}

	public HCFData getHCF(int n) {return hcfdata.get(n);}
	public ArrayList<HCFData> getHCFArray() {
		return hcfdata;
	}

	@Override
	public String toString() {
		return id + " " + title;
	}
}
