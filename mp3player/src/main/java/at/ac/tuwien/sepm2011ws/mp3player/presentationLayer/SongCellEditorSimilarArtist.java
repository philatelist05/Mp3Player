package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.CoreInteractionService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;
import com.googlecode.starrating.*;

public class SongCellEditorSimilarArtist extends AbstractCellEditor implements
		TableCellEditor, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8738179072889259618L;

	StarRating rating;

	ServiceFactory sf;
	CoreInteractionService cis;

	public SongCellEditorSimilarArtist() {
		sf = ServiceFactory.getInstance();
		cis = sf.getCoreInteractionService();

		rating = new StarRating();

	}

	@Override
	public Object getCellEditorValue() {
		return rating.getRate();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {

		rating.setRate(cis.getCurrentPlaylist()
				.get(row)
				.getRating());
		/*
		 * rating.addPropertyChangeListener(new PropertyChangeListener() {
		 * 
		 * @Override public void propertyChange(PropertyChangeEvent arg0) {
		 * System.out.println("changed"); fireTableChanged(); }
		 * 
		 * });
		 */
		return rating;
	}

}