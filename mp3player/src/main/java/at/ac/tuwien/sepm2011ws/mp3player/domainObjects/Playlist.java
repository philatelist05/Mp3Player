package at.ac.tuwien.sepm2011ws.mp3player.domainObjects;

public class Playlist extends ReadonlyPlaylist {

	public Playlist(int id, String title) {
		super(id, title);
	}

	public Playlist(String title) {
		super(title);
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		if (id < 0)
			throw new IllegalArgumentException("ID must be greater or equal 0");
		this.id = id;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		if (title == null || title.isEmpty())
			throw new IllegalArgumentException("Title must not be empty");
		this.title = title;
	}
}
