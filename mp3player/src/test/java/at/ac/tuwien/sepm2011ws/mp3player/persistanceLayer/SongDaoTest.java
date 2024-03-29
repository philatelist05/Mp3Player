/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Album;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Lyric;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SongDaoTest extends AbstractDaoTest{

	@Test(expected = IllegalArgumentException.class)
	public void testCreate_WithIllegalArgument()
			throws IllegalArgumentException, DataAccessException {
		super.songDao.create(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testRead_WithIllegalArgument()
			throws IllegalArgumentException, DataAccessException {
		super.songDao.read(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetTopRatedSongs_WithIllegalArgument()
			throws IllegalArgumentException, DataAccessException {
		super.songDao.getTopRatedSongs(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetTopPlayedSongs_WithIllegalArgument()
			throws IllegalArgumentException, DataAccessException {
		super.songDao.getTopPlayedSongs(-1);
	}

	@Test
	public void testCreate_ShouldCreateSongWithLyric()
			throws DataAccessException {
		Song expected = new Song("Machine Head", "Halo", 300, "C:\\music\\halo");
		Lyric lyric = new Lyric("Das ist eine Lyric");
		expected.setLyric(lyric);
		super.songDao.create(expected);

		Song actual = super.songDao.read(expected.getId());
		assertEquals(expected, actual);
	}

	@Test
	public void testCreate_ShouldCreateSongWithAlbum()
			throws DataAccessException {
		Song expected = new Song("Machine Head", "Halo", 300, "C:\\music\\halo");
		Album album = new Album("Album1");
		expected.setAlbum(album);
		super.songDao.create(expected);

		Song actual = super.songDao.read(expected.getId());
		assertEquals(expected, actual);
	}

	@Test
	public void testUpdate_ShouldUpdateSongWithLyric() throws DataAccessException {
		Song expected = new Song("Machine Head", "Halo", 300, "C:\\music\\halo");
		super.songDao.create(expected);
		
		Lyric lyric = new Lyric("Das ist eine Lyric");
		expected.setLyric(lyric);
		super.songDao.update(expected);

		Song actual = super.songDao.read(expected.getId());
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCreate_ShouldUpdateSongWithNoExistingAlbum()
			throws DataAccessException {
		Song expected = new Song("Machine Head", "Halo", 300, "C:\\music\\halo");
		super.songDao.create(expected);

		Album album = new Album("Album1");
		expected.setAlbum(album);
		super.songDao.update(expected);
		
		Song actual = super.songDao.read(expected.getId());
		assertEquals(expected, actual);
	}

	@Test
	public void testReadAll_ShouldReadAllSongs() throws DataAccessException {
		Song s = new Song("Machine Head", "Halo", 300, "C:\\music\\halo");
		super.songDao.create(s);
		Song s1 = new Song("Machine Head", "Halo", 300, "C:\\music\\halo");
		super.songDao.create(s1);

		List<Song> dList = super.songDao.readAll();
		assertTrue(dList.contains(s));
		assertTrue(dList.contains(s1));
	}
	
	@Test
	public void testReadAll_ShouldReadAllSongsWithLyric() throws DataAccessException {
		Song s = new Song("Machine Head", "Halo", 300, "C:\\music\\halo");
		Lyric lyric1 = new Lyric("Lyric1");
		s.setLyric(lyric1);
		super.songDao.create(s);
		
		Song s1 = new Song("Machine Head", "Halo", 300, "C:\\music\\halo");
		Lyric lyric2 = new Lyric("Lyric2");
		s1.setLyric(lyric2);
		super.songDao.create(s1);

		List<Song> dList = super.songDao.readAll();
		assertTrue(dList.contains(s));
		assertTrue(dList.contains(s1));
	}

	@Test
	public void testRead_ReadsExistingSong() throws DataAccessException {
		Song expected = new Song("Machine Head", "Halo", 300, "C:\\music\\halo");
		super.songDao.create(expected);

		Song actual = super.songDao.read(expected.getId());
		assertEquals(expected, actual);
	}

	@Test
	public void testRead_ReadsUnexistingSong() throws DataAccessException {
		Song s = super.songDao.read(Integer.MAX_VALUE);
		assertNull(s);
	}

	@Test
	public void testCreate_CreateValidSong() throws DataAccessException {
		Song s = new Song("Machine Head", "Halo", 300, "C:\\music\\halo");
		super.songDao.create(s);
		assertTrue(s.getId() > 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreate_CreateSongWithInvalidArtist()
			throws DataAccessException {
		Song s = new Song(null, "Halo", 300, "C:\\music\\halo");

		super.songDao.create(s);
	}

	@Test
	public void testUpdate_TestsValidUpdate() throws DataAccessException {
		Song excepted = new Song("Machine Head", "Halo", 300, "C:\\music\\halo");
		super.songDao.create(excepted);
		excepted.setGenre("Thrash Metal");
		super.songDao.update(excepted);
		Song actual = super.songDao.read(excepted.getId());
		assertEquals(excepted, actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUpdate_TestsInvalidSong() throws DataAccessException {
		super.songDao.update(null);
	}

	@Test
	public void testDelete_TestsValidDelete() throws DataAccessException {
		Song s = new Song("Machine Head", "Halo", 300, "C:\\music\\halo");
		super.songDao.create(s);
		super.songDao.delete(s.getId());
		Song actual = super.songDao.read(s.getId());
		assertNull(actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDelete_TestsInvalidId() throws DataAccessException {
		super.songDao.delete(-1);
	}

	@Test
	public void testGetTopPlayedSongs_ShouldReturnTopPlayedSongs()
			throws DataAccessException {
		List<Song> list = new ArrayList<Song>();
		for (int i = 0; i < 5; i++) {
			Song s = new Song("artist", "title", 0, "myPath" + i);
			s.setRating(i);
			s.setPlaycount(4 - i);
			list.add(s);
			super.songDao.create(s);
		}

		Collections.sort(list, new Comparator<Song>() {
			public int compare(Song s1, Song s2) {
				if (s1.getPlaycount() < s2.getPlaycount())
					return 1;
				else if (s1.getPlaycount() < s2.getPlaycount())
					return 0;
				return -1;
			}
		});

		List<Song> topPlayed = super.songDao.getTopPlayedSongs(40);
		Iterator<Song> iter = topPlayed.iterator();

		while (iter.hasNext()) {
			Song current = iter.next();
			if (!list.contains(current))
				iter.remove();
		}

		assertEquals(topPlayed.size(), list.size());
		for (int i = 0; i < topPlayed.size(); i++) {
			assertEquals(topPlayed.get(i), list.get(i));
		}
	}

	@Test
	public void testGetTopRatedSongs_ShouldReturnTopRatedSongs()
			throws DataAccessException {
		List<Song> list = new ArrayList<Song>();
		for (int i = 0; i < 5; i++) {
			Song s = new Song("artist", "title", 0, "myPath" + i);
			s.setRating(4 - i);
			s.setPlaycount(i);
			list.add(s);
			super.songDao.create(s);
		}

		Collections.sort(list, new Comparator<Song>() {
			public int compare(Song s1, Song s2) {
				if (s1.getRating() < s2.getRating())
					return 1;
				else if (s1.getRating() < s2.getRating())
					return 0;
				return -1;
			}
		});

		List<Song> topRated = super.songDao.getTopRatedSongs(40);
		Iterator<Song> iter = topRated.iterator();

		while (iter.hasNext()) {
			Song current = iter.next();
			if (!list.contains(current))
				iter.remove();
		}

		assertEquals(topRated.size(), list.size());
		for (int i = 0; i < topRated.size(); i++) {
			assertEquals(topRated.get(i), list.get(i));
		}
	}
}
