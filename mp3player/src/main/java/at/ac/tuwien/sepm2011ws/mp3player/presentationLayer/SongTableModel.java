package at.ac.tuwien.sepm2011ws.mp3player.presentationLayer;


import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class SongTableModel extends DefaultTableModel {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 700061947762224827L;
	
	public SongTableModel() {
		super();
	}

	/**
	 * @param rowCount
	 * @param columnCount
	 */
	public SongTableModel(int rowCount, int columnCount) {
		super(rowCount, columnCount);
	}

	/**
	 * @param columnNames
	 * @param rowCount
	 */
	public SongTableModel(Object[] columnNames, int rowCount) {
		super(columnNames, rowCount);
	}

	/**
	 * @param data
	 * @param columnNames
	 */
	public SongTableModel(Object[][] data, Object[] columnNames) {
		super(data, columnNames);
	}

	/**
	 * @param columnNames
	 * @param rowCount
	 */
	public SongTableModel(Vector<?> columnNames, int rowCount) {
		
		super(columnNames, rowCount);
	}

	/**
	 * @param data
	 * @param columnNames
	 */
	public SongTableModel(Vector<?> data, Vector<?> columnNames) {
		super(data, columnNames);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		if(column == 7)
			return true;
		return false;
	}

}
