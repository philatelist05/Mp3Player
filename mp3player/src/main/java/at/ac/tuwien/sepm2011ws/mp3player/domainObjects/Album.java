/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.domainObjects;

/**
 * @author oli
 *
 */
public class Album {
	
	private int id;
	private String title;
	private int year;
	private String albumartPath;

	public Album() {
		this(-1, "Untitled Album", 0, null);
	}

	/**
	 * @param id
	 * @param title
	 * @param year
	 * @param albumartPath
	 */
	public Album(int id, String title, int year, String albumartPath) {
		this.id = id;
		this.title = title;
		this.year = year;
		this.albumartPath = albumartPath;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the albumartPath
	 */
	public String getAlbumartPath() {
		return this.albumartPath;
	}

	/**
	 * @param albumartPath the albumartPath to set
	 */
	public void setAlbumartPath(String albumartPath) {
		this.albumartPath = albumartPath;
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
	 * @return albumartPath
	 * 
	 */
	public String getAlbumartpath() {
		return this.albumartPath;
	}

	/**
	 * 
	 * Sets the path of the Album Cover
	 * 
	 * @param albumartPath
	 * 
	 */
	public void setAlbumartpath(String albumart_path) {
		this.albumartPath = albumart_path;
	}

}
