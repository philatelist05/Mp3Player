package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.WritablePlaylist;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class PlaylistServiceTest extends AbstractServiceTest {

	@Test(expected = IllegalArgumentException.class)
	public void testImportPlaylist_WithIllegalArguments()
			throws IllegalArgumentException, DataAccessException {
		super.playlistService.importPlaylist(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testImportPlaylist_InvalidFile()
			throws IllegalArgumentException, DataAccessException {
		File[] dir = new File[] {null};
		super.playlistService.importPlaylist(dir);
	}

	@Test
	public void testExportPlaylist_ValidFile() throws IOException {
		WritablePlaylist temp = new WritablePlaylist("Temp");

		File sPath = new ClassPathResource("dummy-message.wav").getFile();
		temp.add(new Song("dummy1", "dummy1", 300, sPath.getAbsolutePath()));
		sPath = new ClassPathResource("The Other Thing.wav").getFile();
		temp.add(new Song("dummy2", "dummy2", 300, sPath.getAbsolutePath()));

		super.playlistService.exportPlaylist(new File("dummyPlaylist"), temp);

		assertTrue(new File("dummyPlaylist.m3u").exists());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExportPlaylist_ShouldThrowIllegalArgumentException1() {
		super.playlistService.exportPlaylist(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExportPlaylist_ShouldThrowIllegalArgumentException2() {
		super.playlistService.exportPlaylist(new File("."), null);
	}

	@Test
	public void testImportPlaylist_ShouldImportPlaylist()
			throws DataAccessException, IOException {
		WritablePlaylist expected = new WritablePlaylist("Temp");

		File path1 = new ClassPathResource("dummy-message.wav").getFile();
		expected.add(new Song("dummy1", "dummy1", 300, path1.getAbsolutePath()));
		File path2 = new ClassPathResource("The Other Thing.wav").getFile();
		expected.add(new Song("dummy2", "dummy2", 300, path2.getAbsolutePath()));

		super.playlistService.exportPlaylist(new File("dummyPlaylist"), expected);
		File newFile = new File("dummyPlaylist.m3u");
		super.playlistService.importPlaylist(new File[] {newFile});

		List<WritablePlaylist> playlists = super.playlistService.getAllPlaylists();

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

		List<WritablePlaylist> playlists = super.playlistService.getAllPlaylists();
		assertTrue(playlists.contains(temp));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddFolder_ShouldThrowIllegalArgumentException()
			throws IllegalArgumentException, DataAccessException {
		super.playlistService.addFolder(null);
	}

	@Test
	public void testAddFolder_ShouldAddFolderToLibrary()
			throws DataAccessException, IOException {
		Playlist oldPl = super.playlistService.getLibrary();
		super.playlistService.addFolder(new ClassPathResource(".").getFile());
		Playlist newPl = super.playlistService.getLibrary();
		assertEquals(2, newPl.size() - oldPl.size());
	}

	@Test
	public void testAddSongs_ShouldAddListOfSongsToLibrary()
			throws DataAccessException, IOException {
		Playlist oldPl = super.playlistService.getLibrary();
		super.playlistService.addSongs(new File[] {
				new ClassPathResource("dummy-message.wav").getFile(),
				new ClassPathResource("The Other Thing.wav").getFile()});
		Playlist newPl = super.playlistService.getLibrary();
		assertEquals(2, newPl.size() - oldPl.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddSongs_ShouldThrowIllegalArgumentException()
			throws IllegalArgumentException, DataAccessException {
		super.playlistService.addSongs(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddSongsToPlaylist_ShouldThrowIllegalArgumentException1()
			throws IllegalArgumentException, DataAccessException {
		super.playlistService.addSongsToPlaylist(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddSongsToPlaylist_ShouldThrowIllegalArgumentException2()
			throws IllegalArgumentException, DataAccessException {
		super.playlistService.addSongsToPlaylist(new File[] {null}, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDeleteSongs_ShouldThrowIllegalArgumentException1()
			throws DataAccessException, IllegalArgumentException {
		super.playlistService.deleteSongs(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDeleteSongs_ShouldThrowIllegalArgumentException2()
			throws DataAccessException, IllegalArgumentException {
		super.playlistService.deleteSongs(new ArrayList<Song>(), null);
	}

	@Test
	public void testDeleteSongs_ShouldDeleteSongInPlaylist()
			throws DataAccessException {
		WritablePlaylist playlist = new WritablePlaylist("");

		List<Song> songs = new ArrayList<Song>();
		Song song = new Song("Artist", "Title", 0, "Path");
		songs.add(song);

		playlist.add(song);
		super.playlistService.deleteSongs(songs, playlist);

		assertFalse(playlist.containsAll(songs));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreatePlaylist_ShouldThrowIllegalArgumentException()
			throws IllegalArgumentException, DataAccessException {
		super.playlistService.createPlaylist(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDeletePlaylist_ShouldThrowIllegalArgumentException()
			throws IllegalArgumentException, DataAccessException {
		super.playlistService.deletePlaylist(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUpdatePlaylist_ShouldThrowIllegalArgumentException()
			throws IllegalArgumentException, DataAccessException {
		super.playlistService.updatePlaylist(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRenamePlaylist_ShouldThrowIllegalArgumentException1()
			throws IllegalArgumentException, DataAccessException {
		super.playlistService.renamePlaylist(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRenamePlaylist_ShouldThrowIllegalArgumentException2()
			throws IllegalArgumentException, DataAccessException {
		super.playlistService.renamePlaylist(new WritablePlaylist(""), null);
	}

	@Test
	public void testRenamePlaylist_ShouldRenamePlaylist()
			throws IllegalArgumentException, DataAccessException {
		WritablePlaylist playlist = new WritablePlaylist("Test");
		super.playlistService.renamePlaylist(playlist, "Test1");
		assertEquals("Test1", playlist.getTitle());
	}

	@Test
	public void testGlobalSearch_ShouldSearchForSong() {

	}

}
