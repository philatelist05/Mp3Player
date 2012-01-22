package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;


import java.awt.Color;
import java.util.Vector;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class SongTableRenderer extends DefaultTableCellRenderer {
	public void setValue(Object value) {
		
		Color c = new Color(220,220,250);
		setBackground(c);
		//setForeground(Color.BLUE);
		super.setValue(value);
	}
}