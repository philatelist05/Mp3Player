/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.junit.Assert.*;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.WritablePlaylist;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.PlaylistDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.SongDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db.DaoFactory;

/**
 * @author klaus
 * 
 */
public class PlaylistServiceTest {
	private PlaylistService ps;
	private Connection con1, con2;
	private PlaylistDao playlistDao;
	private SongDao songDao;

	@Before
	public void setUp() throws Exception {
		ServiceFactory sf = ServiceFactory.getInstance();
		ps = sf.getPlaylistService();
		DaoFactory factory = DaoFactory.getInstance();
		playlistDao = factory.getPlaylistDao();
		songDao = factory.getSongDao();
		con1 = playlistDao.getConnection();
		con1.setAutoCommit(false);
		con2 = playlistDao.getConnection();
		con2.setAutoCommit(false);
	}

	@After
	public void tearDown() throws Exception {
		con1.rollback();
		con2.rollback();
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
	public void testExportPlaylist_ShouldThrowIllegalArgumentException() {
		ps.exportPlaylist(null, null);
	}

	@Test
	public void testImportPlaylist_ShouldImportPlaylist()
			throws DataAccessException, IOException {
		WritablePlaylist expected = new WritablePlaylist("Temp");

		File path1 = new ClassPathResource("dummy-message.wav").getFile();
		expected.add(new Song("dummy1", "dummy1", 300, path1.getAbsolutePath()));
		File path2 = new ClassPathResource("The Other Thing.wav").getFile();
		expected.add(new Song("dummy2", "dummy2", 300, path2.getAbsolutePath()));

		ps.exportPlaylist(new File("dummyPlaylist"),
				expected);
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

			String url = convertFilePathToURLPath(song2.getPath());

			if (!url.equals(song1.getPath()))
				return false;
		}
		return true;
	}

	private String convertFilePathToURLPath(String path) {
		try {
			String file = new ClassPathResource(path).getFile().toURI().toURL().getPath();
			return file;
		} catch (MalformedURLException e) {
			return "";
		} catch (IOException e) {
			return "";
		}
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
		ps.addSongs(new File[] { new ClassPathResource("dummy-message.wav").getFile(),
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
	public void testAddSongsToPlaylist_ShouldThrowIllegalArgumentException()
			throws IllegalArgumentException, DataAccessException {
		ps.addSongsToPlaylist(null, null);
	}
}
