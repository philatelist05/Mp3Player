package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.CoreInteractionService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vvv.ServiceFactory;

public class MainFrame extends JFrame implements ActionListener {
    /**
	 * 
	 */
    private static final long serialVersionUID = -959319978002415594L;
    private static Logger logger = Logger.getLogger(MainFrame.class);
	private JTable songTable;
	private SongTableModel songmodel = new SongTableModel(new String[] { "Track Nr.", "Title", "Artist", "Album", "Year", "Genre", "Duration", "Rating", "Playcount" }, 0);
	private JButton btnPrevious;
	private JButton btnPlayPause;
	private JButton btnNext;
	private JSlider volume;
	private JSlider progress;
	
	ServiceFactory sf = ServiceFactory.getInstance();
	private PlaylistService ps;
	private CoreInteractionService cis;

    /**
	 * Gets all Songs from the Database
	 */
	private void getWholeLibrary () {
		ps = sf.getPlaylistService();
		Playlist library;
		
		// Holen aller Songs der Library
		library = ps.getLibrary();
		// Einfügen der Daten in dlTable
		fillSongTable(library);
	}
	
	/**
	 * Fills the songTable with the specified songs
	 * @param List containing song items
	 */
	private void fillSongTable(Playlist list) {
		songmodel.setRowCount(0);
		for (Song x : list.getSongs())
			songmodel.addRow(new Object[] { x, x.getTitle() , x.getArtist(), x.getAlbum(), x.getYear(), x.getGenre(), x.getDuration(), x.getRating(), x.getPlaycount()});
	}
	
	/**
	 * Sends the "previous Song" Signal to the Service Layer
	 */
	private void previous() {
		cis = sf.getCoreInteractionService();
		
		btnPlayPause.setText("Play");
		cis.playPrevious();
		btnPlayPause.setText("Pause");
	}
	
	/**
	 * Sends the play Song action to the SercviceLayer
	 * @param x The Song which should be played
	 */
	private void play (Song x) {
		cis = sf.getCoreInteractionService();
		
		btnPlayPause.setActionCommand("play");
		btnPlayPause.setText("Play");
		cis = sf.getCoreInteractionService();
		cis.playPause(x);
		btnPlayPause.setActionCommand("pause");
		btnPlayPause.setText("Pause");
		
	}
	
	/**
	 * Sends the pause Song action to the ServiceLayer
	 */
	private void pause() {
		cis = sf.getCoreInteractionService();
		
		cis.pause();
		btnPlayPause.setActionCommand("play");
		btnPlayPause.setText("Play");
	}
	
	/**
	 * Sends the next Song action to the ServiceLayer
	 */
	private void next () {
		cis = sf.getCoreInteractionService();
		
		btnPlayPause.setText("Play");
		cis.playNext();
		btnPlayPause.setText("Pause");
	}
	
	/**
	 * Sends the volume action to the ServiceLayser
	 * @param x the volume from 0 - 50
	 */
	private void setVol (int x) {
		cis = sf.getCoreInteractionService();
		cis.setVolume(x);
	}

    private  void setMediaTime(int value) {
		// TODO Auto-generated method stub
    	cis = sf.getCoreInteractionService();
    	cis.seek(value);
		
	}
	
    /**
     * Starts the MainFrame
     */
    public MainFrame() {

    	setBounds(100, 100, 900, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		cis = sf.getCoreInteractionService();
		
		initialize();
		
		setVisible(true);
		
		logger.info("Components successfully initialized");
		
		getWholeLibrary();
		cis.setVolume(volume.getValue());
    }

    /**
	 * Initializes the contents of the frame
	 */
	private void initialize() {
		/**
		 * JPanels
		 */
		// playerPanel
		JPanel playerPanel = new JPanel(new MigLayout("", "[][grow][grow]", "[][grow][][]"));
		getContentPane().add(playerPanel);
		
		/**
		 * JLabels
		 */
		// lblHeader
		JLabel lblHeader = new JLabel("mp3@player");
		playerPanel.add(lblHeader, "cell 0 0");
		
		
		/**
		 * JTrees
		 */
		// pl_tree
		JTree pl_tree = new JTree();
		pl_tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("mp3@player") {
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
			}
		));
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
		playerPanel.add(songTable_sp, "cell 1 1 2 1,grow");
		
		songTable.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2){
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
		btnPrevious = new JButton("Previous");
		playerPanel.add(btnPrevious, "flowx,cell 1 3");
		btnPrevious.addActionListener(this);
		btnPrevious.setActionCommand("previous");
		
		// Play_Pause		
		btnPlayPause = new JButton("Play");
		playerPanel.add(btnPlayPause, "cell 1 3");
		btnPlayPause.addActionListener(this);
		btnPlayPause.setActionCommand("play");
		
		// Next
		btnNext = new JButton("Next");
		playerPanel.add(btnNext, "cell 1 3");
		btnNext.addActionListener(this);
		btnNext.setActionCommand("next");
		
		
		/**
		 * JSliders
		 */
		// ProgressBar
		progress = new JSlider(0,100);
		playerPanel.add(progress, "flowx,cell 0 2 3 1");
		progress.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				setMediaTime(progress.getValue());
			}
		});
		
		// Volume
		volume = new JSlider(0, 50);
		volume.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				setVol(volume.getValue());
			}
		});
		playerPanel.add(volume, "cell 1 3");
		
		
		/**
		 * JCheckboxes
		 */
		// Repeat
		JCheckBox chckbxRepeat = new JCheckBox("Repeat");
		playerPanel.add(chckbxRepeat, "cell 1 3");
		
		// Shuffle
		JCheckBox chckbxShuffle = new JCheckBox("Shuffle");
		playerPanel.add(chckbxShuffle, "cell 1 3");
		
		
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
    	if(e.getActionCommand().equals("previous")){
			previous();
		}
		
		else if(e.getActionCommand().equals("play")){
			int row = this.songTable.getSelectedRow();
			Song x;

			if (row > -1) {
				x = (Song) this.songTable.getValueAt(row, 0);
				play(x);
			}
		}
    	
		else if(e.getActionCommand().equals("pause")){
			pause();
		}
		
		else if(e.getActionCommand().equals("next")){
			next();
		}
    }
}