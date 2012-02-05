package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Album;
import org.junit.Test;

import static org.junit.Assert.*;

public class AlbumDaoTest extends AbstractDaoTest {

	@Test
	public void testRead_ReadsExistingSimpleAlbum() throws DataAccessException {
		Album expected = new Album("Test1");
		super.albumDao.create(expected);

		Album actual = super.albumDao.read(expected.getId());
		assertEquals(expected, actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreate_ShouldThrowIllegalArgumentException()
			throws IllegalArgumentException, DataAccessException {
		super.albumDao.create(null);
	}

	@Test
	public void testCreate_ShouldInsertAlreadyExistingAlbum()
			throws DataAccessException {
		Album existingAlbum = new Album("Test1");
		existingAlbum.setTitle("Test");
		existingAlbum.setYear(2001);
		super.albumDao.create(existingAlbum);

		Album newAlbum = new Album("Test1");
		newAlbum.setTitle("Test");
		newAlbum.setYear(2001);
		super.albumDao.create(newAlbum);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRead_InvalidId() throws IllegalArgumentException,
			DataAccessException {
		super.albumDao.read(-10);
	}

	@Test
	public void testRead_ReadsExistingComplexAlbum() throws DataAccessException {
		Album expected = new Album("Test1");
		expected.setYear(9999);
		expected.setAlbumartPath("/myPath/p.mp3");
		super.albumDao.create(expected);

		Album actual = super.albumDao.read(expected.getId());
		assertEquals(expected, actual);
	}

	@Test
	public void testRead_ReadsUnexistingSong() throws DataAccessException {
		Album a = super.albumDao.read(Integer.MAX_VALUE);
		assertNull(a);
	}

	@Test
	public void testCreate_CreateValidAlbum() throws DataAccessException {
		Album expected = new Album("Test1");
		super.albumDao.create(expected);
		assertTrue(expected.getId() > 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreate_CreateSongWithInvalidTitle()
			throws DataAccessException {
		Album expected = new Album(null);
		super.albumDao.create(expected);
	}

	@Test
	public void testUpdate_TestsValidUpdate() throws DataAccessException {
		Album expected = new Album("Test1");
		super.albumDao.create(expected);
		expected.setTitle("Test2");
		super.albumDao.update(expected);
		Album actual = super.albumDao.read(expected.getId());
		assertEquals(expected, actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUpdate_TestsInvalidSong() throws DataAccessException {
		super.albumDao.update(null);
	}

	@Test
	public void testDelete_TestsValidDelete() throws DataAccessException {
		Album a = new Album("Test1");
		super.albumDao.create(a);
		super.albumDao.delete(a.getId());
		Album actual = super.albumDao.read(a.getId());
		assertNull(actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDelete_TestsInvalidId() throws DataAccessException {
		super.albumDao.delete(-1);
	}

}
