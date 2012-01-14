package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import net.miginfocom.swing.MigLayout;

public class GlobalSearch extends JDialog {
	
	private static Logger logger = Logger.getLogger(GlobalSearch.class);
	private JPanel globalSearchPanel;
	private JTextField playlistName;
	
	public GlobalSearch() {
		logger.info("GlobalSearch(): Started constructor GlobalSearch()");
		initialize();
		logger.info("GlobalSearch(): Components successfully initialized");
		setTitle("Create new playlist...");
		setBounds(100, 100, 450, 100);
		setModal(true);
		setVisible(true);
	}
	
	private void initialize() {
		globalSearchPanel = new JPanel(new MigLayout("", "[grow]", "[]"));
		playlistName = new JTextField("");
		playlistName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				// key == KeyEvent.VK_CONTROL && key == KeyEvent.VK_F
		        if (key == KeyEvent.VK_ENTER) {
		        	logger.info("GlobalSearch(): Pressed Enter Key while focussed on playlistName");
		        	if (playlistName.getText().trim().length() > 0) {
		        		//TODO: start globalSearch()
		        		logger.info("GlobalSearch(): loaded search results into songTable");
		        	}
		        	else {
		        		logger.info("GlobalSearch(): Search criterion too short or blank");
		        		new DynamicDialog("Search criterion too short or blank", "Search criteria too short! Specify at least one letter or digit...");
		        	}
		        }
			}
		});

		getContentPane().add(globalSearchPanel);
		globalSearchPanel.add(playlistName, "cell 0 0,growx");
	}
}