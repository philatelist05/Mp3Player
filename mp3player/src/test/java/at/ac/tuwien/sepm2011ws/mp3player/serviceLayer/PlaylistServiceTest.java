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

import static org.junit.Assert.*;

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
		File playlist = new ClassPathResource("dummyPlaylist.m3u").getFile();
		playlist.delete();
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

		ps.exportPlaylist(new File("music/dummyPlaylist"), temp);

		assertTrue(new File("music/dummyPlaylist.m3u").exists());
	}
	
	@Test
	public void testImportPlaylist_ShouldImportPlaylist()
			throws DataAccessException, IOException {
		WritablePlaylist expected = new WritablePlaylist("Temp");

		File path1 = new ClassPathResource("dummy-message.wav").getFile();
		expected.add(new Song("dummy1", "dummy1", 300, path1.getAbsolutePath()));
		File path2 = new ClassPathResource("The Other Thing.wav").getFile();
		expected.add(new Song("dummy2", "dummy2", 300, path2.getAbsolutePath()));

		ps.exportPlaylist(new ClassPathResource("dummyPlaylist").getFile(), expected);
		File newFile = new ClassPathResource("dummyPlaylist.m3u").getFile();
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

	private boolean haveSamePaths(List<Song> l1,
			List<Song> l2) {
		if(l1.size() != l2.size())
			return false;
		
		int index = 0;
		for (Song song1 : l1) {
			Song song2 = l2.get(index++);
			
			String url = convertFilePathToURLPath(song2.getPath());
			
			if(!url.equals(song1.getPath()))
				return false;
		}
		return true;
	}
	
	private String convertFilePathToURLPath(String path) {
		try {
			String file = new File(path).toURI().toURL().getPath();
			return file;
		} catch (MalformedURLException e) {
			return "";
		}
	}

	// @Test
	// public void testGetLibrary_AtLeastOneSong() {
	// Playlist lib = null;
	//
	// try {
	// lib = ps.getLibrary();
	// } catch (DataAccessException e) {
	// }
	//
	// assertTrue(lib.size() > 0);
	// }

}
