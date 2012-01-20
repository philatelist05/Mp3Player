/**
 * Veni Vidi Vici PlaylistService
 */
package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vvv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.PlaylistDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.SongDao;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.SettingsService;
import christophedelory.playlist.AbstractPlaylistComponent;
import christophedelory.playlist.Media;
import christophedelory.playlist.Sequence;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.playlist.SpecificPlaylistFactory;

/**
 * @author klaus
 * 
 */
class VvvPlaylistService implements PlaylistService {

	private static final Logger logger = Logger
			.getLogger(VvvPlaylistService.class);
	private final PlaylistDao pd;
	private final SongDao sd;
	private final SettingsService ss;

	VvvPlaylistService(SongDao sd, PlaylistDao pd, SettingsService ss) {
		this.pd = pd;
		this.sd = sd;
		this.ss = ss;
	}

	@Override
	public Playlist getLibrary() throws DataAccessException {
		Playlist lib = new Playlist("Library");
		lib.setReadonly(true);

		lib.setSongs(this.sd.readAll());

		return lib;
	}

	@Override
	public void importPlaylist(File[] files) throws DataAccessException {
		for (File file : files) {
			if (checkFileExtensionAccepted(file.getName(), PlaylistFileTypes)) {
				importPlaylist(file);
			}
		}
	}

	private boolean checkFileExtensionAccepted(String fileName,
			String[] acceptedExtensions) {
		for (int i = 0; i < acceptedExtensions.length; i++) {
			if (acceptedExtensions[i].toLowerCase().equals(
					getExtension(fileName).toLowerCase())) {
				return true;
			}
		}

		return false;
	}

