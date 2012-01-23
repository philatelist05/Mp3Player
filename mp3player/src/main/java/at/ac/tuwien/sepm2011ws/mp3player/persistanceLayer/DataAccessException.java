package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

public class DataAccessException extends Exception {
	private static final long serialVersionUID = -2911645354109063445L;

	/**
	 * @param message
	 */
	public DataAccessException(String message) {
		super(message);
	}

}
