package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Album;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db.DaoFactory;

public class AlbumDaoTest {
	private AlbumDao ad;
	private Connection con;

	@Before
	public void setUp() throws Exception {
		DaoFactory factory = DaoFactory.getInstance();
		ad = factory.getAlbumDao();
		con = ad.getConnection();
		con.setAutoCommit(false);
	}

	@After
	public void tearDown() throws Exception {
		con.rollback();
	}

	@Test
	public void testRead_ReadsExistingSimpleAlbum() throws DataAccessException {
		Album expected = new Album("Test1");
		ad.create(expected);

		Album actual = ad.read(expected.getId());
		assertEquals(expected, actual);
	}

	@Test
	public void testRead_ReadsExistingComplexAlbum() throws DataAccessException {
		Album expected = new Album("Test1");
		expected.setYear(9999);
		expected.setAlbumartPath("/myPath/p.mp3");
		ad.create(expected);

		Album actual = ad.read(expected.getId());
		assertEquals(expected, actual);
	}

	@Test
	public void testRead_ReadsUnexistingSong() throws DataAccessException {
		Album a = ad.read(Integer.MAX_VALUE);
		assertNull(a);
	}

	@Test
	public void testCreate_CreateValidAlbum() throws DataAccessException {
		Album expected = new Album("Test1");
		ad.create(expected);
		assertTrue(expected.getId() > 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreate_CreateSongWithInvalidTitle()
			throws DataAccessException {
		Album expected = new Album(null);
		ad.create(expected);
	}

	@Test
	public void testUpdate_TestsValidUpdate() throws DataAccessException {
		Album expected = new Album("Test1");
		ad.create(expected);
		expected.setTitle("Test2");
		ad.update(expected);
		Album actual = ad.read(expected.getId());
		assertEquals(expected, actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUpdate_TestsInvalidSong() throws DataAccessException {
		ad.update(null);
	}

	@Test
	public void testDelete_TestsValidDelete() throws DataAccessException {
		Album a = new Album("Test1");
		ad.create(a);
		ad.delete(a.getId());
		Album actual = ad.read(a.getId());
		assertNull(actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDelete_TestsInvalidId() throws DataAccessException {
		ad.delete(-1);
	}

}
