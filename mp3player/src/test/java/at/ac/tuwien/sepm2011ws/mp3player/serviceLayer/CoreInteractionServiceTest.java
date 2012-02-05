package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.PlayMode;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.WritablePlaylist;
import org.junit.After;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CoreInteractionServiceTest extends AbstractServiceTest {

	@After
	public void tearDown() throws Exception {
		super.coreInteractionService.stop();
		super.coreInteractionService.setCurrentPlaylist(new Playlist(""));
		super.coreInteractionService.setPlayMode(PlayMode.NORMAL);
	}

	@Test
	public void testEndOfMedia_ShouldPlayNext() throws InterruptedException,
			IOException {
		WritablePlaylist temp = new WritablePlaylist("Temp");

		File sPath = new ClassPathResource("dummy-message.wav").getFile();
		temp.add(new Song("dummy1", "dummy1", 300, sPath.getAbsolutePath()));
		sPath = new ClassPathResource("The Other Thing.wav").getFile();
		temp.add(new Song("dummy2", "dummy2", 300, sPath.getAbsolutePath()));

		super.coreInteractionService.setCurrentPlaylist(temp);

		// now start playing
		super.coreInteractionService.playPause();
		Thread.sleep(500);
		assertEquals(super.coreInteractionService.getCurrentSong(), temp.get(0));
		// Here the endOfMediaEvent should be fired
		Thread.sleep(3500); // The player needs a bit time to realize that the
		// song is at the end... -,-
		Song actual = super.coreInteractionService.getCurrentSong();
		Song expected = temp.get(1);

		assertEquals(actual, expected);
	}

	@Test
	public void testPlayPause_ShouldPlay() throws InterruptedException, IOException {
		File sPath = new ClassPathResource("dummy-message.wav").getFile();
		Song s = new Song("dummy", "dummy", 300, sPath.getAbsolutePath());
		Playlist p = new Playlist("Test");
		p.add(s);
		super.coreInteractionService.setCurrentPlaylist(p);

		super.coreInteractionService.playPause(0);
		Thread.sleep(1000);
	}

	@Test
	public void testSeek_ShouldSeek() throws InterruptedException, IOException {
		File sPath = new ClassPathResource("The Other Thing.wav").getFile();
		Song s = new Song("dummy", "dummy", 300, sPath.getAbsolutePath());
		Playlist p = new Playlist("Test");
		p.add(s);
		super.coreInteractionService.setCurrentPlaylist(p);

		super.coreInteractionService.playPause(0);
		Thread.sleep(500);
		super.coreInteractionService.seek(20);
		Thread.sleep(1000);
		super.coreInteractionService.seek(50);
		Thread.sleep(1000);
	}

	@Test
	public void testPlayPause_ShouldIncrementPlayTime()
			throws InterruptedException, IOException {
		File sPath = new ClassPathResource("dummy-message.wav").getFile();
		Song s = new Song("dummy", "dummy", 300, sPath.getAbsolutePath());
		Playlist p = new Playlist("Test");
		p.add(s);
		super.coreInteractionService.setCurrentPlaylist(p);

		// play
		super.coreInteractionService.playFromBeginning(0);
		Thread.sleep(500);
		double then = super.coreInteractionService.getPlayTime();
		Thread.sleep(1000);
		// Now has to be greater than then
		double actual = super.coreInteractionService.getPlayTime();
		assertTrue(actual > then);
	}
}
