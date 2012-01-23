package at.ac.tuwien.sepm2011ws.mp3player.domainObjects;

import static org.junit.Assert.*;

import org.junit.Test;

public class PlaylistTest {

	@Test
	public void testSetText_ShouldReturnText() {
		Playlist pl = new Playlist(1,"text");
		assertEquals(pl.getTitle(), "text");
	}
	
	@Test
	public void testSetId_ShouldReturnText() {
		Playlist pl = new Playlist(1,"text");
		assertEquals(pl.getId(), 1);
	}

	@Test
	public void testToString_ShouldReturnText() {
		Playlist pl = new Playlist(1,"text");
		assertEquals(pl.toString(), "text");
	}
	
	@Test
	public void testEquals_ShouldBeEquals1() {
		Playlist pl1 = new Playlist("test");
		Playlist pl2 = new Playlist("test");
		assertEquals(pl1, pl2);
	}
	
	@Test
	public void testEquals_ShouldBeEquals2() {
		Playlist pl1 = new Playlist(1,"text");
		Playlist pl2 = new Playlist(1,"text");
		assertEquals(pl1, pl2);
	}
	
	@Test
	public void testEquals_ShouldBeEquals3() {
		Song song1 = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		Song song2 = new Song("Song2", "Halo2", 3002, "C:\\music\\halo2");
		
		Song song3 = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		Song song4 = new Song("Song2", "Halo2", 3002, "C:\\music\\halo2");
		
		Playlist pl1 = new Playlist(1,"text");
		pl1.add(song1);
		pl1.add(song2);
		
		Playlist pl2 = new Playlist(1,"text");
		pl2.add(song3);
		pl2.add(song4);
		
		assertEquals(pl1, pl2);
	}
}
