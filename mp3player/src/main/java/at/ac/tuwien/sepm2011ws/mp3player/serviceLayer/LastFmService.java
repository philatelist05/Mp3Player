/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import java.util.List;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;

/**
 * @author klaus
 * 
 */
public interface LastFmService {
	public static final String LastFmApiKey = "b05fea7d70f41670bc5cdb05d9f584e5";
	public static final String LastFmSecret = "3bb892d5387529bf02fc9fa0d02c8673";

	/**
	 * The number of songs which are returned by getSimilarArtistsWithSongs
	 */
	public static final int topSongsCount = 10;
	/**
	 * The number of similar artists which are returned by
	 * getSimilarArtistsWithSongs
	 */
	public static final int similarArtistsCount = 10;

	/**
	 * Gets a playlist for every artist similar to the artist of the specified
	 * song containing the top XX (specified in <code>topSongsCount</code>)
	 * rated and played songs of this artist.
	 * 
	 * @param song
	 *            The song with the artist to search with
	 * @return a playlist for each similar artist
	 * @throws DataAccessException 
	 */
	public List<Playlist> getSimilarArtistsWithSongs(Song song) throws DataAccessException;

}
