package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;

public class checkSongPathGUI extends JDialog implements ActionListener {

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
	
	public checkSongPathGUI() {
		logger.info("checkSongPathGUI(): Started constructor checkSongPathGUI()");
		initialize();
		logger.info("checkSongPathGUI(): Components successfully initialized");
		
		ServiceFactory sf = ServiceFactory.getInstance();
		ps = sf.getPlaylistService();
		
		setTitle("Checking songpaths...");
		setBounds(100, 100, 450, 150);
		setModal(true);
		setVisible(true);
	}
	
	private void initialize() {
		checkPanel = new JPanel(new MigLayout("", "[grow]", "[][]"));
		checklabel = new JLabel("Checking songpaths...");
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
			try {
				ps.checkSongPaths();
			} catch (DataAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			logger.info("checkSongPathGUI(): Songpaths successfully checked");
			new MainFrame("reloadsongTable");
			logger.info("checkSongPathGUI(): Back from Mainframe");
			dispose();
		}
	}
}