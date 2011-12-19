/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;

/**
 * @author klaus
 * 
 */
public interface PlayerListener {

	/**
	 * Fires if the player is at the end of a song.
	 * 
	 * @param song
	 *            The next song the player will play.
	 */
	public void endOfMediaEvent(Song song);
	
}
