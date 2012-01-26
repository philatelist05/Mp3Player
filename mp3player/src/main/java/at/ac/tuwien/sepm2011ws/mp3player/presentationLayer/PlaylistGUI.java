package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.WritablePlaylist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;
import java.lang.String;

public class PlaylistGUI extends JDialog implements ActionListener, Runnable {

	/**
	 * 
	 */
	private JPanel playlistPanel;
	private JLabel lblplaylistName;
	private JTextField playlistName;
	private JButton btnCancel;
	private JButton btnOK;
	private JDialog checkDialog;
	private JPanel checkPanel;
	private JLabel checkLabel;

	private static final long serialVersionUID = -6905419881645068040L;
	private static Logger logger = Logger.getLogger(PlaylistGUI.class);
	private JFileChooser chooser;
	private PlaylistService ps;
	private WritablePlaylist tempPlaylist = null;
	private int width;
	private int height;
	private int positionX;
	private int positionY;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private Dimension dim = toolkit.getScreenSize();
	private String command;
	private Thread fred;
	private File export;
	private Playlist list;
	private File[] playlists;
	private String text;

	private void doWork(String command, String text) {
		this.command = command;
		this.text = text;
		checkDialog = new JDialog();

		checkPanel = new JPanel(new MigLayout("", "[grow]", "[]"));
		checkLabel = new JLabel(text);

		checkDialog.getContentPane().add(checkPanel);
		checkPanel.add(checkLabel, "cell 0 0");

		checkDialog.setTitle(text);

		int width = 200, height = 100;
		int positionX = (int) Math.round(dim.getWidth() / 2 - width / 2);
		int positionY = (int) Math.round(dim.getHeight() / 2 - height / 2);

		checkDialog.setBounds(positionX, positionY, width, height);
		checkDialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		checkDialog.setModal(true);
		checkDialog.setResizable(false);

		fred = new Thread(this);
		fred.start();
		logger.info("PlaylistGui(): Started Thread");

		checkDialog.setVisible(true);
		logger.info("PlaylistGui: Made doWorkDialog visible");
	}

	public PlaylistGUI() {
		ServiceFactory sf = ServiceFactory.getInstance();
		ps = sf.getPlaylistService();
	}

