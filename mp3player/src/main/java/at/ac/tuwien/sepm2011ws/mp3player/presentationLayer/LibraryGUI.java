package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.io.File;

import javax.sql.rowset.JdbcRowSet;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.SettingsService;

public class LibraryGUI extends JDialog implements Runnable {
	
	private static Logger logger = Logger.getLogger(LibraryGUI.class);
	private JFileChooser chooser;
	private PlaylistService ps;
	private SettingsService ss;
	private JPanel checkPanel;
	private JLabel checklabel;
	private JButton btnCancel;
	private JButton btnStart;
	private File folder;
	private Thread fred;

	private void initialize() {
		checkPanel = new JPanel(new MigLayout("", "[grow]", "[]"));
		checklabel = new JLabel("Adding Song(s)...");
		
		getContentPane().add(checkPanel);
		checkPanel.add(checklabel, "cell 0 0");
		
		setTitle("Checking songpaths...");
		setBounds(100, 100, 450, 150);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModal(true);
		
		fred = new Thread(this);
		fred.start();
		
		setVisible(true);
		//dispose();
	}
	
	public void run() {
			try {
				ps.addFolder(folder);
				logger.info("addFolder(): Folder "+folder.getAbsolutePath()+" added");
				
				new MainFrame("reloadsongTable");
				logger.info("checkSongPathGUI(): Back from Mainframe");
				
				this.dispose();
				fred.stop();
			} catch (DataAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public LibraryGUI() {
		ServiceFactory sf = ServiceFactory.getInstance();
		ps = sf.getPlaylistService();
		ss = sf.getSettingsService();
	}
	
	/**
	 * Opens a "openFile" Dialog, prompting the user to choose a file from the filesystem which should be added to the library (only, if the file matches the specified filetypes and the library hasn't contained it yet)
	 */
	public void addFile() {
		File[] songs;
		chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setMultiSelectionEnabled(true);
		chooser.addChoosableFileFilter(new CustomFileFilter(ss.getUserFileTypes(),"Audio files"));
		
		int rVal = chooser.showOpenDialog(null);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			songs = chooser.getSelectedFiles();
			try {
				ps.addSongs(songs);
			} catch (DataAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("addFile(): Array of Files added");
		}
		if (rVal == JFileChooser.CANCEL_OPTION) {
			logger.info("addFile(): canceled");
		}
	}
	
	/**
	 * Opens a "openFolder" Dialog, prompting the user to choose a folder from the filesystem. All new (recursively) found files matching the specified filetypes are added to the library
	 */
	public void addFolder() {
		chooser = new JFileChooser();
		//chooser.setAcceptAllFileFilterUsed(false);
		//chooser.addChoosableFileFilter(new CustomFileFilter(".wav"));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int rVal = chooser.showOpenDialog(null);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			folder = chooser.getSelectedFile();
			logger.info("addFolder(): Start thread...");
			
			initialize();
		}
		if (rVal == JFileChooser.CANCEL_OPTION) {
			logger.info("addFolder(): canceled");
		}
	}
}