	private String getExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf(".");
		return fileName.substring(dotIndex + 1, fileName.length());
	}

	private String getBasename(String fileName) {
		int dotIndex = fileName.lastIndexOf(".");
		return fileName.substring(0, dotIndex);
	}

	private Playlist importPlaylist(File file) throws DataAccessException {
		Playlist playlist;

		// Initialize playlist
		playlist = createPlaylist(getBasename(file.getName()));

		try {
			// Get song files from playlist file
			SpecificPlaylistFactory spf = SpecificPlaylistFactory.getInstance();
			SpecificPlaylist specificPlaylist = spf.readFrom(file);
			Sequence plSeq = specificPlaylist.toPlaylist().getRootSequence();

			Media m;
			for (AbstractPlaylistComponent apc : plSeq.getComponents()) {
				m = (Media) apc;
				addSongsToPlaylist(
						new File[] { new File(m.getSource().getURI()) },
						playlist);
			}

		} catch (IOException e) {
			throw new DataAccessException("Error reading playlist "
					+ file.getPath());
		} catch (URISyntaxException e) {
			throw new DataAccessException("Error reading playlist "
					+ file.getPath());
		}

		return playlist;
	}

	@Override
	public void exportPlaylist(File file, Playlist playlist) {
		FileWriter writer = null;
		BufferedWriter bwriter = null;
		try {
			writer = new FileWriter(file.getAbsolutePath() + ".m3u");
			bwriter = new BufferedWriter(writer);
			for (Song song : playlist.getSongs()) {
				bwriter.write(song.getPath());
				bwriter.newLine();
			}
		} catch (IOException e) {
		} finally {
			try {
				if (bwriter != null) {
					bwriter.flush();
					bwriter.close();
				}
				if (writer != null)
					writer.close();
			} catch (IOException e) {

			}
		}
	}

	@Override
	public List<Playlist> getAllPlaylists() throws DataAccessException {
		return this.pd.readAll();
	}

	@Override
	public void addFolder(File folder) throws DataAccessException {
		addSongs(traverseFolderRecursively(folder).toArray(new File[] {}));
	}

	private List<File> traverseFolderRecursively(File folder) {
		List<File> retFiles = new ArrayList<File>();

		String[] all = folder.list();
		File f;
		for (String s : all) {
			f = new File(folder + File.separator + s);
			if (f.isDirectory()) {
				// Recursive add all files from all folders in the current
				// folder
				retFiles.addAll(traverseFolderRecursively(f));
			} else if (f.isFile()) {
				// Add files of the current folder
				retFiles.add(f);
			}
		}

		return retFiles;
	}

	@Override
	public void addSongs(File[] files) throws DataAccessException {
		createSongs(files);
	}

	@Override
	public void addSongsToPlaylist(File[] files, Playlist playlist)
			throws DataAccessException {
		List<Song> list = createSongs(files);

		for (Song s : list) {
			playlist.addSong(s);
		}

		updatePlaylist(playlist);
	}

	private List<Song> createSongs(File[] files) throws DataAccessException {
		List<Song> songs = new ArrayList<Song>();
		Song s;

		String[] userFileTypes = ss.getUserFileTypes();

		for (File file : files) {
			if (checkFileExtensionAccepted(file.getName(), userFileTypes)) {
				// TODO: Read metadata from song with SongInformationService
				s = new Song("Dummy Artist", file.getName(), 0,
						file.getAbsolutePath());

				sd.create(s);
				songs.add(s);
			}
		}

		return songs;
	}

	@Override
	public void deleteSongs(List<Song> songs, Playlist playlist)
			throws DataAccessException {
		if (songs == null || playlist == null)
			throw new IllegalArgumentException(
					"Song list and playlist must not be null");

		if (songs.size() > 0) {

			List<Song> plSongs = playlist.getSongs();

			for (Iterator<Song> iterator = plSongs.iterator(); iterator
					.hasNext();) {
				Song s = (Song) iterator.next();
				if (songs.contains(s))
					iterator.remove();
			}

			updatePlaylist(playlist);
		}
	}

	@Override
	public Playlist createPlaylist(String name) throws DataAccessException {
		Playlist playlist = new Playlist(name);
		this.pd.create(playlist);
		return playlist;
	}

	@Override
	public void deletePlaylist(Playlist playlist) throws DataAccessException {
		if (!playlist.isReadonly())
			this.pd.delete(playlist.getId());
	}

	@Override
	public void updatePlaylist(Playlist playlist) throws DataAccessException {
		if (!playlist.isReadonly())
			this.pd.update(playlist);
	}

	@Override
	public void renamePlaylist(Playlist playlist, String name)
			throws DataAccessException {
		if (!playlist.isReadonly())
			pd.rename(playlist, name);
	}

	@Override
	public Playlist getTopRated() throws DataAccessException {
		Playlist playlist = new Playlist("TopRated");
		playlist.setReadonly(true);
		playlist.setSongs(sd.getTopRatedSongs(ss.getTopXXRatedCount()));

		return playlist;
	}

	@Override
	public Playlist getTopPlayed() throws DataAccessException {
		Playlist playlist = new Playlist("TopPlayed");
		playlist.setReadonly(true);
		playlist.setSongs(sd.getTopRatedSongs(ss.getTopXXPlayedCount()));

		return playlist;
	}

	@Override
	public Playlist globalSearch(String pattern) throws DataAccessException {
		List<Song> songs = getLibrary().getSongs();

		for (Iterator<Song> iterator = songs.iterator(); iterator.hasNext();) {
			Song s = (Song) iterator.next();
			// If song doesn't match pattern, remove it from search results
			if (!(s.getArtist().contains(pattern)
					|| s.getTitle().contains(pattern)
					|| (s.getGenre() != null && s.getGenre().contains(pattern))
					|| (s.getLyric() != null && s.getLyric().getText() != null && s
							.getLyric().getText().contains(pattern))
					|| String.valueOf(s.getYear()).contains(pattern)
					|| (s.getAlbum() != null && s.getAlbum().getTitle() != null && s
							.getAlbum().getTitle().contains(pattern)) || s
					.getPath().contains(pattern))) {

				iterator.remove();
			}
		}

		Playlist pl = new Playlist("Search");
		pl.setReadonly(true);
		pl.setSongs(songs);

		return pl;
	}

	@Override
	public void checkSongPaths() throws DataAccessException {
		List<Song> songs = this.sd.readAll();
		for (Song song : songs) {
			song.setPathOk(new File(song.getPath()).isFile());
		}
	}

	@Override
	public void reloadPlaylist(Playlist p) throws DataAccessException {
		Playlist np = pd.read(p.getId());

		p.setReadonly(np.isReadonly());
		p.setTitle(np.getTitle());
		p.setSongs(np.getSongs());
	}
}
