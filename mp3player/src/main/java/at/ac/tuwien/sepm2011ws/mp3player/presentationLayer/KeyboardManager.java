package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.DefaultKeyboardFocusManager;
import java.awt.event.KeyEvent;

import org.apache.log4j.Logger;

public class KeyboardManager extends DefaultKeyboardFocusManager {
	private static Logger logger = Logger.getLogger(KeyboardManager.class);
	
	public boolean dispatchKeyEvent(KeyEvent e) {
		if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_F) {
			logger.info("dispatchKeyEvent(): Strg+f pressed");
			new GlobalSearch();
			return true;
		}
		return super.dispatchKeyEvent(e);
	}
}
