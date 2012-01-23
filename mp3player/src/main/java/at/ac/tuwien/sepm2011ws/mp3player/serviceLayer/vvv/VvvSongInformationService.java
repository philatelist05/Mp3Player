package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vvv;

import java.util.List;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Lyric;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.MetaTags;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.SongInformationService;

public class VvvSongInformationService implements SongInformationService {

	@Override
	public void getMetaTags(Song song) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMetaTags(Song song) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<MetaTags> downloadMetaTags(Song song) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Lyric> downloadLyrics(Song song) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void incrementPlaycount(Song song) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRating(Song song, int rating) {
		// TODO Auto-generated method stub

	}

}
