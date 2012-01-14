/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import java.sql.Connection;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Album;

/**
 * @author klaus K
 *
 */

interface AlbumDao {
	/**
	 * Creates a album.
	 * 
	 * @param a
	 *            The album which will be created.
	 * @return The generated id of the album.
	 * @throws IllegalArgumentException
	 */
	public int create(Album a);
	
	/**
	 * Updates a album.
	 * 
	 * @param a
	 *            The album with the new values and the id of the old
	 *            album.
	 * @throws IllegalArgumentException
	 */
	public void update(Album a);

	/**
	 * Deletes a album.
	 * 
	 * @param id
	 *            The id of the album which should be deleted.
	 * @throws IllegalArgumentException
	 */
	public void delete(int id);
	

	/**
	 * Reads a album with the given id.
	 * 
	 * @param id
	 *            Id of the album which should be read.
	 * @return The album from the DB.
	 * @throws IllegalArgumentException
	 */
	public Album read(int id);
	
	/**
	 * @return the connection used by this dao
	 */
	Connection getConnection();
}
