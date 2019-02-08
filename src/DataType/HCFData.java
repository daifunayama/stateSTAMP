package DataType;

public class HCFData extends BasicData{
	private int hcfId;

	public HCFData(String id, String title, String note) {
		super(id,title,note);
		hcfId = -1;
	}

	public HCFData() {
		super();
		hcfId = -1;
	}

	public void setHCFData(String title, int hcfId) {
		this.title = title;
		this.hcfId = hcfId;
	}

	public void setHCFId(int h) {hcfId = h;}
	public int getHCFId() {return hcfId;}

	@Override
	public String toString() {
		return id + " " + title;
	}

}
