package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.MetaTags;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.PlaylistDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.SongDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db.DaoFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class SongInformationServiceTest {
	private SongInformationService songInformationService;
	private Connection con1;
	private Connection con2;
	private PlaylistDao playlistDao;
	private SongDao songDao;

	@Before
	public void setUp() throws Exception {
		ServiceFactory sf = ServiceFactory.getInstance();
		songInformationService = sf.getSongInformationService();
		DaoFactory factory = DaoFactory.getInstance();
		playlistDao = factory.getPlaylistDao();
		songDao = factory.getSongDao();
		con1 = playlistDao.getDbConnection();
		con1.setAutoCommit(false);
        con2 = songDao.getDbConnection();
		con2.setAutoCommit(false);
	}

	@After
	public void tearDown() throws Exception {
		con1.rollback();
		con2.rollback();
	}

	@Test
	public void testDownloadMetaTags_ShouldReturnAtLeastOneMetatag() throws DataAccessException {
		Song song = new Song("Blur", "Song2", 0, "nil");
		List<MetaTags> metatags = songInformationService.downloadMetaTags(song);
		assertTrue(metatags.size() > 0);
	}

}
