package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.CoreInteractionService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;

public class SongTableRenderer extends DefaultTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * public void setValue(Object value) {
	 * 
	 * //Color c = new Color(220, 220, 250); //setBackground(c);
	 * 
	 * // setForeground(Color.BLUE); super.setValue(value); }
	 */

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

		// table.set
		// table.setSelectionBackground(Color.red);
	/*	
		
		
		
			StarRating rating = new StarRating(5);
			rating.addPropertyChangeListener(new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent arg0) {
					System.out.println("Change");

				}

			});
			
			System.out.println(table.getColumnName(3));
			
			JComboBox combo = new JComboBox();
			combo.addItem("Rating");
			JButton button = new JButton("Button");
			JPanel panel = new JPanel();
			panel.add(new JLabel("test"));
			panel.add(button);
			panel.add(rating);
			//table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(combo));
		//	table.setEditingColumn(3);
		//	table.setEditingRow(2);
			table.getColumnModel().getColumn(3).setCellEditor(new SongCellEditor());
			DefaultTableCellRenderer render = new DefaultTableCellRenderer();
			render.setToolTipText("Click for Ratin");
			table.getColumnModel().getColumn(3).setCellRenderer(render);
		//	return component;
			//table.setValueAt(rating, row, 7);
			//table.add(rating);
		
*/
		if (row % 2 == 0) {

			setBackground(Color.white);
			// table.setSelectionBackground(Color.red);
		} else {
			setBackground(new Color(220, 220, 250));

		}
		table.setSelectionForeground(Color.red);
		//table.setSelectionBackground(Color.red);
		// if(cis.isPlaying())
		// {

		if (cis.getCurrentSongIndex() > -1) {
			// if (table.getRowSorter().convertRowIndexToView(cis.getCurrentSongIndex()) == row
			// )
			if (cis.getCurrentSongIndex() == row) 
			{
				// System.out.println(table.getRowSorter().convertRowIndexToView(cis.getCurrentSongIndex()));
				component.setFont(component.getFont().deriveFont(Font.BOLD));
			}
		}
		
		return component;
	}
}