package at.ac.tuwien.sepm2011ws.mp3player;

import java.awt.EventQueue;
import javax.swing.UIManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import at.ac.tuwien.sepm2011ws.mp3player.presentationLayer.GetMetaTag;
import at.ac.tuwien.sepm2011ws.mp3player.presentationLayer.MainFrame;

/**
 * Hello world!
 * 
 */
public class Main {
	private static Logger logger = Logger.getLogger(GetMetaTag.class);
	public static void main(String[] args) {
		DOMConfigurator.configureAndWatch("src/main/resources/log4j-4.xml", 60 * 1000);
		startMainFrame();
	}
	private static SplashScreen splash;

	private static void startMainFrame() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					logger.info("Logger successfully initialized");
					logger.info("Starting MainFrame...");
					
					splash = SplashScreen.getSplash();
					splash.start();
					
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
					new MainFrame();
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		});
	}
}
