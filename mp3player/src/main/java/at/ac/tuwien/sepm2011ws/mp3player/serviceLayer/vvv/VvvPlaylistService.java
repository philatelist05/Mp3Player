/**
 * Veni Vidi Vici PlaylistService
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vvv;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.SongDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db.DaoFactory;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.playlist.SpecificPlaylistFactory;

/**
 * @author klaus
 * 
 */
class VvvPlaylistService implements PlaylistService {
    private static final Logger logger = Logger
	    .getLogger(VvvPlaylistService.class);

    public VvvPlaylistService() {
    }

    public Playlist getLibrary() throws DataAccessException {
	Playlist lib = new Playlist("Library");
	lib.setReadonly(true);

	DaoFactory df = DaoFactory.getInstance();
	SongDao sd = df.getSongDao();
	lib.setSongs(sd.readAll());

	return lib;
    }

    public void importPlaylist(File[] files) throws DataAccessException {
	
    }
    


    private void writePlaylistM3U(File file) throws DataAccessException {
	try {
	    SpecificPlaylist specificPlaylist = SpecificPlaylistFactory.getInstance().readFrom(file);
	    if (specificPlaylist == null)
		throw new DataAccessException("Couldn't read Playlist " + file.getName());
	    
	    christophedelory.playlist.Playlist plst = specificPlaylist.toPlaylist();
//	    plst.acceptDown(metadataVisitor);
	    
	    
	} catch (IOException e) {
	    throw new DataAccessException("Couldn't read Playlist " + file.getName());
	}
    }

    public void exportPlaylist(File file, Playlist playlist) {
	for (Song song : playlist.getSongs()) {
	    
	}
    }

    public List<Playlist> getAllPlaylists() {
	// TODO Auto-generated method stub
	return null;
    }

    public void addFolder(File folder) {
	// TODO Auto-generated method stub

    }

    public void addSongs(File[] files) {
	// TODO Auto-generated method stub

    }

    public void addSongsToPlaylist(File[] files, Playlist playlist) {
	// TODO Auto-generated method stub

    }

    public void deleteSong(Song song, Playlist playlist) {
	// TODO Auto-generated method stub

    }

    public Playlist createPlaylist(String name) {
	// TODO Auto-generated method stub
	return null;
    }

    public void deletePlaylist(Playlist playlist) {
	// TODO Auto-generated method stub

    }

    public void updatePlaylist(Playlist playlist) {
	// TODO Auto-generated method stub

    }

    public void renamePlaylist(Playlist playlist, String name) {
	// TODO Auto-generated method stub

    }

    public Playlist getTopRated() {
	// TODO Auto-generated method stub
	return null;
    }

    public Playlist getTopPlayed() {
	// TODO Auto-generated method stub
	return null;
    }

    public Playlist globalSearch(String pattern) {
	// TODO Auto-generated method stub
	return null;
    }

    public void checkSongPaths() {
	// TODO Auto-generated method stub

    }

}
