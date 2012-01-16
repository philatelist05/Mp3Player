package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.CoreInteractionService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlayMode;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlayerListener;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.SettingsService;

public class MainFrame extends JFrame implements ActionListener, Runnable,
		KeyListener, TableModelListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -959319978002415594L;
	private static Logger logger = Logger.getLogger(MainFrame.class);
	private static Playlist currentPlaylistGUI;

	private JTree pl_tree = new JTree();

	private PlaylistGUI playlistgui;
	private LibraryGUI librarygui;
	private JTable songTable;
	private HidableTableColumnModel cTableModel;
	private SongTableModel songmodel = new SongTableModel(new String[] { "",
			"Title", "Artist", "Album", "Year", "Genre", "Duration", "Rating",
			"Playcount" }, 0);
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
	private JPopupMenu tablePopupMenu = new JPopupMenu();
	private JPopupMenu treePopupMenu = new JPopupMenu();

	private Icon l1 = new ImageIcon(getClass().getResource("img/left_blue.png"));
	private Icon l2 = new ImageIcon(getClass().getResource(
			"img/left_orange.png"));
	private Icon l3 = new ImageIcon(getClass().getResource(
			"img/left_orange_pressed.png"));

	private Icon m1 = new ImageIcon(getClass().getResource("img/play_blue.png"));
	private Icon m2 = new ImageIcon(getClass().getResource(
			"img/play_orange.png"));
	private Icon m3 = new ImageIcon(getClass().getResource(
			"img/play_orange_pressed.png"));

	private Icon r1 = new ImageIcon(getClass()
			.getResource("img/right_blue.png"));
	private Icon r2 = new ImageIcon(getClass().getResource(
			"img/right_orange.png"));
	private Icon r3 = new ImageIcon(getClass().getResource(
			"img/right_orange_pressed.png"));

	private Icon mp1 = new ImageIcon(getClass().getResource(
			"img/pause_blue.png"));
	private Icon mp2 = new ImageIcon(getClass().getResource(
			"img/pause_orange.png"));
	private Icon mp3 = new ImageIcon(getClass().getResource(
			"img/pause_orange_pressed.png"));

	private List<Playlist> playlists = null;

	private PlaylistService ps;
	private CoreInteractionService cis;
	private SettingsService ss;

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
		int row = songmodel.getRowCount();

		for (int i = 0; i < row; i++) {
			song = (Song) songTable.getValueAt(i, 0);
			temp.add(song);
		}

		list.setSongs(temp);

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
		for (Song x : list.getSongs()) {
			if (x.getAlbum() != null)
				album = x.getAlbum().getTitle();
			else
				album = "";
			songmodel.addRow(new Object[] { x, x.getTitle(), x.getArtist(),
					album, x.getYear(), x.getGenre(), x.getDuration(),
					x.getRating(), x.getPlaycount() });
		}
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
		if (cis.isPlaying()) {

			btnPlayPause.setActionCommand("play");
			setPlayIcons();
			lblCurrentStateSong.setText("");

			cis.playPrevious();
			Song temp = cis.getCurrentSong();

			// progress.setEnabled(true);
			if (fred == null || fred.isAlive() == false) {
				createThread();

			}
			/*
			 * else fred.start();
			 * 
			 * if (cis.isPlaying()) {
			 * lblCurrentStateSong.setText("Currently playing: " +
			 * temp.getArtist() + " - " + temp.getTitle() + "");
			 * 
			 * btnPlayPause.setActionCommand("pause"); setPauseIcons(); } else
			 * setProgressBartoDefault();
			 */
		}
	}

	/**
	 * Sends the play Song action to the SercviceLayer
	 * 
	 * @param x
	 *            The Song to play
	 */
	public void play(Song x) {
		setPlayIcons();

		cis.playFromBeginning(x);
		cis.setCurrentPlaylist(currentPlaylistGUI);
		Song temp = cis.getCurrentSong();

		progress.setEnabled(true);
		lblPlayedTime.setText(getPlayedTimeInSeconds());
		progress.setVisible(true);

		lblCurrentStateSong.setVisible(true);
		lblPlayedTime.setVisible(true);
		lblDurationSeperator.setVisible(true);
		lblDuration.setVisible(true);
		if (fred == null || fred.isAlive() == false) {
			createThread();

		}

		if (cis.isPlaying()) {
			lblCurrentStateSong.setText("Currently playing: "
					+ temp.getArtist() + " - " + temp.getTitle() + "");
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
		if (fred != null) {
			fred.interrupt();
		}

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
		if (cis.isPlaying()) {
			btnPlayPause.setActionCommand("play");
			setPlayIcons();
			lblCurrentStateSong.setText("");

			cis.playNext();
			// Song temp = cis.getCurrentSong();
			// progress.setEnabled(true);
			if (fred == null || fred.isAlive() == false) {
				createThread();

			}
			/*
			 * else { fred.start(); } if (cis.isPlaying()) {
			 * lblCurrentStateSong.setText("Currently playing: " +
			 * temp.getArtist() + " - " + temp.getTitle() + "");
			 * 
			 * btnPlayPause.setActionCommand("pause"); setPauseIcons(); } else
			 * setProgressBartoDefault();
			 */
		}
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

	private void createThread() {
		fred = new Thread(this);
		fred.start();
	}

	public void run() {

		while (!fred.isInterrupted()) {

			Song temp = cis.getCurrentSong();
			progress.setValue((int) cis.getPlayTime());
			lblPlayedTime.setText(getPlayedTimeInSeconds());
			lblDuration.setText(getMediaTimeAt(100)); // in percent

			if (cis.isPlaying()) {
				lblCurrentStateSong.setText("Currently playing: "
						+ temp.getArtist() + " - " + temp.getTitle() + "");
				btnPlayPause.setActionCommand("pause");
				setPauseIcons();
			}
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
					ps.updatePlaylist(currentPlaylistGUI);
				} catch (DataAccessException e) {
					// TODO: Show error dialog
				}
				currentPlaylistGUI = clicked.getNodePlaylist();
				fillSongTable(clicked.getNodePlaylist());
			} else {
			}
		}
	}

	private void buildPlTree() {
		logger.info("start buildPlTree...");
		try {
			try {
				playlists = ps.getAllPlaylists();
			} catch (DataAccessException e1) {
			}
			pl_tree.setModel(new DefaultTreeModel(new PlaylistTreeNode(
					"mp3player") {
				/**
				 * mp3@player
				 */
				private static final long serialVersionUID = -7228695694680777407L;

				{
					PlaylistTreeNode node_1;
					add(new PlaylistTreeNode(ps.getLibrary().toString(), false,
							ps.getLibrary()));
					Playlist qu = new Playlist("Queue");
					add(new PlaylistTreeNode("Queue", false, qu));

					// node_1 = new PlaylistTreeNode(ps.getLibrary().toString(),
					// false, ps.getLibrary());
					node_1 = new PlaylistTreeNode("Playlists");

					Playlist current = null;
					ListIterator<Playlist> iter = playlists.listIterator();

					while (iter.hasNext()) {
						current = iter.next();
						node_1.add(new PlaylistTreeNode(current.getTitle(),
								false, current));
					}
					add(node_1);

					node_1 = new PlaylistTreeNode("Intelligent Playlists");
					node_1.add(new PlaylistTreeNode("TopRated"));
					/*
					 * node_1.add(new PlaylistTreeNode(
					 * ps.getTopRated().getTitle(), false, ps .getTopRated()));
					 * node_1.add(new PlaylistTreeNode(ps.getTopPlayed()
					 * .getTitle(), false, ps.getTopPlayed()));
					 */

					add(node_1);

				}
			}));
		} catch (DataAccessException e1) {
		}
	}

	public void setVisibleColumns() {
		HashMap Zuordnung = new HashMap();

		for (int i = 0; i < 9; i++) {
			cTableModel.setColumnVisible(i, false);

		}

		Zuordnung.put("status", 0);
		Zuordnung.put("title", 1);
		Zuordnung.put("artist", 2);
		Zuordnung.put("album", 3);
		Zuordnung.put("year", 4);
		Zuordnung.put("genre", 5);
		Zuordnung.put("duration", 6);
		Zuordnung.put("rating", 7);
		Zuordnung.put("playcount", 8);

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
		songTableCols[0] = "status";

		for (int i = 0; i < userCols.length; i++) {
			songTableCols[i + 1] = userCols[i];
		}

		for (int i = 0; i < songTableCols.length; i++) {
			if (Zuordnung.containsKey(songTableCols[i]))
				// System.out.println(Zuordnung.get(col[i]));
				// System.out.println(ss.getUserColumns()[i]);
				cTableModel.setColumnVisible(
						(Integer) Zuordnung.get(songTableCols[i]), true);
		}

		// TODO: Add reloading for "TopXX played" and "TopXX rated", if
		// selected
	}

	/**
	 * Starts the MainFrame
	 */
	public MainFrame() {
		ServiceFactory sf = ServiceFactory.getInstance();
		cis = sf.getCoreInteractionService();
		ps = sf.getPlaylistService();
		ss = sf.getSettingsService();

		setBounds(100, 100, 1000, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("mp3@player");

		initialize();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				currentPlaylistGUI = parseSongTable(currentPlaylistGUI);
				try {
					ps.updatePlaylist(currentPlaylistGUI);
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
	}

	public MainFrame(Playlist list) {
		currentPlaylistGUI = list;
	}

	public MainFrame(String command) {
		if (command == "reloadsongTable")
			fillSongTable(currentPlaylistGUI);
	}

	/**
	 * Initializes the contents of the frame
	 */
	private void initialize() {

		/**
		 * MainFrame
		 */
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
			image = ImageIO.read(getClass().getResource("img/background.png"));
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

		JMenuItem treeentry1 = new JMenuItem("Delete Playlist");
		treePopupMenu.add(treeentry1);
		treeentry1.addActionListener(new TreeActionAdapter());
		treeentry1.setActionCommand("deletePlaylist");
		
		JMenuItem treeentry2 = new JMenuItem("Rename Playlist");
		treePopupMenu.add(treeentry2);
		treeentry2.addActionListener(new TreeActionAdapter());
		treeentry2.setActionCommand("renamePlaylist");

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
		 * ps.getLibrary())); Playlist qu = new Playlist("Queue"); add(new
		 * PlaylistTreeNode("Queue", false, qu));
		 * 
		 * // node_1 = new PlaylistTreeNode(ps.getLibrary().toString(), //
		 * false, ps.getLibrary()); node_1 = new PlaylistTreeNode("Playlists");
		 * 
		 * Playlist current = null; ListIterator<Playlist> iter =
		 * playlists.listIterator();
		 * 
		 * while (iter.hasNext()) { current = iter.next(); node_1.add(new
		 * PlaylistTreeNode(current.getTitle(), false, current)); } add(node_1);
		 * 
		 * node_1 = new PlaylistTreeNode("Intelligent Playlists");
		 * node_1.add(new PlaylistTreeNode("TopRated"));
		 * 
		 * node_1.add(new PlaylistTreeNode( ps.getTopRated().getTitle(), false,
		 * ps .getTopRated())); node_1.add(new
		 * PlaylistTreeNode(ps.getTopPlayed() .getTitle(), false,
		 * ps.getTopPlayed()));
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

		playerPanel.add(pl_tree_sp, "cell 0 1,grow");

		/**
		 * JTables
		 */
		// songTable
		/*
		 * JMenuItem menuItem = new JMenuItem("");
		 * menuItem.addActionListener(new InsertRowsActionAdapter(this));
		 * tablePopupMenu.add(menuItem);
		 */
		JMenuItem entry = new JMenuItem("Delete Song");
		tablePopupMenu.add(entry);
		entry.addActionListener(new TableActionAdapter());
		entry.setActionCommand("deleteSong");

		songTable = new JTable(songmodel);
		songTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane songTable_sp = new JScrollPane(songTable);
		songTable_sp
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		songTable_sp
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		songTable.getTableHeader().setReorderingAllowed(false);
		songTable
				.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		songTable.setDragEnabled(true);
		songTable.setTransferHandler(new JTableSongTransferHandler());
		playerPanel.add(songTable_sp, "cell 1 1 3 1,grow");
		// songTable.setAutoCreateRowSorter(true);
		songTable.getModel().addTableModelListener(this);
		cTableModel = new HidableTableColumnModel(songTable.getColumnModel());
		// htcm.setColumnVisible(0, false);
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
					Song x;

					if (row > -1) {
						x = (Song) songTable.getValueAt(row, 0);
						play(x);
					}
				}
			}
		});

		MouseListener popupListener = new PopupListener();
		// add the listener specifically to the header
		songTable.addMouseListener(popupListener);
		songTable.getTableHeader().addMouseListener(popupListener);

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
		progress.setMajorTickSpacing(10);
		progress.setMinorTickSpacing(1);
		progress.setPaintTicks(true);
		progress.setSnapToTicks(false);
		progress.putClientProperty("JSlider.isFilled", Boolean.TRUE);
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
				logger.info("Beginn");
				new MainFrame("reloadsongTable");

				// fillSongTable(currentPlaylistGUI);
			}

			public void songEndEvent() {
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
		chckbxMute = new JCheckBox("Mute");
		chckbxMute.setOpaque(false);
		chckbxMute.setContentAreaFilled(false);
		chckbxMute.setBorderPainted(false);
		chckbxMute.setFocusPainted(false);
		playerPanel.add(chckbxMute, "cell 3 3 1 2,alignx right,aligny center");

		chckbxMute.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				setMute();
			}
		});

		// Repeat
		chckbxRepeat = new JCheckBox("Repeat");
		chckbxRepeat.setOpaque(false);
		chckbxRepeat.setContentAreaFilled(false);
		chckbxRepeat.setBorderPainted(false);
		chckbxRepeat.setFocusPainted(false);
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
		chckbxShuffle = new JCheckBox("Shuffle");
		chckbxShuffle.setOpaque(false);
		chckbxShuffle.setContentAreaFilled(false);
		chckbxShuffle.setBorderPainted(false);
		chckbxShuffle.setFocusPainted(false);
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

		JMenuItem mntmAddFiles = new JMenuItem("Add File...");
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
			Song x;

			if (row > -1) {
				x = (Song) this.songTable.getValueAt(row, 0);
				play(x);
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
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
			new DynamicDialog("About mp3@Player...",
					"mp3@Player Beta Version by SEPM team VVV");
		}

		else if (e.getActionCommand().equals("exit")) {
			logger.info("Application is going terminate... Have a nice day :)");
			System.exit(0);
		}
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			logger.info("MainFrame(): Pressed Enter");

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
				treePopupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	class TableActionAdapter implements ActionListener {

		TableActionAdapter() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("deleteSong")) {
				int row = songTable.getSelectedRow();
				Song x = null;

				if (row > -1) {
					x = (Song) songTable.getValueAt(row, 0);
				}
				currentPlaylistGUI.removeSong(x);
				fillSongTable(currentPlaylistGUI);
			}

		}
	}

	class TreeActionAdapter implements ActionListener {

		TreeActionAdapter() {
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("deletePlaylist")) {
				PlaylistTreeNode selectedNode = (PlaylistTreeNode) pl_tree
						.getLastSelectedPathComponent();
				Playlist selectedPlaylist = selectedNode.getNodePlaylist();
				try {
					ps.deletePlaylist(selectedPlaylist);
				} catch (DataAccessException e1) {
				}
				buildPlTree();

			} else if (e.getActionCommand().equals("renamePlaylist")) {
				PlaylistTreeNode selectedNode = (PlaylistTreeNode) pl_tree
						.getLastSelectedPathComponent();
				Playlist selectedPlaylist = selectedNode.getNodePlaylist();
				playlistgui = new PlaylistGUI();
				playlistgui.renamePlaylistGUI(selectedPlaylist);
				buildPlTree();
			}
		}
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub

	}

}