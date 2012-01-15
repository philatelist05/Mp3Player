package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.io.File;

import javax.swing.JFileChooser;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceSettings;

public class LibraryGUI {
	
	private static Logger logger = Logger.getLogger(LibraryGUI.class);
	private JFileChooser chooser;
	private PlaylistService ps;
	private ServiceSettings ss;
	
	public LibraryGUI() {
		ServiceFactory sf = ServiceFactory.getInstance();
		ps = sf.getPlaylistService();
		ss = sf.getServiceSettings();
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
			ps.addSongs(songs);
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
		File folder;
		chooser = new JFileChooser();
		//chooser.setAcceptAllFileFilterUsed(false);
		//chooser.addChoosableFileFilter(new CustomFileFilter(".wav"));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int rVal = chooser.showOpenDialog(null);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			folder = chooser.getCurrentDirectory();
			ps.addFolder(folder);
			logger.info("addFolder(): Folder "+folder.getAbsolutePath()+" added");
		}
		if (rVal == JFileChooser.CANCEL_OPTION) {
			logger.info("addFolder(): canceled");
		}
	}
}