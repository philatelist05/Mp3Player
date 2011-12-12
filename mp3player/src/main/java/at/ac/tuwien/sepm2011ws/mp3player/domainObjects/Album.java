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

	/**
	 * 
	 * @param title
	 */
	public Album(String title) {
		this.setTitle(title);
	}

	/**
	 * @param id
	 * @param title
	 * @param year
	 * @param albumartPath
	 */
	public Album(int id, String title, int year, String albumartPath) {
		this.setId(id);
		this.setTitle(title);
		this.setYear(year);
		this.setAlbumartPath(albumartPath);
	}

	/**
	 * Returns the id of the Album
	 * 
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Sets the id of the Album
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		if (id < 0)
			throw new IllegalArgumentException("ID must be greater or equal 0");
		this.id = id;
	}

	/**
	 * Returns the title of the Album
	 * 
	 * @return title
	 * 
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Sets the title of the Album
	 * 
	 * @param title
	 * 
	 */
	public void setTitle(String title) {
		if (title == null || title.isEmpty())
			throw new IllegalArgumentException("Title must not be empty");
		this.title = title;
	}

	/**
	 * Returns the year of the Album
	 * 
	 * @return year
	 * 
	 */
	public int getYear() {
		return this.year;
	}

	/**
	 * Sets the year of the Album
	 * 
	 * @param year
	 * 
	 */
	public void setYear(int year) {
		if (year < 0 || year > 9999)
			throw new IllegalArgumentException(
					"Year must be between 0 and 9999");
		this.year = year;
	}

	/**
	 * Returns the path of the Album Cover
	 * 
	 * @return albumartPath
	 * 
	 */
	public String getAlbumartPath() {
		return this.albumartPath;
	}

	/**
	 * Sets the path of the Album Cover
	 * 
	 * @param albumartPath
	 * 
	 */
	public void setAlbumartPath(String albumartPath) {
		if (albumartPath == null || albumartPath.isEmpty())
			throw new IllegalArgumentException(
					"Path of the albumart must not be empty");
		this.albumartPath = albumartPath;
	}

}
