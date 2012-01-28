package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JPopupMenu.Separator;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import net.miginfocom.swing.MigLayout;

import com.googlecode.starrating.*;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.PlayMode;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.WritablePlaylist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.CoreInteractionService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlayerListener;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.SettingsService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.SongInformationService;

public class MainFrame extends JFrame implements ActionListener, Runnable,
		KeyListener, TableModelListener, RowSorterListener {
	/**
	 * 
	 */
	private static MainFrame mainframe;
	private static final long serialVersionUID = -959319978002415594L;
	private static Logger logger = Logger.getLogger(MainFrame.class);
	private static Playlist currentPlaylistGUI;
	private JSplitPane jsplit;
	private JTree pl_tree = new JTree();
	private TreePath pathToPlaylists = null;
	private TreePath pathToIntPlaylists = null;
	private SongTableRenderer songrendi;

	private PlaylistGUI playlistgui;
	private LibraryGUI librarygui;
	private JTable songTable;
	private Point viewPos = null;
	private HidableTableColumnModel cTableModel;
	private TableRowSorter<TableModel> sorter;
	private SongTableModel songmodel = new SongTableModel(new String[] {
			"Status", "Title", "Artist", "Album", "Year", "Genre", "Duration",
			"Rating", "Playcount" }, 0);
	private RectButton btnPrevious;
	private RoundButton btnPlayPause;
	private RectButton btnNext;
	private JSlider volume;

	private JSlider progress;
	private JLabel lblPlayedTime;
	private JLabel lblDuration;
	private JLabel lblDurationSeperator;

	private String durationAtdefault = "00:00:00";
	private String durationdefault = "00:00:00";

	private JLabel lblHeader;
	private JLabel lblCurrentStateSong;
	private JLabel lblVolume;
	private JCheckBox chckbxMute;
	private JCheckBox chckbxRepeat;
	private JCheckBox chckbxShuffle;
	// private JPopupMenu tablePopupMenu = new JPopupMenu();

	// private JPopupMenu treePopupMenu = new JPopupMenu();

	private Icon l1;
	private Icon l2;
	private Icon l3;
	private Icon m1;
	private Icon m2;
	private Icon m3;
	private Icon r1;
	private Icon r2;
	private Icon r3;
	private Icon mp1;
	private Icon mp2;
	private Icon mp3;
	private Icon nomute_icon;
	private Icon nomuterollover_icon;
	private Icon nomutepressed_icon;
	private Icon mute_icon;
	private Icon muterollover_icon;
	private Icon noshuffle_icon;
	private Icon noshufflerollover_icon;
	private Icon noshufflepressed_icon;
	private Icon shuffle_icon;
	private Icon shufflerollover_icon;
	private Icon norepeat_icon;
	private Icon norepeatrollover_icon;
	private Icon norepeatpressed_icon;
	private Icon repeat_icon;
	private Icon repeatrollover_icon;
	private Icon bluePL;
	private Icon bluePLS;
	private Icon yellowPL;
	private Icon yellowPLS;
	private Icon library_icon;
	private Icon v3;

	private List<WritablePlaylist> playlists = null;
	private PlaylistService ps;
	private CoreInteractionService cis;
	private SettingsService ss;
	private SongInformationService sis;

	private void initIcons() throws IOException {
		l1 = new ImageIcon(new ClassPathResource("img/left_blue.png").getURL());
		l2 = new ImageIcon(
				new ClassPathResource("img/left_orange.png").getURL());
		l3 = new ImageIcon(
				new ClassPathResource("img/left_orange_pressed.png").getURL());

		m1 = new ImageIcon(new ClassPathResource("img/play_blue.png").getURL());
		m2 = new ImageIcon(
				new ClassPathResource("img/play_orange.png").getURL());
		m3 = new ImageIcon(
				new ClassPathResource("img/play_orange_pressed.png").getURL());

		r1 = new ImageIcon(new ClassPathResource("img/right_blue.png").getURL());

		r2 = new ImageIcon(
				new ClassPathResource("img/right_orange.png").getURL());
		r3 = new ImageIcon(
				new ClassPathResource("img/right_orange_pressed.png").getURL());

		mp1 = new ImageIcon(
				new ClassPathResource("img/pause_blue.png").getURL());
		mp2 = new ImageIcon(
				new ClassPathResource("img/pause_orange.png").getURL());
		mp3 = new ImageIcon(new ClassPathResource(
				"img/pause_orange_pressed.png").getURL());

		nomute_icon = new ImageIcon(
				new ClassPathResource("img/nomuteblue.png").getURL());
		nomuterollover_icon = new ImageIcon(new ClassPathResource(
				"img/nomuteorange.png").getURL());
		nomutepressed_icon = new ImageIcon(new ClassPathResource(
				"img/nomuteorange_pressed.png").getURL());
		mute_icon = new ImageIcon(
				new ClassPathResource("img/muteblue.png").getURL());
		muterollover_icon = new ImageIcon(new ClassPathResource(
				"img/muteorange.png").getURL());

		noshuffle_icon = new ImageIcon(new ClassPathResource(
				"img/noshuffleblue.png").getURL());
		noshufflerollover_icon = new ImageIcon(new ClassPathResource(
				"img/noshuffleorange.png").getURL());
		noshufflepressed_icon = new ImageIcon(new ClassPathResource(
				"img/noshuffleorange_pressed.png").getURL());
		shuffle_icon = new ImageIcon(new ClassPathResource(
				"img/shuffleblue.png").getURL());
		shufflerollover_icon = new ImageIcon(new ClassPathResource(
				"img/shuffleorange.png").getURL());

		norepeat_icon = new ImageIcon(new ClassPathResource(
				"img/norepeatblue.png").getURL());
		norepeatrollover_icon = new ImageIcon(new ClassPathResource(
				"img/norepeatorange.png").getURL());
		norepeatpressed_icon = new ImageIcon(new ClassPathResource(
				"img/norepeatorange_pressed.png").getURL());
		repeat_icon = new ImageIcon(
				new ClassPathResource("img/repeatblue.png").getURL());
		repeatrollover_icon = new ImageIcon(new ClassPathResource(
				"img/repeatorange.png").getURL());

		bluePL = new ImageIcon(new ClassPathResource("img/bluePL.png").getURL());
		bluePLS = new ImageIcon(
				new ClassPathResource("img/bluePLS.png").getURL());
		yellowPL = new ImageIcon(
				new ClassPathResource("img/yellowPL.png").getURL());
		yellowPLS = new ImageIcon(
				new ClassPathResource("img/yellowPLS.png").getURL());
		library_icon = new ImageIcon(
				new ClassPathResource("img/library.png").getURL());
		v3 = new ImageIcon(new ClassPathResource("img/v3.png").getURL());
	}
	
	public Playlist getCurrentPlaylistGUI() {
		return currentPlaylistGUI;		
	}
	
	/**
	 * Parses and replaces the songs of the songTable into the specified
	 * playlist
	 * 
	 * @param list
	 *            the specified playlist
	 * @return the new parsed playlist
	 */
	public Playlist parseSongTable(Playlist list) {
		ArrayList<Song> temp = new ArrayList<Song>();
		Song song;
		// logger.info("parse song play list: " + list.getTitle());
		int row = songmodel.getRowCount();
		list.clear();
		for (int i = 0; i < row; i++) {
			song = (Song) songTable.getValueAt(i, 0);
			temp.add(song);
		}

		list.addAll(temp);

		return list;
	}

	/**
	 * Gets all Songs from the Database
	 */
	public void getWholeLibrary() {
		Playlist library;

		try {

			// Holen aller Songs der Library
			library = ps.getLibrary();
			// Current Playlist setzen (Servicelayer and GUI)
			cis.setCurrentPlaylist(library);
			currentPlaylistGUI = library;
			// Einfügen der Daten in dlTable
			fillSongTable(library);
		} catch (DataAccessException e) {
			JOptionPane.showMessageDialog(null, "Couldn't load library" + e);
		}
	}

	/**
	 * Fills the songTable with the specified songs
	 * 
	 * @param List
	 *            containing song items
	 */
	protected void fillSongTable(Playlist list) {
		String album = null;
		songmodel.setRowCount(0);

		for (Song x : list) {

			if (x.getAlbum() != null)
				album = x.getAlbum().getTitle();
			else
				album = "";

			songmodel.addRow(new Object[] { x, x.getTitle(), x.getArtist(),
					album, x.getYear(), x.getGenre(), x.getDuration(),
					x.getRating(), x.getPlaycount() });
		}
		// songTable.setRowSelectionInterval(25, 26);
		// restoreTableView(25, 0);

	}

	private void restoreTableView(int rowIndex, int vColIndex) {
		if (!(songTable.getParent() instanceof JViewport)) {
			return;
		}
		JViewport viewport = (JViewport) songTable.getParent();

		// This rectangle is relative to the table where the
		// northwest corner of cell (0,0) is always (0,0).
		Rectangle rect = songTable.getCellRect(rowIndex, vColIndex, true);

		// The location of the viewport relative to the table
		viewPos = viewport.getViewPosition();

		// Translate the cell location so that it is relative
		// to the view, assuming the northwest corner of the
		// view is (0,0)
		// rect.setLocation(rect.x-pt.x, rect.y-pt.y);

		// Scroll the area into view
		viewport.scrollRectToVisible(rect);
	}

	/**
	 * Switches the Button Icons to Pause
	 */
	public void setPauseIcons() {
		btnPlayPause.setStandardIcon(mp1);
		btnPlayPause.settRolloverIcon(mp2);
		btnPlayPause.settPressedIcon(mp3);
	}

	/**
	 * Switches the Button Icons to Play
	 */
	public void setPlayIcons() {
		btnPlayPause.setStandardIcon(m1);
		btnPlayPause.settRolloverIcon(m2);
		btnPlayPause.settPressedIcon(m3);
	}

	/**
	 * Sends the "previous Song" Signal to the Service Layer
	 */
	public void previous() {
		// if (cis.isPlaying()) {

		btnPlayPause.setActionCommand("play");
		setPlayIcons();
		lblCurrentStateSong.setText("");

		if (cis.hasPreviousSong()) {
			progress.setEnabled(true);
			lblPlayedTime.setText(getPlayedTimeInSeconds());
			progress.setVisible(true);
			lblCurrentStateSong.setVisible(true);
			lblPlayedTime.setVisible(true);
			lblDurationSeperator.setVisible(true);
			lblDuration.setVisible(true);
		} else {
			progress.setEnabled(false);
			lblPlayedTime.setText("");
			progress.setVisible(true);
			lblCurrentStateSong.setVisible(false);
			lblPlayedTime.setVisible(false);
			lblDurationSeperator.setVisible(false);
			lblDuration.setVisible(false);
			songTable.repaint();
		}

		cis.playPrevious();

		// Song temp = cis.getCurrentSong();

		// progress.setEnabled(true);
		/*
		 * if (fred == null || fred.isAlive() == false) { createThread();
		 * 
		 * }
		 */
		/*
		 * else fred.start();
		 * 
		 * if (cis.isPlaying()) {
		 * lblCurrentStateSong.setText("Currently playing: " + temp.getArtist()
		 * + " - " + temp.getTitle() + "");
		 * 
		 * btnPlayPause.setActionCommand("pause"); setPauseIcons(); } else
		 * setProgressBartoDefault();
		 */
		// }
	}

	/**
	 * Sends the play Song action to the SercviceLayer
	 * 
	 * @param x
	 *            The Song to play
	 */
	public void play(int songIndex) {
		setPlayIcons();

		cis.setCurrentPlaylist(currentPlaylistGUI);
		cis.playFromBeginning(songIndex);
		// Song temp = cis.getCurrentSong();

		progress.setEnabled(true);
		lblPlayedTime.setText(getPlayedTimeInSeconds());
		progress.setVisible(true);

		lblCurrentStateSong.setVisible(true);
		lblPlayedTime.setVisible(true);
		lblDurationSeperator.setVisible(true);
		lblDuration.setVisible(true);
		/*
		 * if (fred == null || fred.isAlive() == false) { createThread();
		 * 
		 * }
		 */

		if (cis.isPlaying()) {
			/*
			 * lblCurrentStateSong.setText("Currently playing: " +
			 * temp.getArtist() + " - " + temp.getTitle() + "");
			 */
			btnPlayPause.setActionCommand("pause");
			setPauseIcons();
			btnNext.setActionCommand("next");
			btnPrevious.setActionCommand("previous");
			System.out.println(btnNext.getActionCommand());
			System.out.println(btnPrevious.getActionCommand());
		}

		// lblDuration.setText(getMediaTimeAt(100));
		// lblDuration.validate();
	}

	/**
	 * Sends the play Song Signal to the ServiceLayer
	 */

	public void pauseplay() {
		cis.playPause();
		if (fred == null || fred.isAlive() == false)
			createThread();

		Song temp = cis.getCurrentSong();
		progress.setVisible(true);
		if (cis.isPlaying()) {
			lblCurrentStateSong.setText("Currently playing: "
					+ temp.getArtist() + " - " + temp.getTitle() + "");
			btnPlayPause.setActionCommand("pause");
			setPauseIcons();
		}
	}

	/**
	 * Sends the pause Song action to the ServiceLayer
	 * 
	 * @throws InterruptedException
	 */
	public void pause() throws InterruptedException {
		cis.pause();
		/*
		 * if (fred != null) { fred.interrupt(); }
		 */

		progress.setVisible(true);
		if (cis.isPaused()) {
			lblCurrentStateSong.setText(lblCurrentStateSong.getText().concat(
					" (Paused)"));
			btnPlayPause.setActionCommand("pauseplay");
			setPlayIcons();
		}
	}

	/**
	 * Sends the next Song action to the ServiceLayer
	 */
	public void next() {
		// if (cis.isPlaying()) {
		btnPlayPause.setActionCommand("play");
		setPlayIcons();
		lblCurrentStateSong.setText("");

		if (cis.hasNextSong()) {
			progress.setEnabled(true);
			lblPlayedTime.setText(getPlayedTimeInSeconds());
			progress.setVisible(true);
			lblCurrentStateSong.setVisible(true);
			lblPlayedTime.setVisible(true);
			lblDurationSeperator.setVisible(true);
			lblDuration.setVisible(true);
		} else {
			progress.setEnabled(false);
			lblPlayedTime.setText("");
			progress.setVisible(true);
			lblCurrentStateSong.setVisible(false);
			lblPlayedTime.setVisible(false);
			lblDurationSeperator.setVisible(false);
			lblDuration.setVisible(false);
			songTable.repaint();
		}

		cis.playNext();
		// Song temp = cis.getCurrentSong();
		// progress.setEnabled(true);
		/*
		 * if (fred == null || fred.isAlive() == false) { createThread();
		 * 
		 * }
		 */
		/*
		 * else { fred.start(); } if (cis.isPlaying()) {
		 * lblCurrentStateSong.setText("Currently playing: " + temp.getArtist()
		 * + " - " + temp.getTitle() + "");
		 * 
		 * btnPlayPause.setActionCommand("pause"); setPauseIcons(); } else
		 * setProgressBartoDefault();
		 */
		// }
	}

	/**
	 * Sends the volume action to the ServiceLayer
	 * 
	 * @param x
	 *            the volume from 0 - 50
	 */
	public void setVol(int x) {
		if (!chckbxMute.isSelected()) {
			cis.setVolume(x);
		}
	}

	/**
	 * Sends the Specified time position of the song to the ServiceLayer
	 * 
	 * @param time
	 *            position from 0 - 100
	 */
	public void setMediaTime(int value) {

		if (cis.isPlaying() || cis.isPaused() == true)
			// cis.seekToSecond(value);
			cis.seek(value); // seek in percent
	}

	public String getMediaTimeAt(double percent) {

		double timeAt = cis.getDurationAt(percent);
		String timeStringAt = String.format("%02.0f:%02.0f:%02.0f",
				Math.floor(timeAt / 3600), Math.floor((timeAt % 3600) / 60),
				Math.floor(timeAt % 60));
		return timeStringAt;

	}

	public String getPlayedTimeInSeconds() {

		double timeAt = cis.getPlayTimeInSeconds();
		String timeStringAt = String.format("%02.0f:%02.0f:%02.0f",
				Math.floor(timeAt / 3600), Math.floor((timeAt % 3600) / 60),
				Math.floor(timeAt % 60));
		return timeStringAt;

	}

	public String getMediaTime() {

		double timeAt = cis.getDuration();
		String timeStringAt = String.format("%02.0f:%02.0f:%02.0f",
				Math.floor(timeAt / 3600), Math.floor((timeAt % 3600) / 60),
				Math.floor(timeAt % 60));
		return timeStringAt;

	}

	public void setProgressBartoDefault() {
		progress.setValue(0);
		progress.setEnabled(false);
		lblPlayedTime.setText(durationAtdefault);
		lblDuration.setText(durationdefault);
		lblDuration.setVisible(false);
		lblDurationSeperator.setVisible(false);
		lblPlayedTime.setVisible(false);
	}

	/*
	 * private String getMediaTime() {
	 * 
	 * double timeAt = cis.getDuration(); String timeStringAt =
	 * String.format("%02.0f:%02.0f:%02.0f", Math.floor(timeAt / 3600),
	 * Math.floor((timeAt % 3600) / 60), Math.floor(timeAt % 60)); return
	 * timeStringAt;
	 * 
	 * }
	 */

	private Thread fred;
	private int width;
	private int height;
	private int positionX;
	private int positionY;

	private void createThread() {
		fred = new Thread(this);
		fred.start();
	}

	public void run() {

		while (!fred.isInterrupted()) {

			// Song temp = cis.getCurrentSong();
			progress.setValue((int) cis.getPlayTime());
			lblPlayedTime.setText(getPlayedTimeInSeconds());
			lblDuration.setText(getMediaTimeAt(100)); // in percent

			/*
			 * if (cis.isPlaying()) {
			 * lblCurrentStateSong.setText("Currently playing: " +
			 * temp.getArtist() + " - " + temp.getTitle() + "");
			 * btnPlayPause.setActionCommand("pause"); setPauseIcons(); }
			 */
			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {
				fred.interrupt();
			}
		}
	}

	/**
	 * Sends the Mute Signal to the Service Layer
	 * 
	 * @param mute
	 *            true or false
	 */
	private void setMute() {
		cis.toggleMute();
	}

	/**
	 * Sends the changePlayMode signal to the ServiceLayer
	 */
	private void setMode() {
		if (!chckbxRepeat.isSelected() && !chckbxShuffle.isSelected())
			cis.setPlayMode(PlayMode.NORMAL);
		else if (chckbxRepeat.isSelected() && !chckbxShuffle.isSelected())
			cis.setPlayMode(PlayMode.REPEAT);
		else if (!chckbxRepeat.isSelected() && chckbxShuffle.isSelected())
			cis.setPlayMode(PlayMode.SHUFFLE);
	}

	/**
	 * Shows the clicked Playlist from the Tree.
	 * 
	 * @param MouseEvent
	 */
	private void doPlaylistClicked(MouseEvent me) {
		TreePath tp = pl_tree.getPathForLocation(me.getX(), me.getY());
		// PlaylistTreeNode clicked = (PlaylistTreeNode)
		// tp.getLastPathComponent();
		PlaylistTreeNode clicked = (PlaylistTreeNode) pl_tree
				.getLastSelectedPathComponent();
		if (tp != null && clicked != null) {
			if (clicked.hasNodePlaylist()) {
				currentPlaylistGUI = parseSongTable(currentPlaylistGUI);
				try {
					if (currentPlaylistGUI.getClass() == WritablePlaylist.class)
						ps.updatePlaylist((WritablePlaylist) currentPlaylistGUI);
				} catch (DataAccessException e) {
					// TODO: Show error dialog
				}

				currentPlaylistGUI = clicked.getNodePlaylist();
				
				fillSongTable(clicked.getNodePlaylist());
			} else {
			}
		}
		//PlaylistTreeCellRenderer rendi = (PlaylistTreeCellRenderer) pl_tree.getCellRenderer();
		//rendi.setCurrentFont(new Font("Algerian",Font.PLAIN,25));
	}

	private void buildPlTree() {
		//mainframe=this;
		logger.info("start buildPlTree...");
		boolean expandPlaylists = false;
		boolean expandIntPlaylists = false;
		if (!(pathToPlaylists == null)) {
			if (pl_tree.isExpanded(pathToPlaylists)) {
				expandPlaylists = true;
			}
		}
		if (!(pathToIntPlaylists == null)) {
			if (pl_tree.isExpanded(pathToIntPlaylists)) {
				expandIntPlaylists = true;
			}
		}
		// pl_tree.
		try {
			playlists = ps.getAllPlaylists();
		} catch (DataAccessException e1) {
		}
		try {
			pl_tree.setModel(new DefaultTreeModel(new PlaylistTreeNode(
					new TreeNodeObject("mp3player", v3)) {
				/**
				 * mp3@player
				 */
				private static final long serialVersionUID = -7228695694680777407L;

				{
					PlaylistTreeNode node_1;
					Playlist lib = ps.getLibrary();
					add(new PlaylistTreeNode(new TreeNodeObject(lib.toString(),
							library_icon), false, lib));
					// node_1 = new PlaylistTreeNode(ps.getLibrary().toString(),
					// false, ps.getLibrary());
					PlaylistTreeNode node_2 = new PlaylistTreeNode(
							new TreeNodeObject("Playlists", yellowPLS));
					Playlist current = null;
					ListIterator<WritablePlaylist> iter = playlists
							.listIterator();

					while (iter.hasNext()) {
						current = iter.next();
						node_2.add(new PlaylistTreeNode(new TreeNodeObject(
								current.getTitle(), yellowPL), false, current));
					}
					add(node_2);

					PlaylistTreeNode node_3 = new PlaylistTreeNode(
							new TreeNodeObject("Intelligent Playlists", bluePLS));
					Playlist toprated = ps.getTopRated();
					node_3.add(new PlaylistTreeNode(new TreeNodeObject(toprated
							.getTitle(), bluePL), false, toprated));
					Playlist topplayed = ps.getTopPlayed();
					node_3.add(new PlaylistTreeNode(new TreeNodeObject(
							topplayed.getTitle(), bluePL), false, topplayed));
					/*
					 * node_1.add(new PlaylistTreeNode(
					 * ps.getTopRated().getTitle(), false, ps .getTopRated()));
					 * node_1.add(new PlaylistTreeNode(ps.getTopPlayed()
					 * .getTitle(), false, ps.getTopPlayed()));
					 */
					add(node_3);
					pl_tree.setRowHeight(20);
					PlaylistTreeCellRenderer pl_renderer = new PlaylistTreeCellRenderer(mainframe);
					pl_tree.setCellRenderer(pl_renderer);

					pathToPlaylists = getNodePath(node_2);
					pathToIntPlaylists = getNodePath(node_3);
				}
			}));
			if (expandPlaylists) {
				pl_tree.expandPath(pathToPlaylists);
			}
			if (expandIntPlaylists) {
				pl_tree.expandPath(pathToIntPlaylists);
			}
			// expandAll(pl_tree, true);
		} catch (DataAccessException e) {
			JOptionPane.showConfirmDialog(null, e.getMessage(),
					"Error", JOptionPane.CLOSED_OPTION);
		}
	}

	// Returns a TreePath containing the specified node.
	public TreePath getNodePath(PlaylistTreeNode node) {
		List list = new ArrayList();

		// Add all nodes to list
		while (node != null) {
			list.add(node);
			node = (PlaylistTreeNode) node.getParent();
		}
		Collections.reverse(list);

		// Convert array of nodes to TreePath
		return new TreePath(list.toArray());
	}

	/*
	 * // If expand is true, expands all nodes in the tree. // Otherwise,
	 * collapses all nodes in the tree. public void expandAll(JTree tree,
	 * boolean expand) { PlaylistTreeNode root =
	 * (PlaylistTreeNode)tree.getModel().getRoot(); // Traverse tree from root
	 * expandAll(tree, new TreePath(root), expand); } private void
	 * expandAll(JTree tree, TreePath parent, boolean expand) { // Traverse
	 * children PlaylistTreeNode node =
	 * (PlaylistTreeNode)parent.getLastPathComponent(); if (node.getChildCount()
	 * >= 0) { for (Enumeration e=node.children(); e.hasMoreElements(); ) {
	 * PlaylistTreeNode n = (PlaylistTreeNode)e.nextElement(); TreePath path =
	 * parent.pathByAddingChild(n); expandAll(tree, path, expand); } }
	 * 
	 * // Expansion or collapse must be done bottom-up if (expand) {
	 * tree.expandPath(parent); } else { tree.collapsePath(parent); } }
	 */
	public void setVisibleColumns() {
		HashMap<String, Integer> Zuordnung = new HashMap<String, Integer>();
		String[] columnsAll = new String[SettingsService.SongTableColumnsAll.length + 1];

		columnsAll[0] = "Status";
		for (int i = 0; i < SettingsService.SongTableColumnsAll.length; i++) {
			columnsAll[i + 1] = SettingsService.SongTableColumnsAll[i];
		}

		for (int i = 0; i < columnsAll.length; i++) {
			cTableModel.setColumnVisible(i, false);
			Zuordnung.put(columnsAll[i], i);
		}

		/*
		 * for(int i= 0; i < ss.getUserColumns().length; i++) {
		 * cTableModel.setColumnVisible((Integer)
		 * Zuordnung.get(ss.getUserColumns()[i]), false);
		 * System.out.println(ss.getUserColumns()[i]);
		 * System.out.println((Integer) Zuordnung.get(ss.getUserColumns()[i]));
		 * }
		 */

		String[] userCols = ss.getUserColumns();
		String[] songTableCols = new String[userCols.length + 1];
		songTableCols[0] = "Status";

		for (int i = 0; i < userCols.length; i++) {
			songTableCols[i + 1] = userCols[i];
		}
		boolean color = false;
		for (int i = 0; i < songTableCols.length; i++) {
			if (Zuordnung.containsKey(songTableCols[i])) {
				// System.out.println(Zuordnung.get(col[i]));
				// System.out.println(ss.getUserColumns()[i]);
				cTableModel.setColumnVisible(
						(Integer) Zuordnung.get(songTableCols[i]), true);
			}

		}

		for (int i = 0; i < songTableCols.length; i++) {
			//logger.info("TableRenderer: " + currentPlaylistGUI.getTitle());

			cTableModel.getColumn(i).setCellRenderer(songrendi);
			// cTableModel.getColumn(i).setCellRenderer(new
			// SongTableRenderer(currentPlaylistGUI));
			if (Zuordnung.containsKey("Rating")) {
				cTableModel.getColumn(i).setCellEditor(new SongCellEditor());

			}
			// cTableModel.getColumnByModelIndex(7).
			// songTable.setEditingColumn(i);
			// cTableModel.getColumn(i).set
			/*
			 * if (color)
			 * 
			 * cTableModel.getColumn(i).setCellRenderer( new
			 * SongTableRenderer()); color = !color;
			 */
		}

		// TODO: Add reloading for "TopXX played" and "TopXX rated", if
		// selected
	}

	/**
	 * Starts the MainFrame
	 */
	public MainFrame() {
		mainframe=this;
		ServiceFactory sf = ServiceFactory.getInstance();
		cis = sf.getCoreInteractionService();
		ps = sf.getPlaylistService();
		ss = sf.getSettingsService();
		sis = sf.getSongInformationService();
		songrendi = new SongTableRenderer(
				currentPlaylistGUI);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dim = toolkit.getScreenSize();

		width = 1160;
		height = 700;
		positionX = (int) Math.round(dim.getWidth() / 2 - width / 2);
		positionY = (int) Math.round(dim.getHeight() / 2 - height / 2);

		setBounds(positionX, positionY, width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("mp3@player");

		initialize();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				currentPlaylistGUI = parseSongTable(currentPlaylistGUI);
				try {
					if (currentPlaylistGUI.getClass() == WritablePlaylist.class)
						ps.updatePlaylist((WritablePlaylist) currentPlaylistGUI);
				} catch (DataAccessException a) {
				}
			}
		});
		getWholeLibrary();

		setVisibleColumns();

		cis.setVolume(volume.getValue());
		cis.setPlayMode(PlayMode.NORMAL);

		// setExtendedState(JFrame.MAXIMIZED_BOTH);

		setVisible(true);
		// setResizable(false);

		logger.info("Components successfully initialized");

		// TODO for Johannes: test for SimilarArtist

		// ArrayList<Song> list = new ArrayList<Song>();
		// list.add((Song) songTable.getValueAt(0, 0));
		// list.add((Song) songTable.getValueAt(15, 0));
		// new SimilarArtist(list);

	}

	public MainFrame(Playlist list) {
		currentPlaylistGUI = list;
	}

	public MainFrame(String command) {
		if (command == "reloadsongTable")
			fillSongTable(currentPlaylistGUI);
	}

	public MainFrame(Playlist list, String command) {
		currentPlaylistGUI = list;
		if (command == "reloadsongTable")
			fillSongTable(currentPlaylistGUI);
	}

	/**
	 * Initializes the contents of the frame
	 */
	private void initialize() {

		try {
			initIcons();
		} catch (IOException e2) {
			logger.error(e2);
		}
		// Resizing MainFrame
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				/*
				 * System.out.println("----------Start----------"); Thread
				 * progressThread = new Thread() { public void run() {
				 * System.out.println("Thread: "+getWidth()+"");
				 * progress.setPreferredSize(new Dimension(getWidth(), 25));
				 * System
				 * .out.println("Thread progress: "+progress.getPreferredSize
				 * ()+""); System.out.println("-----------End-----------"); } };
				 * progressThread.start();
				 * System.out.println("Main: "+getWidth()+"");
				 */
				progress.setPreferredSize(new Dimension(getWidth(), 25));
				// System.out.println("Main progress: "+progress.getPreferredSize()+"");
				// setResizable(false);
				Rectangle rv = getBounds();
				positionX = rv.x;
				positionY = rv.y;
				width = rv.width;
				height = rv.height;
				logger.info(rv);
			}
		});

		// MainFrame state changing
		addWindowStateListener(new WindowStateListener() {
			public void windowStateChanged(WindowEvent arg0) {
				progress.setPreferredSize(new Dimension(getWidth(), 25));
				// setResizable(false);
			}
		});

		// add Hotkey Strg+F for GlobalSearch
		// KeyboardFocusManager kbfm =
		// KeyboardFocusManager.getCurrentKeyboardFocusManager();
		// kbfm.addKeyEventDispatcher(new KeyboardManager());

		addKeyListener(this);

		/*
		 * addKeyListener(new KeyAdapter() {
		 * 
		 * @Override public void keyPressed(KeyEvent e) { int key =
		 * e.getKeyCode(); if (key == KeyEvent.VK_ENTER) {
		 * logger.info("MainFrame(): Pressed Enter"); } } });
		 */

		/**
		 * JPanels
		 */
		// playerPanel
		Image image = null;
		try {
			image = ImageIO.read(new ClassPathResource("img/background.png")
					.getFile());
		} catch (IOException e1) {
		}
		JPanel playerPanel = new ImagePanel(new MigLayout("",
				"[210!][grow][200!][]", "[][grow][center][][]"), image);
		getContentPane().add(playerPanel);

		/**
		 * JLabels
		 */
		// lblHeader
		lblHeader = new JLabel("mp3@player");
		playerPanel.add(lblHeader, "cell 0 0");

		// lblCurrentStateSong
		lblCurrentStateSong = new JLabel("");
		playerPanel.add(lblCurrentStateSong,
				"cell 1 3,alignx left,aligny center");

		/**
		 * JTrees
		 */
		// pl_tree
		/*
		 * JMenuItem treeentry1 = new JMenuItem("Delete Playlist");
		 * treePopupMenu.add(treeentry1); treeentry1.addActionListener(new
		 * TreeActionAdapter()); treeentry1.setActionCommand("deletePlaylist");
		 * 
		 * JMenuItem treeentry2 = new JMenuItem("Rename Playlist");
		 * treePopupMenu.add(treeentry2); treeentry2.addActionListener(new
		 * TreeActionAdapter()); treeentry2.setActionCommand("renamePlaylist");
		 */
		buildPlTree();

		/*
		 * try { try { playlists = ps.getAllPlaylists(); } catch
		 * (DataAccessException e1) { } pl_tree.setModel(new
		 * DefaultTreeModel(new PlaylistTreeNode( "mp3player") {
		 *//**
		 * mp3@player
		 */
		/*
		 * private static final long serialVersionUID = -7228695694680777407L;
		 * 
		 * { PlaylistTreeNode node_1; add(new
		 * PlaylistTreeNode(ps.getLibrary().toString(), false,
		 * ps.getLibrary())); WritablePlaylist qu = new
		 * WritablePlaylist("Queue"); add(new PlaylistTreeNode("Queue", false,
		 * qu));
		 * 
		 * // node_1 = new PlaylistTreeNode(ps.getLibrary().toString(), //
		 * false, ps.getLibrary()); node_1 = new PlaylistTreeNode("Playlists");
		 * 
		 * WritablePlaylist current = null; ListIterator<WritablePlaylist> iter
		 * = playlists.listIterator();
		 * 
		 * while (iter.hasNext()) { current = iter.next(); node_1.add(new
		 * PlaylistTreeNode(current.getTitle(), false, current)); } add(node_1);
		 * 
		 * node_1 = new PlaylistTreeNode("Intelligent Playlists");
		 * node_1.add(new PlaylistTreeNode("TopRated"));
		 * 
		 * node_1.add(new PlaylistTreeNode( ps.getTopRated().getTitle(), false,
		 * ps.getTopRated())); node_1.add(new PlaylistTreeNode(ps.getTopPlayed()
		 * .getTitle(), false, ps.getTopPlayed()));
		 * 
		 * 
		 * add(node_1);
		 * 
		 * } })); } catch (DataAccessException e1) { }
		 */
		pl_tree.setEditable(false);
		pl_tree.setVisibleRowCount(5);
		JScrollPane pl_tree_sp = new JScrollPane(pl_tree);
		pl_tree.setDragEnabled(false);
		pl_tree.setTransferHandler(new JTreeSongTransferHandler(ps));

		pl_tree.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				doPlaylistClicked(me);
			}
		});

		MouseListener treePopupListener = new treePopupListener();
		// add the listener specifically to the header
		pl_tree.addMouseListener(treePopupListener);

		// playerPanel.add(pl_tree_sp, "cell 0 1,grow");

		/**
		 * JTables
		 */
		// songTable
		/*
		 * JMenuItem menuItem = new JMenuItem("");
		 * menuItem.addActionListener(new InsertRowsActionAdapter(this));
		 * tablePopupMenu.add(menuItem);
		 */

		/*
		 * JMenuItem entry = new JMenuItem("Delete Song");
		 * tablePopupMenu.add(entry); entry.addActionListener(new
		 * TableActionAdapter()); entry.setActionCommand("deleteSong");
		 * 
		 * Separator sep = new JPopupMenu.Separator(); tablePopupMenu.add(sep);
		 * 
		 * JMenuItem entry1 = new JMenuItem("Get Lyrics");
		 * tablePopupMenu.add(entry1); entry1.addActionListener(new
		 * TableActionAdapter()); entry1.setActionCommand("getLyrics");
		 * 
		 * JMenuItem entry2 = new JMenuItem("Edit Lyrics");
		 * tablePopupMenu.add(entry2); entry2.addActionListener(new
		 * TableActionAdapter()); entry2.setActionCommand("editLyrics");
		 * 
		 * JMenuItem entry3 = new JMenuItem("Get Meta-Tags");
		 * tablePopupMenu.add(entry3); entry3.addActionListener(new
		 * TableActionAdapter()); entry3.setActionCommand("getMetatags");
		 * 
		 * JMenuItem entry4 = new JMenuItem("Edit Meta-Tags");
		 * tablePopupMenu.add(entry4); entry4.addActionListener(new
		 * TableActionAdapter()); entry4.setActionCommand("editMetatags");
		 */

		songTable = new JTable(songmodel);
		// songTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane songTable_sp = new JScrollPane(songTable);
		songTable_sp
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		songTable_sp
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		songTable.getTableHeader().setReorderingAllowed(false);
		songTable
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		songTable.setDragEnabled(true);
		songTable.setTransferHandler(new JTableSongTransferHandler());
		songTable.setRowSelectionAllowed(true);

		songTable.setSelectionBackground(new Color(255, 0, 0));
		jsplit = new JSplitPane();
		jsplit.setLeftComponent(pl_tree_sp);
		jsplit.setRightComponent(songTable_sp);

		// playerPanel.add(songTable_sp, "cell 1 1 3 1,grow");
		playerPanel.add(jsplit, "cell 0 1 4 1 ,grow");
		// songTable.setAutoCreateRowSorter(true);
		songTable.getModel().addTableModelListener(this);
		cTableModel = new HidableTableColumnModel(songTable.getColumnModel());

		sorter = new TableRowSorter<TableModel>();
		songTable.setRowSorter(sorter);
		sorter.setModel(songmodel);
		songTable.setRowHeight(30);
		sorter.addRowSorterListener(this);

		/*
		 * JPopupMenu popup = new JPopupMenu("Hide Menu"); Action[] actions =
		 * cTableModel.createColumnActions(); for (Action act : actions) {
		 * popup.add(new JCheckBoxMenuItem(act)); }
		 * songTable.setComponentPopupMenu(popup);
		 */
		songTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row = songTable.getSelectedRow();

					if (row > -1) {
						play(row);
					}
				}
			}
		});

		MouseListener popupListener = new PopupListener();
		// add the listener specifically to the header
		songTable.addMouseListener(popupListener);
		songTable.getTableHeader().addMouseListener(popupListener);
		songTable.addKeyListener(this);

		/**
		 * JButtons
		 */

		// Previous
		btnPrevious = new RectButton(l1, l2, l3);
		playerPanel
				.add(btnPrevious, "cell 0 3 1 2,alignx center,aligny center");
		btnPrevious.addActionListener(this);
		btnPrevious.setActionCommand("previous");

		// Play_Pause
		btnPlayPause = new RoundButton(m1, m2, m3);
		playerPanel.add(btnPlayPause,
				"cell 0 3 1 2,alignx center,aligny center");
		btnPlayPause.addActionListener(this);
		btnPlayPause.setActionCommand("play");

		// Next
		btnNext = new RectButton(r1, r2, r3);
		playerPanel.add(btnNext, "cell 0 3 1 2,alignx center,aligny center");
		btnNext.addActionListener(this);
		btnNext.setActionCommand("next");

		/**
		 * JSliders
		 */
		// ProgressBar
		progress = new JSlider(0, 100, 0);
		progress.setOpaque(false);
		progress.setMajorTickSpacing(100);
		progress.setMinorTickSpacing(1);
		progress.setPaintTicks(true);
		progress.setSnapToTicks(false);
		progress.putClientProperty("JSlider.isFilled", Boolean.TRUE);

		/*
		 * progress.addChangeListener(new ChangeListener() {
		 * 
		 * @Override public void stateChanged(ChangeEvent ce) { JSlider source =
		 * (JSlider) ce.getSource(); setMediaTime(source.getValue());
		 * 
		 * }
		 * 
		 * });
		 */

		progress.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent evt) {
				setMediaTime(progress.getValue());
			}

			public void mouseClicked(MouseEvent evt) {
				setMediaTime(progress.getValue());
			}

			public void mouseDragged(MouseEvent evt) {
				setMediaTime(progress.getValue());
			}
		});

		progress.addMouseMotionListener(new MouseMotionListener() {

			public void mouseMoved(MouseEvent e) {

			}

			public void mouseDragged(MouseEvent e) {
				setMediaTime(progress.getValue());
				// System.out.println(progress.getValue());
			}
		});

		/*
		 * progress.addChangeListener(new ChangeListener() { public void
		 * stateChanged(ChangeEvent e) { setMediaTime(progress.getValue()); }
		 * });
		 */

		cis.setPlayerListener(new PlayerListener() {

			public void songBeginnEvent() {
				//logger.info("Beginn");

				if (fred == null || fred.isAlive() == false) {
					createThread();

				}
				
				// new MainFrame("reloadsongTable");
				fillSongTable(currentPlaylistGUI);			
				songTable.repaint();
			//	logger.info("Test: " + songTable.getValueAt(2, 7));
				songTable.setRowSelectionInterval(cis.getCurrentSongIndex(), cis.getCurrentSongIndex());
				if (cis.isPlaying()) {
					lblCurrentStateSong.setText("Currently playing: "
							+ cis.getCurrentSong().getArtist() + " - "
							+ cis.getCurrentSong().getTitle() + "");
					btnPlayPause.setActionCommand("pause");
					setPauseIcons();
				}

			}

			public void songEndEvent() {

				if (fred != null) {
					fred.interrupt();
				}
				fillSongTable(currentPlaylistGUI);
				songTable.repaint();
				songTable.setRowSelectionInterval(cis.getCurrentSongIndex(), cis.getCurrentSongIndex());
				// new MainFrame("reloadsongTable");
				// fillSongTable(currentPlaylistGUI);
			}

		});

		progress.setPreferredSize(new Dimension(getWidth(), 25));
		playerPanel.add(progress,
				"flowx,cell 0 2 4 1,alignx left,aligny center");
		progress.setEnabled(false);

		lblPlayedTime = new JLabel("");
		playerPanel.add(lblPlayedTime, "cell 1 4,alignx left, aligny center");
		lblPlayedTime.setVisible(false);

		// lblDurationSeperator
		lblDurationSeperator = new JLabel("/");
		playerPanel.add(lblDurationSeperator,
				",cell 1 4,alignx left, aligny center");
		lblDurationSeperator.setVisible(false);

		// lblDuration
		// lblDuration = new JLabel(getMediaTime());
		lblDuration = new JLabel("");
		playerPanel.add(lblDuration, ",cell 1 4,alignx left, aligny center");
		lblDuration.setVisible(false);

		// lblVolume
		lblVolume = new JLabel("Volume:");
		playerPanel.add(lblVolume, "cell 2 3 1 2,alignx right,aligny center");

		// Volume
		volume = new JSlider(0, CoreInteractionService.MAX_VOLUME, 50);
		volume.setMajorTickSpacing(25);
		volume.setMinorTickSpacing(5);
		volume.setSnapToTicks(false);
		volume.setOpaque(false);
		volume.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				setVol(volume.getValue());
			}
		});
		volume.setPreferredSize(new Dimension(100, 25));
		playerPanel.add(volume, "cell 2 3 1 2,alignx right,aligny center");

		/**
		 * JCheckboxes
		 */
		// Mute
		chckbxMute = new JCheckBox("");
		chckbxMute.setOpaque(false);
		chckbxMute.setContentAreaFilled(false);
		chckbxMute.setBorderPainted(false);
		chckbxMute.setFocusPainted(false);
		playerPanel.add(chckbxMute, "cell 3 3 1 2,alignx right,aligny center");
		chckbxMute.setIcon(nomute_icon);
		chckbxMute.setRolloverIcon(nomuterollover_icon);
		chckbxMute.setPressedIcon(nomutepressed_icon);
		chckbxMute.setSelectedIcon(mute_icon);
		chckbxMute.setRolloverSelectedIcon(muterollover_icon);
		chckbxMute.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				setMute();
			}
		});

		// Repeat
		chckbxRepeat = new JCheckBox("");
		chckbxRepeat.setOpaque(false);
		chckbxRepeat.setContentAreaFilled(false);
		chckbxRepeat.setBorderPainted(false);
		chckbxRepeat.setFocusPainted(false);
		chckbxRepeat.setIcon(norepeat_icon);
		chckbxRepeat.setRolloverIcon(norepeatrollover_icon);
		chckbxRepeat.setPressedIcon(norepeatpressed_icon);
		chckbxRepeat.setSelectedIcon(repeat_icon);
		chckbxRepeat.setRolloverSelectedIcon(repeatrollover_icon);
		playerPanel
				.add(chckbxRepeat, "cell 3 3 1 2,alignx right,aligny center");

		chckbxRepeat.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (chckbxShuffle.isSelected() && chckbxRepeat.isSelected())
					chckbxShuffle.setSelected(false);
				setMode();
			}
		});

		// Shuffle
		chckbxShuffle = new JCheckBox("");
		chckbxShuffle.setOpaque(false);
		chckbxShuffle.setContentAreaFilled(false);
		chckbxShuffle.setBorderPainted(false);
		chckbxShuffle.setFocusPainted(false);
		chckbxShuffle.setIcon(noshuffle_icon);
		chckbxShuffle.setRolloverIcon(noshufflerollover_icon);
		chckbxShuffle.setPressedIcon(noshufflepressed_icon);
		chckbxShuffle.setSelectedIcon(shuffle_icon);
		chckbxShuffle.setRolloverSelectedIcon(shufflerollover_icon);
		playerPanel.add(chckbxShuffle,
				"cell 3 3 1 2,alignx right,aligny center");

		chckbxShuffle.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (chckbxRepeat.isSelected() && chckbxShuffle.isSelected())
					chckbxRepeat.setSelected(false);
				setMode();
			}
		});

		/**
		 * The whole MenuBar
		 */
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenu mnLibrary = new JMenu("Library");
		mnFile.add(mnLibrary);

		JMenuItem mntmAddFiles = new JMenuItem("Add Files...");
		mntmAddFiles.addActionListener(this);
		mntmAddFiles.setActionCommand("addfile");
		mnLibrary.add(mntmAddFiles);

		JMenuItem mntmAddFolders = new JMenuItem("Add Folder...");
		mnLibrary.add(mntmAddFolders);
		mntmAddFolders.addActionListener(this);
		mntmAddFolders.setActionCommand("addfolder");

		JSeparator separator_1 = new JSeparator();
		mnLibrary.add(separator_1);

		JMenuItem mntmCheckSongpaths = new JMenuItem("Check songpaths...");
		mnLibrary.add(mntmCheckSongpaths);
		mntmCheckSongpaths.addActionListener(this);
		mntmCheckSongpaths.setActionCommand("checksongpaths");

		JMenu mnPlaylist = new JMenu("Playlist");
		mnFile.add(mnPlaylist);

		JMenuItem mntmNew = new JMenuItem("New...");
		mnPlaylist.add(mntmNew);
		mntmNew.addActionListener(this);
		mntmNew.setActionCommand("newplaylist");

		JSeparator separator_2 = new JSeparator();
		mnPlaylist.add(separator_2);

		JMenuItem mntmImport = new JMenuItem("Import...");
		mnPlaylist.add(mntmImport);
		mntmImport.addActionListener(this);
		mntmImport.setActionCommand("importplaylist");

		JMenuItem mntmExport = new JMenuItem("Export...");
		mnPlaylist.add(mntmExport);
		mntmExport.addActionListener(this);
		mntmExport.setActionCommand("exportplaylist");

		JMenuItem mntmSearch = new JMenuItem("Search...");
		mnFile.add(mntmSearch);
		mntmSearch.addActionListener(this);
		mntmSearch.setActionCommand("mntmsearch");

		JMenuItem mntmSettings = new JMenuItem("Settings");
		mnFile.add(mntmSettings);
		mntmSettings.addActionListener(this);
		mntmSettings.setActionCommand("settings");

		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		mntmExit.addActionListener(this);
		mntmExit.setActionCommand("exit");

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About...");
		mnHelp.add(mntmAbout);
		mntmAbout.addActionListener(this);
		mntmAbout.setActionCommand("about");
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("previous")) {
			previous();
		}

		else if (e.getActionCommand().equals("play")) {
			int row = this.songTable.getSelectedRow();

			if (row > -1) {
				play(row);
			}
		}

		else if (e.getActionCommand().equals("pauseplay")) {
			pauseplay();
		}

		else if (e.getActionCommand().equals("pause")) {
			try {
				pause();
			} catch (InterruptedException e1) {
				logger.error(e1);
			}
		}

		else if (e.getActionCommand().equals("next")) {
			next();
		}

		else if (e.getActionCommand().equals("addfile")) {
			librarygui = new LibraryGUI();
			librarygui.addFile();
			buildPlTree();
			try {
				Playlist list = ps.getLibrary();
				fillSongTable(list);
				currentPlaylistGUI = list;
			} catch (DataAccessException e1) {
				JOptionPane.showConfirmDialog(null, e1.getMessage(), "Error",
						JOptionPane.CLOSED_OPTION);
			}
		}

		else if (e.getActionCommand().equals("addfolder")) {
			librarygui = new LibraryGUI();
			librarygui.addFolder();
			buildPlTree();
			try {
				Playlist list = ps.getLibrary();
				fillSongTable(list);
				currentPlaylistGUI = list;
			} catch (DataAccessException e1) {
				JOptionPane.showConfirmDialog(null, e1.getMessage(), "Error",
						JOptionPane.CLOSED_OPTION);
			}
		}

		else if (e.getActionCommand().equals("importplaylist")) {
			playlistgui = new PlaylistGUI();
			playlistgui.importPlaylist();
			buildPlTree();
			try {
				Playlist list = ps.getLibrary();
				fillSongTable(list);
				currentPlaylistGUI = list;
			} catch (DataAccessException e1) {
				JOptionPane.showConfirmDialog(null, e1.getMessage(), "Error",
						JOptionPane.CLOSED_OPTION);
			}

		}

		else if (e.getActionCommand().equals("exportplaylist")) {
			playlistgui = new PlaylistGUI();
			playlistgui.exportPlaylist(currentPlaylistGUI);
		}

		else if (e.getActionCommand().equals("newplaylist")) {
			playlistgui = new PlaylistGUI();
			playlistgui.newPlaylist();
			buildPlTree();
		}

		else if (e.getActionCommand().equals("mntmsearch")) {
			new GlobalSearch();
			fillSongTable(currentPlaylistGUI);
		}

		else if (e.getActionCommand().equals("checksongpaths")) {
			new checkSongPathGUI();
			buildPlTree();
			try {
				Playlist list = ps.getLibrary();
				fillSongTable(list);
				currentPlaylistGUI = list;
			} catch (DataAccessException e1) {
				JOptionPane.showConfirmDialog(null, e1.getMessage(), "Error",
						JOptionPane.CLOSED_OPTION);
			}
		}

		else if (e.getActionCommand().equals("settings")) {
			new Settings();
			setVisibleColumns();

			/*
			 * HashMap Zuordnung = new HashMap();
			 * 
			 * for (int i = 0; i < 9; i++) { cTableModel.setColumnVisible(i,
			 * false);
			 * 
			 * }
			 * 
			 * Zuordnung.put("", 0); Zuordnung.put("title", 1);
			 * Zuordnung.put("artist", 2); Zuordnung.put("album", 3);
			 * Zuordnung.put("year", 4); Zuordnung.put("genre", 5);
			 * Zuordnung.put("duration", 6); Zuordnung.put("rating", 7);
			 * Zuordnung.put("playcount", 8);
			 * 
			 * 
			 * for(int i= 0; i < ss.getUserColumns().length; i++) {
			 * cTableModel.setColumnVisible((Integer)
			 * Zuordnung.get(ss.getUserColumns()[i]), false);
			 * System.out.println(ss.getUserColumns()[i]);
			 * System.out.println((Integer)
			 * Zuordnung.get(ss.getUserColumns()[i])); }
			 * 
			 * 
			 * String[] userCols = ss.getUserColumns(); String[] songTableCols =
			 * new String[userCols.length + 1]; songTableCols[0] = "";
			 * 
			 * for (int i = 0; i < userCols.length; i++) { songTableCols[i+1] =
			 * userCols[i]; }
			 * 
			 * for (int i = 0; i < songTableCols.length; i++) { if
			 * (Zuordnung.containsKey(songTableCols[i])) //
			 * System.out.println(Zuordnung.get(col[i])); //
			 * System.out.println(ss.getUserColumns()[i]);
			 * cTableModel.setColumnVisible( (Integer)
			 * Zuordnung.get(songTableCols[i]), true); }
			 * 
			 * // TODO: Add reloading for "TopXX played" and "TopXX rated", if
			 * // selected
			 */}

		else if (e.getActionCommand().equals("about")) {
			logger.info("About: Going to show About dialog");
			JOptionPane.showConfirmDialog(null,
					"mp3@Player Beta Version by SEPM team VVV",
					"About mp3@Player...", JOptionPane.CLOSED_OPTION);
		}

		else if (e.getActionCommand().equals("exit")) {
			logger.info("Application is going terminate... Have a nice day :)");
			System.exit(0);
		}
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			// logger.info("MainFrame(): Pressed Enter");
			int row = this.songTable.getSelectedRow();

			if (row > -1) {
				play(row);
			}
		}

	}

	public void keyReleased(KeyEvent e) {

	}

	public void keyTyped(KeyEvent e) {

	}

	class PopupListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			showPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			showPopup(e);
		}

		private void showPopup(MouseEvent e) {
			JPopupMenu tablePopupMenu = new JPopupMenu();
			JMenuItem entry = new JMenuItem("Delete Song");
			tablePopupMenu.add(entry);
			entry.addActionListener(new TableActionAdapter());
			entry.setActionCommand("deleteSong");

			if (currentPlaylistGUI.getTitle().equals("Library")
					|| currentPlaylistGUI.getTitle().equals("TopRated")
					|| currentPlaylistGUI.getTitle().equals("TopPlayed")
					|| currentPlaylistGUI.getTitle().equals("Queue")) {
				entry.setEnabled(false);
			}

			Separator sep = new JPopupMenu.Separator();
			tablePopupMenu.add(sep);

			JMenuItem entry1 = new JMenuItem("Get Lyrics");
			tablePopupMenu.add(entry1);
			entry1.addActionListener(new TableActionAdapter());
			entry1.setActionCommand("getLyrics");

			JMenuItem entry2 = new JMenuItem("Edit Lyrics");
			tablePopupMenu.add(entry2);
			entry2.addActionListener(new TableActionAdapter());
			entry2.setActionCommand("editLyrics");

			JMenuItem entry3 = new JMenuItem("Get Meta-Tags");
			tablePopupMenu.add(entry3);
			entry3.addActionListener(new TableActionAdapter());
			entry3.setActionCommand("getMetatags");

			JMenuItem entry4 = new JMenuItem("Edit Meta-Tags");
			tablePopupMenu.add(entry4);
			entry4.addActionListener(new TableActionAdapter());
			entry4.setActionCommand("editMetatags");
			if (e.isPopupTrigger()) {
				tablePopupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	class treePopupListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			showPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			showPopup(e);
		}

		private void showPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				try {
					JTree tree = null;
					tree = (JTree) e.getComponent();
					Playlist clickedPlaylist = null;
					PlaylistTreeNode clickedNode = null;
					if (tree != null) {
						TreePath path = tree.getPathForLocation(e.getX(),
								e.getY());

						clickedNode = (PlaylistTreeNode) path
								.getLastPathComponent();
						if (!clickedNode.isLeaf()) {
							throw new NullPointerException();
						} else {
							clickedPlaylist = clickedNode.getNodePlaylist();
						}

					}

					JPopupMenu treePopupMenu = new JPopupMenu();
					JMenuItem treeentry1 = new JMenuItem("Delete Playlist");
					treePopupMenu.add(treeentry1);
					treeentry1.addActionListener(new TreeActionAdapter());
					treeentry1.setActionCommand("deletePlaylist");

					if (clickedPlaylist != null) {
						if (clickedPlaylist.getTitle().equals("Library")
								|| clickedPlaylist.getTitle()
										.equals("TopRated")
								|| clickedPlaylist.getTitle().equals(
										"TopPlayed")
								|| clickedPlaylist.getTitle().equals("Queue")) {
							treeentry1.setEnabled(false);
						}
					}

					JMenuItem treeentry2 = new JMenuItem("Rename Playlist");
					treePopupMenu.add(treeentry2);
					treeentry2.addActionListener(new TreeActionAdapter());
					treeentry2.setActionCommand("renamePlaylist");

					if (clickedPlaylist != null) {
						if (clickedPlaylist.getTitle().equals("Library")
								|| clickedPlaylist.getTitle()
										.equals("TopRated")
								|| clickedPlaylist.getTitle().equals(
										"TopPlayed")
								|| clickedPlaylist.getTitle().equals("Queue")) {
							treeentry2.setEnabled(false);
						}
					}
					
					JMenuItem treeentry3 = new JMenuItem("Export Playlist");
					treePopupMenu.add(treeentry3);
					treeentry3.addActionListener(new TreeActionAdapter());
					treeentry3.setActionCommand("exportPlaylist");

					if (clickedPlaylist != null) {
						if (clickedPlaylist.getTitle().equals("Library")) {
							treeentry3.setEnabled(false);
						}
					}
					treePopupMenu.show(e.getComponent(), e.getX(), e.getY());
				} catch (NullPointerException ex) {
				}
			}
		}
	}

	class TableActionAdapter implements ActionListener {

		TableActionAdapter() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("deleteSong")) {
				int[] row = songTable.getSelectedRows();
				Song x = null;
				ArrayList<Song> deleteSongs = new ArrayList<Song>();
				for (int i = 0; i < row.length; i++) {
					int currentRow = row[i];
					if (currentRow > -1) {
						x = (Song) songTable.getValueAt(currentRow, 0);
						deleteSongs.add(x);
					}
				}

				try {
					ps.deleteSongs(deleteSongs,
							(WritablePlaylist) currentPlaylistGUI);
					currentPlaylistGUI.removeAll(deleteSongs);
				} catch (DataAccessException e1) {
					new DynamicDialog("ERROR", e1.toString());
				}
				fillSongTable(currentPlaylistGUI);
			} else if (e.getActionCommand().equals("getLyrics")) {
				int[] row = songTable.getSelectedRows();
				Song x = null;
				ArrayList<Song> getLyricSongs = new ArrayList<Song>();
				for (int i = 0; i < row.length; i++) {
					int currentRow = row[i];
					if (currentRow > -1) {
						x = (Song) songTable.getValueAt(currentRow, 0);
						getLyricSongs.add(x);
					}
				}
				new GetLyric(getLyricSongs);
				fillSongTable(currentPlaylistGUI);
			}

			else if (e.getActionCommand().equals("editLyrics")) {
				int[] row = songTable.getSelectedRows();
				Song x = null;
				ArrayList<Song> editLyricSongs = new ArrayList<Song>();
				for (int i = 0; i < row.length; i++) {
					int currentRow = row[i];
					if (currentRow > -1) {
						x = (Song) songTable.getValueAt(currentRow, 0);
						editLyricSongs.add(x);
					}
				}
				new EditLyric(editLyricSongs);
				fillSongTable(currentPlaylistGUI);
			}

			else if (e.getActionCommand().equals("getMetatags")) {
				int[] row = songTable.getSelectedRows();
				Song x = null;
				ArrayList<Song> getMetaSongs = new ArrayList<Song>();
				for (int i = 0; i < row.length; i++) {
					int currentRow = row[i];
					if (currentRow > -1) {
						x = (Song) songTable.getValueAt(currentRow, 0);
						getMetaSongs.add(x);
					}
				}
				new GetMetaTag(getMetaSongs);
				fillSongTable(currentPlaylistGUI);
			}

			else if (e.getActionCommand().equals("editMetatags")) {
				int[] row = songTable.getSelectedRows();
				Song x = null;
				ArrayList<Song> editMetaSongs = new ArrayList<Song>();
				for (int i = 0; i < row.length; i++) {
					int currentRow = row[i];
					if (currentRow > -1) {
						x = (Song) songTable.getValueAt(currentRow, 0);
						editMetaSongs.add(x);
					}
				}
				new EditMetaTag(editMetaSongs);
				fillSongTable(currentPlaylistGUI);
			}

		}
	}

	class TreeActionAdapter implements ActionListener {

		TreeActionAdapter() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("deletePlaylist")) {
				// PlaylistTreeNode selectedNode = (PlaylistTreeNode) pl_tree
				// .getLastSelectedPathComponent();
				TreePath[] treePaths = pl_tree.getSelectionPaths();
				if (treePaths.length != 0) {
					for (int i = 0; i < treePaths.length; i++) {
						TreePath currentPath = treePaths[i];
						PlaylistTreeNode selectedNode = (PlaylistTreeNode) currentPath
								.getLastPathComponent();
						Playlist selectedPlaylist = selectedNode
								.getNodePlaylist();
						try {
							if (selectedPlaylist.getClass() == WritablePlaylist.class)
								ps.deletePlaylist((WritablePlaylist) selectedPlaylist);
						} catch (DataAccessException e1) {
						}
					}
				} else {
					int response = JOptionPane.showConfirmDialog(null,
							"No Playlists chosen!", "No Playlists chosen!",
							JOptionPane.CLOSED_OPTION);
				}
				buildPlTree();

			} else if (e.getActionCommand().equals("renamePlaylist")) {
				// PlaylistTreeNode selectedNode = (PlaylistTreeNode) pl_tree
				// .getLastSelectedPathComponent();
				TreePath[] treePaths = pl_tree.getSelectionPaths();
				if (treePaths.length != 0) {
					for (int i = 0; i < treePaths.length; i++) {
						TreePath currentPath = treePaths[i];
						PlaylistTreeNode selectedNode = (PlaylistTreeNode) currentPath
								.getLastPathComponent();
						Playlist selectedPlaylist = selectedNode
								.getNodePlaylist();
						playlistgui = new PlaylistGUI();
						if (selectedPlaylist.getClass() == WritablePlaylist.class)
							playlistgui
									.renamePlaylistGUI((WritablePlaylist) selectedPlaylist);
					}
				} else {
					int response = JOptionPane.showConfirmDialog(null,
							"No Playlists chosen!", "No Playlists chosen!",
							JOptionPane.CLOSED_OPTION);
				}
				buildPlTree();
			} else if (e.getActionCommand().equals("exportPlaylist")) {
				// PlaylistTreeNode selectedNode = (PlaylistTreeNode) pl_tree
				// .getLastSelectedPathComponent();
				TreePath[] treePaths = pl_tree.getSelectionPaths();
				if (treePaths.length != 0) {
					for (int i = 0; i < treePaths.length; i++) {
						TreePath currentPath = treePaths[i];
						PlaylistTreeNode selectedNode = (PlaylistTreeNode) currentPath
								.getLastPathComponent();
						Playlist selectedPlaylist = selectedNode
								.getNodePlaylist();
						playlistgui = new PlaylistGUI();
						//if (selectedPlaylist.getClass() == WritablePlaylist.class)
						playlistgui.exportPlaylist(selectedPlaylist);
						//renamePlaylistGUI((WritablePlaylist) selectedPlaylist);
					}
				} else {
					int response = JOptionPane.showConfirmDialog(null,
							"No Playlists chosen!", "No Playlists chosen!",
							JOptionPane.CLOSED_OPTION);
				}
				buildPlTree();
			}
		}
	}

	@Override
	public void tableChanged(TableModelEvent e) {

		int column = songTable.getSelectedColumn();
		int row = songTable.getSelectedRow();
		String Rating;
		// logger.info("changed column: " +e.getColumn());
		// logger.info("tableChanged");
		// cis.setCurrentSongIndex(-1);

		// logger.info("Rating changed : " + row );
		// logger.info(column);

		if (e.getColumn() == 7) {

			// logger.info("new Rating " + songTable.getValueAt(row,
			// column).toString());
			Rating = songTable.getValueAt(row, column).toString();

			double rg = Double.parseDouble(Rating);
			// logger.info("Double Rating: " + rg);
			//logger.info("song: " + cis.getCurrentPlaylist().get(row).getTitle());
			try {
				sis.setRating(cis.getCurrentPlaylist().get(row), rg);
			} catch (DataAccessException e1) {
				JOptionPane.showConfirmDialog(null, "No Song fund!",
						"No Song fund!" + e1, JOptionPane.CLOSED_OPTION);

			}
			// cis.getCurrentPlaylist().get(sorter.convertRowIndexToView(row)).setRating(rg);

			// logger.info("new Rating in Songobject: " +
			// cis.getCurrentPlaylist().get(row).getRating());
		} else {
			if (currentPlaylistGUI != null)
				songrendi.setPlaylist(currentPlaylistGUI);
			sorter.setSortKeys(null);
		}

	}

	@Override
	public void sorterChanged(RowSorterEvent e) {
		// logger.info("sorterChanged");
		// cis.getCurrentPlaylist().get(0).setRating(5);
		currentPlaylistGUI = parseSongTable(currentPlaylistGUI);
		cis.setCurrentPlaylist(currentPlaylistGUI);
		if (cis.getCurrentSongIndex() > -1)
			cis.setCurrentSongIndex(sorter.convertRowIndexToView(cis
					.getCurrentSongIndex()));


		songTable.repaint();
	}
}