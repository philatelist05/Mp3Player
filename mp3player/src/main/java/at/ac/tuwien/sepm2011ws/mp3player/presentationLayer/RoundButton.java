package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * Creates a Round Button.
 */
@SuppressWarnings("serial")
public class RoundButton extends JButton
{
	private Shape shape;
	
	/**
	 * Constructor with only one image. This image will be used as active, inactive and pressed icon.
	 * 
	 * @param standardIcon the image for active and inactive and pressed state
	 */
    public RoundButton(Icon standardIcon) 
    {
        this(standardIcon, standardIcon, standardIcon);
    }
    
    /**
     * Constructor with two images.
     * 
     * @param rolloverIcon the image which will be shown on mouse over and pressed event
     * @param standardIcon the image that will otherwise be shown
     */
    public RoundButton(Icon standardIcon, Icon rolloverIcon) 
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
    public RoundButton(Icon standardIcon, Icon rolloverIcon, Icon pressedIcon) 
    {
        this.setIcon(standardIcon);
        this.setRolloverIcon(rolloverIcon);
        this.setPressedIcon(pressedIcon);
        
        int iw = Math.max(standardIcon.getIconWidth(), standardIcon.getIconHeight());
        int sw = 1;
        // create a one pixel wide border around the button
        //setBorder(BorderFactory.createEmptyBorder(sw, sw, sw, sw));
        // the dimension is the image size plus a one pixel border
        //Dimension dim = new Dimension(iw+sw*2, iw+sw*2);
        Dimension dim = new Dimension(iw, iw);
        setPreferredSize(dim);
        setMinimumSize(dim);
        setMaximumSize(dim);
        //setBackground(Color.BLACK);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setAlignmentY(Component.TOP_ALIGNMENT);
        // the round shape of the button must be set in order to handle mouse moves correctly
        initShape();
    }
    
    public void setStandardIcon(Icon standardIcon) {
    	this.setIcon(standardIcon);
    }
    
    public void settRolloverIcon(Icon rolloverIcon) {
        this.setRolloverIcon(rolloverIcon);    	
    }
    
    public void settPressedIcon(Icon pressedIcon) {
        this.setPressedIcon(pressedIcon);    	
    }
    
    protected void initShape() 
    {
    	Dimension s = this.getPreferredSize();
    	shape = new Ellipse2D.Float(0, 0, s.width-1, s.height-1);
    }
    
    @Override
    protected void paintBorder(Graphics g) 
    {
        initShape();
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.draw(shape);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }
    
    @Override
    public boolean contains(int x, int y) 
    {
    	/*
    	 *  This method returns true, if the mouse cursor is positioned inside the circle.
    	 *  Otherwise it returns false.
    	 */
        if(shape == null) initShape();
        return shape.contains(x, y);
    }
}
