package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.datatransfer.DataFlavor;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;

public class JTreeSongTransferHandler extends SongTransferHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int[] indices = null;
	private int addIndex = -1; // Location where items were added
	private int addCount = 0; // Number of items added.

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
		
		/*
		namesPath = tree.getPathForRow(3);
        TreePath namesPath2=new TreePath(new String[] {"things", "sports"});
        if(namesPath.toString().equals(namesPath2.toString())) {
        	System.out.println("kASKmskMSKMs");
        }
        */
        
        

		if (treeModel.isLeaf(clicked)) {
			System.out.println(clicked);
			for (int i = 0; i < songs.length; i++) {
				System.out.println(songs[i]);
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
	public boolean canImport(JComponent c, DataFlavor[] flavors) {
		JTree target = (JTree) c;
		DefaultTreeModel treeModel = (DefaultTreeModel) target.getModel();
		JTree.DropLocation dl = (JTree.DropLocation) target.getDropLocation();
		TreePath path = dl.getPath();
		PlaylistTreeNode clicked = (PlaylistTreeNode) path
				.getLastPathComponent();
		
		/*
		namesPath = tree.getPathForRow(3);
        TreePath namesPath2=new TreePath(new String[] {"things", "sports"});
        if(namesPath.toString().equals(namesPath2.toString())) {
        	System.out.println("kASKmskMSKMs");
        }
        */
		
		for (int i = 0; i < flavors.length; i++) {
			if (songFlavor.equals(flavors[i])) {
				return true;
			}
		}
		return false;
	}
}
