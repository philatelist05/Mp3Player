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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Album;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.SongInformationService;

public class EditMetaTag extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -382149403700069523L;
	private static Logger logger = Logger.getLogger(EditMetaTag.class);
	private Song song;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private Dimension dim = toolkit.getScreenSize();
	private SongInformationService sis;

	private JPanel alterPanel = new JPanel(new MigLayout("", "[][grow]",
			"[][][][][][][]"));

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

	private static int positionX, positionY, width, height;

	protected Song getSong() {
		return song;
	}

	public EditMetaTag(ArrayList<Song> songlist) {
		if (!songlist.isEmpty()) {
			ServiceFactory sf = ServiceFactory.getInstance();
			sis = sf.getSongInformationService();

			song = songlist.get(0);
			String temp = "";
			logger.info("AlterMetaTag(): Start initializing AlterMetaTag");

			initialize();

			temp = song.getArtist() + " - " + song.getTitle();

			if (temp.length() > 35)
				temp = temp.substring(0, 30) + "...";
			lblArtistTitle.setText(temp);
			textArtist.setText(song.getArtist());
			textTitle.setText(song.getTitle());
			if (song.getAlbum() != null)
				textAlbum.setText(song.getAlbum().getTitle());
			textYear.setText(Integer.toString(song.getYear()));
			textGenre.setText(song.getGenre());

			width = 400;
			height = 210;
			positionX = (int) Math.round(dim.getWidth() / 2 - width / 2);
			positionY = (int) Math.round(dim.getHeight() / 2 - height / 2);

			setBounds(positionX, positionY, width, height);
			setTitle("Edit Metatags...");

			setModal(true);
			setVisible(true);
		}

		else {
			int response = JOptionPane.showConfirmDialog(null,
					"No song chosen!", "No song chosen!",
					JOptionPane.CLOSED_OPTION);
			if (response == JOptionPane.CLOSED_OPTION) {
				logger.info("AlterMetaTag(): close AlterMetaTag()");
				dispose();
			} else {
				logger.info("AlterMetaTag(): close AlterMetaTag()");
				dispose();
			}
		}
	}

	private void initialize() {
		logger.info("AlterMetaTag(): start initializing components...");

		getContentPane().add(alterPanel);
		
		lblSong.setFont(lblSong.getFont().deriveFont(Font.BOLD));
		lblArtistTitle.setFont(lblArtistTitle.getFont().deriveFont(Font.BOLD));

		alterPanel.add(lblSong, "cell 0 0");
		alterPanel.add(lblArtistTitle, "cell 1 0");
		alterPanel.add(lblArtist, "cell 0 1");
		alterPanel.add(textArtist, "cell 1 1, growx");
		alterPanel.add(lblTitle, "cell 0 2");
		alterPanel.add(textTitle, "cell 1 2, growx");
		alterPanel.add(lblAlbum, "cell 0 3");
		alterPanel.add(textAlbum, "cell 1 3, growx");
		alterPanel.add(lblYear, "cell 0 4");
		alterPanel.add(textYear, "cell 1 4, growx");
		alterPanel.add(lblGenre, "cell 0 5");
		alterPanel.add(textGenre, "cell 1 5, growx");

		alterPanel.add(btnCancel, "cell 1 6, alignx right, aligny center");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("cancel");

		alterPanel.add(btnSave, "cell 1 6, alignx right, aligny center");
		btnSave.addActionListener(this);
		btnSave.setActionCommand("save");

		logger.info("AlterMetaTag(): successfully initialized components");

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
			dispose();
		}
	}
}