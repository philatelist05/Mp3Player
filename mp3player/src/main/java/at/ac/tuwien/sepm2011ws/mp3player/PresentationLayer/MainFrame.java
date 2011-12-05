package at.ac.tuwien.sepm2011ws.mp3player.PresentationLayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

public class MainFrame extends JFrame implements ActionListener {
    /**
	 * 
	 */
    private static final long serialVersionUID = -959319978002415594L;
    private static Logger logger = Logger.getLogger(MainFrame.class);
    private JTable songTable;

    /**
     * Create the application.
     */
    public MainFrame() {

	JPanel playerPanel = new JPanel(new MigLayout("", "[][grow][grow]",
		"[][grow][][][][][][]"));
	getContentPane().add(playerPanel);

	JLabel lblLibraryQueue = new JLabel("Library - Queue");
	playerPanel.add(lblLibraryQueue, "cell 0 0");

	JList mainList = new JList();
	mainList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	mainList.setModel(new AbstractListModel() {
	    String[] values = new String[] { "Library", "Queue" };

	    public int getSize() {
		return values.length;
	    }

	    public Object getElementAt(int index) {
		return values[index];
	    }
	});

	JScrollPane mainList_sp = new JScrollPane(mainList);
	playerPanel.add(mainList_sp, "cell 0 1,grow");

	songTable = new JTable();
	songTable
		.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	songTable.setModel(new DefaultTableModel(new Object[][] { { "1",
		"The Hero", "Amon Amarth", "Twilight Of The Tundergod", "2008",
		"04:04", "Melodic Death Metal", "5", "12" }, }, new String[] {
		"Track Nr.", "Title", "Artist", "Album", "Date", "Genre",
		"Length", "Rating", "Playcount" }));

	JScrollPane songTable_sp = new JScrollPane(songTable);
	playerPanel.add(songTable_sp, "cell 1 1 2 5,grow");

	JLabel lblPlaylists = new JLabel("Playlists");
	playerPanel.add(lblPlaylists, "cell 0 2");

	JList playlistsList = new JList();
	playlistsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	playlistsList.setModel(new AbstractListModel() {
	    String[] values = new String[] { "Metal", "80s", "Funk", "Pop" };

	    public int getSize() {
		return values.length;
	    }

	    public Object getElementAt(int index) {
		return values[index];
	    }
	});

	JScrollPane playlist_sp = new JScrollPane(playlistsList);
	playerPanel.add(playlist_sp, "cell 0 3,grow");

	JLabel lblInteligentPlaylists = new JLabel("Inteligent Playlists");
	playerPanel.add(lblInteligentPlaylists, "cell 0 4");

	JList intplaylistsList = new JList();
	intplaylistsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	JScrollPane intplaylist_sp = new JScrollPane(intplaylistsList);
	playerPanel.add(intplaylist_sp, "cell 0 5,grow");

	JSlider progress = new JSlider();
	playerPanel.add(progress, "flowx,cell 0 6 3 1");

	JButton btnPrevious = new JButton("Previous");
	playerPanel.add(btnPrevious, "flowx,cell 1 7");

	JButton btnPlay = new JButton("Play");
	playerPanel.add(btnPlay, "cell 1 7");

	JButton btnNext = new JButton("Next");
	playerPanel.add(btnNext, "cell 1 7");

	JSlider volume = new JSlider();
	playerPanel.add(volume, "cell 1 7");

	JCheckBox chckbxRepeat = new JCheckBox("Repeat");
	playerPanel.add(chckbxRepeat, "cell 1 7");

	JCheckBox chckbxShuffle = new JCheckBox("Shuffle");
	playerPanel.add(chckbxShuffle, "cell 1 7");

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

	initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
	setBounds(100, 100, 900, 600);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setVisible(true);

	logger.info("Components successfully initialized");
    }

    public void actionPerformed(ActionEvent arg0) {
    }

}
