package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db.DaoFactory;

import static org.junit.Assert.*;

public class PlaylistDaoTest {
    private PlaylistDao plstdao;
    private SongDao sdao;
    private Connection con;

    @Before
    public void setUp() throws Exception {
	DaoFactory factory = DaoFactory.getInstance();
	plstdao = factory.getPlaylistDao();
	sdao = factory.getSongDao();
	con = plstdao.getConnection();
	con.setAutoCommit(false);
    }

    @After
    public void tearDown() throws Exception {
	con.rollback();
    }
    
    @Test
    public void testReplstdao_ReplstdaosExistingSimplePlaylist() throws DataAccessException {
	Playlist expected = new Playlist("Test1");
	plstdao.create(expected);

	Playlist actual = plstdao.read(expected.getId());
	assertEquals(expected, actual);
    }
    
    @Test
    public void testReplstdao_ReplstdaosExistingComplexPlaylist() throws DataAccessException {
	Playlist expected = new Playlist("Test1");
	expected.setReadonly(false);
	
	List<Song> list = new ArrayList<Song>();
	Song s1 = new Song("Artist1", "Title1", 0, "/myPath/test1.mp3");
	sdao.create(s1);
	list.add(s1);
	
	Song s2 = new Song("Artist2", "Title2", 0, "/myPath/test2.mp3");
	sdao.create(s2);
	list.add(s2);
	
	Song s3 = new Song("Artist3", "Title3", 0, "/myPath/test3.mp3");
	sdao.create(s3);
	list.add(s3);
	
	expected.setSongs(list);
	plstdao.create(expected);

	Playlist actual = plstdao.read(expected.getId());
	assertEquals(expected, actual);
    }

    @Test
    public void testReplstdao_ReplstdaosUnexistingSong() throws DataAccessException {
	Playlist a = plstdao.read(Integer.MAX_VALUE);
	assertNull(a);
    }

    @Test
    public void testCreate_CreateValidPlaylist() throws DataAccessException {
	Playlist expected = new Playlist("Test1");
	plstdao.create(expected);
	assertTrue(expected.getId() > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_CreateSongWithInvalidTitle()
	    throws DataAccessException {
	Playlist expected = new Playlist(null);
	plstdao.create(expected);
    }

    @Test
    public void testUpdate_TestsValidUpdate() throws DataAccessException {
	Playlist expected = new Playlist("Test1");
	plstdao.create(expected);
	expected.setTitle("Test2");
	plstdao.update(expected);
	Playlist actual = plstdao.read(expected.getId());
	assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdate_TestsInvalidSong() throws DataAccessException {
	plstdao.update(null);
    }

    @Test
    public void testDelete_TestsValidDelete() throws DataAccessException {
	Playlist a = new Playlist("Test1");
	plstdao.create(a);
	plstdao.delete(a.getId());
	Playlist actual = plstdao.read(a.getId());
	assertNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDelete_TestsInvalidId() throws DataAccessException {
	plstdao.delete(-1);
    }

}
