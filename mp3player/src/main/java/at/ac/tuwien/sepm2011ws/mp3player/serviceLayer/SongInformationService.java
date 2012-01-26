package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import java.util.List;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Lyric;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.MetaTags;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;

/**
 * @author klaus
 * 
 */
public interface SongInformationService {

	/**
	 * Reads the ID3 tags of the song file and sets them in the
	 * <code>Song</code> object.
	 * 
	 * @param song
	 *            The song whichs tags should be updated from its song file
	 */
	public void getMetaTags(Song song);

	/**
	 * Sets the ID3 tags of the song persistently.
	 * 
	 * @param song
	 *            The song with the updated tags
	 */
	public void setMetaTags(Song song);

	/**
	 * Downloads a list of similar and/or corrected meta tags for the specified
	 * song.
	 * 
	 * @param song
	 *            The song to search for
	 * @return a list of similar and/or corrected meta tags
	 */
	public List<MetaTags> downloadMetaTags(Song song);

	/**
	 * Downloads a list of lyrics for the specified song.
	 * 
	 * @param song
	 *            The song to search the lyrics for
	 * @return the lyrics of the song
	 * @throws DataAccessException
	 */
	public List<Lyric> downloadLyrics(Song song) throws DataAccessException;

	/**
	 * Increments the playcount of the song.
	 * 
	 * @param song
	 */
	public void incrementPlaycount(Song song);

	/**
	 * Sets the rating of a song.
	 * 
	 * @param song
	 *            The song for which to set the rating
	 * @param rating
	 *            The rating to set
	 */
	public void setRating(Song song, int rating);

	/**
	 * Saves the lyrics of a song.
	 * 
	 * @param song
	 *            The song with the lyrics
	 */
	public void saveLyrics(Song song);

}
