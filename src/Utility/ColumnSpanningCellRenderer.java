package Utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

public class ColumnSpanningCellRenderer extends JPanel implements TableCellRenderer {
	private static final int TARGET_COLIDX = 0;
	private final JTextArea textArea = new JTextArea(2, 999999);
	private final JLabel label = new JLabel();
	private final JLabel iconLabel = new JLabel();
	private final JScrollPane scroll = new JScrollPane(textArea);

	public ColumnSpanningCellRenderer() {
		super(new BorderLayout());

		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.setViewportBorder(BorderFactory.createEmptyBorder());
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);

		textArea.setBorder(BorderFactory.createEmptyBorder());
		textArea.setMargin(new Insets(0, 0, 0, 0));
		textArea.setForeground(Color.RED);
		textArea.setEditable(false);
		textArea.setFocusable(false);
		textArea.setOpaque(false);

		iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
		iconLabel.setOpaque(false);

		Border b1 = BorderFactory.createEmptyBorder(2, 2, 2, 2);
		Border b2 = BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY);
		label.setBorder(BorderFactory.createCompoundBorder(b2, b1));

		setBackground(textArea.getBackground());
		setOpaque(true);
		add(label, BorderLayout.NORTH);
		add(scroll);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		OptionPaneDescription d;
		if (value instanceof OptionPaneDescription) {
			d = (OptionPaneDescription) value;
			add(iconLabel, BorderLayout.WEST);
		} else {
			String title = Objects.toString(value, "");
			int mrow = table.convertRowIndexToModel(row);
			Object o = table.getModel().getValueAt(mrow, 0);
			if (o instanceof OptionPaneDescription) {
				OptionPaneDescription t = (OptionPaneDescription) o;
				d = new OptionPaneDescription(title, t.icon, t.text);
			} else {
				d = new OptionPaneDescription(title, null, "");
			}
			remove(iconLabel);
		}
		label.setText(d.title);
		textArea.setText(d.text);
		iconLabel.setIcon(d.icon);

		Rectangle cr = table.getCellRect(row, column, false);
		/*
		 * / // Flickering on first visible row ? if (column == TARGET_COLIDX) { cr.x =
		 * 0; cr.width -= iconLabel.getPreferredSize().width; } else { cr.x -=
		 * iconLabel.getPreferredSize().width; } textArea.scrollRectToVisible(cr); /
		 */
		if (column != TARGET_COLIDX) {
			cr.x -= iconLabel.getPreferredSize().width;
		}
		scroll.getViewport().setViewPosition(cr.getLocation());
		// */
		if (isSelected) {
			setBackground(Color.ORANGE);
		} else {
			setBackground(Color.WHITE);
		}
		return this;
	}

	class OptionPaneDescription {
		public final String title;
		public final Icon icon;
		public final String text;

		protected OptionPaneDescription(String title, Icon icon, String text) {
			this.title = title;
			this.icon = icon;
			this.text = text;
		}
	}
}
