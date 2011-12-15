/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vvv.ServiceFactory;

/**
 * @author klaus
 *
 */
public class CoreInteractionServiceTest {
	private CoreInteractionService cs;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ServiceFactory sf = ServiceFactory.getInstance();
		cs = sf.getCoreInteractionService();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPlayPause_ShouldPlay() throws InterruptedException {
		File sPath = new File("music/dummy-message.wav");
		Song s = new Song("dummy", "dummy", 300, sPath.getAbsolutePath());
		
		cs.playPause(s);
		Thread.sleep(2000);
		cs.stop();
	}
	
	@Test
	public void testSeek_ShouldSeek() throws InterruptedException {
		File sPath = new File("music/The Other Thing.wav");
		Song s = new Song("dummy", "dummy", 300, sPath.getAbsolutePath());
		
		cs.playPause(s);
		Thread.sleep(4000);
		cs.seek(50);
		Thread.sleep(4000);
		cs.stop();
	}

}
