package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import javax.swing.tree.DefaultMutableTreeNode;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.ReadonlyPlaylist;

/**
 * Special TreeNode class, extends DefaultMutableTreeNode. Offers the option to 
 * save a Variable of the type Playlist.
 * @see DefaultMutableTreeNode
 */
public class PlaylistTreeNode extends DefaultMutableTreeNode {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ReadonlyPlaylist nodePL = null;
    
	/**
	 * Normal Constructer, inherit from DefaultMutableTreeNode
	 * @param Object
	 * @see DefaultMutableTreeNode
	 */
    public PlaylistTreeNode(Object userObject) {
        super(userObject);
    }
    
	/**
	 * Normal Constructer, inherit from DefaultMutableTreeNode
	 * @param Object
	 * @param boolean
	 * @see DefaultMutableTreeNode
	 */
    public PlaylistTreeNode(Object userObject, boolean childrenAllowed) {
        super(userObject, childrenAllowed);
    }
    
	/**
	 * Special Constructer, inherit from DefaultMutableTreeNode, saves a Playlist
	 * @param Object
	 * @param boolean
	 * @param ReadonlyPlaylist
	 * @see DefaultMutableTreeNode
	 */
    public PlaylistTreeNode(Object userObject, boolean childrenAllowed, ReadonlyPlaylist nodePL) {
        super(userObject, childrenAllowed);
        this.nodePL = nodePL;
    }
    
	/**
	 * Sets the Playlist
	 * @param ReadonlyPlaylist
	 */
    public void setNodePlaylist(Playlist nodePL) {
        this.nodePL = nodePL;
    }
    
    /**
	 * Returns the Playlist
	 * @return Playlist
	 */
    public ReadonlyPlaylist getNodePlaylist() {
        return nodePL;
    }
    
    /**
	 * Checks if the Node has a Playlist
	 * @return true if it has a Playlist, otherwise false
	 */
    public boolean hasNodePlaylist() {
    	if(nodePL==null)
    		return false;
		return true;
    }
}