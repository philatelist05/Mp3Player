package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;

public class PlaylistGUI extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private JPanel playlistPanel;
	private JLabel lblplaylistName;
	private JTextField playlistName;
	private JButton btnCancel;
	private JButton btnOK;

	private static final long serialVersionUID = -6905419881645068040L;
	private static Logger logger = Logger.getLogger(PlaylistGUI.class);
	private JFileChooser chooser;

	/**
	 * Opens a "openFile" Dialog, prompting the user to choose a file from the
	 * filesystem which should be added to the library (only, if the file
	 * matches the specified filetypes)
	 */
	// TODO: importPlaylist(): pass file to servicelayer
	// TODO: importPlaylist(): get file filters from settings
	public void importPlaylist() {
		File[] files;
		chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.addChoosableFileFilter(new CustomFileFilter(".m3u"));

		int rVal = chooser.showOpenDialog(null);

		if (rVal == JFileChooser.APPROVE_OPTION) {
			files = chooser.getSelectedFiles();
			//TODO start importPlaylist(files)
			logger.info("importPlaylist(): Array of playlists added");
		}
		if (rVal == JFileChooser.CANCEL_OPTION) {
			logger.info("importPlaylist(): canceled");
		}
	}

	/**
	 * Opens a "saveFile" Dialog, prompting the user to choose to choose a path
	 * in the filesystem where the playlist should be exported to
	 */
	// TODO: exportPlaylist(): pass file to servicelayer
	// TODO: exportPlaylist(): get file filters from settings
	public void exportPlaylist(Playlist list) {
		File export;
		chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.addChoosableFileFilter(new CustomFileFilter(".png"));
		int rVal = chooser.showSaveDialog(null);

		if (rVal == JFileChooser.APPROVE_OPTION) {
			export = chooser.getSelectedFile();
			logger.info("exportPlaylist(): "
					+ export.getAbsolutePath() + "");
			if (export.exists()) {
				int response = JOptionPane.showConfirmDialog(null,
						"Overwrite existing file?", "Confirm Overwrite",
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.OK_OPTION)
					//TODO: start exportPlaylist(export)
					logger.info("exportPlaylist(): "
							+ export.getAbsolutePath()
							+ " overwritten");
				else
					logger.info("exportPlaylist(): didn't overwrite "
							+ export.getAbsolutePath());
			}
		}
		if (rVal == JFileChooser.CANCEL_OPTION) {
			logger.info("exportPlaylist(): canceled");
		}
	}

	/**
	 * Opens a JDialog prompting the user to specify a name for the playlist
	 * beeing created
	 */
	public void newPlaylist() {
		logger.info("Started Method newPlaylist()");

		playlistPanel = new JPanel(new MigLayout("", "[][grow]", "[][]"));
		;
		lblplaylistName = new JLabel("Name:");
		playlistName = new JTextField("");
		btnCancel = new JButton("Cancel");
		btnOK = new JButton("OK");

		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("cancel");

		btnOK.addActionListener(this);
		btnOK.setActionCommand("ok");

		setTitle("Create new playlist...");
		setBounds(100, 100, 450, 100);
		setModal(true);

		getContentPane().add(playlistPanel);
		playlistPanel.add(lblplaylistName, "cell 0 0");
		playlistPanel.add(playlistName, "cell 1 0,growx");
		playlistPanel.add(btnCancel, "cell 1 1,alignx right");
		playlistPanel.add(btnOK, "cell 1 1,alignx right");

		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("cancel")) {
			logger.info("newPlaylist(): canceled");
			dispose();
		}

		else if (e.getActionCommand().equals("ok")) {
			if (playlistName.getText().trim().length() > 0) {
				logger.info("newPlaylist(): start addPlaylist("
						+ playlistName.getText() + ")");
				// TODO: Call addPlaylist from ServiceLayer
				dispose();
			}
			else {
				logger.info("newPlaylist(): Name of Playlist too short or blank");
				new DynamicDialog("Name of Playlist too short or blank", "Name of playlist is too short! Specify at least one letter or digit...");
			}
		}
	}
}