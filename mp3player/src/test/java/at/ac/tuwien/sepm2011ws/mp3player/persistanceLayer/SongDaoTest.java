/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;

/**
 * @author klaus
 *
 */
public abstract class SongDaoTest {
	
	private SongDao songDao;

	protected void setSongDao(SongDao s) {
		this.songDao = s;
	}

	@Test
	public void testReadAll() {
		List<Song> dList = songDao.readAll();
		assertFalse(dList == null);
		assertTrue(dList.size() >= 1);
	}

}
