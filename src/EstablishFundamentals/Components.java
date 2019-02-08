package EstablishFundamentals;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import DataType.AnalysisData;
import DataType.BasicData;
import DataType.ComponentData;
import Utility.TMGridBagConstraints;
import Utility.TMGridBagLayout;

public class Components extends JPanel implements ActionListener {
	JTextField titleField, noteField,stateTitleField;
	JLabel exp;
	JList list,slist;
	

	AnalysisData analysisData = AnalysisData.getInstance();
	DefaultListModel<ComponentData> listModel;
	ComponentData myData;

	public Components() {

		// リストの初期化
		listModel = AnalysisData.getInstance().getComponentData();
		//listModel = new DefaultListModel<ComponentData>();

		//コンポーネントの設置
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane sp = new JScrollPane();
		sp.getViewport().setView(list);

		titleField = new JTextField();
		noteField = new JTextField();
		JButton AddButton = new JButton("Add");
		exp = new JLabel("");
		
		JButton UpButton = new JButton("↑");
		JButton DownButton = new JButton("↓");
		JButton DeleteButton = new JButton("Delete");
		
		
		slist = new JList();
		JScrollPane sp2 = new JScrollPane();
		sp2.getViewport().setView(slist);
		
		stateTitleField = new JTextField();
		JButton AddButton2 = new JButton("Add");
		JButton DeleteButton2 = new JButton("Delete");

		AddButton.addActionListener(this);
		AddButton.setActionCommand("AddButton");
		UpButton.addActionListener(this);
		UpButton.setActionCommand("UpButton");
		DownButton.addActionListener(this);
		DownButton.setActionCommand("DownButton");
		DeleteButton.addActionListener(this);
		DeleteButton.setActionCommand("DeleteButton");
		
		AddButton2.addActionListener(this);
		AddButton2.setActionCommand("AddButton2");
		DeleteButton2.addActionListener(this);
		DeleteButton2.setActionCommand("DeleteButton2");
		

		TMGridBagLayout layout = new TMGridBagLayout(5, 20, 1, -1);
		setLayout(layout);

		add(new JLabel("　"), new TMGridBagConstraints(0, 0, 2, 1));
		add(new JLabel("Please input System Accident title and note."), new TMGridBagConstraints(0, 1, 2, 1));
		add(new JLabel("Title: "), new TMGridBagConstraints(0, 2, 1, 1));
		add(titleField, new TMGridBagConstraints(1, 2, 4, 1));
		add(new JLabel("Note: "), new TMGridBagConstraints(0, 3, 1, 1));
		add(noteField, new TMGridBagConstraints(1, 3, 4, 1));
		add(AddButton, new TMGridBagConstraints(4, 4, 1, 1));
		add(new JLabel("　"), new TMGridBagConstraints(0, 5, 2, 1));
		add(new JLabel("Component list"), new TMGridBagConstraints(0, 6, 2, 1));
		add(sp, new TMGridBagConstraints(0, 7, 5, 1));
		add(UpButton, new TMGridBagConstraints(2, 8, 1, 1));
		add(DownButton, new TMGridBagConstraints(3, 8, 1, 1));
		add(DeleteButton, new TMGridBagConstraints(4, 8, 1, 1));
		add(new JLabel("　"), new TMGridBagConstraints(0, 9, 2, 1));
		add(exp, new TMGridBagConstraints(0, 10, 2, 1));

		add(new JLabel("Please input states of selected components."), new TMGridBagConstraints(0, 11, 2, 1));
		add(new JLabel("Title: "), new TMGridBagConstraints(0, 12, 1, 1));
		add(stateTitleField, new TMGridBagConstraints(1, 12, 4, 1));
		add(AddButton2, new TMGridBagConstraints(4, 13, 1, 1));
		add(new JLabel("　"), new TMGridBagConstraints(0, 14, 2, 1));
		add(new JLabel("State list"), new TMGridBagConstraints(0, 15, 2, 1));
		add(sp2, new TMGridBagConstraints(0, 16, 5, 1));
		add(DeleteButton2, new TMGridBagConstraints(4, 17, 1, 1));
		
		// リストがフォーカスされたときの処理
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					// The user is still manipulating the selection.
					return;
				}
				Object obj = list.getSelectedValue();
				if (obj != null) {
					exp.setText(listModel.getElementAt(list.getSelectedIndex()).getString());
					
					slist = new JList(listModel.getElementAt(list.getSelectedIndex()).getStates());
					sp2.getViewport().setView(slist);
				}
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();

