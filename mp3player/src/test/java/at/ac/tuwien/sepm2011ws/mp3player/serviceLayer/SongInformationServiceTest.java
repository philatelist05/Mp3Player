package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.MetaTags;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SongInformationServiceTest extends AbstractServiceTest {

	@Test
	public void testDownloadMetaTags_ShouldReturnAtLeastOneMetatag() throws DataAccessException {
		Song song = new Song("Blur", "Song2", 0, "nil");
		List<MetaTags> metatags = super.songInformationService.downloadMetaTags(song);
		assertTrue(metatags.size() > 0);
	}

}
