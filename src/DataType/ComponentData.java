package DataType;

import java.util.ArrayList;

import javax.swing.DefaultListModel;

public class ComponentData extends BasicData{
	private DefaultListModel<String> states;
	
	public ComponentData() {
		super();
		states = new DefaultListModel<String>();
	}
	
	public DefaultListModel<String> getStates(){return states;}
	public String getState(int n) {return states.get(n);}
	public void AddState(String s){states.add(states.size(),s);}
	
}
