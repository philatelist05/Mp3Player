package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

public class DynamicDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 5140197132836547966L;
	private JPanel ok = new JPanel(new MigLayout("", "[grow]", "[]"));;
	private JButton btnOK = new JButton("OK");
	private JLabel msg = new JLabel("");

	/**
	 * Create the frame.
	 */
	public DynamicDialog(String title, String message) {
		this.setTitle(title);
		msg.setText(message);
		initialize();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 100);
		setModal(true);
		setVisible(true);
	}

	private void initialize() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 100);
		setModal(true);
		
		getContentPane().add(ok);
		
		msg.setHorizontalAlignment(SwingConstants.LEFT);
		ok.add(msg, "cell 0 0");
		
		btnOK.setHorizontalAlignment(SwingConstants.RIGHT);
		btnOK.addActionListener(this);
		btnOK.setActionCommand("OK");
		ok.add(this.btnOK, "cell 0 1,alignx center");
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("OK")){
			this.dispose();
		}		
	}
}
