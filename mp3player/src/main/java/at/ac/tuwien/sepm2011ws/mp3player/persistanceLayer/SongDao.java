package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import java.sql.Connection;
import java.util.List;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;

public interface SongDao {
	/**
	 * Creates a song.
	 * 
	 * @param s
	 *            The song which will be created.
	 * @throws DataAccessException 
	 */
	public void create(Song s) throws DataAccessException;
	
	/**
	 * Updates a song.
	 * 
	 * @param s
	 *            The song with the new values and the id of the old
	 *            song.
	 * @throws DataAccessException 
	 */
	public void update(Song s) throws DataAccessException;

	/**
	 * Deletes a song.
	 * 
	 * @param id
	 *            The id of the song which should be deleted.
	 * @throws DataAccessException 
	 */
	public void delete(int id) throws DataAccessException;
	

	/**
	 * Reads a song with the given id.
	 * 
	 * @param id
	 *            Id of the song which should be read.
	 * @return The song from the DB.
	 * @throws DataAccessException 
	 */
	public Song read(int id) throws DataAccessException;

	/**
	 * Reads all songs from the DB.
	 * 
	 * @return A list of all songs from the DB.
	 * @throws DataAccessException 
	 */
	public List<Song> readAll() throws DataAccessException;
	
	/**
	 * Reads all top xx rated songs from the DB.
	 * 
	 * @param number the number xx stands for
	 * @return A list of all xx rated songs from the DB.
	 * @throws DataAccessException 
	 */
	public List<Song> getTopRatedSongs(int number) throws DataAccessException;
	
	/**
	 * Reads all top xx played songs from the DB.
	 * 
	 * @param number the number xx stands for
	 * @return A list of all xx played songs from the DB.
	 * @throws DataAccessException 
	 */
	public List<Song> getTopPlayedSongs(int number) throws DataAccessException;
	
	/**
	 * @return the connection used by this DAO
	 */
	public Connection getConnection();
}
