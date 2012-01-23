package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
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

	private void createThread() {
		fred = new Thread(this);
		fred.start();
	}

	public void run() {
			try {
				btnCancel.setEnabled(false);
				btnStart.setEnabled(false);
				checklabel.setText(checklabel.getText().concat(" Working..."));
				
				ps.checkSongPaths();
				
				logger.info("checkSongPathGUI(): Songpaths successfully checked");
				new MainFrame("reloadsongTable");
				logger.info("checkSongPathGUI(): Back from Mainframe");
				
				checklabel.setText("Press start to check for missing songs: Finished!");
				btnCancel.setEnabled(true);
				btnStart.setEnabled(true);
				
				fred.stop();
			} catch (DataAccessException e) {
				JOptionPane.showMessageDialog(null,
						"Songpath check: " + e);
				e.printStackTrace();
			}
	}
	
	public checkSongPathGUI() {
		logger.info("checkSongPathGUI(): Started constructor checkSongPathGUI()");
		initialize();
		logger.info("checkSongPathGUI(): Components successfully initialized");
		
		ServiceFactory sf = ServiceFactory.getInstance();
		ps = sf.getPlaylistService();
		
		setTitle("Checking songpaths...");
		setBounds(100, 100, 450, 150);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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
	}
}