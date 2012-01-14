package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vlcj;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.CoreInteractionService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlayMode;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlayerListener;

import com.sun.jna.Platform;

public class VlcjCoreInteractionService implements CoreInteractionService {
	
	private final MediaPlayer mediaPlayer;
	private PlayMode playMode;
	private Playlist currentPlaylist;
	private Song currentSong;
	private boolean isPaused;
	private PlayerListener playerListener;

	public VlcjCoreInteractionService() {

		String libPath = getLibPath();
		String pluginPath = getPluginPath();

		System.setProperty("jna.library.path", libPath);

		this.isPaused = false;
		this.playMode = PlayMode.NORMAL;

		MediaPlayerFactory factory = new MediaPlayerFactory(
				new String[] { "--plugin-path=" + pluginPath });
		this.mediaPlayer = factory.newMediaPlayer();

		// mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter()
		// {
		// public void finished(MediaPlayer mediaPlayer) {
		// System.exit(0);
		// }
		//
		// public void error(MediaPlayer mediaPlayer) {
		// System.exit(1);
		// }
		// });
	}

	private String getLibPath() {
		if (RuntimeUtil.isMac())
			return new File("lib/vlc/osx/lib").getAbsolutePath();
		else if (RuntimeUtil.isWindows()) {
		    if (Platform.is64Bit())
			return new File("lib/vlc/windows/64Bit/lib").getAbsolutePath();
		    
		    return new File("lib/vlc/windows/32Bit/lib").getAbsolutePath();
		}
		return new File("lib/vlc/linux/lib").getAbsolutePath();
	}

	private String getPluginPath() {
		if (RuntimeUtil.isMac())
			return new File("lib/vlc/osx/plugins").getAbsolutePath();
		else if (RuntimeUtil.isWindows()) {
		    if (Platform.is64Bit())
			return new File("lib/vlc/windows/64Bit/plugins").getAbsolutePath();
		    
		    return new File("lib/vlc/windows/32Bit/plugins").getAbsolutePath();
		}
		return new File("lib/vlc/linux/plugins").getAbsolutePath();
	}

	public void playFromBeginning(Song song) {
		if (song == null) {
			playNext();
		} else {
			isPaused = false;
			this.currentSong = song;
			File songPath = new File(song.getPath());
			mediaPlayer.playMedia(songPath.getAbsolutePath());
		}
	}

	public void playPause() {
		playPause(this.currentSong);
	}

	public void playPause(Song song) {
		if (song != null && song.equals(this.currentSong)) {
			// If song is the current song, toggle pause
			pause();
		} else {
			// Play the song from the beginning
			playFromBeginning(song);
		}
	}

	public void pause() {
		mediaPlayer.pause();
		this.isPaused = !this.isPaused;
	}

	public void playNext() {
		Song nextSong = getNextSong();

		if (nextSong != null)
			playFromBeginning(nextSong);
	}

	public void playPrevious() {
		Song previousSong = getPreviousSong();

		if (previousSong != null)
			playFromBeginning(previousSong);
	}

	public void stop() {
		this.mediaPlayer.stop();
	}

	public boolean isPlaying() {
		return this.mediaPlayer.isPlaying();
	}

	public void toggleMute() {
		this.mediaPlayer.mute();
	}

	public void setVolume(int level) {
		this.mediaPlayer.setVolume(level);
	}

	public int getVolume() {
		return this.mediaPlayer.getVolume();
	}

	public boolean isMute() {
		return this.mediaPlayer.isMute();
	}

	public boolean isPaused() {
		return isPaused;
	}

	public void seek(double percent) {
		if (this.mediaPlayer.isSeekable())
			this.mediaPlayer.setTime((long) getDurationAt(percent)*1000);

	}

	public void seekToSecond(int seconds) {
		if (seconds < 0 || seconds > getDuration())
			throw new IllegalArgumentException(
					"Amount of seconds out of song duration");

		if (this.mediaPlayer.isSeekable()) {
			this.mediaPlayer.setTime(seconds*1000);
		}
	}

	public double getDuration() {
		return getDurationAt(100);
	}

	public double getDurationAt(double percent) {
		if (percent < 0 || percent > 100)
			throw new IllegalArgumentException(
					"Duration percentage out of range");

		return (this.mediaPlayer.getLength()/1000) *  (percent/100);
	}

	public double getPlayTime() {
		double duration = getDuration();
		double playTime = this.mediaPlayer.getTime()/1000;
		return playTime * 100 / duration;
	}

	public double getPlayTimeInSeconds() {
		return this.mediaPlayer.getTime()/1000;
		//return getDurationAt(getPlayTime());
	}

	public Song getCurrentSong() {
		return this.currentSong;
	}

	public void setPlayMode(PlayMode playMode) {
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
		Song current = getCurrentSong();

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
		}
		return null;
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
		}
		return null;
	}

	public void setPlayerListener(PlayerListener playerListener) {
		if (playerListener == null)
			throw new IllegalArgumentException(
					"Setting a non-existing PlayerListener makes no sense");

		this.playerListener = playerListener;
	}

	public void removePlayerListener() {
		this.playerListener = null;
	}

}
