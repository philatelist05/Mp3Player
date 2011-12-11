/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.domainObjects;

/**
 * @author oli
 *
 */
public class Album {
	
	private String title;
	private int year;
	private String albumart_path;

	public Album() {
		this("Untitled Album", 0, null);
	}

	/**
	 * @param title
	 * @param year
	 * @param albumart_path
	 */
	public Album(String title, int year, String albumart_path) {
		this.title = title;
		this.year = year;
		this.albumart_path = albumart_path;
	}

	/**
	 * 
	 * Returns the title of the Album
	 * 
	 * @return title
	 * 
	 */
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * 
	 * Sets the title of the Album
	 * 
	 * @param title
	 * 
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * Returns the year of the Album
	 * 
	 * @return year
	 * 
	 */
	public int getYear() {
		return this.year;
	}

	/**
	 * 
	 * Sets the year of the Album
	 * 
	 * @param year
	 * 
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * 
	 * Returns the path of the Album Cover
	 * 
	 * @return albumart_path
	 * 
	 */
	public String getAlbumartpath() {
		return this.albumart_path;
	}

	/**
	 * 
	 * Sets the path of the Album Cover
	 * 
	 * @param albumart_path
	 * 
	 */
	public void setAlbumartpath(String albumart_path) {
		this.albumart_path = albumart_path;
	}

}
