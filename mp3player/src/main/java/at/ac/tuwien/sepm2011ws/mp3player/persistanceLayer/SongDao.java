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
	 * @throws DataAccessException 
	 * @throws IllegalArgumentException
	 */
	public int create(Song s) throws DataAccessException;
	
	/**
	 * Updates a song.
	 * 
	 * @param s
	 *            The song with the new values and the id of the old
	 *            song.
	 * @throws DataAccessException 
	 * @throws IllegalArgumentException
	 */
	public void update(Song s) throws DataAccessException;

	/**
	 * Deletes a song.
	 * 
	 * @param id
	 *            The id of the song which should be deleted.
	 * @throws DataAccessException 
	 * @throws IllegalArgumentException
	 */
	public void delete(int id) throws DataAccessException;
	

	/**
	 * Reads a song with the given id.
	 * 
	 * @param id
	 *            Id of the song which should be read.
	 * @return The song from the DB.
	 * @throws DataAccessException 
	 * @throws IllegalArgumentException
	 */
	public Song read(int id) throws DataAccessException;

	/**
	 * Reads all songs from the DB.
	 * 
	 * @return A List of all songs from the DB.
	 * @throws DataAccessException 
	 */
	public List<Song> readAll() throws DataAccessException;
	
	/**
	 * @return the connection used by this dao
	 */
	public Connection getConnection();
}
