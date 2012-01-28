package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.Icon;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.WritablePlaylist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;

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
	private Playlist nodePL = null;
	private Icon icon = null;
    
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
	 * @param Playlist
	 * @see DefaultMutableTreeNode
	 */
    public PlaylistTreeNode(Object userObject, boolean childrenAllowed, Playlist nodePL) {
        super(userObject, childrenAllowed);
        this.nodePL = nodePL;
    }
    
    /**
	 * Special Constructer, inherit from DefaultMutableTreeNode, saves a Playlist with Icon
	 * @param Object
	 * @param boolean
	 * @param Playlist
	 * @param Icon
	 * @see DefaultMutableTreeNode
	 */
    public PlaylistTreeNode(Object userObject, boolean childrenAllowed, Playlist nodePL, Icon icon) {
        super(userObject, childrenAllowed);
        this.nodePL = nodePL;
        this.icon = icon;
    }
    
	/**
	 * Sets the Playlist
	 * @param Playlist
	 */
    public void setNodePlaylist(WritablePlaylist nodePL) {
        this.nodePL = nodePL;
    }
    
    /**
	 * Returns the Playlist
	 * @return Playlist
	 */
    public Playlist getNodePlaylist() {
        return nodePL;
    }
    
    /**
	 * Sets the Icon
	 * @param Icon
	 */
    public void setNodeIcon(Icon icon) {
        this.icon = icon;
    }
    
    /**
	 * Returns the Icon
	 * @return Icon
	 */
    public Icon getNodeIcon() {
        return icon;
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