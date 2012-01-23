package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import uk.co.caprica.vlcj.log.Logger;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.CoreInteractionService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;

public class SongTableRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setValue(Object value) {

		Color c = new Color(220, 220, 250);
		setBackground(c);
		// setForeground(Color.BLUE);
		super.setValue(value);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		ServiceFactory sf = ServiceFactory.getInstance();
		CoreInteractionService cis = sf.getCoreInteractionService();


		Component component = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);
		// System.out.println(row);
		// System.out.println(cis.getCurrentSongIndex());
		// if ( table.getRowSorter().convertRowIndexToView(row) == row)
		// if( table.getModel().getValueAt(row, 0).equals(cis.getCurrentSong()))
		Color cb = new Color(255, 255, 230);
		if(row % 2 == 0 )
			{setBackground(cb);}
	//	if(cis.isPlaying())
	//	{
			if (cis.getCurrentSongIndex() > -1) {
				//if (table.getRowSorter().convertRowIndexToView(
					if(	cis.getCurrentSongIndex() == row) {
					// System.out.println(table.getRowSorter().convertRowIndexToView(cis.getCurrentSongIndex()));
					component.setFont(component.getFont().deriveFont(Font.BOLD));
				}
			}
	//	}
		return component;
	}
}