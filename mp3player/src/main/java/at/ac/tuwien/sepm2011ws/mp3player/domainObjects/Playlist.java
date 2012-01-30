package at.ac.tuwien.sepm2011ws.mp3player.domainObjects;

import java.util.ArrayList;

public class Playlist extends ArrayList<Song> {

	private static final long serialVersionUID = 7141247972795466434L;
	protected int id;
	protected String title;

	/**
	 * @param title
	 */
	public Playlist(String title) {
		this(0, title);
	}

	/**
	 * @param id
	 * @param title
	 */
	public Playlist(int id, String title) {
		if(title == null)
			throw new IllegalArgumentException("Title must not be null");
		
		this.id = id;
		this.title = title;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && this.getClass() == obj.getClass()) {
			Playlist other = (Playlist) obj;

			if (this.id != other.id || !this.title.equals(other.title))
				return false;

			for (int i = 0; i < this.size(); i++) {
				if (i >= other.size() || !this.get(i).equals(other.get(i)))
					return false;
			}

			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return this.title;
	}

}
