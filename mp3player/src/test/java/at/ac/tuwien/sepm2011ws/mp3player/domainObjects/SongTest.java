package at.ac.tuwien.sepm2011ws.mp3player.domainObjects;

import static org.junit.Assert.*;

import org.junit.Test;

public class SongTest {

	@Test
	public void testSet_ShouldReturnCorrectValues() {
		Song song = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");

		assertEquals(song.getArtist(), "Song1");
		assertEquals(song.getTitle(), "Halo1");
		assertEquals(song.getDuration(), 3001);
		assertEquals(song.getPath(), "C:\\music\\halo1");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetId_ShouldThrowIllegalArgumentException() {
		Song song = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		song.setId(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetTitle_ShouldThrowIllegalArgumentException() {
		Song song = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		song.setTitle(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetDuration_ShouldThrowIllegalArgumentException() {
		Song song = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		song.setDuration(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetPlaycount_ShouldThrowIllegalArgumentException() {
		Song song = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		song.setPlaycount(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetRating_ShouldThrowIllegalArgumentException_Low() {
		Song song = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		song.setRating(-2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetRating_ShouldThrowIllegalArgumentException_High() {
		Song song = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		song.setRating(12);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetPath_ShouldThrowIllegalArgumentException() {
		Song song = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		song.setPath(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetArtist_ShouldThrowIllegalArgumentException() {
		Song song = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		song.setArtist(null);
	}

	@Test
	public void testSetGenre_ShouldReturnGenre() {
		Song song = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		song.setGenre("Genre");
		assertEquals("Genre", song.getGenre());
	}

	@Test
	public void testSetAlbum_ShouldReturnAlbum() {
		Song song = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		Album album = new Album("Album");
		song.setAlbum(album);
		assertEquals(album, song.getAlbum());
	}

	@Test
	public void testSetLyric_ShouldReturnLyric() {
		Song song = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		Lyric lyric = new Lyric("Lyric");
		song.setLyric(lyric);
		assertEquals(lyric, song.getLyric());
	}

	@Test
	public void testSetPathOk_ShouldReturnTrue() {
		Song song = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		song.setPathOk(true);
		assertTrue(song.isPathOk());
	}

	@Test
	public void testEquals_ShouldBeEqual1() {
		Song song1 = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		Song song2 = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		assertEquals(song1, song2);
	}

	@Test
	public void testEquals_ShouldBeEqual2() {
		Lyric lyric1 = new Lyric("Lyric");
		Lyric lyric2 = new Lyric("Lyric");

		Album album1 = new Album("Album");
		Album album2 = new Album("Album");

		Song song1 = new Song(1, "Title", 100, 2, 3, "Path", 0, "Artist",
				"Genre", true, album1, lyric1);
		Song song2 = new Song(1, "Title", 100, 2, 3, "Path", 0, "Artist",
				"Genre", true, album2, lyric2);

		assertEquals(song1, song2);
	}

	@Test
	public void testHashcode_ShouldBeEqual1() {
		Song song1 = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		Song song2 = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		assertEquals(song1.hashCode(), song2.hashCode());
	}

	@Test
	public void testHashcode_ShouldBeEqual2() {
		Lyric lyric1 = new Lyric("Lyric");
		Lyric lyric2 = new Lyric("Lyric");

		Album album1 = new Album("Album");
		Album album2 = new Album("Album");

		Song song1 = new Song(1, "Title", 100, 2, 3, "Path", 0, "Artist",
				"Genre", true, album1, lyric1);
		Song song2 = new Song(1, "Title", 100, 2, 3, "Path", 0, "Artist",
				"Genre", true, album2, lyric2);

		assertEquals(song1.hashCode(), song2.hashCode());
	}

	@Test
	public void testToString_ShouldReturnEmptyString() {
		Song song = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		song.setPathOk(true);
		assertEquals("", song.toString());
	}

	@Test
	public void testToString_ShouldReturnCallsign() {
		Song song = new Song("Song1", "Halo1", 3001, "C:\\music\\halo1");
		song.setPathOk(false);
		assertEquals(" ! Datei nicht gefunden !", song.toString());
	}
}
