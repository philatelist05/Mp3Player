package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.CoreInteractionService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;

import com.googlecode.starrating.*;


public class SongCellEditor extends AbstractCellEditor implements
		TableCellEditor, ActionListener {

	StarRating rating;

	ServiceFactory sf;
	CoreInteractionService cis;
	
	public SongCellEditor()
	{
		sf = ServiceFactory.getInstance();
		cis = sf.getCoreInteractionService();
		
		rating = new StarRating();
		
	}
	
	
	@Override
	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		return rating.getRate();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		
		rating.setRate(cis.getCurrentPlaylist().get(table.getRowSorter().convertRowIndexToView(row)).getRating());
		/*rating.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				System.out.println("changed");
				fireTableChanged();
			}
			
		});*/
		return rating;
	}

}