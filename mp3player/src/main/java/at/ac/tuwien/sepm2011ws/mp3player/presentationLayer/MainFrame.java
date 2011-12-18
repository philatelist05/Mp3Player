package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.Image;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.CoreInteractionService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlayMode;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vvv.ServiceFactory;

public class MainFrame extends JFrame implements ActionListener, Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -959319978002415594L;
	private static Logger logger = Logger.getLogger(MainFrame.class);
	private JTable songTable;
	private SongTableModel songmodel = new SongTableModel(new String[] {
			"Track Nr.", "Title", "Artist", "Album", "Year", "Genre",
			"Duration", "Rating", "Playcount" }, 0);
	private RectButton btnPrevious;
	private RoundButton btnPlayPause;
	private RectButton btnNext;
	private JSlider volume;

	private JSlider progress;
	private JLabel lblDurationAt;
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

	private PlaylistService ps;
	private CoreInteractionService cis;

	/**
	 * Gets all Songs from the Database
	 */
	public void getWholeLibrary() {
		Playlist library;

		// Holen aller Songs der Library
		library = ps.getLibrary();
		// Curretn Playlist setzen
		ps.setCurrentPlaylist(library);
		// Einfügen der Daten in dlTable
		fillSongTable(library);
	}

	/**
	 * Fills the songTable with the specified songs
	 * 
	 * @param List
	 *            containing song items
	 */
	public void fillSongTable(Playlist list) {
		songmodel.setRowCount(0);
		for (Song x : list.getSongs())
			songmodel.addRow(new Object[] { x, x.getTitle(), x.getArtist(),
					x.getAlbum(), x.getYear(), x.getGenre(), x.getDuration(),
					x.getRating(), x.getPlaycount() });
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
		// if (cis.hasPreviousSong()) {

		btnPlayPause.setActionCommand("play");
		setPlayIcons();
		lblCurrentStateSong.setText("");

		cis.playPrevious();
		Song temp = cis.getCurrentSong();

		// progress.setEnabled(true);

		if (cis.isPlaying()) {
			lblCurrentStateSong.setText("Currently playing: "
					+ temp.getArtist() + " - " + temp.getTitle() + "");

			btnPlayPause.setActionCommand("pause");
			setPauseIcons();
		} else
			setProgressBartoDefault();
		// }
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

		Song temp = cis.getCurrentSong();

		progress.setEnabled(true);
		lblDurationAt.setText(getMediaTimeAtInSeconds());
		// lblCurrentStateSong.setVisible(true);
		// lblDurationAt.setVisible(true);
		// lblDuration.setVisible(true);
		// lblDurationAt.setText(getMediaTimeAt(progress.getValue()));
		progress.setMaximum(temp.getDuration());
		progress.setVisible(true);

		lblCurrentStateSong.setVisible(true);
		lblDurationAt.setVisible(true);
		lblDurationSeperator.setVisible(true);
		lblDuration.setVisible(true);
		// lblDuration.setText(getMediaTime());

		createThread();

		if (cis.isPlaying()) {
			lblCurrentStateSong.setText("Currently playing: "
					+ temp.getArtist() + " - " + temp.getTitle() + "");
			btnPlayPause.setActionCommand("pause");
			setPauseIcons();
			btnNext.setActionCommand("next");
			btnPrevious.setActionCommand("previous");
		}

		// lblDuration.setText(getMediaTimeAt(100));
		// lblDuration.validate();
	}

	/**
	 * Sends the play Song Signal to the ServiceLayer
	 */
	public void pauseplay() {
		cis.playPause();
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
	 */
	public void pause() {
		cis.pause();
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
		// if (cis.hasNextSong()) {
		btnPlayPause.setActionCommand("play");
		setPlayIcons();
		lblCurrentStateSong.setText("");

		cis.playNext();
		Song temp = cis.getCurrentSong();
		// progress.setEnabled(true);

		if (cis.isPlaying()) {
			lblCurrentStateSong.setText("Currently playing: "
					+ temp.getArtist() + " - " + temp.getTitle() + "");

			btnPlayPause.setActionCommand("pause");
			setPauseIcons();
		} else
			setProgressBartoDefault();
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
			cis.seekToSecond(value);
		// cis.seek(value);
	}

	public String getMediaTimeAt(int percent) {
		// cis = sf.getCoreInteractionService();
		// cis.
		double timeAt = cis.getDurationAt(percent);
		String timeStringAt = String.format("%02.0f:%02.0f:%02.0f",
				Math.floor(timeAt / 3600), Math.floor((timeAt % 3600) / 60),
				Math.floor(timeAt % 60));
		return timeStringAt;

	}

	public String getMediaTimeAtInSeconds() {
		// cis = sf.getCoreInteractionService();
		// cis.
		double timeAt = cis.getDurationInSeconds();
		String timeStringAt = String.format("%02.0f:%02.0f:%02.0f",
				Math.floor(timeAt / 3600), Math.floor((timeAt % 3600) / 60),
				Math.floor(timeAt % 60));
		return timeStringAt;

	}

	public int getMediaTimeAtInSecondsInt() {
		return (int) cis.getDurationInSeconds();
	}

	public String getMediaTime() {
		// cis = sf.getCoreInteractionService();
		double timeAt = cis.getDuration();
		String timeStringAt = String.format("%02.0f:%02.0f:%02.0f",
				Math.floor(timeAt / 3600), Math.floor((timeAt % 3600) / 60),
				Math.floor(timeAt % 60));
		return timeStringAt;

	}

	public void setProgressBartoDefault() {
		progress.setValue(0);
		progress.setEnabled(false);
		lblDurationAt.setText(durationAtdefault);
		lblDuration.setText(durationdefault);
		lblDuration.setVisible(false);
		lblDurationSeperator.setVisible(false);
		lblDurationAt.setVisible(false);
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

	private Thread fred; // ;-) @Johannes: XD

	private void createThread() {
		fred = new Thread(this);
		fred.start();
	}

	public void run() {

		// TODO Auto-generated method stub

		while (!fred.isInterrupted()) {

			Song temp = cis.getCurrentSong();
			progress.setValue(getMediaTimeAtInSecondsInt());
			// progress.setValue(cis.getPlayTime()); //for percent
			progress.setMaximum(temp.getDuration());
			lblDurationAt.setText(getMediaTimeAtInSeconds());
			// lblDuration.setText(getMediaTimeAtInSeconds()); //for percent
			lblDuration.setText(getMediaTimeAt(100));
			// lblDuration.setText(getMediaTimeAt(100)); // in percent

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
			ps.setPlayMode(PlayMode.NORMAL);
		else if (chckbxRepeat.isSelected() && !chckbxShuffle.isSelected())
			ps.setPlayMode(PlayMode.REPEAT);
		else if (!chckbxRepeat.isSelected() && chckbxShuffle.isSelected())
			ps.setPlayMode(PlayMode.SHUFFLE);
	}

	/**
	 * Starts the MainFrame
	 */
	public MainFrame() {
		ServiceFactory sf = ServiceFactory.getInstance();
		cis = sf.getCoreInteractionService();
		ps = sf.getPlaylistService();

		setBounds(100, 100, 1000, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("mp3@player");

		initialize();

		getWholeLibrary();

		cis.setVolume(volume.getValue());
		ps.setPlayMode(PlayMode.NORMAL);

		// setExtendedState(JFrame.MAXIMIZED_BOTH);

		setVisible(true);
		// setResizable(false);

		logger.info("Components successfully initialized");
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
		JTree pl_tree = new JTree();
		pl_tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode(
				"mp3@player") {
			/**
				 * 
				 */
			private static final long serialVersionUID = -7228695694680777407L;

			{
				DefaultMutableTreeNode node_1;
				add(new DefaultMutableTreeNode("Library"));
				add(new DefaultMutableTreeNode("Queue"));
				node_1 = new DefaultMutableTreeNode("Playlists");
				node_1.add(new DefaultMutableTreeNode("Metal"));
				node_1.add(new DefaultMutableTreeNode("80s"));
				node_1.add(new DefaultMutableTreeNode("Funk"));
				node_1.add(new DefaultMutableTreeNode("Pop"));
				add(node_1);
				node_1 = new DefaultMutableTreeNode("Intelligent Playlists");
				node_1.add(new DefaultMutableTreeNode("Top40 rated"));
				node_1.add(new DefaultMutableTreeNode("Top40 played"));
				add(node_1);
			}
		}));
		pl_tree.setEditable(true);
		pl_tree.setVisibleRowCount(5);
		JScrollPane pl_tree_sp = new JScrollPane(pl_tree);
		playerPanel.add(pl_tree_sp, "cell 0 1,grow");

		/**
		 * JTables
		 */
		// songTable
		songTable = new JTable(songmodel);
		songTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane songTable_sp = new JScrollPane(songTable);
		songTable_sp
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		songTable_sp
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		playerPanel.add(songTable_sp, "cell 1 1 3 1,grow");

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

		/**
		 * JButtons
		 */

		// Previous
		btnPrevious = new RectButton(l1, l2, l3);
		playerPanel
				.add(btnPrevious, "cell 0 3 1 2,alignx center,aligny center");
		btnPrevious.addActionListener(this);
		// btnPrevious.setActionCommand("previous");

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
		// btnNext.setActionCommand("next");

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
				//setMediaTime(progress.getValue());
			}
			public void mouseDragged(MouseEvent evt) {
				setMediaTime(progress.getValue());
			}
		});

		/*
		 * progress.addChangeListener(new ChangeListener() { public void
		 * stateChanged(ChangeEvent e) { setMediaTime(progress.getValue()); }
		 * });
		 */

		progress.setPreferredSize(new Dimension(getWidth(), 25));
		playerPanel.add(progress,
				"flowx,cell 0 2 4 1,alignx left,aligny center");
		progress.setEnabled(false);

		// lblDurationAt = new JLabel(getMediaTimeAt(progress.getValue()));
		
		lblDurationAt = new JLabel("");
		playerPanel.add(lblDurationAt, "cell 1 4,alignx left, aligny center");
		lblDurationAt.setVisible(false);

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
		volume = new JSlider(0, cis.MAX_VOLUME, 50);
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

		JMenuItem mntmAddFiles = new JMenuItem("Add Files...");
		mnLibrary.add(mntmAddFiles);

		JMenuItem mntmAddFolders = new JMenuItem("Add Folders...");
		mnLibrary.add(mntmAddFolders);

		JSeparator separator_1 = new JSeparator();
		mnLibrary.add(separator_1);

		JMenuItem mntmReallocateSongs_1 = new JMenuItem("Reallocate Songs...");
		mnLibrary.add(mntmReallocateSongs_1);

		JMenu mnPlaylist = new JMenu("Playlist");
		mnFile.add(mnPlaylist);

		JMenuItem mntmImport = new JMenuItem("Import...");
		mnPlaylist.add(mntmImport);

		JMenuItem mntmExport = new JMenuItem("Export...");
		mnPlaylist.add(mntmExport);

		JMenuItem mntmSettings = new JMenuItem("Settings");
		mnFile.add(mntmSettings);

		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmAbout = new JMenuItem("About...");
		mnHelp.add(mntmAbout);
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
			pause();
		}

		else if (e.getActionCommand().equals("next")) {
			next();
		}
	}
}