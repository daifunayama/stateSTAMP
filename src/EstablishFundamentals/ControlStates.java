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
import DataType.CAData;
import DataType.StateData;
import Utility.TMGridBagConstraints;
import Utility.TMGridBagLayout;

public class ControlStates extends JPanel implements ActionListener {
	JTextField titleField, noteField;
	JLabel exp;
	JList list,list2,CAlist;


	AnalysisData analysisData = AnalysisData.getInstance();
	DefaultListModel<StateData> listModel,listModel2;
	DefaultListModel<CAData> CAlistModel;
	StateData myData;


	public ControlStates() {

		// リストの初期化
		listModel = AnalysisData.getInstance().getStateData();

		//コンポーネントの設置
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		list2 = new JList(listModel);
		list2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);


		JScrollPane sp = new JScrollPane();
		sp.getViewport().setView(list);
		JScrollPane sp2 = new JScrollPane();
		sp2.getViewport().setView(list2);

		JLabel Title = new JLabel("Title: ");
		titleField = new JTextField();
		JLabel Note = new JLabel("Note: ");
		noteField = new JTextField();
		JButton AddButton = new JButton("Add");
		exp = new JLabel("");

		JButton UpButton = new JButton("↑");
		JButton DownButton = new JButton("↓");
		JButton DeleteButton = new JButton("Delete");

		JButton LinkButton = new JButton("Link");

		AddButton.addActionListener(this);
		AddButton.setActionCommand("AddButton");
		UpButton.addActionListener(this);
		UpButton.setActionCommand("UpButton");
		DownButton.addActionListener(this);
		DownButton.setActionCommand("DownButton");
		DeleteButton.addActionListener(this);
		DeleteButton.setActionCommand("DeleteButton");
		LinkButton.addActionListener(this);
		LinkButton.setActionCommand("LinkButton");


		TMGridBagLayout layout = new TMGridBagLayout(5, 15, 1, -1);
		setLayout(layout);

		add(new JLabel("　"), new TMGridBagConstraints(0, 0, 2, 1));
		add(new JLabel("Please input Control State and link with next states."), new TMGridBagConstraints(0, 1, 2, 1));
		add(Title, new TMGridBagConstraints(0, 2, 1, 1));
		add(titleField, new TMGridBagConstraints(1, 2, 4, 1));
		//add(Note, new TMGridBagConstraints(0, 3, 1, 1));
		//add(noteField, new TMGridBagConstraints(1, 3, 4, 1));
		add(AddButton, new TMGridBagConstraints(4, 4, 1, 1));
		add(new JLabel("　"), new TMGridBagConstraints(0, 5, 2, 1));
		add(new JLabel("State list"), new TMGridBagConstraints(0, 6, 2, 1));
		add(sp, new TMGridBagConstraints(0, 7, 5, 1));
		add(UpButton, new TMGridBagConstraints(2, 8, 1, 1));
		add(DownButton, new TMGridBagConstraints(3, 8, 1, 1));
		add(DeleteButton, new TMGridBagConstraints(4, 8, 1, 1));
		add(new JLabel("　"), new TMGridBagConstraints(0, 9, 2, 1));
		//add(exp, new TMGridBagConstraints(0, 10, 2, 1));

		//add(new JPanel(), new TMGridBagConstraints(0, 11, 5, 1));
		//add(new JLabel("Next state"), new TMGridBagConstraints(0, 9, 2, 1));
		//add(sp2, new TMGridBagConstraints(0, 13, 5, 1));
		//add(LinkButton, new TMGridBagConstraints(4, 14, 1, 1));

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
				myData = new StateData();
				myData.setId("S-" + (listModel.size()));
				myData.setTitle(titleField.getText());
				myData.setNote(noteField.getText());

				listModel.addElement(myData);

				titleField.setText("");
				noteField.setText("");

