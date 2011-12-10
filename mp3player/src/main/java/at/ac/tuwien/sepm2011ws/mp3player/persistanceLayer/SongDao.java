/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import java.util.List;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;

/**
 * @author klaus
 *
 */
public interface SongDao {
	
	/**
	 * Reads a song with the given id.
	 * 
	 * @param id
	 *            Id of the song which should be read.
	 * @return The song from the DB.
	 * @throws IllegalArgumentException
	 */
	public Song read(int id) throws IllegalArgumentException;
	
	/**
	 * Reads all songs from the DB.
	 * 
	 * @return A List of all songs from the DB.
	 */
	public List<Song> readAll();
	
}
