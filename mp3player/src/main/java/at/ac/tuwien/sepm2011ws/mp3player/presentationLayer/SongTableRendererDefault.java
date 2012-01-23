package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;


import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.CoreInteractionService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;

public class SongTableRendererDefault extends DefaultTableCellRenderer {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public void setValue(Object value) {
		
		//Color c = new Color(220,220,250);
		//setBackground(c);
		//setForeground(Color.BLUE);
		super.setValue(value);
	}
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

		ServiceFactory sf = ServiceFactory.getInstance();
		CoreInteractionService cis = sf.getCoreInteractionService();
    	  Component component = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

  		if (cis.getCurrentSongIndex() > -1) {
			if (table.getRowSorter().convertRowIndexToView(
					cis.getCurrentSongIndex()) == row) {
				component.setFont(component.getFont().deriveFont(Font.BOLD));
			}
		}

        return component;
    }
}