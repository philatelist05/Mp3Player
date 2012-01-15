package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;

import net.miginfocom.swing.MigLayout;

public class GlobalSearch extends JDialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2115447615731002587L;
	private static Logger logger = Logger.getLogger(GlobalSearch.class);
	private JPanel globalSearchPanel;
	private JTextField playlistName;
	private PlaylistService ps;
	private Playlist result;
	
	public GlobalSearch() {
		ServiceFactory sf = ServiceFactory.getInstance();
		ps = sf.getPlaylistService();
		logger.info("GlobalSearch(): Started constructor GlobalSearch()");
		initialize();
		logger.info("GlobalSearch(): Components successfully initialized");
		
		setTitle("Search globally...");
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
		        		result = ps.globalSearch(playlistName.getText());
		        		new MainFrame(result);
		        		dispose();
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