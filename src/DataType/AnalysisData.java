package DataType;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.DefaultListModel;

/*分析用データを保持するクラス（シングルトン）*/
public class AnalysisData {
	public static AnalysisData analysisData = new AnalysisData();
	private DefaultListModel<BasicData> accidentData;
	private DefaultListModel<BasicData> hazardData;
	private DefaultListModel<BasicData> safetyData;
	private DefaultListModel<CAData> mCAData;
	private DefaultListModel<StateData> stateData;
	private DefaultListModel<ComponentData> componentData;

	private ArrayList<ModelComponent> modelData;
	private ArrayList<ArrowComponent> arrowData;

	private Dimension windowsize;

	public void setWindowsSize(Dimension d) {
		windowsize = d;
	}

	public Dimension getWindowSize() {
		return windowsize;
	}

	private AnalysisData() {
		accidentData = new DefaultListModel<BasicData>();
		hazardData = new DefaultListModel<BasicData>();
		safetyData = new DefaultListModel<BasicData>();
		mCAData = new DefaultListModel<CAData>();
		stateData = new DefaultListModel<StateData>();
		componentData = new DefaultListModel<ComponentData>();

		modelData = new ArrayList<ModelComponent>();
		arrowData = new ArrayList<ArrowComponent>();
	}

	public static AnalysisData getInstance() {
		return analysisData;
	}

	/*各フィールドのゲッター・セッター*/
	public void setAccidentData(DefaultListModel<BasicData> data) {
		accidentData = data;
	}

	public void setHazardData(DefaultListModel<BasicData> data) {
		hazardData = data;
	}

	public void setSafetyData(DefaultListModel<BasicData> data) {
		safetyData = data;
	}

	public void setCAData(DefaultListModel<CAData> data) {
		mCAData = data;
	}

	public void setCAData(int i, CAData data) {
		mCAData.set(i, data);
	}

	public void setStateData(DefaultListModel<StateData> data) {
		stateData = data;
	}

	public void setModelComponentData(ArrayList<ModelComponent> data) {
		modelData = data;
	}

	public void setArrowData(ArrayList<ArrowComponent> data) {
		arrowData = data;
	}

	public void setComponentData(DefaultListModel<ComponentData> data) {
		componentData = data;
	}

	public DefaultListModel<BasicData> getAccidentData() {
		return accidentData;
	}

	public DefaultListModel<BasicData> getHazardData() {
		return hazardData;
	}

	public DefaultListModel<BasicData> getSafetyData() {
		return safetyData;
	}

	public DefaultListModel<CAData> getCAData() {
		return mCAData;
	}

	public DefaultListModel<ComponentData> getComponentData() {
		return componentData;
	}

	public int getCAColumnNumber(int c) {
		int n = 0;
		int counter = 0;

		for(int i=0;i<mCAData.getSize();i++) {
			n += mCAData.get(i).getMaxUCASize()+1;
			System.out.println("column " + n + " " + mCAData.get(i).getMaxUCASize());
			if(n > c)break;
			counter++;
		}

		return counter;
	}

	public int getCAColumnNumber(int s, int c) {
		int n = 0;
		int counter = 0;

		for(int i=0;i<stateData.get(s).getmCAData().getSize();i++) {
			n += stateData.get(s).getmCAData().get(i).getMaxUCASize()+1;
			System.out.println("column " + n + " " + stateData.get(s).getmCAData().get(i).getMaxUCASize());
			if(n > c)break;
			counter++;
		}

		return counter;
	}

	public int getUCAColumnNumber(int c) {
		int n = c;
		for(int i=0;i<getCAColumnNumber(c);i++)n -= (mCAData.get(i).getMaxUCASize()+1);

		return n;
	}

	public int getUCAColumnNumber(int s,int c) {
		int n = c;
		for(int i=0;i<getCAColumnNumber(s,c);i++)n -= (stateData.get(s).getmCAData().get(i).getMaxUCASize()+1);

		return n;
	}


	public Boolean hasCAinAllState(int CA) {
		for(int i=0;i<stateData.size();i++) {
			for(int j=0;j<stateData.get(i).getTransitions().size();j++) {
				if(stateData.get(i).getTransition(j).getCA() == CA)return true;
			}
		}

		return false;
	}

	public DefaultListModel<StateData> getStateData(){
		return stateData;
	}

	public ArrayList<ModelComponent> getModelComponentData(){
		return modelData;
	}

	public ArrayList<ArrowComponent> getArrowData(){
		return arrowData;
	}

	/*データの初期化*/
	public void ClearData() {
		accidentData.clear();
		hazardData.clear();
		safetyData.clear();
		mCAData.clear();
		stateData.clear();
		modelData.clear();
		arrowData.clear();
		componentData.clear();
	}

