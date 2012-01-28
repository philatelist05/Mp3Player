package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.SettingsService;

public class LibraryGUI extends JDialog implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3957579889665439757L;
	private static Logger logger = Logger.getLogger(LibraryGUI.class);
	private JFileChooser chooser;
	private PlaylistService ps;
	private SettingsService ss;
	private JPanel checkPanel;
	private JLabel checklabel;
	private File[] songs;
	private File folder;
	private Thread fred;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private Dimension dim = toolkit.getScreenSize();
	private String command;
	private ImageIcon loading;
	private JLabel lblLoading = new JLabel();

	private void add(String command) {
		this.command = command;
		new JDialog();

		checkPanel = new JPanel(new MigLayout("", "[grow]", "[][]"));
		checklabel = new JLabel("Adding files...");

		getContentPane().add(checkPanel);
		checkPanel.add(checklabel, "cell 0 0, align center");
		
		try {
			loading = new ImageIcon(
					new ClassPathResource("img/loading.gif").getURL());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		lblLoading.setIcon(loading);
		checkPanel.add(lblLoading, "cell 0 1, align center");

		setTitle("Adding files...");

		int width = 250, height = 80;
		int positionX = (int) Math.round(dim.getWidth() / 2 - width / 2);
		int positionY = (int) Math.round(dim.getHeight() / 2 - height / 2);

		setBounds(positionX, positionY, width, height);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModal(true);
		setResizable(false);

		fred = new Thread(this);
		fred.start();
		logger.info("GetMetaTag(): Started Thread");

		setVisible(true);
		logger.info("GetMetaTag(): Made checkDialog visible");
	}

	public void run() {
		try {
			if (command == "folders") {
				if (folder != null) {
					ps.addFolder(folder);
					logger.info("addFolder(): Folder "
							+ folder.getAbsolutePath() + " added");
					
					new MainFrame("reloadsongTable");
					logger.info("checkSongPathGUI(): Back from Mainframe");
					
					this.dispose();
					
					JOptionPane.showConfirmDialog(null, "Folder successfully added!",
							"Add folder...",
							JOptionPane.CLOSED_OPTION);
				} else {
					this.dispose();
					JOptionPane.showConfirmDialog(null, "No folder chosen!",
							"No folder chosen!", JOptionPane.CLOSED_OPTION);
				}
			}

			else if (command == "files") {
				if (songs != null) {
					ps.addSongs(songs);
					logger.info("addFile(): Array of files added");
					
					new MainFrame("reloadsongTable");
					logger.info("checkSongPathGUI(): Back from Mainframe");
					
					this.dispose();
					
					JOptionPane.showConfirmDialog(null, "Files(s) successfully added!",
							"Add folder...",
							JOptionPane.CLOSED_OPTION);
				} else {
					this.dispose();
					JOptionPane.showConfirmDialog(null, "No files chosen!",
							"No files chosen!", JOptionPane.CLOSED_OPTION);
				}
			}
			
			else {
				this.dispose();
				JOptionPane.showConfirmDialog(null, "Kind of adding not chosen!",
						"Kind of adding not chosen!", JOptionPane.CLOSED_OPTION);
			}
		} catch (DataAccessException e) {
			this.dispose();
			JOptionPane.showConfirmDialog(null, e.getMessage(),
					"Error", JOptionPane.CLOSED_OPTION);
		}
	}

	public LibraryGUI() {
		ServiceFactory sf = ServiceFactory.getInstance();
		ps = sf.getPlaylistService();
		ss = sf.getSettingsService();
	}

	/**
	 * Opens a "openFile" Dialog, prompting the user to choose a file from the
	 * filesystem which should be added to the library (only, if the file
	 * matches the specified filetypes and the library hasn't contained it yet)
	 */
	public void addFile() {
		chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setMultiSelectionEnabled(true);
		chooser.addChoosableFileFilter(new CustomFileFilter(ss
				.getUserFileTypes(), "Audio files"));

		int rVal = chooser.showOpenDialog(null);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			songs = chooser.getSelectedFiles();
			add("files");
		}

		if (rVal == JFileChooser.CANCEL_OPTION) {
			logger.info("addFile(): canceled");
		}
	}

	/**
	 * Opens a "openFolder" Dialog, prompting the user to choose a folder from
	 * the filesystem. All new (recursively) found files matching the specified
	 * filetypes are added to the library
	 */
	public void addFolder() {
		chooser = new JFileChooser();
		// chooser.setAcceptAllFileFilterUsed(false);
		// chooser.addChoosableFileFilter(new CustomFileFilter(".wav"));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int rVal = chooser.showOpenDialog(null);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			folder = chooser.getSelectedFile();

			logger.info("addFolder(): Start thread...");

			add("folders");
		}
		if (rVal == JFileChooser.CANCEL_OPTION) {
			logger.info("addFolder(): canceled");
		}
	}
}