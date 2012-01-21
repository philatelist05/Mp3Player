package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vlcj;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.VideoMetaData;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.ReadonlyPlaylist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.SongDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db.DaoFactory;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.CoreInteractionService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlayMode;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlayerListener;

import com.sun.jna.Platform;

public class VlcjCoreInteractionService implements CoreInteractionService {

	private static final Logger logger = Logger
			.getLogger(VlcjCoreInteractionService.class);
	private final MediaPlayer mediaPlayer;
	private PlayMode playMode;
	private ReadonlyPlaylist currentPlaylist;
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
		mediaPlayer.addMediaPlayerEventListener(new VvvMediaPlayerEventListener());
	}

	private String getLibPath() {
		try {
			if (RuntimeUtil.isMac())
				return new ClassPathResource("vlc/osx/lib").getFile()
						.getAbsolutePath();
			else if (RuntimeUtil.isWindows()) {
				if (Platform.is64Bit())
					return new ClassPathResource("vlc/windows/64Bit/lib")
							.getFile().getAbsolutePath();

				return new ClassPathResource("vlc/windows/32Bit/lib").getFile()
						.getAbsolutePath();
			}
			return new ClassPathResource("vlc/linux/lib").getFile()
					.getAbsolutePath();
		} catch (IOException e) {

		}
		return "";
	}

	private String getPluginPath() {
		try {
			if (RuntimeUtil.isMac())
				return new ClassPathResource("vlc/osx/plugins").getFile()
						.getAbsolutePath();
			else if (RuntimeUtil.isWindows()) {
				if (Platform.is64Bit())
					return new ClassPathResource("vlc/windows/64Bit/plugins")
							.getFile().getAbsolutePath();

				return new ClassPathResource("vlc/windows/32Bit/plugins")
						.getFile().getAbsolutePath();
			}
			return new ClassPathResource("vlc/linux/plugins").getFile()
					.getAbsolutePath();
		} catch (IOException e) {

		}
		return "";
	}

	public void playFromBeginning(Song song) {
		if (song == null) {
			playNext();
		} else {
			isPaused = false;
			this.currentSong = song;
			File songFile = new File(song.getPath());

			mediaPlayer.playMedia(songFile.getAbsolutePath());
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
			this.mediaPlayer.setTime((long) getDurationAt(percent) * 1000);

	}

	public void seekToSecond(int seconds) {
		if (seconds < 0 || seconds > getDuration())
			throw new IllegalArgumentException(
					"Amount of seconds out of song duration");

		if (this.mediaPlayer.isSeekable()) {
			this.mediaPlayer.setTime(seconds * 1000);
		}
	}

	public double getDuration() {
		return getDurationAt(100);
	}

	public double getDurationAt(double percent) {
		if (percent < 0 || percent > 100)
			throw new IllegalArgumentException(
					"Duration percentage out of range");

		return (this.mediaPlayer.getLength() / 1000) * (percent / 100);
	}

	public double getPlayTime() {
		double duration = getDuration();
		double playTime = this.mediaPlayer.getTime() / 1000;
		return playTime * 100 / duration;
	}

	public double getPlayTimeInSeconds() {
		return this.mediaPlayer.getTime() / 1000;
		// return getDurationAt(getPlayTime());
	}

	public Song getCurrentSong() {
		return this.currentSong;
	}

	public void setPlayMode(PlayMode playMode) {
		this.playMode = playMode;
	}

	public ReadonlyPlaylist getCurrentPlaylist() {
		return this.currentPlaylist;
	}

	public void setCurrentPlaylist(ReadonlyPlaylist playlist) {
		this.currentPlaylist = playlist;
	}

	public PlayMode getPlayMode() {
		return this.playMode;
	}

	public int getCurrentSongIndex() {
		if (this.currentPlaylist == null) {
			return -1;
		} else if (this.currentPlaylist.size() == 0) {
			return -1;
		}
		return this.currentPlaylist.indexOf(currentSong);
	}

	public Song getNextSong() {
		if (this.currentPlaylist == null) {
			return null;
		} else if (this.currentPlaylist.size() == 0) {
			return null;
		}

		int maxIndex = this.currentPlaylist.size() - 1;
		int nextIndex = 0;

		switch (this.playMode) {
		case NORMAL:
			nextIndex = getCurrentSongIndex() + 1;
			if (nextIndex > maxIndex) {
				// If the last song was the last of the playlist, stop playing
				nextIndex = -1;
			}
			break;
		case REPEAT:
			nextIndex = getCurrentSongIndex() + 1;
			if (nextIndex > maxIndex) {
				// If the last song was the last of the playlist, the next song
				// is the first of the playlist
				nextIndex = 0;
			}
			break;
		case SHUFFLE:
			nextIndex = (int) Math.floor(Math.random() * maxIndex);
			break;
		default:
			nextIndex = 0;
			break;
		}

		if (nextIndex >= 0 && nextIndex <= maxIndex) {
			return this.currentPlaylist.get(nextIndex);
		}
		return null;
	}

	public Song getPreviousSong() {
		if (this.currentPlaylist == null) {
			return null;
		} else if (this.currentPlaylist.size() == 0) {
			return null;
		}

		int maxIndex = this.currentPlaylist.size() - 1;
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
			return this.currentPlaylist.get(index);
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

	private class VvvMediaPlayerEventListener implements
			MediaPlayerEventListener {

		@Override
		public void backward(MediaPlayer arg0) {
		}

		@Override
		public void buffering(MediaPlayer arg0) {
		}

		@Override
		public void error(MediaPlayer arg0) {
			currentSong.setPathOk(false);
			DaoFactory df = DaoFactory.getInstance();
			SongDao sd = df.getSongDao();
			try {
				sd.update(currentSong);
			} catch (DataAccessException e) {
				logger.error(e.getMessage());
			}
			if (playerListener != null) {
				playerListener.songBeginnEvent();
				playerListener.songEndEvent();
			}
			playNext();
		}

		@Override
		public void finished(MediaPlayer arg0) {
			if (playerListener != null)
				playerListener.songEndEvent();
			playNext();
		}

		@Override
		public void forward(MediaPlayer arg0) {
		}

		@Override
		public void lengthChanged(MediaPlayer arg0, long arg1) {
		}

		@Override
		public void mediaChanged(MediaPlayer arg0) {
		}

		@Override
		public void metaDataAvailable(MediaPlayer arg0, VideoMetaData arg1) {
		}

		@Override
		public void opening(MediaPlayer arg0) {
		}

		@Override
		public void pausableChanged(MediaPlayer arg0, int arg1) {
		}

		@Override
		public void paused(MediaPlayer arg0) {
		}

		@Override
		public void playing(MediaPlayer arg0) {
			currentSong.setPathOk(true);
			DaoFactory df = DaoFactory.getInstance();
			SongDao sd = df.getSongDao();
			try {
				sd.update(currentSong);
			} catch (DataAccessException e) {
				logger.error(e.getMessage());
			}
			if (playerListener != null)
				playerListener.songBeginnEvent();
		}

		@Override
		public void positionChanged(MediaPlayer arg0, float arg1) {
		}

		@Override
		public void seekableChanged(MediaPlayer arg0, int arg1) {
		}

		@Override
		public void snapshotTaken(MediaPlayer arg0, String arg1) {
		}

		@Override
		public void stopped(MediaPlayer arg0) {
		}

		@Override
		public void timeChanged(MediaPlayer arg0, long arg1) {
		}

		@Override
		public void titleChanged(MediaPlayer arg0, int arg1) {
		}

	}

}
