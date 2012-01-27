package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;

import java.util.Enumeration;

/**
 * Special TreeCellRenderer, extends DefaultTreeCellRenderer
 */
public class PlaylistTreeCellRenderer extends DefaultTreeCellRenderer {

	private JTree tree;
	private MainFrame mainframe;

	public PlaylistTreeCellRenderer() {
	}

	public PlaylistTreeCellRenderer(MainFrame mainframe2) {
		this.mainframe = mainframe2;
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean wSelected, boolean wExpanded, boolean wLeaf, int pRow,
			boolean wFocus)

	{

		super.getTreeCellRendererComponent(tree, value, wSelected, wExpanded,
				wLeaf, pRow, wFocus);
		Object nodeObj = ((PlaylistTreeNode) value).getUserObject();
		// tree.get

		if (nodeObj instanceof TreeNodeObject) {
			setIcon(((TreeNodeObject) nodeObj).getIcon());
		}
		
		//tree.setFont(tree.getFont().deriveFont(Font.PLAIN));
		//traverse(tree);
		/*
		try {
			if (nodeObj.toString() == mainframe.getCurrentPlaylistGUI()
					.getTitle()) {
				setFont(getFont().deriveFont(Font.ITALIC));
			} else {
				setFont(getFont().deriveFont(Font.PLAIN));
			}
		} catch (NullPointerException ee) {
			setFont(getFont().deriveFont(Font.PLAIN));
		}
		*/
		return this;
	}
	
	public void traverse(JTree tree) { 
	    TreeModel model = tree.getModel();
	    if (model != null) {
	        Object root = model.getRoot();
	        System.out.println(root.toString());
	        walk(model,root);    
	        }
	    else
	       System.out.println("Tree is empty.");
	    }
	    
	  protected void walk(TreeModel model, Object o){
	    int  cc;
	    cc = model.getChildCount(o);
	    for( int i=0; i < cc; i++) {
	      Object child = model.getChild(o, i );
	      if (model.isLeaf(child))
	        System.out.println(child.toString());
	      else {
	        System.out.print(child.toString()+"--");
	        walk(model,child ); 
	        }
	     }
	   }
	  
/*
	@Override
	public Dimension getPreferredSize() {
		Dimension dim = super.getPreferredSize();
		FontMetrics fm = getFontMetrics(getFont());
		char[] chars = getText().toCharArray();

		int w = getIconTextGap() + 16;
		for (char ch : chars) {
			w += fm.charWidth(ch);
		}
		w += getText().length();
		dim.width = w;
		return dim;
	}
*/
	/**
	 * This method takes the node string and traverses the tree till it finds
	 * the node matching the string. If the match is found the node is returned
	 * else null is returned
	 * 
	 * @param nodeStr
	 *            node string to search for
	 * @return tree node
	 */
	/*
	 * private PlaylistTreeNode searchNode(String nodeStr) { PlaylistTreeNode
	 * node = null; PlaylistTreeNode m_rootNode=(PlaylistTreeNode)
	 * tree.getModel().getRoot(); //Get the enumeration Enumeration enum =
	 * m_rootNode.breadthFirstEnumeration();
	 * 
	 * //iterate through the enumeration while(enum.hasMoreElements()) { //get
	 * the node node = (PlaylistTreeNode)enum.nextElement();
	 * 
	 * //match the string with the user-object of the node
	 * if(nodeStr.equals(node.getUserObject().toString())) { //tree node with
	 * string found return node; } }
	 * 
	 * //tree node with string node found return null return null; }
	 */

}