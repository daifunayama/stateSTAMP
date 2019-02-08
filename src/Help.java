import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Help extends JPanel{
	
	public Help() {
		JTextArea text = new JTextArea();
		
		setLayout(null);
		
		text.setBounds(10, 10, 600, 500);
		text.setEditable(false);
		
		text.setText("Establish Fundamentals\n"
				+ "システムの基本情報を入力します。\n"
				+ "これらの情報は");
		
		add(text);
	}
}
