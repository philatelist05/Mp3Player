/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.impl.DaoFactory;

/**
 * @author klaus
 * 
 */
public class SongDaoTest {
    
    @Test
    public void testReadAll() {
	DaoFactory factory = DaoFactory.getInstance();
	SongDao songDao = factory.getSongDao();
	List<Song> dList = songDao.readAll();
	assertFalse(dList == null);
	assertTrue(dList.size() >= 1);
    }

}
