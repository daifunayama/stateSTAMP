package Utility;

import java.awt.Component;
import java.util.Objects;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ButtonsRenderer extends ButtonsPanel implements TableCellRenderer {
	public ButtonsRenderer() {
		super();
		setName("Table.cellRenderer");
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		this.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
		label.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
		label.setText(Objects.toString(value, ""));
		return this;
	}
}