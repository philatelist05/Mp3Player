/**
 * Veni Vidi Vici PlaylistService
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vvv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.PlaylistDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.SongDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db.DaoFactory;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;

/**
 * @author klaus
 * 
 */
class VvvPlaylistService implements PlaylistService {
    
    private static final Logger logger = Logger.getLogger(VvvPlaylistService.class);
    private final PlaylistDao playlistDao;
    private final SongDao songDao;

    public VvvPlaylistService() {
	DaoFactory factory = DaoFactory.getInstance();
	this.playlistDao = factory.getPlaylistDao();
	this.songDao = factory.getSongDao();
    }

    public Playlist getLibrary() throws DataAccessException {
	Playlist lib = new Playlist("Library");
	lib.setReadonly(true);

	lib.setSongs(this.songDao.readAll());

	return lib;
    }

    public void importPlaylist(File[] files) throws DataAccessException {
	for (File file : files) {
	    Playlist playlist = readPlaylist(file);
	    playlistDao.create(playlist);
	}
    }
    
    private Playlist readPlaylist(File file) throws DataAccessException {
	FileReader reader = null;
	BufferedReader breader = null;
	try {
	    reader = new FileReader(file);
	    breader = new BufferedReader(reader);
	    String line;
	    Playlist playlist = new Playlist("Dummy Title");
	    while ((line = breader.readLine()) != "") {
		playlist.addSong(new Song(1, "Dummy Title", 0, 0, 0, line,
			0000, "Dummy Artist", "Dummy Genre", true, null, null));
	    }
	    return playlist;
	} catch (IOException e) {
	    throw new DataAccessException("Could't read Playlist from path" + file.getAbsolutePath());
	} finally {
	    try {
		if (reader != null)
		    reader.close();
		if (breader != null)
		    breader.close();
	    } catch (IOException e) {

	    }
	}
    }
    
    private Song readSong(File file) {	
	return new Song(1, file.getName(), 0, 0, 0, file.getAbsolutePath(),
		0000, "Dummy Artist", "Dummy Genre", true, null, null);
    }
    
    public void exportPlaylist(File file, Playlist playlist) {
	FileWriter writer = null;
	BufferedWriter bwriter = null;
	try {
	    writer = new FileWriter(file);
	    bwriter = new BufferedWriter(writer);
	    for (Song song : playlist.getSongs()) {
		bwriter.write(song.getPath());
		bwriter.newLine();
	    }
	} catch (IOException e) {
	} finally {
	    try {
		if (bwriter != null) {
		    bwriter.flush();
		    bwriter.close();
		}
		if (writer != null)
		    writer.close();
	    } catch (IOException e) {

	    }
	}
    }

    public List<Playlist> getAllPlaylists() throws DataAccessException {
		return this.playlistDao.readAll();
    }
    
    private void readSongsRecursive(File folder, List<Song> list) {
	if (!folder.isDirectory() && folder.getName().endsWith( ".m3u" )) {
	    list.add(readSong(folder));
	}
	
	FilenameFilter filter =  new FilenameFilter() {
	    public boolean accept(File f, String s) {
		return s.toLowerCase().endsWith( ".m3u" );
	    }
	};
	for (File file : folder.listFiles(filter)) {
	    readSongsRecursive(file, list);
	}
    }

    public void addFolder(File folder) throws DataAccessException {
	List<Song> list = new ArrayList<Song>();
	readSongsRecursive(folder, list);
	for (Song song : list) {
	    this.songDao.create(song);
	}
    }

    public void addSongs(File[] files) throws DataAccessException {
	for (File file : files) {
	    addFolder(file);
	}
    }

    public void addSongsToPlaylist(File[] files, Playlist playlist) {
	List<Song> list = new ArrayList<Song>();
	for (File file : files) {
	    readSongsRecursive(file, list);
	}
	for (Song song : list) {
	    playlist.addSong(song);
	}
    }

    public void deleteSong(Song song, Playlist playlist) {
	List<Song> songsInPlaylist = playlist.getSongs();
	for (Song song2 : songsInPlaylist) {
	    if (song.equals(song2))
		songsInPlaylist.remove(song2);
	}
    }

    public Playlist createPlaylist(String name) throws DataAccessException {
	Playlist playlist = new Playlist(name);
	this.playlistDao.create(playlist);
	return playlist;
    }

    public void deletePlaylist(Playlist playlist) throws DataAccessException {
	this.playlistDao.delete(playlist.getId());
    }

    public void updatePlaylist(Playlist playlist) throws DataAccessException {
	this.playlistDao.update(playlist);
    }

    public void renamePlaylist(Playlist playlist, String name) throws DataAccessException {
	playlist.setTitle(name);
	updatePlaylist(playlist);
    }

    public Playlist getTopRated() {
	//TODO toprated
	return null;
    }

    public Playlist getTopPlayed() {
	//TODO topPlayed
	return null;
    }

    public Playlist globalSearch(String pattern) {
	//TODO globalsearch
	return null;
    }

    public void checkSongPaths() throws DataAccessException {
	List<Song> songs = this.songDao.readAll();
	for (Song song : songs) {
	    song.setPathOk(isPathAccessible(song.getPath()));
	}
    }

    private boolean isPathAccessible(String path) {
	FileInputStream stream = null;
	try {
	    stream = new FileInputStream(new File(path));
	    return true;
	} catch (FileNotFoundException e) {
	    return false;
	} finally {
	    try {
		if (stream != null)
		    stream.close();
	    } catch (IOException e) {
	    }
	}
    }
}
