package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class SongTableRenderer extends DefaultTableCellRenderer {
	public void setValue(Object value) {
		
		Color c = new Color(220,220,250);
		setBackground(c);
		//setForeground(Color.BLUE);
		super.setValue(value);
	}
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

    	  Component component = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

          if (row == 2)  
          {
            component.setFont(component.getFont().deriveFont(Font.BOLD));
       }

        return component;
    }
}