				// データを同期させる
				analysisData.setStateData(listModel);
			}
		}

		//削除ボタンが押された時の処理
		if(actionCommand.equals("DeleteButton")) {
			if(list.getSelectedIndex() >= 0) {
				int index = list.getSelectedIndex();

				for(int i= list.getSelectedIndex(); i < listModel.size();i++) {
					listModel.getElementAt(i).setId("S-" + i);
				}

				listModel.remove(list.getSelectedIndex());

				exp.setText("");

				if(index >=listModel.size())index--;
				list.setSelectedIndex(index);

				analysisData.setStateData(listModel);
			}
		}

		//↑ボタンが押された時の処理
		if(actionCommand.equals("UpButton")) {
			if(list.getSelectedIndex() >= 1) {
				int index = list.getSelectedIndex();
				StateData tmpData = new StateData();
				String tmpId;

				tmpData.set(listModel.getElementAt(index));

				listModel.getElementAt(index).set(listModel.getElementAt(index-1));
				listModel.getElementAt(index-1).set(tmpData);

				tmpId = listModel.getElementAt(index).getId();
				listModel.getElementAt(index).setId(listModel.getElementAt(index-1).getId());
				listModel.getElementAt(index-1).setId(tmpId);

				list.setSelectedIndex(index-1);

				analysisData.setStateData(listModel);
			}
		}

		//↓ボタンが押された時の処理
		if(actionCommand.equals("DownButton")) {
			if(listModel.size() > 1 && list.getSelectedIndex() >= 0 && list.getSelectedIndex() < listModel.size()-1) {
				int index = list.getSelectedIndex();
				StateData tmpData = new StateData();
				String tmpId;

				tmpData.set(listModel.getElementAt(index));

				listModel.getElementAt(index).set(listModel.getElementAt(index+1));
				listModel.getElementAt(index+1).set(tmpData);

				tmpId = listModel.getElementAt(index).getId();
				listModel.getElementAt(index).setId(listModel.getElementAt(index+1).getId());
				listModel.getElementAt(index+1).setId(tmpId);

				list.setSelectedIndex(index+1);

				analysisData.setStateData(listModel);
			}
		}

		//リンクボタンが押された時の処理
		if(actionCommand.equals("LinkButton")) {
			int selected = list.getSelectedIndex();
			int[] selectedList = list2.getSelectedIndices();

			if(selected >= 0) {
				//listModel.getElementAt(selected).clearNext();

				System.out.println(selected);
				System.out.println(selectedList.length);

				for(int i=0;i<selectedList.length;i++) {
					//listModel.getElementAt(selected).addNext(selectedList[i]);
				}
				analysisData.setStateData(listModel);

				//for(int i=0;i<analysisData.getStateData().get(selected).getNexts().size();i++) {
				//	System.out.println(analysisData.getStateData().get(selected).getNext(i));
				//}

				revalidate();
			}
		}
	}

	/*
	public ControlStates() {
		// リストの初期化
		listModel = AnalysisData.getInstance().getStateData();
		CAlistModel = AnalysisData.getInstance().getCAData();

		//コンポーネントの設置
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		list2 = new JList(listModel);
		list2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		CAlist = new JList(CAlistModel);
		CAlist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);



		// コンポーネント設置

		JScrollPane sp = new JScrollPane();
		sp.getViewport().setView(list);
		JScrollPane sp2 = new JScrollPane();
		sp2.getViewport().setView(list2);

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


		TMGridBagLayout layout = new TMGridBagLayout(5, 15, 1, -1);
		setLayout(layout);

		add(new JLabel("　"), new TMGridBagConstraints(0, 0, 2, 1));
		add(new JLabel("Please input Control State and link with next states."), new TMGridBagConstraints(0, 1, 2, 1));
		add(Title, new TMGridBagConstraints(0, 2, 1, 1));
		add(titleField, new TMGridBagConstraints(1, 2, 4, 1));
		//add(Note, new TMGridBagConstraints(0, 3, 1, 1));
		//add(noteField, new TMGridBagConstraints(1, 3, 4, 1));
		add(AddButton, new TMGridBagConstraints(4, 4, 1, 1));
		add(new JLabel("　"), new TMGridBagConstraints(0, 5, 2, 1));
		add(new JLabel("State list"), new TMGridBagConstraints(0, 6, 2, 1));
		add(sp, new TMGridBagConstraints(0, 7, 5, 1));
		add(UpButton, new TMGridBagConstraints(2, 8, 1, 1));
		add(DownButton, new TMGridBagConstraints(3, 8, 1, 1));
		add(DeleteButton, new TMGridBagConstraints(4, 8, 1, 1));
		add(new JLabel("　"), new TMGridBagConstraints(0, 9, 2, 1));
		//add(exp, new TMGridBagConstraints(0, 10, 2, 1));

		add(new JPanel(), new TMGridBagConstraints(0, 11, 5, 1));
		add(new JLabel("Next state"), new TMGridBagConstraints(0, 9, 2, 1));
		add(sp2, new TMGridBagConstraints(0, 13, 5, 1));


		setLayout(null);

		JLabel Alabel = new JLabel("State list");
		Alabel.setBounds(20, 300, 300, 10);

		JScrollPane Asp = new JScrollPane();
		Asp.getViewport().setView(list);
		Asp.setBounds(10, 320, 300, 250);

		JLabel Hlabel = new JLabel("CA list");
		Hlabel.setBounds(330, 300, 300, 10);

		JScrollPane Hsp = new JScrollPane();
		Hsp.getViewport().setView(CAlist);
		Hsp.setBounds(320, 320, 300, 250);

		JLabel Slabel = new JLabel("Next State list");
		Slabel.setBounds(640, 300, 300, 10);

		JScrollPane Ssp = new JScrollPane();
		Ssp.getViewport().setView(list2);
		Ssp.setBounds(630, 320, 300, 250);

		JButton LinkButton = new JButton("Linking");
		LinkButton.setBounds(345, 600, 250, 30);
		LinkButton.addActionListener(this);
		LinkButton.setActionCommand("LinkButton");

		add(Alabel);
		add(Asp);
		add(Hlabel);
		add(Hsp);
		add(Slabel);
		add(Ssp);

		add(LinkButton);


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

				}
			}
		});

		// リストがフォーカスされたときの処理
		list2.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (e.getValueIsAdjusting()) {
					// The user is still manipulating the selection.
					return;
				}
				Object obj = list.getSelectedValue();
				if (obj != null) {

				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();


		if (actionCommand == "LinkButton") {


		}
	}
	*/
}

