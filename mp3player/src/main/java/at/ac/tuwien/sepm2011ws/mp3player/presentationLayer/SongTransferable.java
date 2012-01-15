package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;

public class SongTransferable implements Transferable {
	protected static DataFlavor songFlavor = new DataFlavor(Song.class,
			"A Song Object");
	protected static DataFlavor[] supportedFlavors = { songFlavor };
	Song[] songs;

	public SongTransferable(Song[] songs) {
		this.songs=new Song[songs.length];
		for (int i = 0; i < songs.length; i++) {
			this.songs[i] = songs[i];
		}
	}

	public DataFlavor[] getTransferDataFlavors() {
		return supportedFlavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		if (flavor.equals(songFlavor) || flavor.equals(DataFlavor.stringFlavor))
			return true;
		return false;
	}

	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException {
		if (flavor.equals(songFlavor))
			return songs;
		else if (flavor.equals(DataFlavor.stringFlavor))
			return songs.toString();
		else
			throw new UnsupportedFlavorException(flavor);
	}

}