	/**
	 * Opens a "openFile" Dialog, prompting the user to choose a file from the
	 * filesystem which should be added to the library (only, if the file
	 * matches the specified filetypes)
	 * 
	 * @throws DataAccessException
	 */
	public void importPlaylist() {
		chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setMultiSelectionEnabled(true);

		chooser.addChoosableFileFilter(new CustomFileFilter(
				PlaylistService.PlaylistFileTypes, "Playlist files"));

		int rVal = chooser.showOpenDialog(null);

		if (rVal == JFileChooser.APPROVE_OPTION) {
			playlists = chooser.getSelectedFiles();
			doWork("import", "Imorting playlist(s)");
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
	public void exportPlaylist(Playlist list) {
		this.list = list;
		chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.addChoosableFileFilter(new CustomFileFilter(
				PlaylistService.PlaylistFileTypes, "Playlist files"));
		int rVal = chooser.showSaveDialog(null);

		if (rVal == JFileChooser.APPROVE_OPTION) {
			export = chooser.getSelectedFile();
			logger.info("exportPlaylist(): " + export.getAbsolutePath() + "");
			if (export.exists()) {
				int response = JOptionPane.showConfirmDialog(null,
						"Overwrite existing file?", "Confirm Overwrite",
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.OK_OPTION) {
					doWork("export", "Exporting playlist...");
					logger.info("exportPlaylist(): " + export.getAbsolutePath()
							+ " overwritten");
				} else
					logger.info("exportPlaylist(): didn't overwrite "
							+ export.getAbsolutePath());
			}

			else {
				doWork("export", "Exporting playlist...");
				logger.info("exportPlaylist(): " + export.getAbsolutePath()
						+ " exported");
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

		initialize();

		btnOK.addActionListener(this);
		btnOK.setActionCommand("new");

		setTitle("Create new playlist...");
		setVisible(true);
	}

	/**
	 * Prompts the user to rename the specified playlist
	 * 
	 * @param list
	 *            The specified Playlist object
	 */
	public void renamePlaylistGUI(WritablePlaylist list) {
		logger.info("Started Method renamePlaylist()");

		initialize();

		playlistName.setText(list.getTitle());

		tempPlaylist = list;

		btnOK.addActionListener(this);
		btnOK.setActionCommand("rename");

		setTitle("Create new playlist...");
		setVisible(true);
	}

	public void newPl() {
		String result = playlistName.getText().trim();
		if (result.length() > 0) {
			logger.info("newPlaylist(): start createPlaylist("
					+ playlistName.getText() + ")");
			try {
				ps.createPlaylist(result);
			} catch (DataAccessException e1) {
				JOptionPane.showMessageDialog(null, "createPlaylist: " + e1);
				e1.printStackTrace();
			}
			dispose();
		} else {
			logger.info("newPlaylist(): Name of Playlist too short or blank");
			JOptionPane
					.showConfirmDialog(
							null,
							"Name of playlist is too short! Specify at least one letter or digit...",
							"Name of Playlist too short or blank",
							JOptionPane.CLOSED_OPTION);
		}
	}

	public void renamePl() {
		String result = playlistName.getText().trim();
		if (result.length() > 0 && tempPlaylist != null) {
			logger.info("renamePlaylist(): start renamePlaylist(" + result
					+ ")");
			try {
				ps.renamePlaylist(tempPlaylist, result);
			} catch (DataAccessException e1) {
				JOptionPane.showMessageDialog(null, "renamePlaylist: " + e1);
				e1.printStackTrace();
			}
			tempPlaylist = null;
			dispose();
		} else {
			logger.info("renamePlaylist(): Name of Playlist too short or blank");
			JOptionPane
					.showConfirmDialog(
							null,
							"Name of playlist is too short! Specify at least one letter or digit...",
							"Name of Playlist too short or blank",
							JOptionPane.CLOSED_OPTION);
		}
	}

	private void initialize() {
		playlistPanel = new JPanel(new MigLayout("", "[][grow]", "[][]"));
		lblplaylistName = new JLabel("Name:");
		playlistName = new JTextField("");
		btnCancel = new JButton("Cancel");
		btnOK = new JButton("OK");

		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("cancel");

		getContentPane().add(playlistPanel);
		playlistPanel.add(lblplaylistName, "cell 0 0");
		playlistPanel.add(playlistName, "cell 1 0,growx");
		playlistPanel.add(btnCancel, "cell 1 1,alignx right");
		playlistPanel.add(btnOK, "cell 1 1,alignx right");

		playlistName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				// key == KeyEvent.VK_CONTROL && key == KeyEvent.VK_F
				if (key == KeyEvent.VK_ENTER) {
					logger.info("GlobalSearch(): Pressed Enter Key while focussed on playlistName");
					if (btnOK.getActionCommand() == "new")
						newPl();
					else if (btnOK.getActionCommand() == "rename")
						renamePl();
				}
			}
		});

		width = 450;
		height = 85;
		positionX = (int) Math.round(dim.getWidth() / 2 - width / 2);
		positionY = (int) Math.round(dim.getHeight() / 2 - height / 2);
		setBounds(positionX, positionY, width, height);
		setResizable(false);
		setModal(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("cancel")) {
			logger.info("newPlaylist(): canceled");
			dispose();
		}

		else if (e.getActionCommand().equals("new")) {
			newPl();
		}

		else if (e.getActionCommand().equals("rename")) {
			renamePl();
		}
	}

	@Override
	public void run() {
		try {
			if (command == "import") {
				if (playlists.length > 0) {
					ps.importPlaylist(playlists);

					new MainFrame("reloadsongTable");
					logger.info("checkSongPathGUI(): Back from Mainframe");

					JOptionPane.showConfirmDialog(null,
							"Playlist(s) successfully added!", text,
							JOptionPane.CLOSED_OPTION);
				} else
					JOptionPane.showConfirmDialog(null,
							"No Playlist(s) chosen!", "No Playlist(s) chosen!",
							JOptionPane.CLOSED_OPTION);
			}

			else if (command == "export") {
				if (export != null || list != null) {
					ps.exportPlaylist(export, list);

					new MainFrame("reloadsongTable");
					logger.info("checkSongPathGUI(): Back from Mainframe");

					JOptionPane.showConfirmDialog(null,
							"Playlist successfully exported!", text,
							JOptionPane.CLOSED_OPTION);
				} else
					JOptionPane.showConfirmDialog(null,
							"No filepath or playlist chosen!",
							"No filepath or playlist chosen!",
							JOptionPane.CLOSED_OPTION);
			}

			else
				JOptionPane.showConfirmDialog(null,
						"Kind of playlist management not chosen!",
						"Kind of playlist management not chosen!",
						JOptionPane.CLOSED_OPTION);

			checkDialog.dispose();
		} catch (DataAccessException e) {
			JOptionPane.showConfirmDialog(null, e.getMessage(),
					"Error", JOptionPane.CLOSED_OPTION);
		}
	}
}