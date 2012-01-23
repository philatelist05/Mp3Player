package at.ac.tuwien.sepm2011ws.mp3player.domainObjects;

import static org.junit.Assert.*;

import org.junit.Test;

public class AlbumTest {

	@Test(expected = IllegalArgumentException.class)
	public void testSetYear_ShouldThrowIllegalArgumentException() {
		Album album = new Album("test");
		album.setYear(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetId_ShouldThrowIllegalArgumentException() {
		Album album = new Album("test");
		album.setId(-1);
	}

	public void testEquals_ShouldBeEqual1() {
		Album album1 = new Album("test");
		Album album2 = new Album("test");
		assertEquals(album1, album2);
	}

	public void testEquals_ShouldBeEqual2() {
		Album album1 = new Album(1,"title",2000,"/");
		Album album2 = new Album(1,"title",2000,"/");
		assertEquals(album1, album2);
	}
	
	public void testHashcode_ShouldBeEqual1() {
		Album album1 = new Album("test");
		Album album2 = new Album("test");
		assertEquals(album1.hashCode(), album2.hashCode());
	}

	public void testHashcode_ShouldBeEqual2() {
		Album album1 = new Album(1,"title",2000,"/");
		Album album2 = new Album(1,"title",2000,"/");
		assertEquals(album1.hashCode(), album2.hashCode());
	}
}
