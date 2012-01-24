package at.ac.tuwien.sepm2011ws.mp3player.domainObjects;

import static org.junit.Assert.*;

import org.junit.Test;

public class MetaTagsTest {

	@Test
	public void testSet_ShouldReturnCorrectValues() {
		Album album = new Album("title");
		MetaTags MetaTags = new MetaTags("Artist", "Ttile", 3001, 2000, "Genre", album);

		assertEquals(MetaTags.getArtist(), "Artist");
		assertEquals(MetaTags.getTitle(), "Ttile");
		assertEquals(MetaTags.getDuration(), 3001);
		assertEquals(MetaTags.getYear(), 2000);
		assertEquals(MetaTags.getGenre(), "Genre");
		assertEquals(MetaTags.getAlbum(), album);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetTitle_ShouldThrowIllegalArgumentException() {
		Album album = new Album("title");
		MetaTags MetaTags = new MetaTags("Artist", "Ttile", 3001, 2000, "Genre", album);
		MetaTags.setTitle(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetDuration_ShouldThrowIllegalArgumentException() {
		Album album = new Album("title");
		MetaTags MetaTags = new MetaTags("Artist", "Ttile", 3001, 2000, "Genre", album);
		MetaTags.setDuration(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetArtist_ShouldThrowIllegalArgumentException() {
		Album album = new Album("title");
		MetaTags MetaTags = new MetaTags("Artist", "Ttile", 3001, 2000, "Genre", album);
		MetaTags.setArtist(null);
	}

	@Test
	public void testSetGenre_ShouldReturnGenre() {
		Album album = new Album("title");
		MetaTags MetaTags = new MetaTags("Artist", "Ttile", 3001, 2000, "Genre", album);
		MetaTags.setGenre("Genre");
		assertEquals("Genre", MetaTags.getGenre());
	}

	@Test
	public void testSetAlbum_ShouldReturnAlbum() {
		Album album = new Album("title");
		MetaTags MetaTags = new MetaTags("Artist", "Ttile", 3001, 2000, "Genre", album);
		MetaTags.setAlbum(album);
		assertEquals(album, MetaTags.getAlbum());
	}
}
