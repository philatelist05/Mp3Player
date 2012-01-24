package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
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
	private JLabel lblSupportedFiletypes = new JLabel("Available Filetypes:");
	private JLabel lblAcceptedFiletypes = new JLabel("Accepted Filetypes:");
	private JLabel lblSupportedColumns = new JLabel("Available Columns:");
	private JLabel lblAcceptedColumns = new JLabel("Chosen Columns:");
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
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private Dimension dim = toolkit.getScreenSize();
	private int width;
	private int height;
	private int positionX;
	private int positionY;

	public void fillFiletypesAll(String[] filetypes, DefaultListModel model) {
		logger.info("fillFiletypesAll(): start filling filetypes into the specified model");
		List<String> all = Arrays.asList(filetypes);
		List<String> user = Arrays.asList(ss.getUserFileTypes());

		for (String x : all) {
			if (!user.contains(x))
				model.addElement(x);
		}
	}

	public void fillColumnsAll(String[] columns, DefaultListModel model) {
		logger.info("fillColumnsAll(): start filling filetypes into the specified model");
		List<String> all = Arrays.asList(columns);
		List<String> user = Arrays.asList(ss.getUserColumns());

		for (String x : all) {
			if (!user.contains(x))
				model.addElement(x);
		}
	}

	public void fillFiletypesUser(String[] filetypes, DefaultListModel model) {
		logger.info("fillFiletypesUser(): start filling filetypes into the specified model");
		for (int i = 0; i < filetypes.length; i++) {
			model.addElement(filetypes[i]);
		}
	}

	public void fillColumnsUser(String[] columns, DefaultListModel model) {
		logger.info("fillColumnsUser(): start filling filetypes into the specified model");
		for (int i = 0; i < columns.length; i++) {
			model.addElement(columns[i]);
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

		width = 450;
		height = 300;
		positionX = (int) Math.round(dim.getWidth() / 2 - width / 2);
		positionY = (int) Math.round(dim.getHeight() / 2 - height / 2);
		setBounds(positionX, positionY, width, height);
		setTitle("Settings");

		ServiceFactory sf = ServiceFactory.getInstance();
		ss = sf.getSettingsService();

		initialize();

		fillFiletypesAll(SettingsService.SongFileTypesAll, ftAllListModel);
		fillFiletypesUser(ss.getUserFileTypes(), ftUserListModel);

		fillXXXPlayedCount(ss.getTopXXPlayedCount());
		fillXXXRatedCount(ss.getTopXXRatedCount());

		fillColumnsAll(SettingsService.SongTableColumnsAll, tmAllListModel);
		fillColumnsUser(ss.getUserColumns(), tmUserListModel);

		setResizable(false);
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

		allFileTypesList.setVisibleRowCount(9);
		allFileTypesList.setFixedCellWidth(165);
		allFileTypesList.setModel(ftAllListModel);
		spAllFiletypes = new JScrollPane(allFileTypesList);
		ft.add(spAllFiletypes, "flowx,cell 0 1, growx");

		btnAddFT.addActionListener(this);
		btnAddFT.setActionCommand("addft");
		ft.add(btnAddFT, "flowy,cell 1 1, alignx center");

		btnDeleteFT.addActionListener(this);
		btnDeleteFT.setActionCommand("deleteft");
		ft.add(btnDeleteFT, "cell 1 1, alignx center");

		userFileTypesList.setVisibleRowCount(9);
		userFileTypesList.setFixedCellWidth(165);
		userFileTypesList.setModel(ftUserListModel);
		spUserFiletypes = new JScrollPane(userFileTypesList);
		ft.add(spUserFiletypes, "flowx,cell 2 1, growx");

		// Intelligent Playlists
		ip.add(lblXXplayed, "cell 0 0");
		ip.add(xxPlayed, "cell 0 1,growx");
		ip.add(lblXXrated, "cell 0 2");
		ip.add(xxRated, "cell 0 3,growx");

		// Table Management
		tm.add(lblSupportedColumns, "cell 0 0");
		tm.add(lblAcceptedColumns, "cell 2 0");

		allColumnsList.setVisibleRowCount(9);
		allColumnsList.setFixedCellWidth(165);
		allColumnsList.setModel(tmAllListModel);
		spAllColumns = new JScrollPane(allColumnsList);
		tm.add(spAllColumns, "flowx,cell 0 1, growx");

		btnAddTM.addActionListener(this);
		btnAddTM.setActionCommand("addtm");
		tm.add(btnAddTM, "flowy,cell 1 1, alignx center");

		btnDeleteTM.addActionListener(this);
		btnDeleteTM.setActionCommand("deletetm");
		tm.add(btnDeleteTM, "cell 1 1, alignx center");

		userColumnsList.setVisibleRowCount(9);
		userColumnsList.setFixedCellWidth(165);
		userColumnsList.setModel(tmUserListModel);
		spUserColumns = new JScrollPane(userColumnsList);
		tm.add(spUserColumns, "flowx,cell 2 1, growx");

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
		if (filetypes.length < 1 || columns.length < 1)
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
				ftUser[i] = (String) userFileTypesList.getModel().getElementAt(
						i);
			}

			for (int i = 0; i < tmUserSize; i++) {
				tmUser[i] = (String) userColumnsList.getModel().getElementAt(i);
			}

			if (check(ftUser, tmUser)) {
				ss.setUserFileTypes((String[]) ftUser);
				ss.setUserColumns((String[]) tmUser);
				ss.setTopXXPlayedCount(Integer.parseInt(xxPlayed.getText()));
				ss.setTopXXRatedCount(Integer.parseInt(xxRated.getText()));

				logger.info("Settings(): completed saving");

				dispose();
			}

			else
				JOptionPane
						.showConfirmDialog(
								null,
								"Count of filetypes and columns must be > 0 items; Sizes of intelligent playlists > 0",
								"Check your inputs!", JOptionPane.CLOSED_OPTION);
		}

		else if (e.getActionCommand().equals("cancel")) {
			logger.info("Settings(): canceled");
			dispose();
		}

		else if (e.getActionCommand().equals("addft")) {
			logger.info("Settings(): Start filetype adding functionality");
			Object[] selectedFtAll = allFileTypesList.getSelectedValues();

			if (selectedFtAll.length > 0) {

				for (Object x : selectedFtAll) {
					ftUserListModel.addElement(x);
					ftAllListModel.removeElement(x);
				}
			}
		}

		else if (e.getActionCommand().equals("deleteft")) {
			logger.info("Settings(): Start filetype deleting functionality");
			Object[] selectedFtUser = userFileTypesList.getSelectedValues();

			if (selectedFtUser.length > 0) {
				for (Object x : selectedFtUser) {
					ftUserListModel.removeElement(x);
					ftAllListModel.addElement(x);
				}
			}
		}

		else if (e.getActionCommand().equals("addtm")) {
			logger.info("Settings(): Start column adding functionality");
			Object[] selectedTmAll = allColumnsList.getSelectedValues();

			if (selectedTmAll.length > 0) {
				for (Object x : selectedTmAll) {
					tmAllListModel.removeElement(x);
					tmUserListModel.addElement(x);
				}
			}
		}

		else if (e.getActionCommand().equals("deletetm")) {
			logger.info("Settings(): Start columns deleting functionality");
			Object[] selectedTmUser = userColumnsList.getSelectedValues();

			if (selectedTmUser.length > 0) {
				for (Object x : selectedTmUser) {
					tmUserListModel.removeElement(x);
					tmAllListModel.addElement(x);
				}
			}
		}
	}
}