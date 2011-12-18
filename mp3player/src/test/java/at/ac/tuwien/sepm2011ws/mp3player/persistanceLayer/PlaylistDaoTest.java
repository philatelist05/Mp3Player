package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Album;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db.DaoFactory;


public class PlaylistDaoTest {
	private PlaylistDao pd;
	private Connection con;

	/**
	 * @throws java.lang.Exception
	 */
/*
	@Before
	public void setUp() throws Exception {
		DaoFactory factory = DaoFactory.getInstance();
		pd = factory.getAlbumDao();
		con = ad.getConnection();
		con.setAutoCommit(false);
	}
*/
	/**
	 * @throws java.lang.Exception
	 */
/*
	@After
	public void tearDown() throws Exception {
		con.rollback();
	}
*/
}
