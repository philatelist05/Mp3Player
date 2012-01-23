package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

public interface SettingsService {
	/**
	 * All supported song file extensions.
	 */
	public static final String[] SongFileTypesAll = new String[] { "wav", "mp3" };

	/**
	 * All supported columns of the song table of the main window.
	 */
	public static final String[] SongTableColumnsAll = new String[] { "Title",
			"Artist", "Album", "Year", "Genre", "Duration", "Rating",
			"Playcount" };

	/**
	 * Default number of songs in the generated TopXXPlayed playlist.
	 */
	public static final int XXXPlayedCountDefault = 40;

	/**
	 * Default number of songs in the generated TopXXRated playlist.
	 */
	public static final int XXXRatedCountDefault = 40;

	/**
	 * Gets the setting of the selected file types which should be accepted.
	 * 
	 * @return a string array of the user file types
	 */
	public String[] getUserFileTypes();

	/**
	 * Sets the selected file types which should be accepted.
	 * 
	 * @param types
	 *            The accepted file types
	 */
	public void setUserFileTypes(String[] types);

	/**
	 * Gets the setting of the users columns in the song table of the main
	 * window.
	 * 
	 * @return a string array with the user columns
	 */
	public String[] getUserColumns();

	/**
	 * Sets the users columns in the song table of the main window.
	 * 
	 * @param cols
	 *            The selected columns
	 */
	public void setUserColumns(String[] cols);

	/**
	 * Gets the setting of the number of songs in the generated TopXXRated
	 * playlist.
	 * 
	 * @return the count of songs
	 */
	public int getTopXXRatedCount();

	/**
	 * Sets the setting of the number of songs in the generated TopXXRated
	 * playlist.
	 * 
	 * @param count
	 *            The count of songs
	 */
	public void setTopXXRatedCount(int count);

	/**
	 * Gets the setting of the number of songs in the generated TopXXPlayed
	 * playlist.
	 * 
	 * @return the count of songs
	 */
	public int getTopXXPlayedCount();

	/**
	 * Sets the setting of the number of songs in the generated TopXXPlayed
	 * playlist.
	 * 
	 * @param count
	 *            The count of songs
	 */
	public void setTopXXPlayedCount(int count);
}
