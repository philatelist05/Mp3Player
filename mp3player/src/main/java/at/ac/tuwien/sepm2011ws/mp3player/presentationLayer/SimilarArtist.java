package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.SongWrapper;

public class SimilarArtist extends JDialog implements ActionListener,
		ItemListener, Runnable {

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
	private JList artistList = new JList();
	private JScrollPane artistPane = new JScrollPane(artistList);
	private SongTableModel songmodel = new SongTableModel(new String[] { "Status",
			"Title", "Artist", "Album", "Year", "Genre", "Duration", "Rating",
			"Playcount" }, 0);
	private JTable songTable = new JTable(songmodel);
	private JScrollPane songPane = new JScrollPane(songTable);
	private JButton btnOK = new JButton("OK");

	public SimilarArtist(ArrayList<Song> songlist) {
		if (!songlist.isEmpty()) {
			width = 400;
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

			/*
			 * tags.add(new SongWrapper(song, ComponentType.ComboBox,
			 * "stored Lyric"));
			 * 
			 * for (SongWrapper x : tags) lyricBox.addItem(x);
			 * 
			 * fillFields(tags.get(0));
			 */

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

		similarPanel.add(lblArtist, "cell 0 0 2 1");
		similarPanel.add(lblArtistValue, "cell 0 0 2 1");
		similarPanel.add(lblSimilarArtist, "cell 0 1");
		similarPanel.add(lblSimilarSong, "cell 1 1");

		//TODO: add Listener for itemselect events
		//artistList.addItemListener(this); 
		//getPanel.add(lyricBox, "cell 1 1");
		 

		similarPanel.add(artistPane, "cell 0 2");

		songPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		similarPanel.add(songPane, "cell 1 2, growx, growy");

		similarPanel.add(btnOK, "cell 1 3, alignx right, aligny center");
		btnOK.addActionListener(this);
		btnOK.setActionCommand("ok");

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
		checklabel = new JLabel("Searching for Lyrics...");

		checkDialog.getContentPane().add(checkPanel);
		checkPanel.add(checklabel, "cell 0 0");

		checkDialog.setTitle("Checking for similar Artists...");

		int width = 200, height = 100;
		int positionX = (int) Math.round(dim.getWidth() / 2 - width / 2);
		int positionY = (int) Math.round(dim.getHeight() / 2 - height / 2);

		checkDialog.setBounds(positionX, positionY, width, height);
		checkDialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		checkDialog.setModal(true);

		fred = new Thread(this);
		fred.start();
		logger.info("SimilarArtist(): Started Thread");

		checkDialog.setVisible(true);
		logger.info("SimilarArtist(): Made checkDialog visible");
	}

	@Override
	public void run() {
		// TODO: Get Artists from lastFM and write them into artistList; Get
		// similar songs from lastFM, check if they are available in the
		// filesystem, write into songTable
		logger.info("GetMetaTag(): Got into thread");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		checkDialog.dispose();
		fred.stop();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			//TODO: crete PlaylistWrapper and fill songList/songTable
			/*SongWrapper test = (SongWrapper) artistList.getSelectedItem();
			logger.info(test.getSong().getTitle());*/
			dispose();
		}

	}

	@Override
	public void itemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			SongWrapper result = (SongWrapper) evt.getItem();
			logger.info(result.getSong().getTitle());
			//fillFields(result);
		}
	}
}
