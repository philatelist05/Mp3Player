/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;

/**
 * @author klaus
 * 
 */
public class PlaylistServiceTest {
	private PlaylistService ps;
	
	@Before
	public void setUp() throws Exception {
		ServiceFactory sf = ServiceFactory.getInstance();
		ps = sf.getPlaylistService();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetLibrary_AtLeastOneSong() {
		Playlist lib = null;

		try {
			lib = ps.getLibrary();
		} catch (DataAccessException e) {
		}

		assertTrue(lib.size() > 0);
	}

}
