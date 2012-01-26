package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Lyric;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.MetaTags;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.SongInformationService;

public class GetLyric extends JDialog implements ActionListener, ItemListener,
		Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 451628573957769276L;
	private static Logger logger = Logger.getLogger(GetLyric.class);
	private Song song;
	private ArrayList<MetaTagsWrapper> tags = new ArrayList<MetaTagsWrapper>();
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private Dimension dim = toolkit.getScreenSize();
	private int width;
	private int height;
	private int positionX;
	private int positionY;
	private Thread fred;
	private SongInformationService sis;

	private JPanel getPanel = new JPanel(new MigLayout("", "[][grow]",
			"[][][grow][]"));
	private JDialog checkDialog;
	private JPanel checkPanel;
	private JLabel checklabel = new JLabel("Checking for lyrics...");
	private JLabel lblSong = new JLabel("Song:");
	private JLabel lblArtistTitle = new JLabel("");
	private JLabel lblLyric = new JLabel("Lyric:");
	private JComboBox lyricBox = new JComboBox();
	private JEditorPane lyricEditorPane = new JEditorPane();
	private JScrollPane lyricPane = new JScrollPane(lyricEditorPane);
	private JButton btnCancel = new JButton("Cancel");
	private JButton btnSave = new JButton("Save");

	public Song getSong() {
		return song;
	}

	public MetaTagsWrapper createMetaTagsWrapFromSong(Song song) {
		if (song != null) {
			MetaTags firstTags = new MetaTags(song.getArtist(),
					song.getTitle(), song.getDuration(), song.getYear(),
					song.getGenre(), null);

			if (song.getAlbum() != null)
				firstTags.setAlbum(song.getAlbum());

			MetaTagsWrapper wrap = new MetaTagsWrapper(firstTags, null,
					ComponentType.ComboBox, "stored Lyric");

			if (song.getLyric() != null)
				wrap.setLyric(song.getLyric());

			return wrap;
		}

		else
			return null;
	}

	public MetaTagsWrapper createMetaTagsWrapFromLyric(Lyric lyric, String text) {
		if (lyric != null) {
			return new MetaTagsWrapper(lyric, ComponentType.ComboBox, text);
		}

		return null;
	}

	public GetLyric(ArrayList<Song> songlist) {
		if (!songlist.isEmpty()) {
			ServiceFactory sf = ServiceFactory.getInstance();
			sis = sf.getSongInformationService();
			width = 400;
			height = 360;
			positionX = (int) Math.round(dim.getWidth() / 2 - width / 2);
			positionY = (int) Math.round(dim.getHeight() / 2 - height / 2);
			setBounds(positionX, positionY, width, height);
			setTitle("Get Lyrics from LastFM...");
			setModal(true);
			song = songlist.get(0);

			String temp = "";
			logger.info("GetLyric(): Start initializing GetLyic()");

			initialize();

			temp = song.getArtist() + " - " + song.getTitle();

			if (temp.length() > 35)
				temp = temp.substring(0, 30) + "...";
			lblArtistTitle.setText(temp);

			tags.add(createMetaTagsWrapFromSong(song));

			for (MetaTagsWrapper x : tags)
				lyricBox.addItem(x);

			fillFields(tags.get(0));

			setVisible(true);
		}

		else {
			int response = JOptionPane.showConfirmDialog(null,
					"No song chosen!", "No song chosen!",
					JOptionPane.CLOSED_OPTION);
			if (response == JOptionPane.CLOSED_OPTION) {
				logger.info("GetLyric(): close GetLyric()");
				dispose();
			} else {
				logger.info("GetLyric(): close GetLyric()");
				dispose();
			}
		}
	}

	private void initialize() {
		getContentPane().add(getPanel);

		lblSong.setFont(lblSong.getFont().deriveFont(Font.BOLD));
		lblArtistTitle.setFont(lblArtistTitle.getFont().deriveFont(Font.BOLD));

		getPanel.add(lblSong, "cell 0 0");
		getPanel.add(lblArtistTitle, "cell 1 0");
		getPanel.add(lblLyric, "cell 0 1");

		lyricBox.addItemListener(this);
		getPanel.add(lyricBox, "cell 1 1");

		lyricPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		getPanel.add(lyricPane, "cell 0 2 2 1, growx, growy");

		getPanel.add(btnCancel, "cell 1 3, alignx right, aligny center");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("cancel");

		getPanel.add(btnSave, "cell 1 3, alignx right, aligny center");
		btnSave.addActionListener(this);
		btnSave.setActionCommand("save");

		logger.info("GetMetaTag(): successfully initialized components");

		/*
		 * addComponentListener(new ComponentAdapter() {
		 * 
		 * @Override public void componentResized(ComponentEvent e) { Rectangle
		 * rv = getBounds(); positionX = rv.x; positionY = rv.y; width =
		 * rv.width; height = rv.height; logger.info(rv); } });
		 */

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
				checkTags();
			}
		});
	}

	private void checkTags() {
		checkDialog = new JDialog();

		checkPanel = new JPanel(new MigLayout("", "[grow]", "[]"));
		checklabel = new JLabel("Searching for Lyrics...");

		checkDialog.getContentPane().add(checkPanel);
		checkPanel.add(checklabel, "cell 0 0");

		checkDialog.setTitle("Checking for Lyrics...");

		int width = 200, height = 100;
		int positionX = (int) Math.round(dim.getWidth() / 2 - width / 2);
		int positionY = (int) Math.round(dim.getHeight() / 2 - height / 2);

		checkDialog.setBounds(positionX, positionY, width, height);
		checkDialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		checkDialog.setModal(true);
		checkDialog.setResizable(false);

		fred = new Thread(this);
		fred.start();
		logger.info("GetMetaTag(): Started Thread");

		checkDialog.setVisible(true);
		logger.info("GetMetaTag(): Made checkDialog visible");
	}

	private void fillFields(MetaTagsWrapper mtw) {
		if (mtw != null) {
			if (mtw.getTags() != null) {
				if (mtw.getLyric() != null)
					lyricEditorPane.setText(mtw.getLyric().getText());
			}
		}
	}

	@Override
	public void run() {
		logger.info("GetMetaTag(): Got into thread");

		// try {
		int i = 1;

		List<Lyric> lyricList = null;
		try {
			lyricList = sis.downloadLyrics(song);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
		}

		if (lyricList != null) {
			if (lyricList.size() > 0) {
				for (Lyric x : lyricList) {
					lyricBox.addItem(createMetaTagsWrapFromLyric(x,
							"chartLyric: #" + i));
					i++;
				}
			}
		}

		else
			JOptionPane.showConfirmDialog(null, "No Lyrics found!",
					"Chartlyric...", JOptionPane.CLOSED_OPTION);

		// Thread.sleep(2000);

		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("save")) {
			logger.info("GetLyric(): Started saving of Lyric");

			if (song.getLyric() != null)
				song.setLyric(new Lyric(lyricEditorPane.getText()));

			sis.setMetaTags(song);
			dispose();
		}

		else if (e.getActionCommand().equals("cancel")) {
			MetaTagsWrapper test = (MetaTagsWrapper) lyricBox.getSelectedItem();
			logger.info(test.getTags().getTitle());
			dispose();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			MetaTagsWrapper result = (MetaTagsWrapper) evt.getItem();
			logger.info(result.getTags().getTitle());
			fillFields(result);
		}
	}
}