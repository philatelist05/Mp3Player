package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;


public class Settings extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7279584293687131273L;
	private static Logger logger = Logger.getLogger(Settings.class);
	private JPanel ft = new JPanel(new MigLayout("", "[grow][][grow]",
			"[][]"));
	private JPanel ip = new JPanel(new MigLayout("", "[grow]","[][][][]"));
	private JPanel tm = new JPanel(new MigLayout("", "[grow][][grow]",
			"[][]"));
	private JPanel contentPane;
	private JTabbedPane settingsPane;
	private JButton btnCancel = new JButton("Cancel");
	private JButton btnOK = new JButton("OK");
	private JList allFileTypesList = new JList();
	private JList userFileTypesList = new JList();
	private JList allColumnsList = new JList();
	private JList userColumnsList = new JList();
	private JScrollPane spAllFiletypes;
	private JScrollPane spUserFiletypes;
	private JScrollPane spAllColumns;
	private JScrollPane spUserColumns;
	private JButton btnDeleteFT = new JButton("<<");
	private JButton btnAddFT = new JButton(">>");
	private JButton btnDeleteTM = new JButton("<<");
	private JButton btnAddTM = new JButton(">>");
	private JLabel lblSupportedFiletypes = new JLabel("Supported Filetypes:");
	private JLabel lblAcceptedFiletypes = new JLabel("Accepted Filetypes:");
	private JLabel lblSupportedColumns = new JLabel("Supported Columns:");
	private JLabel lblAcceptedColumns = new JLabel("Accepted Columns:");
	private JLabel lblXXplayed = new JLabel("Number of songs in playlist \"TopXX\" played\":");
	private JLabel lblXXrated = new JLabel("Number of songs in playlist \"TopXX rated\":");
	private JTextField xxPlayed = new JTextField();
	private JTextField xxRated = new JTextField();


	public Settings() {
		logger.info("Settings(): started Settings()");
		setBounds(100, 100, 450, 300);
		setTitle("Settings");
		
		initialize();
		
		setModal(true);
		setVisible(true);	
	}
	
	private void initialize() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[grow][]"));
		
		settingsPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(settingsPane, "cell 0 0,grow");
		
		// Filetypes
		ft.add(lblSupportedFiletypes, "cell 0 0");
		ft.add(lblAcceptedFiletypes, "cell 2 0");		
		spAllFiletypes = new JScrollPane(allFileTypesList);
		ft.add(spAllFiletypes, "flowx,cell 0 1");
		
		btnAddFT.addActionListener(this);
		btnAddFT.setActionCommand("addft");
		ft.add(btnAddFT, "flowy,cell 1 1");
		
		btnDeleteFT.addActionListener(this);
		btnDeleteFT.setActionCommand("deleteft");
		ft.add(btnDeleteFT, "cell 1 1");
		
		spUserFiletypes = new JScrollPane(userFileTypesList);
		ft.add(spUserFiletypes, "flowx,cell 2 1");
		
		// Intelligent Playlists
		//TODO: add functionality to initialize with SettingsService values
		ip.add(lblXXplayed, "cell 0 0");
		ip.add(xxPlayed, "cell 0 1,growx");
		ip.add(lblXXrated, "cell 0 2");
		ip.add(xxRated, "cell 0 3,growx");
		
		// Table Management
		tm.add(lblSupportedColumns, "cell 0 0");
		tm.add(lblAcceptedColumns, "cell 2 0");		
		spAllColumns = new JScrollPane(allColumnsList);
		tm.add(spAllColumns, "flowx,cell 0 1");
		
		btnAddTM.addActionListener(this);
		btnAddTM.setActionCommand("addtm");
		tm.add(btnAddTM, "flowy,cell 1 1");
		
		btnDeleteTM.addActionListener(this);
		btnDeleteTM.setActionCommand("deletetm");
		tm.add(btnDeleteTM, "cell 1 1");
		
		spUserColumns = new JScrollPane(userColumnsList);
		tm.add(spUserColumns, "flowx,cell 2 1");
		
		settingsPane.addTab("Filetypes", ft);
		settingsPane.addTab("Intelligent Playlists", ip);
		settingsPane.addTab("Table Management", tm);
		
		// Cancel- and OK Button
		btnCancel.addActionListener(this);
		btnCancel.setActionCommand("cancel");
		btnOK.addActionListener(this);
		btnOK.setActionCommand("ok");
		contentPane.add(btnCancel, "cell 0 1,alignx right");
		contentPane.add(btnOK, "cell 0 1,alignx right");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			logger.info("Settings(): start saving functionylity");
			//TODO: Add save functionality
			dispose();
		}
		
		else if (e.getActionCommand().equals("cancel")) {
			logger.info("Settings(): canceled");
			dispose();
		}
		
		else if (e.getActionCommand().equals("addft")) {
			logger.info("Settings(): Start filetype adding functionality");
			//TODO: Add "add fileype" functionality
		}
		
		else if (e.getActionCommand().equals("deleteft")) {
			logger.info("Settings(): Start filetype deleting functionality");
			//TODO: Add "delete fileype" functionality
		}
		
		else if (e.getActionCommand().equals("addtm")) {
			logger.info("Settings(): Start column adding functionality");
			//TODO: Add "add column" functionality
		}
		
		else if (e.getActionCommand().equals("deletetm")) {
			logger.info("Settings(): Start columns deleting functionality");
			//TODO: Add "delete column" functionality
		}
	}
}