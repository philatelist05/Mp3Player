package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;

public abstract class SongTransferHandler extends TransferHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2219295036479443447L;
	protected static DataFlavor songFlavor = new DataFlavor(Song.class,	"A Song Object");

	protected abstract Song[] exportSong(JComponent c);

	protected abstract void importSong(JComponent c, Song[] songs);

	protected abstract void cleanup(JComponent c, boolean remove);

	protected Transferable createTransferable(JComponent c) {
		return new SongTransferable(exportSong(c));
	}

	public int getSourceActions(JComponent c) {
		return COPY_OR_MOVE;
	}

	public boolean importData(JComponent c, Transferable t) {
		if (canImport(c, t.getTransferDataFlavors())) {
			try {
				Song[] songs = (Song[]) t
						.getTransferData(songFlavor);
				importSong(c, songs);
				return true;
			} catch (UnsupportedFlavorException ufe) {
			} catch (IOException ioe) {
			}
		}

		return false;
	}

	protected void exportDone(JComponent c, Transferable data, int action) {
		cleanup(c, action == MOVE);
	}

	public boolean canImport(JComponent c, DataFlavor[] flavors) {
		//DataFlavor[] flavors = info.getDataFlavors();
		for (int i = 0; i < flavors.length; i++) {
			if (songFlavor.equals(flavors[i])) {
				return true;
			}
		}
		return false;
	}

}
