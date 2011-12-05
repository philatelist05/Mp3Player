package at.ac.tuwien.sepm2011ws.mp3player;

import java.awt.EventQueue;

import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import at.ac.tuwien.sepm2011ws.mp3player.PresentationLayer.MainFrame;

/**
 * Hello world!
 *
 */
public class Main 
{
	private static Logger logger = Logger.getLogger(Main.class);
	
	public static void main( String[] args )
    {
		DOMConfigurator.configureAndWatch( "log4j-4.xml", 60*1000 );
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					logger.info("Logger successfully initialized");
					logger.info("Starting MainWindow...");
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					new MainFrame();
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Error starting Main Window");
				}
			}
		});
    }
}
