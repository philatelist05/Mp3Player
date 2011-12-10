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

	private String title;
	private List<Song> songs;
	private boolean readonly;

	/**
	 * 
	 */
	public Playlist() {
		this("Untitled Playlist", new ArrayList<Song>(), false);
	}

	/**
	 * @param title
	 * @param songs
	 * @param readonly
	 */
	public Playlist(String title, List<Song> songs, boolean readonly) {
		this.title = title;
		this.songs = songs;
		this.readonly = readonly;
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
	 * @param readonly the readonly to set
	 */
	public void setReadonly(boolean isReadonly) {
		this.readonly = isReadonly;
	}

}
