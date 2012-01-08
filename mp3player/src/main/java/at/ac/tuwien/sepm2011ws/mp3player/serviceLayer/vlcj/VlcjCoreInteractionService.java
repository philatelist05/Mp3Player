package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vlcj;

import java.io.File;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.CoreInteractionService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlayerListener;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.ServiceFactory;

import com.sun.jna.NativeLibrary;

public class VlcjCoreInteractionService implements CoreInteractionService {
	private final MediaPlayer mediaPlayer;
	private Song currentSong;
	private boolean isPaused;
	private PlayerListener playerListener;

	public VlcjCoreInteractionService() {

		String libPath = getLibPath();
		String pluginPath = getPluginPath();

		System.setProperty("jna.library.path", libPath);

		this.isPaused = false;

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
		else if (RuntimeUtil.isWindows())
			return new File("lib/vlc/windows/lib").getAbsolutePath();
		return new File("lib/vlc/linux/lib").getAbsolutePath();
	}

	private String getPluginPath() {
		if (RuntimeUtil.isMac())
			return new File("lib/vlc/osx/plugins").getAbsolutePath();
		else if (RuntimeUtil.isWindows())
			return new File("lib/vlc/windows/plugins").getAbsolutePath();
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
		ServiceFactory sf = ServiceFactory.getInstance();
		PlaylistService ps = sf.getPlaylistService();
		Song nextSong = ps.getNextSong();

		if (nextSong != null)
			playFromBeginning(nextSong);
	}

	public void playPrevious() {
		ServiceFactory sf = ServiceFactory.getInstance();
		PlaylistService ps = sf.getPlaylistService();
		Song previousSong = ps.getPreviousSong();

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
			this.mediaPlayer.setTime((long) getDurationAt(percent));

	}

	public void seekToSecond(int seconds) {
		if (seconds < 0 || seconds > getDuration())
			throw new IllegalArgumentException(
					"Amount of seconds out of song duration");

		if (this.mediaPlayer.isSeekable()) {
			this.mediaPlayer.setTime(seconds);
		}
	}

	public double getDuration() {
		return getDurationAt(100);
	}

	public double getDurationAt(double percent) {
		if (percent < 0 || percent > 100)
			throw new IllegalArgumentException(
					"Duration percentage out of range");

		return this.mediaPlayer.getLength() * (percent / 100);
	}

	public double getPlayTime() {
		double duration = getDuration();
		double playTime = this.mediaPlayer.getLength();

		return playTime * 100 / duration;
	}

	public double getPlayTimeInSeconds() {
		return getDurationAt(getPlayTime());
	}

	public Song getCurrentSong() {
		return this.currentSong;
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
