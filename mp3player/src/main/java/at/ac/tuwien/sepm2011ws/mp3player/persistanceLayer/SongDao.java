package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;

import java.sql.Connection;
import java.util.List;

public interface SongDao {
	/**
	 * Creates a song.
	 * 
	 * @param s
	 *            The song which will be created.
	 * @throws DataAccessException 
	 */
	public void create(Song s) ;
	
	/**
	 * Updates a song.
	 * 
	 * @param s
	 *            The song with the new values and the id of the old
	 *            song.
	 * @throws DataAccessException 
	 */
	public void update(Song s) ;

	/**
	 * Deletes a song.
	 * 
	 * @param id
	 *            The id of the song which should be deleted.
	 * @throws DataAccessException 
	 */
	public void delete(int id) ;
	

	/**
	 * Reads a song with the given id.
	 * 
	 * @param id
	 *            Id of the song which should be read.
	 * @return The song from the DB.
	 * @throws DataAccessException 
	 */
	public Song read(int id) ;

	/**
	 * Reads all songs from the DB.
	 * 
	 * @return A list of all songs from the DB.
	 * @throws DataAccessException 
	 */
	public List<Song> readAll() ;
	
	/**
	 * Reads all top xx rated songs from the DB.
	 * 
	 * @param number the number xx stands for
	 * @return A list of all xx rated songs from the DB.
	 * @throws DataAccessException 
	 */
	public List<Song> getTopRatedSongs(int number) ;
	
	/**
	 * Reads all top xx played songs from the DB.
	 * 
	 * @param number the number xx stands for
	 * @return A list of all xx played songs from the DB.
	 * @throws DataAccessException 
	 */
	public List<Song> getTopPlayedSongs(int number) ;

    public Connection getDbConnection();
}
