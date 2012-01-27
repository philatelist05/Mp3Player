package at.ac.tuwien.sepm2011ws.mp3player.domainObjects;

import static org.junit.Assert.*;

import org.junit.Test;

public class AlbumTest {

	@Test(expected = IllegalArgumentException.class)
	public void testSetYear_ShouldThrowIllegalArgumentException() {
		Album album = new Album("test");
		album.setYear(-1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetTitle_ShouldThrowIllegalArgumentException() {
		Album album = new Album("test");
		album.setTitle(null);
	}
	
	public void testSetTitle_ShouldReturnTitle() {
		Album album = new Album("test");
		album.setTitle("Title");
		assertEquals("Title", album.getTitle());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetId_ShouldThrowIllegalArgumentException() {
		Album album = new Album("test");
		album.setId(-1);
	}
	
	public void testSetYear_ShouldReturnYear() {
		Album album = new Album("test");
		album.setYear(2000);
		assertEquals(2000, album.getYear());
	}

	@Test
	public void testEquals_ShouldBeEqual1() {
		Album album1 = new Album("test");
		Album album2 = new Album("test");
		assertEquals(album1, album2);
	}

	@Test
	public void testEquals_ShouldBeEqual2() {
		Album album1 = new Album(1, "title", 2000, "/");
		Album album2 = new Album(1, "title", 2000, "/");
		assertEquals(album1, album2);
	}

	@Test
	public void testHashcode_ShouldBeEqual1() {
		Album album1 = new Album("test");
		Album album2 = new Album("test");
		assertEquals(album1.hashCode(), album2.hashCode());
	}

	@Test
	public void testHashcode_ShouldBeEqual2() {
		Album album1 = new Album(1, "title", 2000, "/");
		Album album2 = new Album(1, "title", 2000, "/");
		assertEquals(album1.hashCode(), album2.hashCode());
	}
}
