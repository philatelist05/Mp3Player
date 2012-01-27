package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import java.sql.Connection;
import java.util.List;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.WritablePlaylist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;

public interface PlaylistDao {
	/**
	 * Creates a playlist.
	 * 
	 * @param p
	 *            The playlist which will be created.
	 * @throws DataAccessException
	 */
	public void create(WritablePlaylist p) throws DataAccessException;

	/**
	 * Updates a playlist but doesn't rename it.
	 * 
	 * @param p
	 *            The playlist with the new values and the id of the old
	 *            playlist.
	 * @throws DataAccessException
	 */
	public void update(WritablePlaylist p) throws DataAccessException;

	/**
	 * Deletes a playlist.
	 * 
	 * @param id
	 *            The id of the playlist which should be deleted.
	 * @throws DataAccessException
	 */
	public void delete(int id) throws DataAccessException;

	/**
	 * Reads a playlist with the given id.
	 * 
	 * @param id
	 *            Id of the playlist which should be read.
	 * @return The playlist from the DB.
	 * @throws DataAccessException
	 */
	public Playlist read(int id) throws DataAccessException;

	/**
	 * Reads all playlists from the DB.
	 * 
	 * @return A list of all playlists from the DB.
	 * @throws DataAccessException
	 */
	public List<? extends Playlist> readAll() throws DataAccessException;

	/**
	 * Only renames the playlist to the new name in opposition to
	 * @ <code>update</code>.
	 * 
	 * @param p
	 *            The playlist which will be renamed
	 * @param name
	 *            The new name of the playlist
	 * @throws DataAccessException
	 */
	public void rename(WritablePlaylist p, String name) throws DataAccessException;

	/**
	 * @return the connection used by this DAO
	 */
	public Connection getConnection();
}
