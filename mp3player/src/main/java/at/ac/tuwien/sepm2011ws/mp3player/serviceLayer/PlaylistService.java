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
	 *            The playlist file
	 * @return a playlist object corresponding to the parsed file
	 */
	public Playlist importPlaylist(File[] files);

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
	 */
	public List<Playlist> getAllPlaylists();

	/**
	 * Recursively adds all files in the specified folder and subfolders with
	 * accepted file type (specified in the program properties) to the library.
	 * 
	 * @param folder
	 *            The folder which will be added
	 */
	public void addFolder(File folder);

	/**
	 * Adds all files given in the path array with accepted file types
	 * (specified in the program properties) to the library.
	 * 
	 * @param files
	 *            The files which will be added
	 */
	public void addSongs(File[] files);

	/**
	 * Adds all files given in the path array with accepted file types
	 * (specified in the program properties) to the playlist.
	 * 
	 * @param files
	 *            The files which will be added
	 * @param playlist
	 *            The playlist to which the files will be added
	 */
	public void addSongsToPlaylist(File[] files, Playlist playlist);

	/**
	 * Deletes the given Song from the playlist.
	 * 
	 * @param song
	 *            The song to delete
	 * @param playlist
	 *            The playlist from which to delete
	 */
	public void deleteSong(Song song, Playlist playlist);

	/**
	 * Creates an empty playlist with the specitied name.
	 * 
	 * @param name
	 *            The name of the playlist
	 * @return the created playlist
	 */
	public Playlist createPlaylist(String name);

	/**
	 * Deletes the specified playlist from the library.
	 * 
	 * @param playlist
	 *            The playlist to delete
	 */
	public void deletePlaylist(Playlist playlist);

	/**
	 * Updates the specified playlist in the library.
	 * 
	 * @param playlist
	 */
	public void updatePlaylist(Playlist playlist);

	/**
	 * Renames the playlist in the library.
	 * 
	 * @param playlist
	 *            The playlist with the old name
	 * @param name
	 *            The new name of the playlist
	 */
	public void renamePlaylist(Playlist playlist, String name);

	/**
	 * Generates the magic TopXXRated playlist.
	 * 
	 * @return the magic playlist
	 */
	public Playlist getTopRated();

	/**
	 * Generates the magic TopXXPlayed playlist.
	 * 
	 * @return the magic playlist
	 */
	public Playlist getTopPlayed();

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
	 */
	public Playlist globalSearch(String pattern);

	/**
	 * Checks the song paths of all songs of the library and sets their
	 * <code>pathOk</code> field.
	 */
	public void checkSongPaths();

	/**
	 * Get the Library playlist
	 * 
	 * @return a playlist with all songs of the library
	 * @throws DataAccessException
	 */
	public Playlist getLibrary() throws DataAccessException;

}
