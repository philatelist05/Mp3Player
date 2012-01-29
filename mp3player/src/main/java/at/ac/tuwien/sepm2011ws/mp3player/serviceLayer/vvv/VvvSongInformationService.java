package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vvv;

import java.io.File;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.rpc.ServiceException;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Album;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Lyric;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.MetaTags;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.SongDao;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.LastFmService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.SongInformationService;

import com.chartlyrics.ChartLyricsLocator;
import com.chartlyrics.ChartLyricsSoap;
import com.chartlyrics.GetLyricResult;

import de.umass.lastfm.Track;
import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.Tag;
import entagged.audioformats.exceptions.CannotReadException;
import entagged.audioformats.exceptions.CannotWriteException;

public class VvvSongInformationService implements SongInformationService {

	private final SongDao sd;

	VvvSongInformationService(SongDao sd) {
		this.sd = sd;
	}

	@Override
	public void getMetaTags(Song song) throws DataAccessException {
		if (song == null || song.getPath() == null)
			throw new IllegalArgumentException(
					"The song object and its path field must not be null");

		try {
			//TODO: error message: "ApicId3Frame-> No space for picture data left."
			// Keep look at: http://entagged.sourcearchive.com/documentation/0.35/ApicId3Frame_8java-source.html
			File path = new File(song.getPath());
			AudioFile file = AudioFileIO.read(path);
			Tag tags = file.getTag();

			String temp;
			temp = getStringFromTagList(tags.getArtist());
			song.setArtist((temp != null) ? temp : "Unknown Artist");
			temp = getStringFromTagList(tags.getTitle());
			song.setTitle((temp != null) ? temp : "Unknown Title");
			song.setGenre(getStringFromTagList(tags.getGenre()));
			song.setDuration(file.getLength());

			temp = getStringFromTagList(tags.getAlbum());
			temp = (temp != null) ? temp : "Unknown Album";
			if (song.getAlbum() != null) {
				song.getAlbum().setTitle(temp);
			} else {
				song.setAlbum(new Album(temp));
			}

			List<?> years = tags.getYear();
			if (!years.isEmpty()) {
				try {
					song.setYear(Integer.parseInt(years.get(0).toString()));
				} catch (NumberFormatException e) {
					// If the year is not a valid integer, just do nothing
				}
			} else {
				song.setYear(0);
			}

			sd.update(song);

		} catch (CannotReadException e) {
			// throw new DataAccessException("Couldn't read ID3 tags of file \""
			// + song.getPath() + "\"");
		}
	}

	private String getStringFromTagList(List<?> tagList) {
		if (tagList == null || tagList.isEmpty()) {
			return null;
		}
		Iterator<?> iterator = tagList.iterator();
		String out = iterator.next().toString();
		while (iterator.hasNext()) {
			out += "," + iterator.next().toString();
		}

		return out;
	}

	@Override
	public void setMetaTags(Song song) throws DataAccessException {
		if (song == null || song.getPath() == null)
			throw new IllegalArgumentException(
					"The song object and its path field must not be null");

		try {
			AudioFile file = AudioFileIO.read(new File(song.getPath()));
			Tag tags = file.getTag();

			if (song.getArtist() != null)
				tags.setArtist(song.getArtist());
			if (song.getTitle() != null)
				tags.setTitle(song.getTitle());
			if (song.getAlbum() != null)
				if (song.getAlbum().getTitle() != null)
					tags.setAlbum(song.getAlbum().getTitle());
			if (song.getGenre() != null)
				tags.setGenre(song.getGenre());
			tags.setYear(String.valueOf(song.getYear()));

			AudioFileIO.write(file);

		} catch (CannotReadException e) {
			throw new DataAccessException("Couldn't write ID3 tags to file \""
					+ song.getPath() + "\"");
		} catch (CannotWriteException e) {
			throw new DataAccessException("Couldn't write ID3 tags to file \""
					+ song.getPath() + "\"");
		} finally {
			sd.update(song);
		}
	}

	@Override
	public List<MetaTags> downloadMetaTags(Song song)
			throws DataAccessException {
		if (song == null)
			throw new IllegalArgumentException("Song must not be null");
		if (song.getTitle() == null || song.getTitle().isEmpty())
			throw new IllegalArgumentException(
					"Song title must not be null or empty");
		if (song.getArtist() == null || song.getArtist().isEmpty())
			throw new IllegalArgumentException(
					"Song artist must not be null or empty");
		
		Set<MetaTags> metaTags = new HashSet<MetaTags>();
		Track correctedTrack = Track.getCorrection(song.getArtist(), song.getTitle(), LastFmService.LastFmApiKey);
		if(correctedTrack != null) {
			Collection<Track> tracks = Track.search(correctedTrack.getArtist(), correctedTrack.getName(), 10, LastFmService.LastFmApiKey);
			metaTags.addAll(mapTracksToMetaTag(tracks, song));
		}
		Collection<Track> tracks = Track.search(song.getArtist(), song.getTitle(), 10, LastFmService.LastFmApiKey);
		metaTags.addAll(mapTracksToMetaTag(tracks, song));
		
		ArrayList<MetaTags> list = new ArrayList<MetaTags>();
		list.addAll(metaTags);
		return list;
	}
	
