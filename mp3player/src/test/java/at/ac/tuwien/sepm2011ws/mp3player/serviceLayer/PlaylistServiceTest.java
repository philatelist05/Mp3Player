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

/**
 * @author klaus
 *
 */
public class PlaylistServiceTest {
	private PlaylistService ps;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ServiceFactory sf = ServiceFactory.getInstance();
		ps = sf.getPlaylistService();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetLibrary_AtLeastOneSong() {
		Playlist lib = ps.getLibrary();
		
		assertNotNull(lib.getSongs());
		assertTrue(lib.getSongs().size() > 0);
	}

}
