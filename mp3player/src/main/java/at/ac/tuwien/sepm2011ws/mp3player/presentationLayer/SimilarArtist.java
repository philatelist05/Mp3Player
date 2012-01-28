package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.LastFmService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;

public class SimilarArtist extends JDialog implements ActionListener, Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4718120773580096142L;
	private static Logger logger = Logger.getLogger(SimilarArtist.class);
	private int width;
	private int height;
	private int positionX;
	private int positionY;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private Dimension dim = toolkit.getScreenSize();
	private Song song;
	private Thread fred;

	private JPanel similarPanel = new JPanel(new MigLayout("", "[][grow]",
			"[][][][]"));
	private JDialog checkDialog;
	private JPanel checkPanel;
	private JLabel checklabel = new JLabel("Checking for lyrics...");
	private JLabel lblArtist = new JLabel("Artist:");
	private JLabel lblArtistValue = new JLabel("");
	private JLabel lblSimilarArtist = new JLabel("Similar Artists:");
	private JLabel lblSimilarSong = new JLabel("Similar Songs:");
	private DefaultListModel artistModel = new DefaultListModel();
	private JList artistList = new JList(artistModel);
	private JScrollPane artistPane = new JScrollPane(artistList);
	private SongTableModel songmodel = new SongTableModel(new String[] {
			"Status", "Title", "Artist", "Album", "Year", "Genre", "Duration",
			"Rating", "Playcount" }, 0);
	private JTable songTable = new JTable(songmodel);
	private JScrollPane songPane = new JScrollPane(songTable);
	private JButton btnCancel = new JButton("Cancel");
	private LastFmService lfms;
	private ImageIcon loading;
	private JLabel lblLoading = new JLabel();

	public SimilarArtist(ArrayList<Song> songlist) {
		if (!songlist.isEmpty()) {
			ServiceFactory sf = ServiceFactory.getInstance();
			lfms = sf.getLastFmService();
			width = 650;
			height = 360;
			positionX = (int) Math.round(dim.getWidth() / 2 - width / 2);
			positionY = (int) Math.round(dim.getHeight() / 2 - height / 2);
			setBounds(positionX, positionY, width, height);
			setTitle("Get similar Artists from LastFM...");
			setModal(true);
			song = songlist.get(0);
			String temp = "";
			logger.info("SimilarArtist(): Start initializing GetLyic()");

			initialize();

			temp = song.getArtist();

			if (temp.length() > 35)
				temp = temp.substring(0, 30) + "...";
			lblArtistValue.setText(temp);

			setVisible(true);
		}

		else {
			int response = JOptionPane.showConfirmDialog(null,
					"No song chosen!", "No song chosen!",
					JOptionPane.CLOSED_OPTION);
			if (response == JOptionPane.CLOSED_OPTION) {
				logger.info("SimilarArtist(): close SimilarArtist()");
				dispose();
			} else {
				logger.info("SimilarArtist(): close SimilarArtist()");
				dispose();
			}
		}
	}

	private void initialize() {
		getContentPane().add(similarPanel);

		lblArtist.setFont(lblArtist.getFont().deriveFont(Font.BOLD));
		lblArtistValue.setFont(lblArtistValue.getFont().deriveFont(Font.BOLD));

		similarPanel.add(lblArtist, "cell 0 0 2 1, alignx left");
		similarPanel.add(lblArtistValue, "cell 0 0 2 1, alignx left");
		similarPanel.add(lblSimilarArtist, "cell 0 1");
		similarPanel.add(lblSimilarSong, "cell 1 1");

		similarPanel.add(artistPane, "cell 0 2, growy");

		songPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		similarPanel.add(songPane, "cell 1 2, grow");

		similarPanel.add(btnCancel, "cell 1 3, alignx right, aligny center");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("cancel");

		logger.info("SimilarArtist(): successfully initialized components");

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				Rectangle rv = getBounds();
				positionX = rv.x;
				positionY = rv.y;
				width = rv.width;
				height = rv.height;
				logger.info(rv);
			}
		});

		addWindowListener(new WindowListener() {
			public void windowClosed(WindowEvent arg0) {

			}

			public void windowActivated(WindowEvent arg0) {

			}

			public void windowClosing(WindowEvent arg0) {

			}

			public void windowDeactivated(WindowEvent arg0) {

			}

			public void windowDeiconified(WindowEvent arg0) {

			}

			public void windowIconified(WindowEvent arg0) {

			}

			public void windowOpened(WindowEvent arg0) {
				checkSimilarArtist();
			}
		});

	}

	private void checkSimilarArtist() {
		checkDialog = new JDialog();

		checkPanel = new JPanel(new MigLayout("", "[grow]", "[]"));
		checklabel = new JLabel("Searching for similar Artists...");

		checkDialog.getContentPane().add(checkPanel);
		checkPanel.add(checklabel, "cell 0 0, align center");

		try {
			loading = new ImageIcon(
					new ClassPathResource("img/loading.gif").getURL());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		lblLoading.setIcon(loading);
		checkPanel.add(lblLoading, "cell 0 1, align center");

		checkDialog.setTitle("Searching for similar Artists...");

		int width = 250, height = 80;
		int positionX = (int) Math.round(dim.getWidth() / 2 - width / 2);
		int positionY = (int) Math.round(dim.getHeight() / 2 - height / 2);

		checkDialog.setBounds(positionX, positionY, width, height);
		checkDialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		checkDialog.setResizable(false);
		checkDialog.setModal(true);

		fred = new Thread(this);
		fred.start();
		logger.info("SimilarArtist(): Started Thread");

		checkDialog.setVisible(true);
		logger.info("SimilarArtist(): Made checkSimilarArtistDialog visible");
	}

	@Override
	public void run() {
		List<Playlist> similarArtists;

		logger.info("SimilarArtist(): Got into thread");
		logger.info("SimilarArtist(): get List of playlists (similar artists and the best rated/most playled songs in the library)");

		similarArtists = lfms.getSimilarArtistsWithSongs(song);

		if (similarArtists != null) {
			if (similarArtists.size() > 0) {
				for (Playlist x : similarArtists) {
					artistModel.addElement(x);
				}
				
				checkDialog.dispose();
			}

			else {
				checkDialog.dispose();
				JOptionPane.showConfirmDialog(null,
						"No similar Artists found!", "LastFM...",
						JOptionPane.CLOSED_OPTION);
			}
		}

		else {
			checkDialog.dispose();
			JOptionPane.showConfirmDialog(null, "No similar Artists found!",
					"LastFM...", JOptionPane.CLOSED_OPTION);
		}

		/*
		 * if (artistModel.getSize() > 0) artistList.setSelectedIndex(0);
		 */

		try {
			// Just for testing ;)
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			checkDialog.dispose();
			JOptionPane.showConfirmDialog(null, e.getMessage(),
					"Error", JOptionPane.CLOSED_OPTION);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("cancel")) {
			dispose();
		}
	}

	// TODO for Johannes: implement itemListener for selection change in
	// songList (into initialize())
	// TODO for Johannes: implement JSplitPane
	// TODO for Johannes: implement Method for filling songTable with songs from
	// playlist object in selected songList item
	// TODO for Johannes: implement songTable (full)
	// TODO for Johannes: implement play Song on double click
}
