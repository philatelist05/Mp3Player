package at.ac.tuwien.sepm2011ws.mp3player.domainObjects;

public class MetaTags {
	private String artist;
	private String title;
	private int duration; // in seconds
	private int year;
	private String genre;
	private Album album;

	/**
	 * @param artist
	 * @param title
	 * @param duration
	 * @param year
	 * @param genre
	 * @param album
	 */
	public MetaTags(String artist, String title, int duration, int year,
			String genre, Album album) {
		this.artist = artist;
		this.title = title;
		this.duration = duration;
		this.year = year;
		this.genre = genre;
		this.album = album;
	}

	/**
	 * @return the artist
	 */
	public String getArtist() {
		return this.artist;
	}

	/**
	 * @param artist
	 *            the artist to set
	 */
	public void setArtist(String artist) throws IllegalArgumentException {
		if (artist == null || artist.isEmpty())
			throw new IllegalArgumentException("Artist must not be empty");
		this.artist = artist;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) throws IllegalArgumentException {
		if (title == null || title.isEmpty())
			throw new IllegalArgumentException("Title must not be empty");
		this.title = title;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return this.duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(int duration) throws IllegalArgumentException {
		if (duration < 0)
			throw new IllegalArgumentException(
					"Duration must be greater than 0");
		this.duration = duration;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return this.year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(int year) throws IllegalArgumentException {
		if (year < 0)
			throw new IllegalArgumentException("Year must be above 0");
		this.year = year;
	}

	/**
	 * @return the genre
	 */
	public String getGenre() {
		return this.genre;
	}

	/**
	 * @param genre
	 *            the genre to set
	 */
	public void setGenre(String genre) {
		this.genre = genre;
	}

	/**
	 * @return the album
	 */
	public Album getAlbum() {
		return this.album;
	}

	/**
	 * @param album
	 *            the album to set
	 */
	public void setAlbum(Album album) {
		this.album = album;
	}
}
