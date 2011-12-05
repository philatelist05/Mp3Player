/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.jdbc;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;

import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DBConnection;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.SongDaoTest;

/**
 * @author klaus
 * 
 */
public class JDBCSongDaoTest extends SongDaoTest {

	private Connection con;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		con = DBConnection.getInstance();
		con.setAutoCommit(false);

		setSongDao(new JDBCSongDao());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		con.rollback();
		con.setAutoCommit(true);
	}

}
