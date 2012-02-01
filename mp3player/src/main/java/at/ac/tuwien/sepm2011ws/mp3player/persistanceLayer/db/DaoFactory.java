package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.AlbumDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.PlaylistDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.SongDao;

public class DaoFactory {
	private static DaoFactory instance;
	private final ApplicationContext context;

	private DaoFactory() {
		context = new ClassPathXmlApplicationContext("DaoSettings.xml");
	}

	public static DaoFactory getInstance() {
		if (instance == null)
			instance = new DaoFactory();
		return instance;
	}

	public SongDao getSongDao() {
		return (SongDao) context.getBean("SongDao");
	}

	public AlbumDao getAlbumDao() {
		return (AlbumDao) context.getBean("AlbumDao");
	}

	public PlaylistDao getPlaylistDao() {
		return (PlaylistDao) context.getBean("PlaylistDao");
	}
}