		// 追加ボタンが押された時の処理
		if (actionCommand.equals("AddButton")) {

			if (titleField.getText().length() > 0) {
				myData = new ComponentData();
				myData.setId("C-" + (listModel.size() + 1));
				myData.setTitle(titleField.getText());
				myData.setNote(noteField.getText());

				listModel.addElement(myData);

				titleField.setText("");
				noteField.setText("");

				// データを同期させる
				analysisData.setComponentData(listModel);
			}
		}
		
		//削除ボタンが押された時の処理
		if(actionCommand.equals("DeleteButton")) {
			if(list.getSelectedIndex() >= 0) {
				int index = list.getSelectedIndex();
				
				for(int i= list.getSelectedIndex(); i < listModel.size();i++) {
					listModel.getElementAt(i).setId("A-" + i);
				}
				
				listModel.remove(list.getSelectedIndex());

				exp.setText("");
				
				if(index >=listModel.size())index--;
				list.setSelectedIndex(index);
				
				analysisData.setComponentData(listModel);
			}
		}
		
		//↑ボタンが押された時の処理
		if(actionCommand.equals("UpButton")) {
			if(list.getSelectedIndex() >= 1) {
				int index = list.getSelectedIndex();
				BasicData tmpData = new BasicData();
				String tmpId;
				
				tmpData.set(listModel.getElementAt(index));
				
				listModel.getElementAt(index).set(listModel.getElementAt(index-1));
				listModel.getElementAt(index-1).set(tmpData);
				
				tmpId = listModel.getElementAt(index).getId();
				listModel.getElementAt(index).setId(listModel.getElementAt(index-1).getId());
				listModel.getElementAt(index-1).setId(tmpId);
				
				list.setSelectedIndex(index-1);
				
				//analysisData.setAccidentData(listModel);
			}
		}
		
		//↓ボタンが押された時の処理
		if(actionCommand.equals("DownButton")) {
			if(listModel.size() > 1 && list.getSelectedIndex() >= 0 && list.getSelectedIndex() < listModel.size()-1) {
				int index = list.getSelectedIndex();
				BasicData tmpData = new BasicData();
				String tmpId;
				
				tmpData.set(listModel.getElementAt(index));
				
				listModel.getElementAt(index).set(listModel.getElementAt(index+1));
				listModel.getElementAt(index+1).set(tmpData);
				
				tmpId = listModel.getElementAt(index).getId();
				listModel.getElementAt(index).setId(listModel.getElementAt(index+1).getId());
				listModel.getElementAt(index+1).setId(tmpId);
				
				list.setSelectedIndex(index+1);
				
				analysisData.setComponentData(listModel);
			}
		}
		
		// 状態追加ボタンが押された時の処理
		if (actionCommand.equals("AddButton2")) {

			if (stateTitleField.getText().length() > 0 && list.getSelectedIndex() >= 0) {
				int index = list.getSelectedIndex();
				
				listModel.getElementAt(index).AddState(stateTitleField.getText());

				stateTitleField.setText("");
			
				// データを同期させる
				analysisData.setComponentData(listModel);
			}
		}
		
		//状態削除ボタンが押された時の処理
		if(actionCommand.equals("DeleteButton2")) {
			if(slist.getSelectedIndex() >= 0) {
				int index = slist.getSelectedIndex();
				
				
				listModel.get(list.getSelectedIndex()).getStates().remove(slist.getSelectedIndex());
				
				if(index >=listModel.get(list.getSelectedIndex()).getStates().size())index--;
				slist.setSelectedIndex(index);
				
				analysisData.setComponentData(listModel);
			}
		}
	}
}

