package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vvv;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.LastFmService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;
import de.umass.lastfm.Artist;

import java.util.*;

class VvvLastFmService implements LastFmService {
	private PlaylistService ps;

	VvvLastFmService(PlaylistService ps) {
		this.ps = ps;
	}

	public List<Playlist> getSimilarArtistsWithSongs(Song song)
			throws DataAccessException {
		if (song == null)
			throw new IllegalArgumentException("Song must not be null");
		if (song.getArtist() == null || song.getArtist().isEmpty())
			throw new IllegalArgumentException(
					"Song artist must not be null or empty");

		Collection<Artist> artists = Artist.getSimilar(song.getArtist(),
				similarArtistsCount, LastFmApiKey);
		Playlist library = ps.getLibrary();
		Playlist artistPl;
		List<Playlist> retList = new ArrayList<Playlist>();

		for (Artist artist : artists) {
			// Collection<Track> tracks = Artist.getTopTracks(artist.getName(),
			// LastFmApiKey);

			artistPl = new Playlist(artist.getName());
			for (Song s : library) {
				if (artist.getName().equalsIgnoreCase(s.getArtist())) {
					artistPl.add(s);
				}
			}

			Collections.sort(artistPl, new PlaylistComperator());
			retList.add(artistPl);
		}

		return retList;
	}

	private class PlaylistComperator implements Comparator<Song> {

		@Override
		public int compare(Song s1, Song s2) {
			if (s1 == null || s2 == null)
				throw new IllegalArgumentException(
						"Cannot compare with null values");

			if (s1.getRating() < s2.getRating()) {
				return -1;
			} else if (s1.getRating() > s2.getRating()) {
				return 1;
			}

			if (s1.getPlaycount() < s2.getPlaycount()) {
				return -1;
			} else if (s1.getPlaycount() > s2.getPlaycount()) {
				return 1;
			}

			return 0;
		}

	}

}
