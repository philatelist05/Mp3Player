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
	
}
