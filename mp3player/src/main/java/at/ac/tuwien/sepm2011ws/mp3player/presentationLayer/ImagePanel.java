package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {
	private Image image;
	private boolean tile;

	public ImagePanel(MigLayout migLayout, Image image) {
		super(migLayout);
		this.image = image;
		this.tile = false;
		// final JCheckBox checkBox = new JCheckBox();
		// checkBox.setAction(new AbstractAction("Tile") {
		// public void actionPerformed(ActionEvent e) {
		// tile = checkBox.isSelected();
		// repaint();
		// }
		// });
		// add(checkBox, BorderLayout.SOUTH);
	};

	public ImagePanel(MigLayout migLayout) {
		super(migLayout);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (tile) {
			int iw = image.getWidth(this);
			int ih = image.getHeight(this);
			if (iw > 0 && ih > 0) {
				for (int x = 0; x < getWidth(); x += iw) {
					for (int y = 0; y < getHeight(); y += ih) {
						g.drawImage(image, x, y, iw, ih, this);
					}
				}
			}
		} else {
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		}
	}
}
