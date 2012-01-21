package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.datatransfer.DataFlavor;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.ReadonlyPlaylist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;

public class JTreeSongTransferHandler extends SongTransferHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int[] indices = null;
	private int addIndex = -1; // Location where items were added
	private int addCount = 0; // Number of items added.
	private PlaylistService ps;
	
	public JTreeSongTransferHandler(PlaylistService ps) {
		this.ps=ps;
	}
	protected Song[] exportSong(JComponent c) {
		return null;
	}

	protected void importSong(JComponent c, Song[] songs) {

		JTree target = (JTree) c;
		DefaultTreeModel treeModel = (DefaultTreeModel) target.getModel();
		JTree.DropLocation dl = (JTree.DropLocation) target.getDropLocation();
		TreePath path = dl.getPath();
		PlaylistTreeNode clicked = (PlaylistTreeNode) path
				.getLastPathComponent();
		if (treeModel.isLeaf(clicked)) {
			TreePath check = new TreePath(new String[] { "", "Library" });
			if (path.toString().equals(check.toString())) {
			} else {
				ReadonlyPlaylist cp = clicked.getNodePlaylist();
				for (int i = 0; i < songs.length; i++) {
					cp.add(songs[i]);
					try {
						if(cp.getClass() == Playlist.class)
							ps.updatePlaylist((Playlist)cp);
					} catch (DataAccessException e) {
						//TODO: ErrorDialog
					}
				}
			}
		}
	}

	protected void cleanup(JComponent c, boolean remove) {
	}
	/*
	 * public boolean canImport(JComponent c, DataFlavor[] flavors) { JTree
	 * target = (JTree) c; DefaultTreeModel treeModel = (DefaultTreeModel)
	 * target.getModel();
	 * 
	 * JTree.DropLocation dl = (JTree.DropLocation) target.getDropLocation();
	 * TreePath path = dl.getPath(); PlaylistTreeNode clicked =
	 * (PlaylistTreeNode) path .getLastPathComponent();
	 * 
	 * if (path == null || !treeModel.isLeaf(clicked)) { return false; } for
	 * (int i = 0; i < flavors.length; i++) { if (songFlavor.equals(flavors[i]))
	 * { return true; } } return false;
	 * 
	 * }
	 */
}
