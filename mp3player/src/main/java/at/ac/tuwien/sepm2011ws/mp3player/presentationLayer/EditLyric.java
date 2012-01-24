package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Lyric;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.SongInformationService;

public class EditLyric extends JDialog implements ActionListener {

	private static final long serialVersionUID = 451628573957769276L;
	private static Logger logger = Logger.getLogger(EditLyric.class);
	private Song song;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private Dimension dim = toolkit.getScreenSize();
	private int width;
	private int height;
	private int positionX;
	private int positionY;
	private SongInformationService sis;

	private JPanel getPanel = new JPanel(new MigLayout("", "[][grow]",
			"[][][grow][]"));
	private JLabel lblSong = new JLabel("Song:");
	private JLabel lblArtistTitle = new JLabel("");
	private JLabel lblLyric = new JLabel("Lyric:");
	private JEditorPane lyricEditorPane = new JEditorPane();
	private JScrollPane lyricPane = new JScrollPane(lyricEditorPane);
	private JButton btnCancel = new JButton("Cancel");
	private JButton btnSave = new JButton("Save");

	public Song getSong() {
		return song;
	}
	
	public EditLyric(ArrayList<Song> songlist) {
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
			fillFields(song);
			
			setVisible(true);
		}

		else {
			int response = JOptionPane.showConfirmDialog(null,
					"No song chosen!", "No song chosen!",
					JOptionPane.CLOSED_OPTION);
			if (response == JOptionPane.CLOSED_OPTION) {
				logger.info("EditLyric(): close EditLyric()()");
				dispose();
			} else {
				logger.info("EditLyric()(): close EditLyric()()");
				dispose();
			}
		}
	}
	
	private void fillFields(Song s) {
		if (s != null) {
			if (s.getLyric() != null)
				lyricEditorPane.setText(s.getLyric().getText());
		}
	}
	
	private void initialize() {
		getContentPane().add(getPanel);

		lblSong.setFont(lblSong.getFont().deriveFont(Font.BOLD));
		lblArtistTitle.setFont(lblArtistTitle.getFont().deriveFont(Font.BOLD));

		getPanel.add(lblSong, "cell 0 0");
		getPanel.add(lblArtistTitle, "cell 1 0");
		getPanel.add(lblLyric, "cell 0 1");
		
		lyricPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		getPanel.add(lyricPane, "cell 0 2 2 1, growx, growy");

		getPanel.add(btnCancel, "cell 1 3, alignx right, aligny center");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("cancel");

		getPanel.add(btnSave, "cell 1 3, alignx right, aligny center");
		btnSave.addActionListener(this);
		btnSave.setActionCommand("save");

		logger.info("EditLyric(): successfully initialized components");

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
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("save")) {
			logger.info("EditLyric(): Started saving of Lyric");
			if (song.getLyric() != null)
				song.setLyric(new Lyric(lyricEditorPane.getText()));

			sis.setMetaTags(song);
			dispose();
		}

		else if (e.getActionCommand().equals("cancel")) {
			logger.info("EditLyric: Cancel");
			dispose();
		}
	}
}