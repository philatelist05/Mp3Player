/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;

/**
 * @author klaus
 * 
 */
public interface CoreInteractionService {
	public final int MAX_VOLUME = 100;

	/**
	 * Plays the song from the beginning of the song.
	 * 
	 * @param song
	 *            The song containing the path to the file that should be played
	 */
	public void playFromBeginning(Song song);

	/**
	 * Plays or pauses the playing song.
	 * 
	 */
	public void playPause();

	/**
	 * Plays the song or pauses the playing if the song is the current playing
	 * song.
	 * 
	 * @param song
	 *            The song containing the path to the file that should be played
	 */
	public void playPause(Song song);

	/**
	 * Pauses the currently playing song
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
	 * Seeks the song to x% of the songs duration, where x is the value of the
	 * parameter. (for GUI Slider)
	 * 
	 * @param percent
	 */
	public void seek(int percent);
	
	
	public void seekToSecond(int seconds);
	
	
	/**
	 * Gets the duration of the current song.
	 * 
	 * @return the duration of the current song <b>in seconds</b>
	 */
	public double getDuration();

	/**
	 * Gets the duration of the current song at x% of the song, where x is the
	 * value of the parameter.
	 * 
	 * @param percent
	 *            The percentage of the duration
	 * @return the percentage of the duration of the current song <b>in
	 *         seconds</b>
	 */
	public double getDurationAt(int percent);

	/**
	 * Gets the percentage of the play time in relation to the duration of
	 * current song. The value is between 0 (just started) and 100 (at the end).
	 * 
	 * @return the percentage of the current play time
	 */
	
	/**
	 * Gets the play time in seconds of the actual played song
	 * The value is between 0 (when started) and the duration of the current song
	 * @return the current play time in seconds
	 */
//	public double getDurationInSeconds();
	
	public double getDurationAtInSeconds();
	
	public int getPlayTime();

	/**
	 * Returns the current active song.
	 * 
	 * @return the current active song
	 */
	public Song getCurrentSong();

}
