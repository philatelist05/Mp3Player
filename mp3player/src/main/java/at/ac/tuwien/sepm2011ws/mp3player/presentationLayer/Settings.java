package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.SettingsService;

public class Settings extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7279584293687131273L;
	private static Logger logger = Logger.getLogger(Settings.class);
	private JPanel ft = new JPanel(new MigLayout("", "[grow][][grow]", "[][]"));
	private JPanel ip = new JPanel(new MigLayout("", "[grow]", "[][][][]"));
	private JPanel tm = new JPanel(new MigLayout("", "[grow][][grow]", "[][]"));
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
	private JLabel lblXXplayed = new JLabel(
			"Number of songs in playlist \"TopXX\" played\":");
	private JLabel lblXXrated = new JLabel(
			"Number of songs in playlist \"TopXX rated\":");
	private JTextField xxPlayed = new JTextField();
	private JTextField xxRated = new JTextField();
	private DefaultListModel ftAllListModel = new DefaultListModel();
	private DefaultListModel ftUserListModel = new DefaultListModel();
	private DefaultListModel tmAllListModel = new DefaultListModel();
	private DefaultListModel tmUserListModel = new DefaultListModel();
	private SettingsService ss;

	public void fillFiletypes(String[] filetypes, DefaultListModel model) {
		logger.info("fillAllFiletypes(): start filling filetypes into the specified model");
		if (filetypes == null)
			filetypes = SettingsService.SongFileTypesAll;
		// filetypes = new String[] {"wav"};
		for (int i = 0; i < filetypes.length; i++) {
			model.addElement(filetypes[i].toLowerCase());
		}
	}

	public void fillColumns(String[] columns, DefaultListModel model) {
		logger.info("fillAllFiletypes(): start filling filetypes into the specified model");
		if (columns == null)
			columns = SettingsService.SongTableColumnsAll;
		// columns = new String[] {"Artist"};
		for (int i = 0; i < columns.length; i++) {
			model.addElement(columns[i].toLowerCase());
		}
	}

	public void fillXXXPlayedCount(int value) {
		if (value == 0)
			value = SettingsService.XXXPlayedCountDefault;
		xxPlayed.setText("" + value + "");
	}

	public void fillXXXRatedCount(int value) {
		if (value == 0)
			value = SettingsService.XXXRatedCountDefault;
		xxRated.setText("" + value + "");
	}

	public Settings() {
		logger.info("Settings(): started Settings()");
		setBounds(100, 100, 450, 300);
		setTitle("Settings");

		ServiceFactory sf = ServiceFactory.getInstance();
		ss = sf.getSettingsService();

		initialize();

		fillFiletypes(SettingsService.SongFileTypesAll, ftAllListModel);
		fillFiletypes(ss.getUserFileTypes(), ftUserListModel);

		fillXXXPlayedCount(ss.getTopXXPlayedCount());
		fillXXXRatedCount(ss.getTopXXRatedCount());

		fillColumns(SettingsService.SongTableColumnsAll, tmAllListModel);
		fillColumns(ss.getUserColumns(), tmUserListModel);

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

		allFileTypesList.setModel(ftAllListModel);
		spAllFiletypes = new JScrollPane(allFileTypesList);
		ft.add(spAllFiletypes, "flowx,cell 0 1");

		btnAddFT.addActionListener(this);
		btnAddFT.setActionCommand("addft");
		ft.add(btnAddFT, "flowy,cell 1 1");

		btnDeleteFT.addActionListener(this);
		btnDeleteFT.setActionCommand("deleteft");
		ft.add(btnDeleteFT, "cell 1 1");

		userFileTypesList.setModel(ftUserListModel);
		spUserFiletypes = new JScrollPane(userFileTypesList);
		ft.add(spUserFiletypes, "flowx,cell 2 1");

		// Intelligent Playlists
		ip.add(lblXXplayed, "cell 0 0");
		ip.add(xxPlayed, "cell 0 1,growx");
		ip.add(lblXXrated, "cell 0 2");
		ip.add(xxRated, "cell 0 3,growx");

		// Table Management
		tm.add(lblSupportedColumns, "cell 0 0");
		tm.add(lblAcceptedColumns, "cell 2 0");

		allColumnsList.setModel(tmAllListModel);
		spAllColumns = new JScrollPane(allColumnsList);
		tm.add(spAllColumns, "flowx,cell 0 1");

		btnAddTM.addActionListener(this);
		btnAddTM.setActionCommand("addtm");
		tm.add(btnAddTM, "flowy,cell 1 1");

		btnDeleteTM.addActionListener(this);
		btnDeleteTM.setActionCommand("deletetm");
		tm.add(btnDeleteTM, "cell 1 1");

		userColumnsList.setModel(tmUserListModel);
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

	private boolean check(String[] filetypes, String[] columns) {
		boolean check = true;
		if (xxPlayed.getText().trim().matches("^[1-9]+[0-9]*$") == false
				|| xxRated.getText().trim().matches("^[1-9]+[0-9]*$") == false)
			return false;
		if (filetypes.length < 1
				|| columns.length < 1)
			return false;
		return check;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			logger.info("Settings(): start saving functionylity");
			int ftUserSize = userFileTypesList.getModel().getSize();
			int tmUserSize = userColumnsList.getModel().getSize();
			String[] ftUser = new String[ftUserSize];
			String[] tmUser = new String[tmUserSize];

			for (int i = 0; i < ftUserSize; i++) {
				ftUser[i] = (String) userFileTypesList.getModel().getElementAt(i);
			}

			for (int i = 0; i < tmUserSize; i++) {
				tmUser[i] = (String) userColumnsList.getModel().getElementAt(i);
			}
			
			if (check(ftUser, tmUser)) {
				ss.setUserFileTypes((String[])ftUser);
				ss.setUserColumns((String[])tmUser);
				ss.setTopXXPlayedCount(Integer.parseInt(xxPlayed.getText()));
				ss.setTopXXRatedCount(Integer.parseInt(xxRated.getText()));
				
				logger.info("Settings(): completed saving");

				dispose();
			}
			
			else
				new DynamicDialog("Check your inputs", "Filetypes and columns must be > 0 items; Int. Playlists sizes > 0");
		}

		else if (e.getActionCommand().equals("cancel")) {
			logger.info("Settings(): canceled");
			dispose();
		}

		else if (e.getActionCommand().equals("addft")) {
			logger.info("Settings(): Start filetype adding functionality");
			Object[] selectedFtAll = allFileTypesList.getSelectedValues();
			int ftUserSize = userFileTypesList.getModel().getSize();
			Object[] ftUser = new Object[ftUserSize];
			boolean temp = true;

			if (selectedFtAll.length > 0) {

				for (int i = 0; i < ftUserSize; i++) {
					ftUser[i] = userFileTypesList.getModel().getElementAt(i);
				}

				for (Object x : selectedFtAll) {
					for (Object y : ftUser) {
						if (y.equals(x))
							temp = false;
					}

					if (temp)
						ftUserListModel.addElement(x);
					temp = true;
				}
			}
		}

		else if (e.getActionCommand().equals("deleteft")) {
			logger.info("Settings(): Start filetype deleting functionality");
			Object[] selectedFtUser = userFileTypesList.getSelectedValues();

			if (selectedFtUser.length > 0) {
				for (Object x : selectedFtUser) {
					ftUserListModel.removeElement(x);
				}
			}
		}

		else if (e.getActionCommand().equals("addtm")) {
			logger.info("Settings(): Start column adding functionality");
			Object[] selectedTmAll = allColumnsList.getSelectedValues();
			int tmUserSize = userColumnsList.getModel().getSize();
			Object[] tmUser = new Object[tmUserSize];
			boolean temp = true;

			if (selectedTmAll.length > 0) {

				for (int i = 0; i < tmUserSize; i++) {
					tmUser[i] = userColumnsList.getModel().getElementAt(i);
				}

				for (Object x : selectedTmAll) {
					for (Object y : tmUser) {
						if (y.equals(x))
							temp = false;
					}

					if (temp)
						tmUserListModel.addElement(x);
					temp = true;
				}
			}
		}

		else if (e.getActionCommand().equals("deletetm")) {
			logger.info("Settings(): Start columns deleting functionality");
			Object[] selectedTmUser = userColumnsList.getSelectedValues();

			if (selectedTmUser.length > 0) {
				for (Object x : selectedTmUser) {
					tmUserListModel.removeElement(x);
				}
			}
		}
	}
}