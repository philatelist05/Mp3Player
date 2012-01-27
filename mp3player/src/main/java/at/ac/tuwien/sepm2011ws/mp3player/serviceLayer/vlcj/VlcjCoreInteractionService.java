package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vlcj;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.VideoMetaData;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.PlayDirection;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.PlayMode;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.SongDao;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.CoreInteractionService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlayerListener;

import com.sun.jna.Platform;

public class VlcjCoreInteractionService implements CoreInteractionService {

	private static final Logger logger = Logger
			.getLogger(VlcjCoreInteractionService.class);
	private SongDao sd;
	private final MediaPlayer mediaPlayer;
	private PlayMode playMode;
	private Playlist currentPlaylist;
	private Song currentSong;
	private boolean isPaused;
	private PlayerListener playerListener;
	private PlayDirection playDirection;
	private int currentSongIndex;

	public VlcjCoreInteractionService(SongDao sd) {
		this.sd = sd;

		System.setProperty("jna.nosys", "true");
		String libPath = getLibPath();
		String pluginPath = getPluginPath();
		System.setProperty("jna.library.path", libPath);

		this.isPaused = false;
		this.playMode = PlayMode.NORMAL;
		this.playDirection = PlayDirection.NEXT;
		this.currentSongIndex = -1;

		MediaPlayerFactory factory = new MediaPlayerFactory(
				new String[] { "--plugin-path=" + pluginPath });

		this.mediaPlayer = factory.newMediaPlayer();
		mediaPlayer
				.addMediaPlayerEventListener(new VvvMediaPlayerEventListener());
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

	public void playFromBeginning(int index) {
		this.currentSongIndex = index;
		Song song = getSong(index);
		this.currentSong = song;

		if (song != null) {
			isPaused = false;
			File songFile = new File(song.getPath());

			mediaPlayer.playMedia(songFile.getAbsolutePath());
		} else if (isPlaying() || isPaused()) {
			stop();
		}
	}

	public void playPause() {
		playPause(this.currentSongIndex);
	}

	public void playPause(int index) {
		Song song = getSong(index);

		if (song != null && song.equals(this.currentSong)) {
			// If song is the current song, toggle pause
			pause();
		} else {
			// Play the song from the beginning
			playFromBeginning(index);
		}
	}

	/**
	 * Gets a song by its index in the current playlist
	 * 
	 * @param index
	 *            The index of the song in the current playlist
	 * @return the song with specified index
	 */
	private Song getSong(int index) {
		if (index < 0 || index >= this.currentPlaylist.size()) {
			return null;
		}
		return this.currentPlaylist.get(index);
	}

	public void pause() {
		mediaPlayer.pause();
		this.isPaused = !this.isPaused;
	}

	public void playNext() {
		playDirection = PlayDirection.NEXT;
		playFromBeginning(getNextSongIndex());
	}

	public void playPrevious() {
		playDirection = PlayDirection.PREVIOUS;
		playFromBeginning(getPreviousSongIndex());
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
			this.mediaPlayer.setTime((long) (getDurationAt(percent) * 1000));

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

		return ((double) this.mediaPlayer.getLength() / 1000) * (percent / 100);
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

	public Playlist getCurrentPlaylist() {
		return this.currentPlaylist;
	}

	public void setCurrentPlaylist(Playlist playlist) {
		this.currentPlaylist = playlist;
	}

	public PlayMode getPlayMode() {
		return this.playMode;
	}

	public int getCurrentSongIndex() {
		return this.currentSongIndex;
	}

	public void setCurrentSongIndex(int index) {
		if(currentSong == null)
			throw new IllegalAccessError("Cannot set the current song index, current song is null");
		if (index < 0 || index >= this.currentPlaylist.size()
				|| !currentSong.equals(currentPlaylist.get(index))) {
			throw new IllegalArgumentException(
					"The song in the playlist at the specified index has to be the current song");
		}
		this.currentSongIndex = index;
	}

	public boolean hasNextSong() {
		if (getNextSongIndex() >= 0) {
			return true;
		}
		return false;
	}

	public boolean hasPreviousSong() {
		if (getPreviousSongIndex() >= 0) {
			return true;
		}
		return false;
	}

	private int getNextSongIndex() {
		if (this.currentPlaylist == null) {
			return -1;
		} else if (this.currentPlaylist.size() == 0) {
			return -1;
		}

		int maxIndex = this.currentPlaylist.size() - 1;
		int index = 0;

		switch (this.playMode) {
		case NORMAL:
			index = getCurrentSongIndex() + 1;
			if (index > maxIndex) {
				// If the last song was the last of the playlist, stop playing
				index = -1;
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
			return index;
		}
		return -1;
	}

	private int getPreviousSongIndex() {
		if (this.currentPlaylist == null) {
			return -1;
		} else if (this.currentPlaylist.size() == 0) {
			return -1;
		}

		int maxIndex = this.currentPlaylist.size() - 1;
		int index = 0;

		switch (this.playMode) {
		case NORMAL:
			index = getCurrentSongIndex() - 1;
			if (index == -1) {
				// If the last song was the first song of the playlist, stop
				// playing
				// index = -1;
			} else if (index < -1) {
				// If the last song was not in the playlist, the next song is
				// the first
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
			return index;
		}
		return -1;
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
			try {
				sd.update(currentSong);
			} catch (DataAccessException e) {
				logger.error(e.getMessage());
			}
			if (playerListener != null) {
				playerListener.songBeginnEvent();
				playerListener.songEndEvent();
			}

			switch (playDirection) {
			case NEXT:
				playNext();
				break;
			case PREVIOUS:
				playPrevious();
				break;
			default:
				break;
			}
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
			currentSong.setPlaycount(currentSong.getPlaycount() + 1);
			currentSong.setPathOk(true);
			try {
				sd.update(currentSong);
				
				// ServiceFactory sf = ServiceFactory.getInstance();
				// SongInformationService sis = sf.getSongInformationService();
				// sis.getMetaTags(currentSong);
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
			if (playerListener != null)
				playerListener.songEndEvent();
		}

		@Override
		public void timeChanged(MediaPlayer arg0, long arg1) {
		}

		@Override
		public void titleChanged(MediaPlayer arg0, int arg1) {
		}

	}

}
