package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

public class Dialog {
	
	private static Logger logger = Logger.getLogger(Dialog.class);
	private JFileChooser chooser;
	
	/**
	 * Opens a "openFile" Dialog, prompting the user to choose a file from the filesystem which should be added to the library (only, if the file matches the specified filetypes and the library hasn't contained it yet)
	 */
	//TODO: addFile(): pass file to servicelayer
	//TODO: addFile(): get file filters from settings
	public void addFile() {
		chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.addChoosableFileFilter(new CustomFileFilter(".wav"));
		
		int rVal = chooser.showOpenDialog(null);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			logger.info("addFile(): "+chooser.getSelectedFile().getAbsolutePath()+"");
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
			logger.info("addFolder(): "+chooser.getCurrentDirectory());
		}
		if (rVal == JFileChooser.CANCEL_OPTION) {
			logger.info("addFolder(): canceled");
		}
	}
	
	/**
	 * Opens a "openFile" Dialog, prompting the user to choose a file from the filesystem which should be added to the library (only, if the file matches the specified filetypes)
	 */
	//TODO: importPlaylist(): pass file to servicelayer
	//TODO: importPlaylist(): get file filters from settings
	public void importPlaylist() {
		chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.addChoosableFileFilter(new CustomFileFilter(".m3u"));
		
		int rVal = chooser.showOpenDialog(null);
		
		if (rVal == JFileChooser.APPROVE_OPTION) {
			logger.info("importPlaylist(): "+chooser.getSelectedFile().getAbsolutePath()+"");
		}
		if (rVal == JFileChooser.CANCEL_OPTION) {
			logger.info("importPlaylist(): canceled");
		}
	}
	
	/**
	 * Opens a "saveFile" Dialog, prompting the user to choose to choose a path in the filesystem where the playlist should be exported to
	 */
	//TODO: exportPlaylist(): pass file to servicelayer
	//TODO: exportPlaylist(): get file filters from settings
	public void exportPlaylist() {
		File fFile = new File ("Default.java");
		chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.addChoosableFileFilter(new CustomFileFilter(".png"));
		int rVal = chooser.showSaveDialog(null);
		
		if (rVal == JFileChooser.APPROVE_OPTION) {
			logger.info("exportPlaylist(): "+chooser.getSelectedFile().getAbsolutePath()+"");
			fFile = chooser.getSelectedFile();
			if (fFile.exists ()) {
	             int response = JOptionPane.showConfirmDialog (null,
	               "Overwrite existing file?","Confirm Overwrite",
	                JOptionPane.OK_CANCEL_OPTION,
	                JOptionPane.QUESTION_MESSAGE);
	             if (response == JOptionPane.OK_OPTION)
	            	 logger.info("exportPlaylist(): "+chooser.getSelectedFile().getAbsolutePath()+" overwritten");
	             else
	            	 logger.info("exportPlaylist(): didn't overwrite "+chooser.getSelectedFile().getAbsolutePath());
	         }
		}
		if (rVal == JFileChooser.CANCEL_OPTION) {
			logger.info("exportPlaylist(): canceled");
		}
	}
}