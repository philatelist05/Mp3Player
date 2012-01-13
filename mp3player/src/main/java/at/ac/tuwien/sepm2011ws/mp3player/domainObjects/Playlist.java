/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.domainObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * @author klaus
 * 
 */
public class Playlist {

	private int id;
	private String title;
	private List<Song> songs;
	private boolean readonly;

	/**
	 * @param title
	 */
	public Playlist(String title) {
		this.setTitle(title);
		this.setSongs(new ArrayList<Song>());
		this.setReadonly(false);
	}

	/**
	 * @param id
	 * @param title
	 * @param songs
	 * @param readonly
	 */
	public Playlist(int id, String title, List<Song> songs, boolean readonly) {
		this.setId(id);
		this.setTitle(title);
		this.setSongs(songs);
		this.setReadonly(readonly);
	}

	/**
	 * Adds a song to the playlist
	 * 
	 * @param song
	 *            The song to be added
	 */
	public void addSong(Song song) {
		this.songs.add(song);
	}

	/**
	 * Removes a song from the playlist if it exists
	 * 
	 * @param song
	 *            The song to be removed
	 */
	public void removeSong(Song song) {
		this.songs.remove(song);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return this.id;
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
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
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

	/**
	 * @return the songs
	 */
	public List<Song> getSongs() {
		return this.songs;
	}

	/**
	 * @param songs
	 *            the songs to set
	 */
	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}

	/**
	 * @return the readonly
	 */
	public boolean isReadonly() {
		return this.readonly;
	}

	/**
	 * @param readonly
	 *            the readonly to set
	 */
	public void setReadonly(boolean isReadonly) {
		this.readonly = isReadonly;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && this.getClass() == obj.getClass()) {
			Playlist other = (Playlist) obj;

			if (!(this.readonly == other.readonly && this.id == other.id && this.title == other.title))
				return false;

			for (int i = 0; i < this.songs.size(); i++) {
				if (i >= other.songs.size() || !this.songs.get(i).equals(other.songs.get(i)))
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
