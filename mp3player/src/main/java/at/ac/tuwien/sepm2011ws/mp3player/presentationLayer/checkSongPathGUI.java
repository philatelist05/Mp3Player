package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;

public class checkSongPathGUI extends JDialog implements ActionListener, Runnable {

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

	private void createThread() {
		fred = new Thread(this);
		fred.start();
	}

	public void run() {
		try {
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			btnCancel.setEnabled(false);
			btnStart.setEnabled(false);
			checklabel.setText("Working...");
			
			ps.checkSongPaths();
			
			logger.info("checkSongPathGUI(): Songpaths successfully checked");
			new MainFrame("reloadsongTable");
			logger.info("checkSongPathGUI(): Back from Mainframe");
			
			checklabel.setText("Press start to check for missing songs:");
			btnCancel.setEnabled(true);
			btnStart.setEnabled(true);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			
			JOptionPane.showConfirmDialog(null, "Successfully checked songpaths!",
					"Checking songspath...", JOptionPane.CLOSED_OPTION);
			dispose();
		} catch (DataAccessException e) {
			JOptionPane.showConfirmDialog(null, e.getMessage(),
					"Error", JOptionPane.CLOSED_OPTION);
			dispose();
		}
	}
	
	public checkSongPathGUI() {
		logger.info("checkSongPathGUI(): Started constructor checkSongPathGUI()");
		initialize();
		logger.info("checkSongPathGUI(): Components successfully initialized");
		
		ServiceFactory sf = ServiceFactory.getInstance();
		ps = sf.getPlaylistService();
		
		setTitle("Checking songpaths...");
		
		width = 450;
		height = 100;
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
		checkPanel.add(checklabel, "cell 0 0");
		checkPanel.add(btnCancel, "cell 0 1,alignx right");
		checkPanel.add(btnStart, "cell 0 1,alignx right");
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("cancel")) {
			logger.info("checkSongPathGUI(): Cancelled");
			dispose();
		}
		
		else if (e.getActionCommand().equals("start")) {
			logger.info("checkSongPathGUI(): started Thread");
			createThread();
		}
		
		/*else if (e.getActionCommand().equals("runcancel")) {
			logger.info("checkSongPathGUI(): started Thread");
			fred.interrupt();
			btnCancel.setActionCommand("cancel");
			dispose();
		}*/
	}
}