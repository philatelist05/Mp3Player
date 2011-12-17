/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;

/**
 * @author klaus
 * 
 */
public interface PlaylistService {

	/**
	 * Get the Library playlist
	 * 
	 * @return a playlist with all songs of the library
	 */
	public Playlist getLibrary();

	/**
	 * Returns the current playlist
	 * 
	 * @return the current playlist
	 */
	public Playlist getCurrentPlaylist();

	/**
	 * Sets the current active playlist
	 * 
	 * @param playlist
	 */
	public void setCurrentPlaylist(Playlist playlist);

	/**
	 * Gets the next song in the playlist due to the play mode.
	 * 
	 * @return the next song or <code>null</code> if no song is available
	 */
	public Song getNextSong();

	/**
	 * Gets the previous song in the playlist due to the play mode.
	 * 
	 * @return the previous song or <code>null</code> if no song is available
	 */
	public Song getPreviousSong();

	/**
	 * Sets the play mode.
	 * 
	 * @param playMode
	 *            The desired PlayMode.
	 */
	public void setPlayMode(PlayMode playMode);

	/**
	 * Returns the current play mode.
	 * 
	 * @return the current play mode.
	 */
	public PlayMode getPlayMode();

}
