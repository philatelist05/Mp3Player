/**
 * Veni Vidi Vici PlaylistService
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vvv;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.SongDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db.DaoFactory;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlayMode;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;

/**
 * @author klaus
 *
 */
class VVVPlaylistService implements PlaylistService {
	private static final Logger logger = Logger
			.getLogger(VVVPlaylistService.class);
	private PlayMode playMode;

	public VVVPlaylistService() {
		this.playMode = PlayMode.NORMAL;
	}

	public Playlist getLibrary() {
		Playlist lib = new Playlist("Library");
		lib.setReadonly(true);
		
		DaoFactory df = DaoFactory.getInstance();
		SongDao sd = df.getSongDao();
		lib.setSongs(sd.readAll());
		
		return lib;
	}

	public void setPlayMode(PlayMode playMode) {
		logger.debug("Current PlayMode set to: " + playMode);
		this.playMode = playMode;
	}

	public PlayMode getPlayMode() {
		return this.playMode;
	}

}
