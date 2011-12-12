/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db.DaoFactory;

/**
 * @author klaus
 * 
 */
public class SongDaoTest {
	private SongDao sd;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		DaoFactory factory = DaoFactory.getInstance();
		sd = factory.getSongDao();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testReadAll_AtLeastOne() {
		List<Song> dList = sd.readAll();
		
		assertFalse(dList == null);
		assertTrue(dList.size() >= 1);
	}
	
	@Test
	public void testCreate_CreateValidSong() {
		Song s = new Song("Machine Head", "Halo", 300, "C:\\music\\halo");
		
		int id = sd.create(s);
		
		assertTrue(id > 0);
	}

}
