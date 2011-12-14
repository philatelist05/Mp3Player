/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db.DaoFactory;

/**
 * @author klaus
 * 
 */
public class SongDaoTest {
	private SongDao sd;
	private Connection con;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		DaoFactory factory = DaoFactory.getInstance();
		sd = factory.getSongDao();
		con = sd.getConnection();
		con.setAutoCommit(false);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		con.rollback();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testReadAll_AtLeastOne() {
		List<Song> dList = sd.readAll();

		assertFalse(dList == null);
		assertTrue(dList.size() >= 1);
	}

	@Test
	public void testRead_ReadsExistingSong() {
		List<Song> sList = sd.readAll();

		Song s = sd.read(sList.get(0).getId());

		assertTrue(sList.get(0).equals(s));
	}

	@Test
	public void testRead_ReadsUnexistingSong() {
		Song s = sd.read(Integer.MAX_VALUE);

		assertTrue(s == null);
	}

	@Test
	public void testCreate_CreateValidSong() {
		Song s = new Song("Machine Head", "Halo", 300, "C:\\music\\halo");

		int id = sd.create(s);

		assertTrue(id > 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreate_CreateSongWithInvalidArtist() {
		Song s = new Song(null, "Halo", 300, "C:\\music\\halo");

		sd.create(s);
	}

	@Test
	public void testUpdate_TestsValidUpdate() {
		Song oldS = new Song("Machine Head", "Halo", 300, "C:\\music\\halo");

		oldS.setId(sd.create(oldS));

		oldS.setGenre("Thrash Metal");

		sd.update(oldS);

		// Song newS = sd.read(oldS.getId());
		//
		// assertTrue(oldS.equals(newS));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUpdate_TestsInvalidSong() {
		sd.update(null);
	}
	
	@Test
	public void testDelete_TestsValidDelete() {
		Song s = new Song("Machine Head", "Halo", 300, "C:\\music\\halo");
		
		s.setId(sd.create(s));
		
		sd.delete(s.getId());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testDelete_TestsInvalidId() {
		sd.delete(-1);
	}

}