	/*CAデータと状態データ内のCAデータを同期させる*/
	public void SynchronizeCAData(){
		DefaultListModel<CAData> mca;
		ArrayList<UCAData> uca1,uca2,uca3,uca4,uca5,uca6;

		for(int i=0;i<stateData.size();i++) {
			mca = new DefaultListModel<CAData>();

			for(int j=0;j<mCAData.size();j++) {
				if(stateData.get(i).getmCAData().size() > j)uca1 = stateData.get(i).getmCAData().get(j).getUCAArray(1);
				else uca1 = new ArrayList<UCAData>();
				if(stateData.get(i).getmCAData().size() > j)uca2 = stateData.get(i).getmCAData().get(j).getUCAArray(2);
				else uca2 = new ArrayList<UCAData>();
				if(stateData.get(i).getmCAData().size() > j)uca3 = stateData.get(i).getmCAData().get(j).getUCAArray(3);
				else uca3 = new ArrayList<UCAData>();
				if(stateData.get(i).getmCAData().size() > j)uca4 = stateData.get(i).getmCAData().get(j).getUCAArray(4);
				else uca4 = new ArrayList<UCAData>();
				if(stateData.get(i).getmCAData().size() > j)uca5 = stateData.get(i).getmCAData().get(j).getUCAArray(5);
				else uca5 = new ArrayList<UCAData>();
				if(stateData.get(i).getmCAData().size() > j)uca6 = stateData.get(i).getmCAData().get(j).getUCAArray(6);
				else uca6 = new ArrayList<UCAData>();


				CAData data = new CAData(mCAData.get(j).getId(),mCAData.get(j).getTitle(),mCAData.get(j).getString(),uca1,uca2,uca3,uca4,uca5,uca6);
				mca.add(mca.size(),data);
			}
			stateData.get(i).setCAData(mca);
		}
	}

