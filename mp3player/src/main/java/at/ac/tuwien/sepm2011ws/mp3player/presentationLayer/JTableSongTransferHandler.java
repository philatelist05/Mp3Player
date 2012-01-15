package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import javax.swing.JComponent;
import javax.swing.JTable;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;

public class JTableSongTransferHandler extends SongTransferHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int[] rows = null;
	private int addIndex = -1; // Location where items were added
	private int addCount = 0; // Number of items added.

	protected Song[] exportSong(JComponent c) {
		JTable table = (JTable) c;
		Song[] songs;
		rows = table.getSelectedRows();
		songs=new Song[rows.length];
		for (int i = 0; i < rows.length; i++) {
			Song currentSong = (Song) table.getValueAt(rows[i], 0);
			songs[i] = currentSong;
		}

		return songs;
	}

	protected void importSong(JComponent c, Song[] songs) {
		JTable target = (JTable) c;
		SongTableModel model = (SongTableModel) target.getModel();
		int index = target.getSelectedRow();

		// Prevent the user from dropping data back on itself.
		// For example, if the user is moving rows #4,#5,#6 and #7 and
		// attempts to insert the rows after row #5, this would
		// be problematic when removing the original rows.
		// So this is not allowed.
		if (rows != null && index >= rows[0] - 1
				&& index <= rows[rows.length - 1]) {
			rows = null;
			return;
		}

		int max = model.getRowCount();
		if (index < 0) {
			index = max;
		} else {
			index++;
			if (index > max) {
				index = max;
			}
		}
		addIndex = index;
		addCount = songs.length;
		for (int i = 0; i < songs.length; i++) {
			model.insertRow(
					index++,
					new Object[] { songs[i], songs[i].getTitle(),
							songs[i].getArtist(), songs[i].getAlbum(),
							songs[i].getYear(), songs[i].getGenre(),
							songs[i].getDuration(), songs[i].getRating(),
							songs[i].getPlaycount() });
		}
	}

	protected void cleanup(JComponent c, boolean remove) {
		JTable source = (JTable) c;
		if (remove) {
			SongTableModel model = (SongTableModel) source.getModel();

			// If we are moving items around in the same table, we
			// need to adjust the rows accordingly, since those
			// after the insertion point have moved.
			if (addCount > 0) {
				for (int i = 0; i < rows.length; i++) {
					if (rows[i] > addIndex) {
						rows[i] += addCount;
					}
				}
				for (int i = rows.length - 1; i >= 0; i--) {
					
					model.removeRow(rows[i]);
				}
			}
		}
		rows = null;
		addCount = 0;
		addIndex = -1;
	}

}
