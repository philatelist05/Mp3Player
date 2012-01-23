package at.ac.tuwien.sepm2011ws.mp3player.domainObjects;

import static org.junit.Assert.*;

import org.junit.Test;

public class WritablePlaylistTest {

	@Test(expected = IllegalArgumentException.class)
	public void testSetId_ShouldThrowIllegalArgumentException() {
		WritablePlaylist playlist = new WritablePlaylist("Title");
		playlist.setId(-1);
	}

	@Test
	public void testSetTitle_ShouldSetTitle() {
		WritablePlaylist playlist = new WritablePlaylist("Title");
		playlist.setTitle("Mytitle");
		assertEquals("Mytitle", playlist.getTitle());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetTitle_ShouldThrowIllegalArgumentException() {
		WritablePlaylist playlist = new WritablePlaylist("Title");
		playlist.setTitle(null);
	}
}