	/*データの読み込み*/
	public void ReadData(File file) {

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			BasicData data;
			CAData cdata;
			StateData sdata;
			ModelComponent mcdata;
			ArrowComponent acdata;
			ComponentData cmdata;
			UCAData ucadata;

			String str;

			ClearData();

			str = br.readLine();
			str = br.readLine();

			while(str != null && !str.equals("hazard")){
				String[] strlist = str.split("  ");
				if(strlist.length == 2) data = new BasicData(strlist[0],strlist[1],"");
				else data = new BasicData(strlist[0],strlist[1],strlist[2]);
			    str = br.readLine();

			    strlist = str.split(" ");
			    for(int i=0;i<strlist.length-1;i++)data.addLink(Integer.valueOf(strlist[i]));
			    str = br.readLine();

			    accidentData.add(0,data);
			}

			str = br.readLine();

			while(str != null && !str.equals("safety")){
				String[] strlist = str.split("  ");
				//System.out.println(str);
				if(strlist.length == 2) data = new BasicData(strlist[0],strlist[1],"");
				else data = new BasicData(strlist[0],strlist[1],strlist[2]);
			    str = br.readLine();

			    strlist = str.split(" ");
			    for(int i=0;i<strlist.length-1;i++)data.addLink(Integer.valueOf(strlist[i]));
			    str = br.readLine();

			    hazardData.add(0,data);
			}

			str = br.readLine();

			while(str != null && !str.equals("CA")){
				String[] strlist = str.split("  ");
				if(strlist.length == 2) data = new BasicData(strlist[0],strlist[1],"");
				else data = new BasicData(strlist[0],strlist[1],strlist[2]);
			    str = br.readLine();

			    safetyData.add(0,data);
			}

			str = br.readLine();

			while(str != null && !str.equals("State")){
				String[] strlist = str.split("  ");
				if(strlist.length == 2) cdata = new CAData(strlist[0],strlist[1],"");
				else cdata = new CAData(strlist[0],strlist[1],strlist[2]);


			    str = br.readLine();

			    mCAData.add(0,cdata);
			}

			str = br.readLine();


			while(str != null && !str.equals("State-UCA")){
				String[] strlist = str.split("  ");
				if(strlist.length == 2) sdata = new StateData(strlist[0],strlist[1],"");
				else sdata = new StateData(strlist[0],strlist[1],strlist[2]);
			    str = br.readLine();
			    strlist = str.split(" ");

			    //Transition
			    for(int i=0;i<strlist.length-1;i+=2) {
			    	sdata.addTransition(Integer.valueOf(strlist[i]), Integer.valueOf(strlist[i+1]));
			    }

			    str = br.readLine();

			    stateData.add(0,sdata);
			}

			SynchronizeCAData();

			//全状態
			for(int i=stateData.getSize()-1;i>=0;i--) {
				str = br.readLine();

				//全CA
				for(int j=0;j<stateData.get(i).getmCAData().getSize();j++) {
					str = br.readLine();

					//UCA6種
					for(int k=1;k<=6;k++) {

						while(true) {
							str = br.readLine();
							String[] strlist = str.split(" ");

							if(strlist.length == 2) {
								ucadata = new UCAData("",strlist[0],"");
								ucadata.setHazard(Integer.valueOf(strlist[1]));

								stateData.get(i).getmCAData().get(j).addUCA(k, ucadata);
							}
							else break;
						}
					}
				}
			}

			str = br.readLine();
			str = br.readLine();

			while(str != null && !str.equals("arrow")){
				String[] strlist = str.split("  ");

				System.out.println(str);
				//System.out.println(strlist.length + " " + strlist[0] + " " + Integer.valueOf(strlist[5]));
				mcdata = new ModelComponent(Integer.valueOf(strlist[0]),strlist[1],Integer.valueOf(strlist[2]),Integer.valueOf(strlist[3]),Integer.valueOf(strlist[4]),Integer.valueOf(strlist[5]));
				//System.out.println("test");
				str = br.readLine();

			    modelData.add(modelData.size(),mcdata);
			}

			str = br.readLine();

			while(str != null&& !str.equals("component")){
				String[] strlist = str.split(" ");
				//System.out.println(str);
				acdata = new ArrowComponent(Integer.valueOf(strlist[0]),Integer.valueOf(strlist[1]),Integer.valueOf(strlist[2]),Integer.valueOf(strlist[3]));

				int counter = 4;

				while(!strlist[counter].equals("e")) {
					acdata.addCA(Integer.valueOf(strlist[counter]));
					counter++;
				}
				if(acdata.getCAs().size() > 0)acdata.sortCAs();

				str = br.readLine();

			    arrowData.add(arrowData.size(),acdata);
			}

			str = br.readLine();

			while(str != null) {
				System.out.println(str);
				String[] strlist = str.split(" ");
				cmdata = new ComponentData();
				cmdata.setTitle(strlist[0]);

				for(int i=1;i<strlist.length;i++) {
					cmdata.AddState(strlist[i]);
				}

				str = br.readLine();

				componentData.add(componentData.size(), cmdata);
			}

			br.close();


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*データの書き出し*/
	public void SaveData(File file) {
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

			pw.println("accident");

			for(int i=accidentData.getSize()-1;i>=0;i--) {
				pw.println(accidentData.getElementAt(i).getString());

				for(int j=0;j<accidentData.getElementAt(i).getLinks().size();j++) {
					pw.print(accidentData.getElementAt(i).getLinks().get(j) + " ");
				}
				pw.println("e");
			}

			pw.println("hazard");

			for(int i=hazardData.getSize()-1;i>=0;i--) {
				pw.println(hazardData.getElementAt(i).getString());

				for(int j=0;j<hazardData.getElementAt(i).getLinks().size();j++) {
					pw.print(hazardData.getElementAt(i).getLinks().get(j) + " ");
				}
				pw.println("e");
			}

			pw.println("safety");

			for(int i=safetyData.getSize()-1;i>=0;i--) {
				pw.println(safetyData.getElementAt(i).getString());
			}

			pw.println("CA");

			for(int i=mCAData.getSize()-1;i>=0;i--) {
				pw.println(mCAData.getElementAt(i).getString());
			}

			pw.println("State");

			for(int i=stateData.getSize()-1;i>=0;i--) {
				pw.println(stateData.getElementAt(i).getString());

				//全Transition
				for(int j=0;j<stateData.get(i).getTransitions().size();j++)pw.print(stateData.get(i).getTransition(j).getNextState() + " " + stateData.get(i).getTransition(j).getCA() + " ");
				pw.println("e");
			}

			pw.println("State-UCA");

			for(int i=stateData.getSize()-1;i>=0;i--) {
				pw.println(stateData.getElementAt(i).getString());

				//全CA
				for(int j=0;j<stateData.get(i).getmCAData().getSize();j++) {
					pw.println("CA-" + j);

					//UCA6種
					for(int k=1;k<=6;k++) {

						//全UCA
						for(int l=0;l<stateData.get(i).getmCAData().get(j).getUCAArray(k).size();l++) {
							pw.println(stateData.get(i).getmCAData().get(j).getUCA(k,l).getTitle() + " " + stateData.get(i).getmCAData().get(j).getUCA(k,l).getHazard() + " ");
						}
						pw.println("e");
					}
				}
			}

			pw.println("model");

			for(int i=0;i<modelData.size();i++) {
				pw.print(modelData.get(i).getId() + "  ");
				pw.print(modelData.get(i).getName() + "  ");
				pw.print(modelData.get(i).getX() + "  ");
				pw.print(modelData.get(i).getY() + "  ");
				pw.print(modelData.get(i).getWidth() + "  ");
				pw.println(modelData.get(i).getHeight() + "  ");
			}

			pw.println("arrow");

			for(int i=0;i<arrowData.size();i++) {
				pw.print(arrowData.get(i).getStart() + " ");
				pw.print(arrowData.get(i).getStartType() + " ");
				pw.print(arrowData.get(i).getEnd() + " ");
				pw.print(arrowData.get(i).getEndType() + " ");

				for(int j=0;j<arrowData.get(i).getCAs().size();j++) {
					pw.print(arrowData.get(i).getCA(j) + " ");
				}
				pw.println("e");
			}

			pw.println("component");

			for(int i=0;i<componentData.size();i++) {
				pw.print(componentData.get(i).getTitle() + " ");
				for(int j=0;j<componentData.get(i).getStates().size();j++) {
					pw.print(componentData.get(i).getState(j) + " ");
				}
				pw.println();
			}

			pw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
