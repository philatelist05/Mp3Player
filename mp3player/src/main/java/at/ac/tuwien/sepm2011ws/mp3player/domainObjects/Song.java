/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.domainObjects;

/**
 * @author klaus
 * 
 */
public class Song {
	private int id;
	private String title;
	private int duration; // in seconds
	private int playcount;
	private int rating;
	private String path;
	private int year;
	private String artist;
	private String genre;
	private Album album;
	private Lyric lyric;

	/**
	 * 
	 */
	public Song() throws IllegalArgumentException {
		this(-1, null, -1, 0, -1, null, -1, null, null, null, null);
	}

	/**
	 * @param id
	 * @param title
	 * @param duration
	 * @param playcount
	 * @param rating
	 * @param path
	 * @param year
	 * @param artist
	 * @param genre
	 * @param album
	 * @param lyric
	 */
	public Song(int id, String title, int duration, int playcount, int rating,
			String path, int year, String artist, String genre, Album album,
			Lyric lyric) throws IllegalArgumentException {
	    	this.setId(id);
	    	this.setTitle(title);
	    	this.setDuration(duration);
	    	this.setPlaycount(playcount);
	    	this.setRating(rating);
	    	this.setPath(path);
	    	this.setYear(year);
	    	this.setArtist(artist);
	    	this.setGenre(genre);
	    	this.setAlbum(album);
	    	this.setLyric(lyric);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) throws IllegalArgumentException {
	    if (id < 0)
		throw new IllegalArgumentException("ID must be greater or equal 0");
	    this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) throws IllegalArgumentException {
	    if (title == null || title.isEmpty())
		throw new IllegalArgumentException("Title must contain characters");
	    this.title = title;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(int duration) throws IllegalArgumentException {
	    if(duration < 0)
		throw new IllegalArgumentException("Duration must be greater than 0");
	    this.duration = duration;
	}

	/**
	 * @return the playcount
	 */
	public int getPlaycount() {
		return playcount;
	}

	/**
	 * @param playcount
	 *            the playcount to set
	 */
	public void setPlaycount(int playcount) throws IllegalArgumentException {
	    if (playcount < 0)
		throw new IllegalArgumentException("PlayCount must be grater than 0");
	    this.playcount = playcount;
	}

	/**
	 * @return the rating
	 */
	public int getRating() {
		return rating;
	}

	/**
	 * @param rating
	 *            the rating to set
	 */
	public void setRating(int rating) throws IllegalArgumentException {
	    if (rating < -1 || rating > 10)
		throw new IllegalArgumentException("Rating must be between -1 and 10");
		this.rating = rating;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) throws IllegalArgumentException {
	    if (path == null || path.isEmpty())
		throw new IllegalArgumentException("Path must contain characters");
	    this.path = path;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(int year) throws IllegalArgumentException {
	    if (year < -1 || year > 9999)
		throw new IllegalArgumentException("Year must have 4 digits");
	    this.year = year;
	}

	/**
	 * @return the artist
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * @param artist
	 *            the artist to set
	 */
	public void setArtist(String artist) {
		this.artist = artist;
	}

	/**
	 * @return the genre
	 */
	public String getGenre() {
		return genre;
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
		return album;
	}

	/**
	 * @param album
	 *            the album to set
	 */
	public void setAlbum(Album album) {
		this.album = album;
	}

	/**
	 * @return the lyric
	 */
	public Lyric getLyric() {
		return lyric;
	}

	/**
	 * @param lyric
	 *            the lyric to set
	 */
	public void setLyric(Lyric lyric) {
		this.lyric = lyric;
	}
}
