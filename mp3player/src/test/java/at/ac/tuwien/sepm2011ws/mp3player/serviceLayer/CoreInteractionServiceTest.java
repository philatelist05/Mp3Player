/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;

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
		cs.setCurrentPlaylist(null);
		cs.setPlayMode(PlayMode.NORMAL);
	}

	@Test
	public void testPlayPause_ShouldPlay() throws InterruptedException {
		File sPath = new File("music/dummy-message.wav");
		Song s = new Song("dummy", "dummy", 300, sPath.getAbsolutePath());

		cs.playPause(s);
		Thread.sleep(1000);
		cs.stop();
	}

	@Test
	public void testSeek_ShouldSeek() throws InterruptedException {
		File sPath = new File("music/The Other Thing.wav");
		Song s = new Song("dummy", "dummy", 300, sPath.getAbsolutePath());

		cs.playPause(s);
		Thread.sleep(500);
		cs.seek(20);
		Thread.sleep(1000);
		cs.seek(50);
		Thread.sleep(1000);
		cs.stop();
	}

	@Test
	public void testEndOfMedia_ShouldPlayNext() throws InterruptedException {
		Playlist temp = new Playlist("Temp");

		File sPath = new File("music/dummy-message.wav");
		temp.addSong(new Song("dummy1", "dummy1", 300, sPath.getAbsolutePath()));
		sPath = new File("music/The Other Thing.wav");
		temp.addSong(new Song("dummy2", "dummy2", 300, sPath.getAbsolutePath()));

		cs.setCurrentPlaylist(temp);

		cs.playPause(null);
		Thread.sleep(500);
		assertTrue(cs.getCurrentSong().equals(temp.getSongs().get(0)));
		cs.seek(100);
		// Here the endOfMediaEvent should be fired
		Thread.sleep(3000); // The player needs a bit time to realize that the
							// song is at the end... -,-
		assertTrue(cs.getCurrentSong().equals(temp.getSongs().get(1)));
		cs.stop();
	}

	@Test
	public void testPlayPause_ShouldPauseAndContinue()
			throws InterruptedException {
		File sPath = new File("music/dummy-message.wav");
		Song s = new Song("dummy", "dummy", 300, sPath.getAbsolutePath());

		// play
		cs.playPause(s);
		Thread.sleep(500);
		// pause
		cs.playPause(s);
		double then = cs.getPlayTime();
		// play again
		cs.playPause(s);
		Thread.sleep(500);
		// Now has to be greater than then
		assertTrue(cs.getPlayTime() > then);
		cs.stop();
	}
}
