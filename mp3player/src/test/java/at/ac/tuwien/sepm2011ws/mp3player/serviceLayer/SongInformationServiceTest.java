package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import java.sql.Connection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.MetaTags;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.PlaylistDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.SongDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db.DaoFactory;

public class SongInformationServiceTest {
	private SongInformationService songInformationService;
	private Connection con;
	private PlaylistDao playlistDao;
	private SongDao songDao;

	@Before
	public void setUp() throws Exception {
		ServiceFactory sf = ServiceFactory.getInstance();
		songInformationService = sf.getSongInformationService();
		DaoFactory factory = DaoFactory.getInstance();
		playlistDao = factory.getPlaylistDao();
		songDao = factory.getSongDao();
		con = playlistDao.getConnection();
		con.setAutoCommit(false);
	}

	@After
	public void tearDown() throws Exception {
		con.rollback();
	}

	@Test
	public void testDownloadMetaTags_ShouldReturnAtLeastOneMetatag() throws DataAccessException {
		Song song = new Song("Guns and Roses", "It’s So Easy", 0, "nil");
		List<MetaTags> metatags = songInformationService.downloadMetaTags(song);
		assertTrue(metatags.size() > 0);
	}

}
