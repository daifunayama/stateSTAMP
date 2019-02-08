package Utility;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import Utility.TMGridBagConstraints;

public class ButtonsPanel extends JPanel {

	public final List<JButton> buttons = Arrays.asList(new JButton("+"), new JButton("link"));
	public final JLabel label = new JLabel() {
		@Override
		public Dimension getPreferredSize() {
			Dimension d = super.getPreferredSize();
			d.width = 50;
			return d;
		}
	};
	public int i = -1;

	public ButtonsPanel() {
		super();
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		setOpaque(true);
		add(label);
		for (JButton b : buttons) {
			b.setFocusable(false);
			b.setRolloverEnabled(false);
			add(b);
		}
	}
}