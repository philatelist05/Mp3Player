package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.WritablePlaylist;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PlaylistDaoTest extends AbstractDaoTest{

	@Test
	public void testUpdate_ShouldRecreateSongAssociation()
			throws DataAccessException {
		WritablePlaylist expected = new WritablePlaylist("Test1");
		super.playlistDao.create(expected);

		Song song1 = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		Song song2 = new Song("Song2", "Halo2", 3002, "C:\\music\\halo2");

		expected.add(song1);
		expected.add(song2);

		super.songDao.create(song1);
		super.songDao.create(song2);

		super.playlistDao.update(expected);

		Playlist actual = super.playlistDao.read(expected.getId());
		assertEquals(expected, actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRead_InvalidIdShouldThrowIllegalArgumentException()
			throws IllegalArgumentException, DataAccessException {
		super.playlistDao.read(-1);
	}

	@Test
	public void testPlaylistDao_ShouldCreateExistingSimplePlaylist()
			throws DataAccessException {
		WritablePlaylist expected = new WritablePlaylist("Test1");
		super.playlistDao.create(expected);

		Playlist actual = super.playlistDao.read(expected.getId());
		assertEquals(expected, actual);
	}

	@Test
	public void testPlaylistDao_ShouldCreateExistingComplexPlaylist()
			throws DataAccessException {
		WritablePlaylist expected = new WritablePlaylist("Test1");

		List<Song> list = new ArrayList<Song>();
		Song s1 = new Song("Artist1", "Title1", 0, "/myPath/test1.mp3");
		super.songDao.create(s1);
		list.add(s1);

		Song s2 = new Song("Artist2", "Title2", 0, "/myPath/test2.mp3");
		super.songDao.create(s2);
		list.add(s2);

		Song s3 = new Song("Artist3", "Title3", 0, "/myPath/test3.mp3");
		super.songDao.create(s3);
		list.add(s3);

		expected.addAll(list);
		super.playlistDao.create(expected);

		Playlist actual = super.playlistDao.read(expected.getId());
		assertEquals(expected, actual);
	}

	@Test
	public void testPlaylistDao_ShouldCreateUnexistingSong()
			throws DataAccessException {
		Playlist a = super.playlistDao.read(Integer.MAX_VALUE);
		assertNull(a);
	}

	@Test
	public void testCreate_CreateValidPlaylist() throws DataAccessException {
		WritablePlaylist expected = new WritablePlaylist("Test1");
		super.playlistDao.create(expected);
		assertTrue(expected.getId() > 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreate_CreateSongWithInvalidTitle()
			throws DataAccessException, IllegalArgumentException {
		WritablePlaylist expected = new WritablePlaylist(null);
		super.playlistDao.create(expected);
	}

	@Test
	public void testUpdate_TestsValidUpdate() throws DataAccessException {
		WritablePlaylist expected = new WritablePlaylist("Test1");
		super.playlistDao.create(expected);
		expected.setTitle("Test2");
		super.playlistDao.update(expected);
		Playlist actual = super.playlistDao.read(expected.getId());
		assertEquals(expected, actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUpdate_TestsInvalidSong() throws DataAccessException {
		super.playlistDao.update(null);
	}

	@Test
	public void testDelete_TestsValidDelete() throws DataAccessException {
		WritablePlaylist a = new WritablePlaylist("Test1");
		super.playlistDao.create(a);
		super.playlistDao.delete(a.getId());
		Playlist actual = super.playlistDao.read(a.getId());
		assertNull(actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDelete_TestsInvalidId() throws DataAccessException {
		super.playlistDao.delete(-1);
	}

	@Test
	public void testReadAll_ShouldReturnAllSongs() throws DataAccessException {
		WritablePlaylist playlist1 = new WritablePlaylist("Test1");
		super.playlistDao.create(playlist1);
		WritablePlaylist playlist2 = new WritablePlaylist("Test2");
		super.playlistDao.create(playlist2);
		WritablePlaylist playlist3 = new WritablePlaylist("Test3");
		super.playlistDao.create(playlist3);

		List<WritablePlaylist> playlists = super.playlistDao.readAll();

		assertTrue(playlists.contains(playlist1));
		assertTrue(playlists.contains(playlist2));
		assertTrue(playlists.contains(playlist3));
	}

	@Test
	public void testRename_ShouldRenamePlaylist() throws DataAccessException {
		WritablePlaylist expected = new WritablePlaylist("Test1");
		super.playlistDao.create(expected);

		super.playlistDao.rename(expected, "Test2");
		Playlist actual = super.playlistDao.read(expected.getId());
		expected.setTitle("Test2");
		assertEquals(expected, actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRename_ShouldThrowIllegalArgumentException()
			throws IllegalArgumentException, DataAccessException {
		super.playlistDao.rename(null, null);
	}
}
