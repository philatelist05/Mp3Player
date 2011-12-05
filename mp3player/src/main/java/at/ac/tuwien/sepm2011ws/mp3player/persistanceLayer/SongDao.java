/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import java.util.List;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;

/**
 * @author klaus
 *
 */
public interface SongDao {
	
	
	public Song read(int id) throws IllegalArgumentException;
	public List<Song> readAll();
	
}
