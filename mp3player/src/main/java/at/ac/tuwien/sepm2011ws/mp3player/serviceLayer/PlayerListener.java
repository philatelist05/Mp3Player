/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;


/**
 * @author klaus
 * 
 */
public interface PlayerListener {

	/**
	 * Fires when the player begins to play a song.
	 */
	public void songBeginnEvent();
	
	/**
	 * Fires when the player is at the end of a song.
	 */
	public void songEndEvent();

}
