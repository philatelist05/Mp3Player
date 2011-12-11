/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.domainObjects;

/**
 * @author oli
 *
 */
public class Lyric {

	private String lyrics;
	
	public Lyric() {
		this("Unkown");
	}
	
	/**
	 * @param lyrics
	 */
	public Lyric(String lyrics) {
		this.lyrics = lyrics;
	}

	/**
	 * 
	 * Returns the lyrics
	 * 
	 * @return lyrics
	 * 
	 */
	public String getLyrics() {
		return this.lyrics;
	}
	
	/**
	 * 
	 * Sets the lyrics
	 * 
	 * @param lyrics
	 * 
	 */
	public void setLyrics(String lyrics) {
		this.lyrics = lyrics;
	}
}
