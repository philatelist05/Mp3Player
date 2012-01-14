package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.io.File;

import javax.swing.JFileChooser;
import org.apache.log4j.Logger;

public class LibraryGUI {
	
	private static Logger logger = Logger.getLogger(LibraryGUI.class);
	private JFileChooser chooser;
	
	/**
	 * Opens a "openFile" Dialog, prompting the user to choose a file from the filesystem which should be added to the library (only, if the file matches the specified filetypes and the library hasn't contained it yet)
	 */
	//TODO: addFile(): pass file to servicelayer
	//TODO: addFile(): get file filters from settings
	public void addFile() {
		File[] files;
		chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setMultiSelectionEnabled(true);
		chooser.addChoosableFileFilter(new CustomFileFilter(".png"));
		
		int rVal = chooser.showOpenDialog(null);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			files = chooser.getSelectedFiles();
			//TODO: start addSong(files)
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
		chooser.setMultiSelectionEnabled(true);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int rVal = chooser.showOpenDialog(null);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			folder = chooser.getCurrentDirectory();
			//TODO: start addFolder(folder)
			logger.info("addFolder(): "+folder.getAbsolutePath());
		}
		if (rVal == JFileChooser.CANCEL_OPTION) {
			logger.info("addFolder(): canceled");
		}
	}
}