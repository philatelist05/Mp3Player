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
	
	@Test
	public void testSetTitle_ShouldReturnTitle() {
		Album album = new Album("title");
		MetaTags MetaTags = new MetaTags("Artist", "Ttile", 3001, 2000, "Genre", album);
		MetaTags.setTitle("Title1");
		assertEquals("Title1", MetaTags.getTitle());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetDuration_ShouldThrowIllegalArgumentException() {
		Album album = new Album("title");
		MetaTags MetaTags = new MetaTags("Artist", "Ttile", 3001, 2000, "Genre", album);
		MetaTags.setDuration(-1);
	}
	
	@Test
	public void testSetDuration_ShouldReturnDuration() {
		Album album = new Album("title");
		MetaTags MetaTags = new MetaTags("Artist", "Ttile", 3001, 2000, "Genre", album);
		MetaTags.setDuration(100);
		assertEquals(100, MetaTags.getDuration());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetArtist_ShouldThrowIllegalArgumentException() {
		Album album = new Album("title");
		MetaTags MetaTags = new MetaTags("Artist", "Ttile", 3001, 2000, "Genre", album);
		MetaTags.setArtist(null);
	}
	
	@Test
	public void testSetArtist_ShouldReturnArtist() {
		Album album = new Album("title");
		MetaTags MetaTags = new MetaTags("Artist", "Ttile", 3001, 2000, "Genre", album);
		MetaTags.setArtist("Artist1");
		assertEquals("Artist1", MetaTags.getArtist());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetYear_ShouldThrowIllegalArgumentException() {
		Album album = new Album("title");
		MetaTags MetaTags = new MetaTags("Artist", "Ttile", 3001, 2000, "Genre", album);
		MetaTags.setYear(-1);
	}
	
	@Test
	public void testSetYear_ShouldReturnYear() {
		Album album = new Album("title");
		MetaTags MetaTags = new MetaTags("Artist", "Ttile", 3001, 2000, "Genre", album);
		MetaTags.setYear(2000);
		assertEquals(2000, MetaTags.getYear());
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
	
	@Test
	public void testHashcode_ShouldBeTheEqual() {
		Album album1 = new Album("title");
		MetaTags metatags1 = new MetaTags("Artist", "Ttile", 3001, 2000, "Genre", album1);
		
		Album album2 = new Album("title");
		MetaTags metatags2 = new MetaTags("Artist", "Ttile", 3001, 2000, "Genre", album2);
		
		assertEquals(metatags1.hashCode(), metatags2.hashCode());
	}
}
