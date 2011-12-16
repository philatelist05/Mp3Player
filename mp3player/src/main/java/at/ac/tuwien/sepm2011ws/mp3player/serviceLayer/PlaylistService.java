/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;

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
