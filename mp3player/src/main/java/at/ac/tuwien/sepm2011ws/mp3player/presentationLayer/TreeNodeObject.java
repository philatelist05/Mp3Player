package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import javax.swing.Icon;

/**
 * SpecialNodeObject for the JTree
 * 
 */
public class TreeNodeObject {
	private String title;
	private Icon icon;
	
	public TreeNodeObject(String title) {
        this.title=title;
    }
	
    public TreeNodeObject(String title, Icon icon) {
        this.title=title;
        this.icon=icon;
    }
    
    public Icon getIcon() {
		return icon;    	
    }
    
    public void setIcon(Icon icon) {
        this.icon=icon;    	
    }
    
    public String toString() {
		return title;    	
    }

}
