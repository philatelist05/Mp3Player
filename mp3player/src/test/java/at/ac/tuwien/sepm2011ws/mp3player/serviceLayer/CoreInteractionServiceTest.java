/**
 * 
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import static org.junit.Assert.*;
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

	@Before
	public void setUp() throws Exception {
		ServiceFactory sf = ServiceFactory.getInstance();
		cs = sf.getCoreInteractionService();
	}

	@After
	public void tearDown() throws Exception {
	    	cs.stop();
		cs.setCurrentPlaylist(null);
		cs.setPlayMode(PlayMode.NORMAL);
	}
	
	@Test
	public void testEndOfMedia_ShouldPlayNext() throws InterruptedException {
		Playlist temp = new Playlist("Temp");

		File sPath = new File("music/dummy-message.wav");
		temp.addSong(new Song("dummy1", "dummy1", 300, sPath.getAbsolutePath()));
		sPath = new File("music/The Other Thing.wav");
		temp.addSong(new Song("dummy2", "dummy2", 300, sPath.getAbsolutePath()));

		cs.setCurrentPlaylist(temp);
		
		//now start playing
		cs.playPause();
		Thread.sleep(500);
		assertEquals(cs.getCurrentSong(),temp.getSongs().get(0));
		// Here the endOfMediaEvent should be fired
		Thread.sleep(3500); // The player needs a bit time to realize that the
							// song is at the end... -,-
		Song actual = cs.getCurrentSong();
		Song expected = temp.getSongs().get(1);
		
		assertEquals(actual,expected);
	}

	@Test
	public void testPlayPause_ShouldPlay() throws InterruptedException {
		File sPath = new File("music/dummy-message.wav");
		Song s = new Song("dummy", "dummy", 300, sPath.getAbsolutePath());

		cs.playPause(s);
		Thread.sleep(1000);
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
	}

	@Test
	public void testPlayPause_ShouldIncrementPlayTime()
			throws InterruptedException {
		File sPath = new File("music/dummy-message.wav");
		Song s = new Song("dummy", "dummy", 300, sPath.getAbsolutePath());

		// play
		cs.playFromBeginning(s);
		Thread.sleep(500);
		double then = cs.getPlayTime();
		Thread.sleep(1000);
		// Now has to be greater than then
		double actual = cs.getPlayTime();
		assertTrue(actual > then);
	}
}
