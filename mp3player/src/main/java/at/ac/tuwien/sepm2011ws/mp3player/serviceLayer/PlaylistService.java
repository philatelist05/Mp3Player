/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import java.io.File;
import java.util.List;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;

/**
 * @author klaus
 * 
 */
public interface PlaylistService {

	public static final String[] PlaylistFileTypes = new String[] { "m3u" };

	/**
	 * Imports a playlist from a playlist file to the library.
	 * 
	 * @param files
	 *            Array of Playlist files(m3u files)
	 * @return a playlist object corresponding to the parsed file
	 * @throws DataAccessException 
	 */
	public void importPlaylist(File[] files) throws DataAccessException;

	/**
	 * Exports a playlist to a playlist file.
	 * 
	 * @param file
	 *            The file where to export it
	 * @param playlist
	 *            The playlist to export
	 */
	public void exportPlaylist(File file, Playlist playlist);

	/**
	 * Gets all saved playlists from the library
	 * 
	 * @return all playlists currently available
	 * @throws DataAccessException 
	 */
	public List<Playlist> getAllPlaylists() throws DataAccessException;
	
	/**
	 * Get a specific playlist by its name
	 * 
	 * @param name The name of the playlist
	 * @return the first playlist with the specified name
	 * @throws DataAccessException
	 */
	public Playlist getPlaylist(String name) throws DataAccessException;

	/**
	 * Recursively adds all files in the specified folder and subfolders with
	 * accepted file type (specified in the program properties) to the library.
	 * 
	 * @param folder
	 *            The folder which will be added
	 * @throws DataAccessException 
	 */
	public void addFolder(File folder) throws DataAccessException;

	/**
	 * Adds all files given in the path array with accepted file types
	 * (specified in the program properties) to the library.
	 * 
	 * @param files
	 *            The files which will be added
	 * @throws DataAccessException 
	 */
	public void addSongs(File[] files) throws DataAccessException;

	/**
	 * Adds all files given in the path array with accepted file types
	 * (specified in the program properties) to the playlist.
	 * 
	 * @param files
	 *            The files which will be added
	 * @param playlist
	 *            The playlist to which the files will be added
	 * @throws DataAccessException 
	 */
	public void addSongsToPlaylist(File[] files, Playlist playlist) throws DataAccessException;

	/**
	 * Deletes the given Song from the playlist.
	 * 
	 * @param song
	 *            The song to delete
	 * @param playlist
	 *            The playlist from which to delete
	 * @throws DataAccessException 
	 */
	public void deleteSong(Song song, Playlist playlist) throws DataAccessException;

	/**
	 * Creates an empty playlist with the specitied name.
	 * 
	 * @param name
	 *            The name of the playlist
	 * @return the created playlist
	 * @throws DataAccessException 
	 */
	public Playlist createPlaylist(String name) throws DataAccessException;

	/**
	 * Deletes the specified playlist from the library.
	 * 
	 * @param playlist
	 *            The playlist to delete
	 * @throws DataAccessException 
	 */
	public void deletePlaylist(Playlist playlist) throws DataAccessException;

	/**
	 * Updates the specified playlist in the library.
	 * 
	 * @param playlist
	 * @throws DataAccessException 
	 */
	public void updatePlaylist(Playlist playlist) throws DataAccessException;

	/**
	 * Renames the playlist in the library.
	 * 
	 * @param playlist
	 *            The playlist with the old name
	 * @param name
	 *            The new name of the playlist
	 * @throws DataAccessException 
	 */
	public void renamePlaylist(Playlist playlist, String name) throws DataAccessException;

	/**
	 * Generates the magic TopXXRated playlist.
	 * 
	 * @return the magic playlist
	 * @throws DataAccessException 
	 */
	public Playlist getTopRated() throws DataAccessException;

	/**
	 * Generates the magic TopXXPlayed playlist.
	 * 
	 * @return the magic playlist
	 * @throws DataAccessException 
	 */
	public Playlist getTopPlayed() throws DataAccessException;

	/**
	 * Searches the whole library for songs where any of the songs fields
	 * matches the search pattern.
	 * 
	 * In later versions maybe with "fuzzy string searching" a.k.a.
	 * "approximate string searching".
	 * 
	 * @param pattern
	 *            The search pattern
	 * @return a playlist named "Search Results" which contains the search
	 *         results
	 * @throws DataAccessException 
	 */
	public Playlist globalSearch(String pattern) throws DataAccessException;

	/**
	 * Checks the song paths of all songs of the library and sets their
	 * <code>pathOk</code> field.
	 * @throws DataAccessException 
	 */
	public void checkSongPaths() throws DataAccessException;

	/**
	 * Get the Library playlist
	 * 
	 * @return a playlist with all songs of the library
	 * @throws DataAccessException
	 */
	public Playlist getLibrary() throws DataAccessException;

}
