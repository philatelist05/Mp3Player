/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import java.sql.Connection;
import java.util.List;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;

/**
 * @author klaus
 * 
 */
public interface SongDao {
	/**
	 * Creates a song.
	 * 
	 * @param s
	 *            The song which will be created.
	 * @return The generated id of the song.
	 * @throws IllegalArgumentException
	 */
	public int create(Song s);
	
	/**
	 * Updates a song.
	 * 
	 * @param s
	 *            The song with the new values and the id of the old
	 *            song.
	 * @throws IllegalArgumentException
	 */
	public void update(Song s);

	/**
	 * Deletes a song.
	 * 
	 * @param id
	 *            The id of the song which should be deleted.
	 * @throws IllegalArgumentException
	 */
	public void delete(int id);
	

	/**
	 * Reads a song with the given id.
	 * 
	 * @param id
	 *            Id of the song which should be read.
	 * @return The song from the DB.
	 * @throws IllegalArgumentException
	 */
	public Song read(int id);

	/**
	 * Reads all songs from the DB.
	 * 
	 * @return A List of all songs from the DB.
	 */
	public List<Song> readAll();
	
	/**
	 * @return the connection used by this dao
	 */
	public Connection getConnection();
}
