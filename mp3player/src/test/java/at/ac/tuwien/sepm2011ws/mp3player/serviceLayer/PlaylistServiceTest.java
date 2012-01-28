/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import java.io.File;
import java.sql.Connection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
	private Connection con;
	private PlaylistDao playlistDao;
	private SongDao songDao;

	@Before
	public void setUp() throws Exception {
		ServiceFactory sf = ServiceFactory.getInstance();
		ps = sf.getPlaylistService();
		DaoFactory factory = DaoFactory.getInstance();
		playlistDao = factory.getPlaylistDao();
		songDao = factory.getSongDao();
		con = playlistDao.getConnection();
		con.setAutoCommit(false);
	}

	@After
	public void tearDown() throws Exception {
		con.rollback();
		File playlist = new File("music/dummyPlaylist.m3u");
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
	public void testExportPlaylist_ValidFile() {
		WritablePlaylist temp = new WritablePlaylist("Temp");

		File sPath = new File("music/dummy-message.wav");
		temp.add(new Song("dummy1", "dummy1", 300, sPath.getAbsolutePath()));
		sPath = new File("music/The Other Thing.wav");
		temp.add(new Song("dummy2", "dummy2", 300, sPath.getAbsolutePath()));

		ps.exportPlaylist(new File("music/dummyPlaylist"), temp);

		assertTrue(new File("music/dummyPlaylist.m3u").exists());
	}

	@Test
	public void testImportPlaylist_ShouldImportPlaylist() throws DataAccessException {
		WritablePlaylist expected = new WritablePlaylist("Temp");

		File sPath = new File("music/dummy-message.wav");
		expected.add(new Song("dummy1", "dummy1", 300, sPath.getAbsolutePath()));
		sPath = new File("music/The Other Thing.wav");
		expected.add(new Song("dummy2", "dummy2", 300, sPath.getAbsolutePath()));

		ps.exportPlaylist(new File("music/dummyPlaylist"), expected);

		File newFile = new File("music/dummyPlaylist.m3u");
		ps.importPlaylist(new File[] {newFile});
		
		List<WritablePlaylist> playlists = playlistDao.readAll();
		
		expected.setTitle(newFile.getName());
		int index = playlists.indexOf(expected);
		
		assertTrue(index >= 0);
		assertEquals(expected, playlists.get(index));
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
