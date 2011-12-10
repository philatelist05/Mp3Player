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
	private String url;
	private int year;
	private String artist;
	private String genre;
	private Album album;
	private Lyric lyric;

	/**
	 * 
	 */
	public Song() {
	}

	/**
	 * @param id
	 * @param title
	 * @param duration
	 * @param playcount
	 * @param rating
	 * @param url
	 * @param year
	 * @param artist
	 * @param genre
	 * @param album
	 * @param lyric
	 */
	public Song(int id, String title, int duration, int playcount, int rating,
			String url, int year, String artist, String genre, Album album,
			Lyric lyric) {
		this.id = id;
		this.title = title;
		this.duration = duration;
		this.playcount = playcount;
		this.rating = rating;
		this.url = url;
		this.year = year;
		this.artist = artist;
		this.genre = genre;
		this.album = album;
		this.lyric = lyric;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * @return the playcount
	 */
	public int getPlaycount() {
		return playcount;
	}

	/**
	 * @param playcount the playcount to set
	 */
	public void setPlaycount(int playcount) {
		this.playcount = playcount;
	}

	/**
	 * @return the rating
	 */
	public int getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return the artist
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * @param artist the artist to set
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
	 * @param genre the genre to set
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
	 * @param album the album to set
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
	 * @param lyric the lyric to set
	 */
	public void setLyric(Lyric lyric) {
		this.lyric = lyric;
	}

}