	private Set<MetaTags> mapTracksToMetaTag(Collection<Track> tracks, Song song){
		Set<MetaTags> list = new HashSet<MetaTags>();
		for (Track track : tracks) {
			Collection<de.umass.lastfm.Tag> genres = track.getTopTags(track.getArtist(), track.getMbid(), LastFmService.LastFmApiKey);
			Iterator<de.umass.lastfm.Tag> iterator = genres.iterator();
			
			String genre = "";
			while(iterator.hasNext()) {
				de.umass.lastfm.Tag genreTag = iterator.next();
				genre += genreTag.getName();	
				genre = iterator.hasNext() ? genre + "," : genre;
			}
			genre = genre.isEmpty() ? song.getGenre() : genre;
			
			//This is necessary due to fetching Albums
			Track updatedTrack = Track.getInfo(track.getArtist(), track.getName(), LastFmService.LastFmApiKey);
			
			Album album = new Album("Unknown");
			String title = updatedTrack.getAlbum();
			if (title != null && !title.isEmpty())
				album.setTitle(title);
				
			list.add(new MetaTags(track.getArtist(), track.getName(), track.getDuration(), song.getYear(), genre, album));
		}
		return list;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<Lyric> downloadLyrics(Song song) throws DataAccessException {
		if (song == null)
			throw new IllegalArgumentException("Song must not be null");

		String artist = song.getArtist();
		String title = song.getTitle();

		String lyrics = null;
		final int hangupWaitTime = 5000; // in milliseconds
		final int retryDelay = 20000; // in milliseconds
		final int retryCount = 10;

		if (artist == null || title == null || artist.length() < 2
				|| title.length() < 2) {
			throw new IllegalArgumentException(
					"The artist and song name must be at least 2 characters long.");
		}

		try {
			boolean failed;
			int failCount = 0;
			LyricsThread lf;

			do {
				failed = false;
				lf = new LyricsThread(artist, title);
				lf.start();
				lf.join(hangupWaitTime);
				if (lf.isAlive()) {
					lf.stop();
				}

				lyrics = lf.getLyrics();

				failed = lyrics == null;

				if (failed) {
					failCount++;
					Thread.sleep(retryDelay);
				}
			} while (!lf.gotNoConnection() && failed && failCount < retryCount);

			if (lf.gotNoConnection()) {
				throw new DataAccessException(
						"Couldn't connect to the lyrics service. Do you have an active internet connection?");
			}
		} catch (InterruptedException e) {
			throw new DataAccessException("Couldn't load lyrics");
		}

		// There are only one or no lyrics for a song of course but the
		// interface is written to
		// be able to return multiple lyrics for a song, so we have to generate
		// a list
		List<Lyric> retList = new ArrayList<Lyric>();
		if (lyrics != null && !lyrics.isEmpty()) {
			retList.add(new Lyric(lyrics));
		}

		return retList;
	}

	@Override
	public void setRating(Song song, double rating) throws DataAccessException {
		if (song == null)
			throw new IllegalArgumentException("Song must not be null");

		song.setRating(rating);
		sd.update(song);
	}

	@Override
	public void saveLyrics(Song song) throws DataAccessException {
		if (song == null)
			throw new IllegalArgumentException("Song must not be null");

		sd.update(song);
	}

	private static class LyricsThread extends Thread {
		private String lyrics;
		private String artist;
		private String title;
		private boolean noConnection;

		public LyricsThread(String artist, String title) {
			super();
			this.artist = artist;
			this.title = title;
		}

		public void run() {
			lyrics = null;
			noConnection = false;

			try {
				ChartLyricsLocator service = new ChartLyricsLocator();
				ChartLyricsSoap soap = service.getapiv1Soap12();
				GetLyricResult lyricsResult = soap.searchLyricDirect(
						this.artist, this.title);
				lyrics = lyricsResult.getLyric();
			} catch (RemoteException e) {
				Throwable cause = e.getCause();
				if (cause.getClass() == SocketException.class
						&& cause.getMessage().equals("Connection reset")) {
					lyrics = null;
				} else if (cause.getClass() == UnknownHostException.class) {
					noConnection = true;
				} else {
					lyrics = null;
				}
			} catch (ServiceException e) {
				lyrics = null;
			}
		}

		/**
		 * @return the lyrics
		 */
		public String getLyrics() {
			return this.lyrics;
		}

		/**
		 * @return if the thread got a connection to the lyrics server
		 */
		public boolean gotNoConnection() {
			return this.noConnection;
		}
	}

}
