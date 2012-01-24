package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;

public class GlobalSearch extends JDialog implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2115447615731002587L;
	private static Logger logger = Logger.getLogger(GlobalSearch.class);
	private JPanel globalSearchPanel;
	private JTextField playlistName;
	private JButton btnCancel = new JButton("Cancel");
	private JButton btnSave = new JButton("OK");
	private PlaylistService ps;
	private Playlist result;
	private int width;
	private int height;
	private int positionX;
	private int positionY;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private Dimension dim = toolkit.getScreenSize();
	
	public GlobalSearch() {
		ServiceFactory sf = ServiceFactory.getInstance();
		ps = sf.getPlaylistService();
		logger.info("GlobalSearch(): Started constructor GlobalSearch()");
		initialize();
		logger.info("GlobalSearch(): Components successfully initialized");
		
		setTitle("Search globally...");
		
		width = 450;
		height = 100;
		positionX = (int) Math.round(dim.getWidth() / 2 - width / 2);
		positionY = (int) Math.round(dim.getHeight() / 2 - height / 2);
		setBounds(positionX, positionY, width, height);
		
		setResizable(false);
		setModal(true);
		setVisible(true);
	}
	
	private void search() {
		if (playlistName.getText().trim().length() > 0) {
    		try {
				result = ps.globalSearch(playlistName.getText());
			} catch (DataAccessException e1) {
				JOptionPane
				.showConfirmDialog(
						null,
						"Error...",
						"Error: " + e1,
						JOptionPane.CLOSED_OPTION);
			}
    		new MainFrame(result, "reloadsongtable");
    		dispose();
    	}
    	else {
    		logger.info("GlobalSearch(): Search criterion too short or blank");
    		JOptionPane
			.showConfirmDialog(
					null,
					"Search criteria too short! Specify at least one letter or digit...",
					"Search criterion too short or blank!",
					JOptionPane.CLOSED_OPTION);
    	}
	}
	
	private void initialize() {
		
		globalSearchPanel = new JPanel(new MigLayout("", "[grow]", "[][]"));
		playlistName = new JTextField("");
		
		playlistName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				// key == KeyEvent.VK_CONTROL && key == KeyEvent.VK_F
		        if (key == KeyEvent.VK_ENTER) {
		        	logger.info("GlobalSearch(): Pressed Enter Key while focussed on playlistName");
		        	search();
		        }
			}
		});

		getContentPane().add(globalSearchPanel);
		globalSearchPanel.add(playlistName, "cell 0 0,growx");
		
		globalSearchPanel.add(btnCancel, "cell 0 1, alignx right, aligny center");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("cancel");

		globalSearchPanel.add(btnSave, "cell 0 1, alignx right, aligny center");
		btnSave.addActionListener(this);
		btnSave.setActionCommand("ok");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("cancel")) {
			this.dispose();
		}
		
		else if (e.getActionCommand().equals("ok")) {
			search();
		}
		
	}
}