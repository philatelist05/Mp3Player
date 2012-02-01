package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.WritablePlaylist;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db.DaoFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PlaylistDaoTest {
	private PlaylistDao plstdao;
	private SongDao sdao;
	private Connection con1;
	private Connection con2;

	@Before
	public void setUp() throws Exception {
		DaoFactory factory = DaoFactory.getInstance();
		plstdao = factory.getPlaylistDao();
		sdao = factory.getSongDao();
		con1 = plstdao.getDbConnection();
		con1.setAutoCommit(false);
        con2 = sdao.getDbConnection();
		con2.setAutoCommit(false);
	}

	@After
	public void tearDown() throws Exception {
		con1.rollback();
		con2.rollback();
	}

	@Test
	public void testUpdate_ShouldRecreateSongAssociation()
			throws DataAccessException {
		WritablePlaylist expected = new WritablePlaylist("Test1");
		plstdao.create(expected);

		Song song1 = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		Song song2 = new Song("Song2", "Halo2", 3002, "C:\\music\\halo2");

		expected.add(song1);
		expected.add(song2);

		sdao.create(song1);
		sdao.create(song2);

		plstdao.update(expected);

		Playlist actual = plstdao.read(expected.getId());
		assertEquals(expected, actual);
	}

	@Test(expected = DataAccessException.class)
	public void testUpdate_ThrowDataAccessExceptionBecauseOfNotExistingSongs()
			throws DataAccessException {
		WritablePlaylist expected = new WritablePlaylist("Test1");
		plstdao.create(expected);

		Song song1 = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		Song song2 = new Song("Song2", "Halo2", 3002, "C:\\music\\halo2");

		expected.add(song1);
		expected.add(song2);

		plstdao.update(expected);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRead_InvalidIdShouldThrowIllegalArgumentException()
			throws IllegalArgumentException, DataAccessException {
		plstdao.read(-1);
	}

	@Test
	public void testReplstdao_ReplstdaosExistingSimplePlaylist()
			throws DataAccessException {
		WritablePlaylist expected = new WritablePlaylist("Test1");
		plstdao.create(expected);

		Playlist actual = plstdao.read(expected.getId());
		assertEquals(expected, actual);
	}

	@Test
	public void testReplstdao_ReplstdaosExistingComplexPlaylist()
			throws DataAccessException {
		WritablePlaylist expected = new WritablePlaylist("Test1");

		List<Song> list = new ArrayList<Song>();
		Song s1 = new Song("Artist1", "Title1", 0, "/myPath/test1.mp3");
		sdao.create(s1);
		list.add(s1);

		Song s2 = new Song("Artist2", "Title2", 0, "/myPath/test2.mp3");
		sdao.create(s2);
		list.add(s2);

		Song s3 = new Song("Artist3", "Title3", 0, "/myPath/test3.mp3");
		sdao.create(s3);
		list.add(s3);

		expected.addAll(list);
		plstdao.create(expected);

		Playlist actual = plstdao.read(expected.getId());
		assertEquals(expected, actual);
	}

	@Test
	public void testReplstdao_ReplstdaosUnexistingSong()
			throws DataAccessException {
		Playlist a = plstdao.read(Integer.MAX_VALUE);
		assertNull(a);
	}

	@Test
	public void testCreate_CreateValidPlaylist() throws DataAccessException {
		WritablePlaylist expected = new WritablePlaylist("Test1");
		plstdao.create(expected);
		assertTrue(expected.getId() > 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreate_CreateSongWithInvalidTitle()
			throws DataAccessException, IllegalArgumentException {
		WritablePlaylist expected = new WritablePlaylist(null);
		plstdao.create(expected);
	}

	@Test
	public void testUpdate_TestsValidUpdate() throws DataAccessException {
		WritablePlaylist expected = new WritablePlaylist("Test1");
		plstdao.create(expected);
		expected.setTitle("Test2");
		plstdao.update(expected);
		Playlist actual = plstdao.read(expected.getId());
		assertEquals(expected, actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUpdate_TestsInvalidSong() throws DataAccessException {
		plstdao.update(null);
	}

	@Test
	public void testDelete_TestsValidDelete() throws DataAccessException {
		WritablePlaylist a = new WritablePlaylist("Test1");
		plstdao.create(a);
		plstdao.delete(a.getId());
		Playlist actual = plstdao.read(a.getId());
		assertNull(actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDelete_TestsInvalidId() throws DataAccessException {
		plstdao.delete(-1);
	}

	@Test
	public void testReadAll_ShouldReturnAllSongs() throws DataAccessException {
		WritablePlaylist playlist1 = new WritablePlaylist("Test1");
		plstdao.create(playlist1);
		WritablePlaylist playlist2 = new WritablePlaylist("Test2");
		plstdao.create(playlist2);
		WritablePlaylist playlist3 = new WritablePlaylist("Test3");
		plstdao.create(playlist3);

		List<WritablePlaylist> playlists = plstdao.readAll();

		assertTrue(playlists.contains(playlist1));
		assertTrue(playlists.contains(playlist2));
		assertTrue(playlists.contains(playlist3));
	}

	@Test
	public void testRename_ShouldRenamePlaylist() throws DataAccessException {
		WritablePlaylist expected = new WritablePlaylist("Test1");
		plstdao.create(expected);

		plstdao.rename(expected, "Test2");
		Playlist actual = plstdao.read(expected.getId());
		expected.setTitle("Test2");
		assertEquals(expected, actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRename_ShouldThrowIllegalArgumentException()
			throws IllegalArgumentException, DataAccessException {
		plstdao.rename(null, null);
	}
}
