package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.jmf;

import java.io.IOException;
import java.net.URL;

import javax.media.Controller;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.PrefetchCompleteEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.Time;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.CoreInteractionService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlayerListener;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;

class JmfCoreInteractionService implements CoreInteractionService {
	private static final Logger logger = Logger
			.getLogger(JmfCoreInteractionService.class);
	private Player player;
	private boolean isMute;
	private boolean isPaused;
	private boolean isStopped;
	private float volume;
	private Song currentSong;
	private PlayerListener pl;

	// TODO Method (maybe event?) for syncing UI and ServiceLayer (e.g. if end
	// of song, play next song --> send acknowledge to UI... (in that manner).
	// The goal: The UI should get known of Song changing (and should also be
	// provided the new song information...))

	/**
	 * 
	 */
	public JmfCoreInteractionService() {
		// Register the mp3 plugin
		// Format input1 = new AudioFormat(AudioFormat.MPEGLAYER3);
		// Format input2 = new AudioFormat(AudioFormat.MPEG);
		// Format output = new AudioFormat(AudioFormat.LINEAR);
		// PlugInManager.addPlugIn(
		// "com.sun.media.codec.audio.mp3.JavaDecoder",
		// new Format[]{input1, input2},
		// new Format[]{output},
		// PlugInManager.CODEC
		// );

		this.isMute = false;
		this.isPaused = false;
		this.isStopped = true;
		this.currentSong = null;
		this.volume = 1;
	}

	public void playFromBeginning(Song song) {
		if (song == null) {
			// If no song is provided, play the next song of the playlist
			playNext();
		} else {
			isStopped = false;
			isPaused = false;

			this.currentSong = song;

			initialzePlayer(song);

			player.start();

			logger.info("Playing: " + song.getTitle());
		}
	}
	
	public void playPause() {
		playPause(this.currentSong);
	}

	public void playPause(Song song) {
		if (song != null && song.equals(this.currentSong) && !isStopped) {
			// If song is the current song, toggle pause
			togglePause();
		} else {
			// Play the song from the beginning
			playFromBeginning(song);
		}
	}

	private void initialzePlayer(Song song) {

		if (song == null) {
			throw new IllegalArgumentException(
					"Cannot initialize player without a song");
		}

		try {
			if (player != null) {
				player.stop();
				player.close();
			}

			player = Manager.createPlayer(new URL("file:///" + song.getPath()));
			player.addControllerListener(new VVVControllerListener());
			player.realize();

		} catch (NoPlayerException ex) {
			logger.error("Error while initializing player:\n" + ex.getMessage());
		} catch (IOException ex) {
			logger.error("Error while initializing player:\n" + ex.getMessage());
		}
	}

	private void togglePause() {
		if (player != null) {
			if (!isPaused && !isStopped) {
				isPaused = true;
				player.stop();
			} else if (isPaused && !isStopped) {
				isPaused = false;
				player.start();
			}
		}
	}

	public void pause() {
		if (!isPaused)
			togglePause();
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
		if (player != null && !isStopped) {
			isPaused = false;
			isStopped = true;

			player.stop();
			player.close();
		}
	}

	public boolean isPlaying() {
		return !isStopped && !isPaused;
	}

	public void toggleMute() {
		isMute = !isMute;
		logger.info("Mute toggled to " + (isMute ? "ON" : "OFF"));

		if (player != null) {
			player.getGainControl().setMute(isMute);
		}
	}

	public void setVolume(int level) {

		if (level < 0 || level > MAX_VOLUME)
			throw new IllegalArgumentException("Volume level out of range");

		this.volume = (float) level / 100f;
		logger.info("Volume set to " + this.volume);

		if (player != null) {
			player.getGainControl().setLevel(this.volume);
		}
	}

	public int getVolume() {
		return (int) this.volume * 100;
	}

	public boolean isMute() {
		return isMute;
	}

	public boolean isPaused() {
		return isPaused;
	}

	public boolean isStopped() {
		return isStopped;
	}

	public void seek(double percent) {
		if (player != null && player.getState() != Controller.Unrealized) {
			player.setMediaTime(new Time(getDurationAt(percent)));
		}
	}
	
	public void seekToSecond(int seconds)
	{
		if(seconds < 0 || seconds > getDuration())
			throw new IllegalArgumentException("Amount of seconds out of song duration");
		
		if (player != null && player.getState() != Controller.Unrealized) {
			player.setMediaTime(new Time((double)seconds));
		}
	}

	public double getDuration() {
		return getDurationAt(100);
	}

	public double getDurationAt(double percent) {
		if (percent < 0 || percent > 100)
			throw new IllegalArgumentException(
					"Duration percentage out of range");

		return player.getDuration().getSeconds() * (percent / 100);
	}
	
	public double getPlayTimeInSeconds() {
		return getDurationAt(getPlayTime());
	}

	public Song getCurrentSong() {
		return this.currentSong;
	}

	public double getPlayTime() {
		if (player == null) 
			return 0;
		
		double duration = getDuration();
		double playTime = player.getMediaTime().getSeconds();

		return playTime * 100 / duration;
	}

	private class VVVControllerListener implements ControllerListener {

		public void controllerUpdate(ControllerEvent evt) {
			if (evt.getClass() == RealizeCompleteEvent.class) {
				player.getGainControl().setLevel(volume);
				player.getGainControl().setMute(isMute);

				player.prefetch();
			} else if (evt.getClass() == PrefetchCompleteEvent.class) {
				// player.start();
			} else if (evt.getClass() == EndOfMediaEvent.class) {
				// player.removeControllerListener(this);
				// player.stop();
				playNext();
				
				if(pl != null)
					pl.endOfMediaEvent(currentSong);
			}
		}

	}

	public void setPlayerListener(PlayerListener pl) {
		if(pl == null)
			throw new IllegalArgumentException("Setting a non-existing PlayerListener makes no sense");
		
		this.pl = pl;
	}

	public void removePlayerListener() {
		this.pl = null;
	}

}