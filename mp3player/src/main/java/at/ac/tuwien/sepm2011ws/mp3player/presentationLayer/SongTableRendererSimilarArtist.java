package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.springframework.core.io.ClassPathResource;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;

import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.CoreInteractionService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;

public class SongTableRendererSimilarArtist extends DefaultTableCellRenderer {

	/**
	 * 
	 */

	private Icon nothing;

	private static final long serialVersionUID = 1L;

	private static Playlist curPlaylist;

	public SongTableRendererSimilarArtist() {

	}

	public void setPlaylist(Playlist list) {
		curPlaylist = list;
	}

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

		if (row % 2 == 0) {

			setBackground(Color.white);
			// table.setSelectionBackground(Color.red);
		} else {
			setBackground(new Color(220, 220, 250));

		}
		table.setSelectionForeground(Color.red);
		// table.setSelectionBackground(Color.red);
		System.out.println("cis:" + cis.getCurrentPlaylist().getTitle());
		System.out.println("cur:" + curPlaylist.getTitle());
		if (cis.getCurrentPlaylist().equals(curPlaylist)) {
			if (cis.getCurrentSongIndex() > -1) {
				/*
				 * if (cis.getCurrentSongIndex() > -1) if
				 * (table.getRowSorter().convertRowIndexToView
				 * (cis.getCurrentSongIndex()) == row )
				 */
				if (cis.getCurrentSongIndex() == row) {
					// System.out.println(table.getRowSorter().convertRowIndexToView(cis.getCurrentSongIndex()));
					component
							.setFont(component.getFont().deriveFont(Font.BOLD));
				}
			}
		}

		return component;
	}
}