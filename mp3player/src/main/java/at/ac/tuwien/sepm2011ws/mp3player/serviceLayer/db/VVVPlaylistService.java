/**
 * Veni Vidi Vici PlaylistService
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.db;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.SongDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db.DaoFactory;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;

/**
 * @author klaus
 *
 */
public class VVVPlaylistService implements PlaylistService {

	public Playlist getLibrary() {
		Playlist lib = new Playlist();
		lib.setTitle("Library");
		lib.setReadonly(true);
		
		DaoFactory df = DaoFactory.getInstance();
		SongDao sd = df.getSongDao();
		lib.setSongs(sd.readAll());
		
		return lib;
	}

}
