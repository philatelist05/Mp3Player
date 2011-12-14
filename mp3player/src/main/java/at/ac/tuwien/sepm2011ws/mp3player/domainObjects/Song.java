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
	 * @param title
	 * @param duration
	 * @param path
	 * @param artist
	 */
	public Song(String artist, String title, int duration, String path) {
		this.setArtist(artist);
		this.setTitle(title);
		this.setDuration(duration);
		this.setPath(path);
		this.setRating(-1);
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
			throw new IllegalArgumentException("Title must not be empty");
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
		if (duration < 0)
			throw new IllegalArgumentException(
					"Duration must be greater than 0");
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
			throw new IllegalArgumentException(
					"PlayCount must be grater than 0");
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
			throw new IllegalArgumentException(
					"Rating must be between -1 and 10");
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
			throw new IllegalArgumentException("Path must not be empty");
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
		if (artist == null || artist.isEmpty())
			throw new IllegalArgumentException("Artist must not be empty");
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

	@Override
	public boolean equals(Object obj) {
		Song other;
		if (obj != null && this.getClass() == obj.getClass()) {
			other = (Song) obj;
			if (this.getId() != other.getId())
				return false;
			if (!this.getTitle().equals(other.getTitle()))
				return false;
			if (this.getDuration() != other.getDuration())
				return false;
			if (this.getPlaycount() != other.getPlaycount())
				return false;
			if (this.getRating() != other.getRating())
				return false;
			if (!this.getPath().equals(other.getPath()))
				return false;
			if (this.getYear() != other.getYear())
				return false;
			if (!this.getArtist().equals(other.getArtist()))
				return false;

			// If not both are null or if they are unequal (to not get a NullPointerException)
			if (this.getGenre() != null && other.getGenre() != null) {
				if (!this.getGenre().equals(other.getGenre()))
					return false;
			} else if (!(this.getGenre() == null && other.getGenre() == null))
				return false;

			if (this.getAlbum() != null && other.getAlbum() != null) {
				if (!this.getAlbum().equals(other.getAlbum()))
					return false;
			} else if (!(this.getAlbum() == null && other.getAlbum() == null))
				return false;

			if (this.getLyric() != null && other.getLyric() != null) {
				if (!this.getLyric().equals(other.getLyric()))
					return false;
			} else if (!(this.getLyric() == null && other.getLyric() == null))
				return false;

		} else {
			return false;
		}

		return true;
	}
}
