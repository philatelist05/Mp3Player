/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.db.ServiceFactory;

/**
 * @author klaus
 *
 */
public class PlaylistServiceTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetLibrary() {
		ServiceFactory sf = ServiceFactory.getInstance();
		PlaylistService ps = sf.getPlaylistService();
		Playlist lib = ps.getLibrary();
		
		assertNotNull(lib.getSongs());
		assertTrue(lib.getSongs().size() > 0);
	}

}
