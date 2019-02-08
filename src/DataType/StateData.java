package DataType;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.DefaultListModel;

public class StateData extends BasicData{
	int type;
	ArrayList<Transition> transitions;
	int[] components;

	private DefaultListModel<CAData> mCAData;

	public class Transition{
		int CA;
		int nextState;

		public Transition() {
			CA = -1;
			nextState = -1;
		}

		public Transition(int next) {
			CA = -1;
			nextState = next;
		}

		public Transition(int next, int ca) {
			CA = ca;
			nextState = next;
		}

		public int getCA() {return CA;}
		public int getNextState() {return nextState;}
		public void setCA(int c) {CA = c;}
		public void setNextState(int s) {nextState = s;}
	}

	public StateData() {
		super();
		transitions = new ArrayList<Transition>();
		mCAData = new DefaultListModel<CAData>();
		components = new int[AnalysisData.getInstance().getComponentData().getSize()];
		for(int c:components)components[c] = 0;

		type = 0;
	}

	public StateData(String id, String title, String note) {
		super(id,title,note);
		transitions = new ArrayList<Transition>();
		mCAData = new DefaultListModel<CAData>();
		components = new int[AnalysisData.getInstance().getComponentData().getSize()];
		for(int c:components)components[c] = 0;
		type = 0;
	}

	public DefaultListModel<CAData> getmCAData() {return mCAData;}
	public void setCAData(DefaultListModel<CAData> data) {mCAData = data;}
	public void setCAData(int i, CAData data) {mCAData.set(i, data);}
	public void AddCAData(CAData data) {mCAData.add(mCAData.size(),data);}
	public void ClearCAData() {
		mCAData.clear();
		mCAData = new DefaultListModel<CAData>();
	}

	public void resizeComponent(int size) {
		int[] temp = new int [size];
        System.arraycopy(components, 0, temp, 0, (components.length < size) ? components.length : size);
        components = temp;
	}
	public int getComponent(int n) {return components[n];}
	public void setComponent(int n, int x) {components[n] = x;}

	public ArrayList<Transition> getTransitions(){return transitions;}
	public Transition getTransition(int n){return transitions.get(n);}
	public void setTransitions(ArrayList<Transition> t) {transitions = t;}
	public void addTransition(Transition t) {transitions.add(t);}
	public void addTransition(int n) {
		Transition t = new Transition(n);
		transitions.add(t);
	}
	public void addTransition(int n, int ca) {
		Transition t = new Transition(n,ca);
		transitions.add(t);
	}

	public void eraceTransition(int n) {transitions.remove(n);}
	public void clearTransitions() {transitions.clear();}
	public int getType() {return type;}
	public void setType(int t) {type = t;}

	public ArrayList<Integer> getPreviousStates(){
		ArrayList<Integer> array = new ArrayList<Integer>();

		DefaultListModel<StateData> stateData = AnalysisData.getInstance().getStateData();

		for(int i=0;i<stateData.getSize();i++) {
			for(Transition t : stateData.get(i).getTransitions()) {
				if(t.nextState == getIntId()) {
					array.add(i);
					break;
				}
			}
		}

		Collections.sort(array);

		return array;
	}

	public int getTransitionFromCA(int CA) {

		DefaultListModel<StateData> stateData = AnalysisData.getInstance().getStateData();

		for(int i=0;i<stateData.getSize();i++) {
			for(Transition t : stateData.get(i).getTransitions()) {
				if(t.CA == CA) {
					return t.nextState;
				}
			}
		}
		return 0;
	}

	public int getUCAnum() {
		int num = 0;

		for(int i=0;i<mCAData.size();i++) {
			num += mCAData.get(i).getMaxUCASize();
		}

		return num;
	}

	public int getAllUCAnum() {
		int num = 0;

		for(int i=0;i<mCAData.size();i++) {
			num += mCAData.get(i).getAllUCAnum();
		}

		return num;
	}

	public UCAData getUCAfromIndex(int index) {
		int focus = 0;

		//System.out.println(mCAData.get(0).getUCAArray(1).size());
		//System.out.println(mCAData.get(0).getUCAArray(2).size());
		//System.out.println(mCAData.get(0).getUCAArray(3).size());

		if(index >= getAllUCAnum())return null;

		//全CA
		for(int i=0;i<mCAData.size();i++) {
			//全UCA
			for(int j=1;j<=6;j++) {


				focus += mCAData.get(i).getUCAArray(j).size();

				//System.out.println("f " +focus);

				if(focus > index) {
					focus -= mCAData.get(i).getUCAArray(j).size();

					//System.out.println("return");
					return mCAData.get(i).getUCA(j,index-focus);
				}
			}
		}

		return null;
	}

	public int getAllHCFnum() {
		int num = 0;

		for(int i=0;i<getAllUCAnum();i++)num +=getUCAfromIndex(i).getHCFArray().size();

		return num;
	}

	//前の状態にUCAがある
	public int hasUCAinPreviousState() {
		DefaultListModel<StateData> sdata = AnalysisData.getInstance().getStateData();

		int point = 0;

		//前の全状態
		for(int i=0;i<getPreviousStates().size();i++) {

			//全CA
			for(int j=0;j<AnalysisData.getInstance().getCAData().size();j++) {

				for(int k=1;k<=6;k++) {
					if(sdata.get(getPreviousStates().get(i)).mCAData.get(j).getUCAArray(k).size() > 0)point++;
				}
			}
		}

		return point;
	}

	//次の状態にUCAがあるCAを返す
	public ArrayList<Integer> getCAhasUCAinNextState() {
		ArrayList<Integer> cas = new ArrayList<Integer>();

		DefaultListModel<StateData> sdata = AnalysisData.getInstance().getStateData();

		//次の全状態
		for(int i=0;i<transitions.size();i++) {

			//全CA
			for(int j=0;j<AnalysisData.getInstance().getCAData().size();j++) {

				Boolean found = false;

				for(int k=1;k<=6;k++) {
					if(sdata.get(transitions.get(i).nextState).mCAData.get(j).getUCAArray(k).size() > 0) {
						found = true;
					}
				}

				if(found)cas.add(transitions.get(i).CA);
			}
		}

		return cas;
	}

	//CAを渡すと、それが上の関数を満たすかを返す
	public Boolean hasUCAinNextState(int CA) {

		ArrayList<Integer> list = getCAhasUCAinNextState();

		for(int i=0;i<list.size();i++) {
			if(CA == list.get(i))return true;
		}

		return false;
	}

	@Override
	public String toString() {
		String str =  id + " " + title + " ";

		return str;
	}


}
