package at.ac.tuwien.sepm2011ws.mp3player;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import at.ac.tuwien.sepm2011ws.mp3player.presentationLayer.MainFrame;

public class SplashScreen extends JWindow implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5031185115713350918L;
	private static Logger logger = Logger.getLogger(SplashScreen.class);
	private Thread fred;
	private static SplashScreen instance;

	public static SplashScreen getSplash() {
		if (instance == null)
			instance = new SplashScreen();
		return instance;
	}

	private SplashScreen() {

	}

	@Override
	public void run() {
		setAlwaysOnTop(true);
		ImageIcon icon = new ImageIcon();
		JLabel l = new JLabel();

		try {
			icon = new ImageIcon(
					new ClassPathResource("img/splash.gif").getURL());
		} catch (IOException e1) {
			logger.info(e1.getMessage());
		}
		
		l.setIcon(icon);
		
		com.sun.awt.AWTUtilities.setWindowOpaque(this, false);

		getContentPane().add(l, BorderLayout.CENTER);
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension labelSize = l.getPreferredSize();
		setLocation(screenSize.width / 2 - (labelSize.width / 2),
				screenSize.height / 2 - (labelSize.height / 2));
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			logger.error(e.getMessage());
//		}
	}

	public void start() {
		fred = new Thread(this);
		fred.start();
		setVisible(true);		
	}

	public void stop() {
		setVisible(false);
		dispose();
	}
}