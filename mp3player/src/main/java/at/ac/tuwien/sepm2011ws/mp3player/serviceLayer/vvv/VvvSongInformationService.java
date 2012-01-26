package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vvv;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Lyric;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.MetaTags;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.SongDao;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.SongInformationService;

import com.chartlyrics.ChartLyricsLocator;
import com.chartlyrics.ChartLyricsSoap;
import com.chartlyrics.GetLyricResult;

public class VvvSongInformationService implements SongInformationService {

	private final SongDao sd;

	VvvSongInformationService(SongDao sd) {
		this.sd = sd;
	}

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

	@SuppressWarnings("deprecation")
	@Override
	public List<Lyric> downloadLyrics(Song song) throws DataAccessException {
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
				throw new DataAccessException("Couln't load lyrics");
			}
		} catch (InterruptedException e) {
			throw new DataAccessException("Couln't load lyrics");
		}

		List<Lyric> retList = new ArrayList<Lyric>();
		if (lyrics != null && !lyrics.isEmpty()) {
			retList.add(new Lyric(lyrics));
		}
		return retList;
	}

	@Override
	public void setRating(Song song, double rating) throws DataAccessException {
		song.setRating(rating);
		sd.update(song);
	}

	@Override
	public void saveLyrics(Song song) {
		// TODO Auto-generated method stub

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
		 * @return the artist
		 */
		public String getArtist() {
			return this.artist;
		}

		/**
		 * @return the title
		 */
		public String getTitle() {
			return this.title;
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
