/**
 * Veni Vidi Vici PlaylistService
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vvv;

import java.util.List;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.SongDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db.DaoFactory;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.CoreInteractionService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlayMode;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;

/**
 * @author klaus
 * 
 */
class VVVPlaylistService implements PlaylistService {
	private static final Logger logger = Logger
			.getLogger(VVVPlaylistService.class);
	private Playlist currentPlaylist;
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

	public Playlist getCurrentPlaylist() {
		return this.currentPlaylist;
	}

	public void setCurrentPlaylist(Playlist playlist) {
		this.currentPlaylist = playlist;
	}

	public PlayMode getPlayMode() {
		return this.playMode;
	}

	/**
	 * Gets the current song index
	 * 
	 * @return the index of the current song or -1 if it is not in the current
	 *         playlist
	 */
	private int getCurrentSongIndex() {
		if(this.currentPlaylist == null) {
			return -1;
		} else if(this.currentPlaylist.getSongs() == null) {
			return -1;
		}
		
		ServiceFactory sf = ServiceFactory.getInstance();
		CoreInteractionService cs = sf.getCoreInteractionService();

		Song current = cs.getCurrentSong();

		List<Song> songs = this.currentPlaylist.getSongs();

		return songs.indexOf(current);
	}

	public Song getNextSong() {
		if(this.currentPlaylist == null) {
			return null;
		} else if(this.currentPlaylist.getSongs() == null) {
			return null;
		}
		
		List<Song> songs = this.currentPlaylist.getSongs();
		int maxIndex = songs.size() - 1;
		int index = 0;

		switch (this.playMode) {
		case NORMAL:
			index = getCurrentSongIndex() + 1;
			if (index > maxIndex) {
				// If the last song was the last of the playlist, stay at this
				// song
				index = maxIndex;
			}
			break;
		case REPEAT:
			index = getCurrentSongIndex() + 1;
			if (index > maxIndex) {
				// If the last song was the last of the playlist, the next song
				// is the first of the playlist
				index = 0;
			}
			break;
		case SHUFFLE:
			index = (int) Math.floor(Math.random() * maxIndex);
			break;
		default:
			index = 0;
			break;
		}

		if (index >= 0 && index <= maxIndex) {
			return songs.get(index);
		} else {
			return null;
		}
	}

	public Song getPreviousSong() {
		if(this.currentPlaylist == null) {
			return null;
		} else if(this.currentPlaylist.getSongs() == null) {
			return null;
		}
		
		List<Song> songs = this.currentPlaylist.getSongs();
		int maxIndex = songs.size() - 1;
		int index = 0;

		switch (this.playMode) {
		case NORMAL:
			index = getCurrentSongIndex() - 1;
			if (index < 0) {
				// If the last song was not in the playlist or it was the first
				// of the playlist, the next song is the first
				index = 0;
			}
			break;
		case REPEAT:
			index = getCurrentSongIndex() - 1;
			if (index == -1) {
				// If the last song was the first of the playlist, repeat
				// backwards. The previous song is the last song of the playlist
				index = maxIndex;
			} else if (index < -1) {
				// If the last song was not in the playlist, the previous song
				// is the first of the playlist
				index = 0;
			}
			break;
		case SHUFFLE:
			index = (int) Math.floor(Math.random() * maxIndex);
			break;
		default:
			index = 0;
			break;
		}

		if (index >= 0 && index <= maxIndex) {
			return songs.get(index);
		} else {
			return null;
		}
	}

}
