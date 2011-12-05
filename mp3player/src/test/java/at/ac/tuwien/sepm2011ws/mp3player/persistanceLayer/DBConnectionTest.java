package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import static org.junit.Assert.*;
import java.sql.Connection;
import org.junit.Test;

import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DBConnection;

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
		Connection con = DBConnection.getInstance();
		assertNotNull(con);
	}
}
