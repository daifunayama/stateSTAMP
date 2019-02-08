package EstablishFundamentals;

import Utility.TMGridBagConstraints;
import Utility.TMGridBagLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
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

public class SafetyConstraints extends JPanel implements ActionListener {
	JTextField titleField, noteField;
	JLabel exp;
	JList list;
	

	AnalysisData analysisData = AnalysisData.getInstance();
	DefaultListModel<BasicData> listModel;
	BasicData myData;

	public SafetyConstraints() {

		// リストの初期化
		listModel = AnalysisData.getInstance().getSafetyData();

		//コンポーネントの設置
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane sp = new JScrollPane();
		sp.getViewport().setView(list);

		JLabel Title = new JLabel("Title: ");
		titleField = new JTextField();
		JLabel Note = new JLabel("Note: ");
		noteField = new JTextField();
		JButton AddButton = new JButton("Add");
		exp = new JLabel("");
		
		JButton UpButton = new JButton("↑");
		JButton DownButton = new JButton("↓");
		JButton DeleteButton = new JButton("Delete");

		AddButton.addActionListener(this);
		AddButton.setActionCommand("AddButton");
		UpButton.addActionListener(this);
		UpButton.setActionCommand("UpButton");
		DownButton.addActionListener(this);
		DownButton.setActionCommand("DownButton");
		DeleteButton.addActionListener(this);
		DeleteButton.setActionCommand("DeleteButton");
		

		TMGridBagLayout layout = new TMGridBagLayout(5, 11, 1, -1);
		setLayout(layout);

		add(new JLabel("　"), new TMGridBagConstraints(0, 0, 2, 1));
		add(new JLabel("Please input Safety Constraints title and note."), new TMGridBagConstraints(0, 1, 2, 1));
		add(Title, new TMGridBagConstraints(0, 2, 1, 1));
		add(titleField, new TMGridBagConstraints(1, 2, 4, 1));
		add(Note, new TMGridBagConstraints(0, 3, 1, 1));
		add(noteField, new TMGridBagConstraints(1, 3, 4, 1));
		add(AddButton, new TMGridBagConstraints(4, 4, 1, 1));
		add(new JLabel("　"), new TMGridBagConstraints(0, 5, 2, 1));
		add(new JLabel("Safety Constraints list"), new TMGridBagConstraints(0, 6, 2, 1));
		add(sp, new TMGridBagConstraints(0, 7, 5, 1));
		add(UpButton, new TMGridBagConstraints(2, 8, 1, 1));
		add(DownButton, new TMGridBagConstraints(3, 8, 1, 1));
		add(DeleteButton, new TMGridBagConstraints(4, 8, 1, 1));
		add(new JLabel("　"), new TMGridBagConstraints(0, 9, 2, 1));
		add(exp, new TMGridBagConstraints(0, 10, 2, 1));

		
		
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
				}
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();

		// 追加ボタンが押された時の処理
		if (actionCommand.equals("AddButton")) {

			if (titleField.getText().length() > 0) {
				myData = new BasicData();
				myData.setId("SC-" + (listModel.size() + 1));
				myData.setTitle(titleField.getText());
				myData.setNote(noteField.getText());

				listModel.addElement(myData);

				titleField.setText("");
				noteField.setText("");

				// データを同期させる
				analysisData.setSafetyData(listModel);
			}
		}
		
		//削除ボタンが押された時の処理
		if(actionCommand.equals("DeleteButton")) {
			if(list.getSelectedIndex() >= 0) {
				int index = list.getSelectedIndex();
				
				for(int i= list.getSelectedIndex(); i < listModel.size();i++) {
					listModel.getElementAt(i).setId("SC-" + i);
				}
				
				listModel.remove(list.getSelectedIndex());

				exp.setText("");
				
				if(index >=listModel.size())index--;
				list.setSelectedIndex(index);
				
				analysisData.setSafetyData(listModel);
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
				
				analysisData.setSafetyData(listModel);
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
				
				analysisData.setSafetyData(listModel);
			}
		}
	}
}
