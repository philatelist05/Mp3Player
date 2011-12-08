package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;

import org.junit.Test;

import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.jdbc.DBConnection;

/**
 * 
 * JUnit test for DBConnection
 * 
 */
public class DBConnectionTest {

	/**
	 * 
	 * Tests if DBConnection offers a valid <code>Connection</code>
	 * 
	 */
	@Test
	public void DBConnectionTester() {
		Connection con = DBConnection.getConnection();
		assertNotNull(con);
	}
}
