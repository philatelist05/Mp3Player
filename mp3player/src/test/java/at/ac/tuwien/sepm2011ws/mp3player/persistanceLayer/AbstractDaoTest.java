package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations = {"classpath:DaoSettings.xml"})
abstract class AbstractDaoTest extends AbstractTransactionalJUnit4SpringContextTests {
	protected AlbumDao albumDao;
	protected PlaylistDao playlistDao;
	protected SongDao songDao;

	@Autowired
	void setAlbumDao(AlbumDao albumDao) {
		this.albumDao = albumDao;
	}

	@Autowired
	void setPlaylistDao(PlaylistDao playlistDao){
		this.playlistDao = playlistDao;
	}

	@Autowired
	void setSongDao(SongDao songDao){
		this.songDao = songDao;
	}
}
