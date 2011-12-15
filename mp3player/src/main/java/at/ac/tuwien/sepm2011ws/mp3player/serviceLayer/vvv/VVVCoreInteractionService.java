package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vvv;

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

class VVVCoreInteractionService implements CoreInteractionService {
	private static final Logger logger = Logger
			.getLogger(VVVCoreInteractionService.class);
	private Player player;
	private boolean isMute;
	private boolean isPause;
	private boolean isStop;
	private PlayMode playMode;
	private Song currentSong;

	/**
	 * 
	 */
	public VVVCoreInteractionService() {
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
		this.isPause = false;
		this.isStop = true;
		this.currentSong = null;
		this.playMode = PlayMode.NORMAL;
	}

	public void playPause() {
		playPause(currentSong);
	}

	public void playPause(Song song) {
		if (song == null) {
			throw new IllegalArgumentException("Cannot play unexisting song");
		}

		// If no player has been initialized yet, or if the player is playing
		if (player == null
				|| (!isPause && player.getState() != Controller.Started)) {
			try {
				isStop = false;
				isPause = false;

				this.currentSong = song;

				player = Manager.createPlayer(new URL("file:///"
						+ song.getPath()));
				player.addControllerListener(new VVVControllerListener());
				player.realize();

				logger.debug("Playing: " + song.getTitle());
			} catch (NoPlayerException ex) {
				logger.error(ex.getMessage());
			} catch (IOException ex) {
				logger.error(ex.getMessage());
			}
		} else {
			if (isPause) {
				isPause = false;

				player.start();

			} else if (player.getState() == Controller.Started) {
				pause();
			}
		}
	}

	public void pause() {
		player.stop();
		isPause = true;
		isStop = false;
	}

	public void playNext() {
		// TODO Get next song from PlaylistService and play it

	}

	public void playPrevious() {
		// TODO Get previous song from PlaylistService and play it

	}

	public void stop() {
		if (player != null) {
			player.stop();
			player.close();
			isPause = false;
			isStop = true;
		}
	}

	public boolean isPlaying() {
		return player != null && player.getState() == Controller.Started;
	}

	public void toggleMute() {
		if (player != null) {
			player.getGainControl().setMute(!isMute);
			isMute = !isMute;
		} else {
			isMute = true;
		}
	}

	public void setVolume(int level) {
		if (player != null) {
			float fLevel = (float) level / 100f;

			player.getGainControl().setLevel(fLevel);

			logger.debug("Volume set to " + fLevel);
		} else {
			logger.debug("Setting volume failed: player is null");
		}
	}

	public int getVolume() {
		if (player != null) {
			float level = player.getGainControl().getLevel();

			return (int) level * 100;
		} else {
			logger.debug("Getting volume failed: player is null");
			return 0;
		}
	}

	public boolean isMute() {
		return isMute;
	}

	public boolean isPaused() {
		return isPause;
	}

	public boolean isStopped() {
		return isStop;
	}

	public void setPlayMode(PlayMode playMode) {
		logger.debug("Current PlayMode set to: " + playMode);
		this.playMode = playMode;
	}

	public PlayMode getPlayMode() {
		return this.playMode;
	}

	public void seek(int percent) {
		player.setMediaTime(new Time(getDurationAt(percent)));
	}

	public double getDuration() {
		return getDurationAt(100);
	}

	public double getDurationAt(int percent) {

		double factor = (double) percent / 100;
		double seconds = (player.getDuration().getSeconds() * factor);

		return seconds;
	}

	public Song getCurrentSong() {
		return this.currentSong;
	}

	private class VVVControllerListener implements ControllerListener {

		public void controllerUpdate(ControllerEvent evt) {
			if (evt.getClass() == RealizeCompleteEvent.class) {
				player.prefetch();
			} else if (evt.getClass() == PrefetchCompleteEvent.class) {
				player.start();

			} else if (evt.getClass() == EndOfMediaEvent.class) {
				player.removeControllerListener(this);
				logger.debug("Player stopped at end of song");
				player.stop();
				playNext();
			}
		}

	}

}
