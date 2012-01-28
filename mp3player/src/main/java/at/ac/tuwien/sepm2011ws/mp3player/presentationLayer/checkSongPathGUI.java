package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;

public class checkSongPathGUI extends JDialog implements ActionListener,
		Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2262220551335147420L;
	private static Logger logger = Logger.getLogger(GlobalSearch.class);
	private JPanel checkPanel;
	private JLabel checklabel;
	private JButton btnCancel;
	private JButton btnStart;
	private PlaylistService ps;
	private Thread fred;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private Dimension dim = toolkit.getScreenSize();
	private int width;
	private int height;
	private int positionX;
	private int positionY;
	private JDialog checkDialog;
	private ImageIcon loading;
	private JLabel lblLoading;
	private JPanel checkPanel2;
	private JLabel checklabel2;

	public void run() {
		try {
			ps.checkSongPaths();

			logger.info("checkSongPathGUI(): Songpaths successfully checked");
			new MainFrame("reloadsongTable");
			logger.info("checkSongPathGUI(): Back from Mainframe");

			checkDialog.dispose();
			
			JOptionPane.showConfirmDialog(null,
					"Successfully checked songpaths!", "Checking songspath...",
					JOptionPane.CLOSED_OPTION);
		} catch (DataAccessException e) {
			checkDialog.dispose();
			JOptionPane.showConfirmDialog(null, e.getMessage(), "Error",
					JOptionPane.CLOSED_OPTION);
		}
	}

	public checkSongPathGUI() {
		logger.info("checkSongPathGUI(): Started constructor checkSongPathGUI()");
		initialize();
		logger.info("checkSongPathGUI(): Components successfully initialized");

		ServiceFactory sf = ServiceFactory.getInstance();
		ps = sf.getPlaylistService();

		setTitle("Check songpaths...");

		width = 210;
		height = 80;
		positionX = (int) Math.round(dim.getWidth() / 2 - width / 2);
		positionY = (int) Math.round(dim.getHeight() / 2 - height / 2);

		setBounds(positionX, positionY, width, height);
		setResizable(false);
		setModal(true);
		setVisible(true);
	}

	private void initialize() {
		checkPanel = new JPanel(new MigLayout("", "[grow]", "[][]"));
		checklabel = new JLabel("Press start to check for missing songs:");
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("cancel");
		btnStart = new JButton("Start");
		btnStart.addActionListener(this);
		btnStart.setActionCommand("start");

		getContentPane().add(checkPanel);
		checkPanel.add(checklabel, "cell 0 0, alignx center");
		checkPanel.add(btnCancel, "cell 0 1, align center");
		checkPanel.add(btnStart, "cell 0 1, align center");
	}

	private void checkSongs() {
		checkDialog = new JDialog();
		lblLoading = new JLabel();

		checkPanel2 = new JPanel(new MigLayout("", "[grow]", "[][]"));
		checklabel2 = new JLabel("Checking songpaths...");

		checkDialog.getContentPane().add(checkPanel2);
		checkPanel2.add(checklabel2, "cell 0 0, align center");

		try {
			loading = new ImageIcon(
					new ClassPathResource("img/loading.gif").getURL());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		lblLoading.setIcon(loading);
		checkPanel2.add(lblLoading, "cell 0 1, align center");

		checkDialog.setTitle("Checking for Lyrics...");

		int width = 250, height = 80;
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
		logger.info("GetMetaTag(): Made checkSongDialog visible");
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("cancel")) {
			logger.info("checkSongPathGUI(): Cancelled");
			dispose();
		}

		else if (e.getActionCommand().equals("start")) {
			logger.info("checkSongPathGUI(): started Thread");
			checkSongs();
		}
	}
}