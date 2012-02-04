/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.WritablePlaylist;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.PlaylistDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db.DaoFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class PlaylistServiceTest {
	private PlaylistService ps;
	private PlaylistDao playlistDao;
	
	@Before
	public void setUp() throws Exception {
		ServiceFactory sf = ServiceFactory.getInstance();
		ps = sf.getPlaylistService();
		DaoFactory factory = DaoFactory.getInstance();
		playlistDao = factory.getPlaylistDao();
		factory.getSongDao();
		
	}

	@After
	public void tearDown() throws Exception {
		Resource resource = new ClassPathResource("dummyPlaylist.m3u");
		if (resource.exists()) {
			File playlist = resource.getFile();
			playlist.delete();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testImportPlaylist_WithIllegalArguments()
			throws IllegalArgumentException, DataAccessException {
		ps.importPlaylist(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testImportPlaylist_InvalidFile()
			throws IllegalArgumentException, DataAccessException {
		File[] dir = new File[] { null };
		ps.importPlaylist(dir);
	}

	@Test
	public void testExportPlaylist_ValidFile() throws IOException {
		WritablePlaylist temp = new WritablePlaylist("Temp");

		File sPath = new ClassPathResource("dummy-message.wav").getFile();
		temp.add(new Song("dummy1", "dummy1", 300, sPath.getAbsolutePath()));
		sPath = new ClassPathResource("The Other Thing.wav").getFile();
		temp.add(new Song("dummy2", "dummy2", 300, sPath.getAbsolutePath()));

		ps.exportPlaylist(new File("dummyPlaylist"), temp);

		assertTrue(new File("dummyPlaylist.m3u").exists());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExportPlaylist_ShouldThrowIllegalArgumentException1() {
		ps.exportPlaylist(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExportPlaylist_ShouldThrowIllegalArgumentException2() {
		ps.exportPlaylist(new File("."), null);
	}

	@Test
	public void testImportPlaylist_ShouldImportPlaylist()
			throws DataAccessException, IOException {
		WritablePlaylist expected = new WritablePlaylist("Temp");

		File path1 = new ClassPathResource("dummy-message.wav").getFile();
		expected.add(new Song("dummy1", "dummy1", 300, path1.getAbsolutePath()));
		File path2 = new ClassPathResource("The Other Thing.wav").getFile();
		expected.add(new Song("dummy2", "dummy2", 300, path2.getAbsolutePath()));

		ps.exportPlaylist(new File("dummyPlaylist"), expected);
		File newFile = new File("dummyPlaylist.m3u");
		ps.importPlaylist(new File[] { newFile });

		List<WritablePlaylist> playlists = playlistDao.readAll();

		expected.setTitle("dummyPlaylist");
		expected.clear();
		expected.add(new Song("Unknown Artist", "Unknown Title", 3, path1
				.getAbsolutePath()));
		expected.add(new Song("Unknown Artist", "Unknown Title", 0, path2
				.getAbsolutePath()));

		assertTrue(hasSamePaths(playlists, expected));
	}

	private boolean hasSamePaths(Collection<WritablePlaylist> playlists,
			WritablePlaylist playlist) {

		for (WritablePlaylist writablePlaylist : playlists) {
			if (writablePlaylist.getTitle().equals(playlist.getTitle())
					&& writablePlaylist.size() == playlist.size())
				return haveSamePaths(writablePlaylist, playlist);
		}
		return false;
	}

	private boolean haveSamePaths(List<Song> l1, List<Song> l2) {
		if (l1.size() != l2.size())
			return false;

		int index = 0;
		for (Song song1 : l1) {
			Song song2 = l2.get(index++);

			if (!song2.getPath().equals(song1.getPath()))
				return false;
		}
		return true;
	}

	@Test
	public void testGetAllPlaylists_ShouldGetAllPlaylists()
			throws DataAccessException, IOException {
		WritablePlaylist temp = new WritablePlaylist("Temp");

		File sPath = new ClassPathResource("dummy-message.wav").getFile();
		temp.add(new Song("dummy1", "dummy1", 300, sPath.getAbsolutePath()));
		sPath = new ClassPathResource("The Other Thing.wav").getFile();
		temp.add(new Song("dummy2", "dummy2", 300, sPath.getAbsolutePath()));

		List<WritablePlaylist> playlists = ps.getAllPlaylists();
		assertTrue(playlists.contains(temp));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddFolder_ShouldThrowIllegalArgumentException()
			throws IllegalArgumentException, DataAccessException {
		ps.addFolder(null);
	}

	@Test
	public void testAddFolder_ShouldAddFolderToLibrary()
			throws DataAccessException, IOException {
		Playlist oldPl = ps.getLibrary();
		ps.addFolder(new ClassPathResource(".").getFile());
		Playlist newPl = ps.getLibrary();
		assertEquals(2, newPl.size() - oldPl.size());
	}

	@Test
	public void testAddSongs_ShouldAddListOfSongsToLibrary()
			throws DataAccessException, IOException {
		Playlist oldPl = ps.getLibrary();
		ps.addSongs(new File[] {
				new ClassPathResource("dummy-message.wav").getFile(),
				new ClassPathResource("The Other Thing.wav").getFile() });
		Playlist newPl = ps.getLibrary();
		assertEquals(2, newPl.size() - oldPl.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddSongs_ShouldThrowIllegalArgumentException()
			throws IllegalArgumentException, DataAccessException {
		ps.addSongs(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddSongsToPlaylist_ShouldThrowIllegalArgumentException1()
			throws IllegalArgumentException, DataAccessException {
		ps.addSongsToPlaylist(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddSongsToPlaylist_ShouldThrowIllegalArgumentException2()
			throws IllegalArgumentException, DataAccessException {
		ps.addSongsToPlaylist(new File[] { null }, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDeleteSongs_ShouldThrowIllegalArgumentException1()
			throws DataAccessException, IllegalArgumentException {
		ps.deleteSongs(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDeleteSongs_ShouldThrowIllegalArgumentException2()
			throws DataAccessException, IllegalArgumentException {
		ps.deleteSongs(new ArrayList<Song>(), null);
	}

	@Test
	public void testDeleteSongs_ShouldDeleteSongInPlaylist()
			throws DataAccessException {
		WritablePlaylist playlist = new WritablePlaylist("");

		List<Song> songs = new ArrayList<Song>();
		Song song = new Song("Artist", "Title", 0, "Path");
		songs.add(song);

		playlist.add(song);
		ps.deleteSongs(songs, playlist);

		assertFalse(playlist.containsAll(songs));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreatePlaylist_ShouldThrowIllegalArgumentException()
			throws IllegalArgumentException, DataAccessException {
		ps.createPlaylist(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDeletePlaylist_ShouldThrowIllegalArgumentException()
			throws IllegalArgumentException, DataAccessException {
		ps.deletePlaylist(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUpdatePlaylist_ShouldThrowIllegalArgumentException()
			throws IllegalArgumentException, DataAccessException {
		ps.updatePlaylist(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRenamePlaylist_ShouldThrowIllegalArgumentException1()
			throws IllegalArgumentException, DataAccessException {
		ps.renamePlaylist(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRenamePlaylist_ShouldThrowIllegalArgumentException2()
			throws IllegalArgumentException, DataAccessException {
		ps.renamePlaylist(new WritablePlaylist(""), null);
	}

	@Test
	public void testRenamePlaylist_ShouldRenamePlaylist()
			throws IllegalArgumentException, DataAccessException {
		WritablePlaylist playlist = new WritablePlaylist("Test");
		ps.renamePlaylist(playlist, "Test1");
		assertEquals("Test1", playlist.getTitle());
	}

	@Test
	public void testGlobalSearch_ShouldSearchForSong() {

	}

}
