/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vvv.PlayMode;

/**
 * @author klaus
 * 
 */
public interface CoreInteractionService {

	/**
	 * This method starts playing the current active song. If any other song is
	 * already running, the previous started song will be stopped first.
	 */
	public void playPause();

	/**
	 * This method starts playing the provided song. If any other song is
	 * already running, the previous started song will be stopped first.
	 * 
	 * @param song
	 *            The song containing the path to the file that should be played
	 */
	public void playPause(Song songToPlay);

	/**
	 * Pauses the currently playing song (if any song is playing)
	 */
	public void pause();

	/**
	 * Plays the successor of the current played song in the current playlist.
	 */
	public void playNext();

	/**
	 * Plays the predecessor of the current played song in the current playlist.
	 */
	public void playPrevious();

	/**
	 * Stops the currently running song
	 */
	public void stop();

	/**
	 * Indicates whether the player is currently playing
	 * 
	 * @return true if the player is currently playing, otherwise false.
	 */
	public boolean isPlaying();

	/**
	 * Mute or unmute the player
	 */
	public void toggleMute();

	/**
	 * Adjusts the volume of the player.
	 * 
	 * @param level
	 *            Volume level, between 0 (muted) and 100 (loudest)
	 */
	public void setVolume(int level);

	/**
	 * Returns the volume of the player.
	 * 
	 * @return Volume level, between 0 (muted) and 100 (loudest)
	 */
	public int getVolume();

	/**
	 * Indicates whether the player is muted or not.
	 * 
	 * @return true if volume is 0, otherwise false.
	 */
	public boolean isMute();

	/**
	 * Indicates whether the current song is paused or not.
	 * 
	 * @return true if (and only if) the song is paused, otherwise false.
	 */
	public boolean isPaused();

	/**
	 * Sets the play mode.
	 * 
	 * @param playMode
	 *            The desired PlayMode.
	 */
	public void setPlayMode(PlayMode playMode);

	/**
	 * Returns the current play mode.
	 * 
	 * @return the current play mode.
	 */
	public PlayMode getPlayMode();

	/**
	 * Seeks the song to x% of the songs duration, where x is the value of the
	 * parameter. (for GUI Slider)
	 * 
	 * @param percent
	 */
	public void seek(int percent);

	/**
	 * Gets the duration of the current song.
	 * 
	 * @return The duration of the current song <b>in seconds</b>
	 */
	public double getDuration();

	/**
	 * Gets the duration of the current song at x% of the song, where x is the
	 * value of the parameter.
	 * 
	 * @param percent
	 *            The percentage of the duration
	 * @return The percentage of the duration of the current song <b>in
	 *         seconds</b>
	 */
	public double getDurationAt(int percent);

	/**
	 * Returns the current active song.
	 * 
	 * @return the current active song
	 */
	public Song getCurrentSong();

}
