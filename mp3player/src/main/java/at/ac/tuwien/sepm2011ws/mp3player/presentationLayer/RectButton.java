package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * Creates a Rectangular Button.
 */
@SuppressWarnings("serial")
public class RectButton extends JButton
{	
	/**
	 * Constructor with only one image. This image will be used as active, inactive and pressed icon.
	 * 
	 * @param standardIcon the image for active and inactive and pressed state
	 */
    public RectButton(Icon standardIcon) 
    {
        this(standardIcon, standardIcon, standardIcon);
    }
    
    /**
     * Constructor with two images.
     * 
     * @param rolloverIcon the image which will be shown on mouse over and pressed event
     * @param standardIcon the image that will otherwise be shown
     */
    public RectButton(Icon standardIcon, Icon rolloverIcon) 
    {
        this(standardIcon, standardIcon, rolloverIcon);
    }
    
    /**
     * Constructor with all parameters.
     * 
     * @param rolloverIcon the image which will be shown on mouse over event
     * @param pressedIcon the image that will be shown on pressed event
     * @param standardIcon the image that will otherwise be shown
     */
    public RectButton(Icon standardIcon, Icon rolloverIcon, Icon pressedIcon) 
    {
        this.setIcon(standardIcon);
        this.setRolloverIcon(rolloverIcon);
        this.setPressedIcon(pressedIcon);
        
        int ih = standardIcon.getIconHeight();
        int iw = standardIcon.getIconWidth();
        // the dimension is the image size
        Dimension dim = new Dimension(iw, ih);
        setPreferredSize(dim);
        setMinimumSize(dim);
        setMaximumSize(dim);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false); 
        setFocusPainted(false);
        setAlignmentY(Component.TOP_ALIGNMENT);
    }
}
