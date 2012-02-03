package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.WritablePlaylist;

import java.sql.Connection;
import java.util.List;

public interface PlaylistDao {
	/**
	 * Creates a playlist.
	 * 
	 * @param p
	 *            The playlist which will be created.
	 * @throws DataAccessException
	 */
	public void create(WritablePlaylist p);

	/**
	 * Updates a playlist but doesn't rename it.
	 * 
	 * @param p
	 *            The playlist with the new values and the id of the old
	 *            playlist.
	 * @throws DataAccessException
	 */
	public void update(WritablePlaylist p);

	/**
	 * Deletes a playlist.
	 * 
	 * @param id
	 *            The id of the playlist which should be deleted.
	 * @throws DataAccessException
	 */
	public void delete(int id);

	/**
	 * Reads a playlist with the given id.
	 * 
	 * @param id
	 *            Id of the playlist which should be read.
	 * @return The playlist from the DB.
	 * @throws DataAccessException
	 */
	public WritablePlaylist read(int id);

	/**
	 * Reads all playlists from the DB.
	 * 
	 * @return A list of all playlists from the DB.
	 * @throws DataAccessException
	 */
	public List<WritablePlaylist> readAll();

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
	public void rename(WritablePlaylist p, String name);

    public Connection getDbConnection();
}
