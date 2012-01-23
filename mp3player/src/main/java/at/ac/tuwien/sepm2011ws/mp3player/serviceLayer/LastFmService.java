/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import java.util.List;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;

/**
 * @author klaus
 * 
 */
public interface LastFmService {

	/**
	 * The number of songs which are returned by (for example)
	 * getSimilarArtistsWithSongs
	 */
	public static final int topSongsCount = 10;

	/**
	 * Gets a playlist for every artist similar to the artist of the specified
	 * song containing the top XX (specified in <code>topSongsCount</code>)
	 * rated and played songs of this artist.
	 * 
	 * @param song
	 *            The song with the artist to search with
	 * @return a playlist for each similar artist
	 */
	public List<Playlist> getSimilarArtistsWithSongs(Song song);

}
