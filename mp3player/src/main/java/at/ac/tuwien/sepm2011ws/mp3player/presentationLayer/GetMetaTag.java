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
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Album;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.MetaTags;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.SongInformationService;

public class GetMetaTag extends JDialog implements ActionListener,
		ItemListener, Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -382149403700069523L;
	private static Logger logger = Logger.getLogger(GetMetaTag.class);
	private Song song;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private Dimension dim = toolkit.getScreenSize();
	private static int positionX, positionY, width, height;
	private ArrayList<MetaTagsWrapper> tags = new ArrayList<MetaTagsWrapper>();
	private SongInformationService sis;

	private JPanel getPanel = new JPanel(new MigLayout("", "[][grow]",
			"[][][][][][][][]"));
	private JComboBox songBox = new JComboBox();

	private JLabel lblSong = new JLabel("Song:");
	private JLabel lblArtist = new JLabel("Artist:");
	private JLabel lblTitle = new JLabel("Title:");
	private JLabel lblAlbum = new JLabel("Album:");
	private JLabel lblYear = new JLabel("Year:");
	private JLabel lblGenre = new JLabel("Genre:");

	private JLabel lblArtistTitle = new JLabel("");
	private JTextField textArtist = new JTextField("");
	private JTextField textTitle = new JTextField("");
	private JTextField textAlbum = new JTextField("");
	private JTextField textYear = new JTextField("");
	private JTextField textGenre = new JTextField("");

	private JButton btnCancel = new JButton("Cancel");
	private JButton btnSave = new JButton("Save");

	private JPanel checkPanel;
	private JLabel checklabel;
	private Thread fred;
	JDialog checkDialog;

	protected Song getSong() {
		return song;
	}

	public MetaTagsWrapper createMetaTagsWrap(Song song) {
		if (song != null) {
			MetaTags firstTags = new MetaTags(song.getArtist(),
					song.getTitle(), song.getDuration(), song.getYear(),
					song.getGenre(), null);

			if (song.getAlbum() != null)
				firstTags.setAlbum(song.getAlbum());

			MetaTagsWrapper wrap = new MetaTagsWrapper(firstTags, null,
					ComponentType.ComboBox, "stored MetaTags");

			if (song.getLyric() != null)
				wrap.setLyric(song.getLyric());

			return wrap;
		}

		else
			return null;
	}

	public MetaTagsWrapper createMetaTagsWrapFromLyric(MetaTags tags,
			String text) {
		if (tags != null) {
			return new MetaTagsWrapper(tags, ComponentType.ComboBox, text);
		}

		return null;
	}

	public GetMetaTag(ArrayList<Song> songlist) {
		if (!songlist.isEmpty()) {
			ServiceFactory sf = ServiceFactory.getInstance();
			sis = sf.getSongInformationService();

			song = songlist.get(0);
			String temp = "";

			initialize();

			tags.add(createMetaTagsWrap(song));

			for (MetaTagsWrapper x : tags)
				songBox.addItem(x);

			temp = song.getArtist() + " - " + song.getTitle();
			if (temp.length() > 35)
				temp = temp.substring(0, 30) + "...";
			lblArtistTitle.setText(temp);
			fillFields((MetaTagsWrapper) songBox.getSelectedItem());

			width = 400;
			height = 232;
			positionX = (int) Math.round(dim.getWidth() / 2 - width / 2);
			positionY = (int) Math.round(dim.getHeight() / 2 - height / 2);

			setBounds(positionX, positionY, width, height);
			setTitle("Get Metatags from LastFM...");

			setModal(true);
			setVisible(true);
		}

		else {
			int response = JOptionPane.showConfirmDialog(null,
					"No song chosen!", "No song chosen!",
					JOptionPane.CLOSED_OPTION);
			if (response == JOptionPane.CLOSED_OPTION) {
				logger.info("GetMetaTag(): close GetMetaTag()");
				dispose();
			} else {
				logger.info("GetMetaTag(): close GetMetaTag()");
				dispose();
			}
		}
	}

	private void initialize() {
		logger.info("GetMetaTag(): start initializing components...");

		getContentPane().add(getPanel);

		lblSong.setFont(lblSong.getFont().deriveFont(Font.BOLD));
		lblArtistTitle.setFont(lblArtistTitle.getFont().deriveFont(Font.BOLD));

		getPanel.add(lblSong, "cell 0 0");
		getPanel.add(lblArtistTitle, "cell 1 0");

		songBox.addItemListener(this);
		getPanel.add(songBox, "cell 1 1");

		getPanel.add(lblArtist, "cell 0 2");
		getPanel.add(textArtist, "cell 1 2, growx");
		getPanel.add(lblTitle, "cell 0 3");
		getPanel.add(textTitle, "cell 1 3, growx");
		getPanel.add(lblAlbum, "cell 0 4");
		getPanel.add(textAlbum, "cell 1 4, growx");
		getPanel.add(lblYear, "cell 0 5");
		getPanel.add(textYear, "cell 1 5, growx");
		getPanel.add(lblGenre, "cell 0 6");
		getPanel.add(textGenre, "cell 1 6, growx");

		getPanel.add(btnCancel, "cell 1 7, alignx right, aligny center");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("cancel");

		getPanel.add(btnSave, "cell 1 7, alignx right, aligny center");
		btnSave.addActionListener(this);
		btnSave.setActionCommand("save");

		logger.info("GetMetaTag(): successfully initialized components");

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
				checkTags();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("save")) {
			if (textYear.getText().trim().matches("^[0-9]+$")) {
				song.setArtist("untitled");
				song.setTitle("untitled");
				if (song.getAlbum() != null) {
					if (textAlbum.getText().trim().length() > 0)
						song.getAlbum().setTitle(textAlbum.getText().trim());
					else
						song.getAlbum().setTitle("untitled");
				}
				
				else {
					if (textAlbum.getText().trim().length() > 0)
						song.setAlbum(new Album(textAlbum.getText().trim()));
					else
						song.setAlbum(new Album(""));
				}

				if (textArtist.getText().trim().length() > 0)
					song.setArtist(textArtist.getText().trim());
				
				if (textTitle.getText().trim().length() > 0)
					song.setTitle(textTitle.getText().trim());

				song.setYear(Integer.parseInt(textYear.getText().trim()));
				song.setGenre(textGenre.getText().trim());

				sis.setMetaTags(song);

				dispose();
			}

			else {
				JOptionPane
						.showConfirmDialog(
								null,
								"The year textfield must contain a valid date (e.g. 1988). Minimum Date is 0",
								"Check specified year!",
								JOptionPane.CLOSED_OPTION);
			}
		}

		else if (e.getActionCommand().equals("cancel")) {
			logger.info("GetMetaTag(): Cancelled");
			dispose();
		}
	}

	private void checkTags() {
		checkDialog = new JDialog();

		checkPanel = new JPanel(new MigLayout("", "[grow]", "[]"));
		checklabel = new JLabel("Searching for Meta-Tags...");

		checkDialog.getContentPane().add(checkPanel);
		checkPanel.add(checklabel, "cell 0 0");

		checkDialog.setTitle("Searching for Meta-Tags...");

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

	private void setBlank() {
		textArtist.setText("");
		textTitle.setText("");
		textAlbum.setText("");
		textYear.setText("0");
		textGenre.setText("");
	}

	private void fillFields(MetaTagsWrapper mtw) {
		if (mtw != null) {
			if (mtw.getTags() != null) {
				MetaTags temp = mtw.getTags();
				textArtist.setText(temp.getArtist());
				textTitle.setText(temp.getTitle());
				if (temp.getAlbum() != null)
					textAlbum.setText(temp.getAlbum().getTitle());
				textYear.setText(Integer.toString(temp.getYear()));
				textGenre.setText(song.getGenre());
			} else
				setBlank();
		} else
			setBlank();
	}

	@Override
	public void run() {
		logger.info("GetMetaTag(): Got into thread");
		// try {
		int i = 0;
		List<MetaTags> tagList = sis.downloadMetaTags(song);

		if (tagList != null) {
			if (tagList.size() > 0) {
				for (MetaTags x : tagList) {
					songBox.addItem(createMetaTagsWrapFromLyric(x, "LastFM: #"
							+ i));
					i++;
				}
			}

			else
				JOptionPane.showConfirmDialog(null, "No Metatags found!",
						"LastFM...", JOptionPane.CLOSED_OPTION);
		}

		else
			JOptionPane.showConfirmDialog(null, "No Metatags found!",
					"LastFM...", JOptionPane.CLOSED_OPTION);

		// Thread.sleep(2000);
		checkDialog.dispose();
		// fred.stop();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	public void itemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			logger.info("GetMetaTag(): Clicked on songBox item");
			fillFields((MetaTagsWrapper) evt.getItem());
		}
	}
}