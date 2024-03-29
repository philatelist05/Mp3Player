package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Album;

public interface AlbumDao {
	/**
	 * Creates a album.
	 * 
	 * @param a
	 *            The album which will be created.
	 * @throws DataAccessException
	 */
	public void create(Album a) ;

	/**
	 * Updates a album.
	 * 
	 * @param a
	 *            The album with the new values and the id of the old album.
	 * @throws DataAccessException
	 */
	public void update(Album a) ;

	/**
	 * Deletes a album.
	 * 
	 * @param id
	 *            The id of the album which should be deleted.
	 * @throws DataAccessException
	 */
	public void delete(int id) ;

	/**
	 * Reads a album with the given id.
	 * 
	 * @param id
	 *            Id of the album which should be read.
	 * @return The album from the DB.
	 * @throws DataAccessException
	 */
	public Album read(int id) ;
}
