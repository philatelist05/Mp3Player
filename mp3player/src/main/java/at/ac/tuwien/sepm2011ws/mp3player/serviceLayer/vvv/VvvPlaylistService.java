package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vvv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Playlist;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.WritablePlaylist;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.PlaylistDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.SongDao;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.PlaylistService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.SettingsService;
import at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.SongInformationService;
import christophedelory.playlist.AbstractPlaylistComponent;
import christophedelory.playlist.Media;
import christophedelory.playlist.Sequence;
import christophedelory.playlist.SpecificPlaylist;
import christophedelory.playlist.SpecificPlaylistFactory;

class VvvPlaylistService implements PlaylistService {

	private final PlaylistDao pd;
	private final SongDao sd;
	private final SettingsService ss;
	private final SongInformationService sis;

	VvvPlaylistService(SongDao sd, PlaylistDao pd, SettingsService ss,
			SongInformationService sis) {
		this.pd = pd;
		this.sd = sd;
		this.ss = ss;
		this.sis = sis;
	}

	@Override
	public Playlist getLibrary() throws DataAccessException {
		Playlist lib = new Playlist("Library");
		lib.addAll(this.sd.readAll());
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

	private WritablePlaylist importPlaylist(File file)
			throws DataAccessException {
		WritablePlaylist playlist;

		if (file == null || !file.isFile()) {
			throw new IllegalArgumentException(
					"The playlist to import is no valid file");
		}

		// Initialize playlist
		playlist = createPlaylist(getBasename(file.getName()));

		try {
			// Get song files from playlist file
			SpecificPlaylistFactory spf = SpecificPlaylistFactory.getInstance();
			SpecificPlaylist specificPlaylist = spf.readFrom(file);
			Sequence plSeq = specificPlaylist.toPlaylist().getRootSequence();

			Media m;
			File f;
			String folder;
			for (AbstractPlaylistComponent apc : plSeq.getComponents()) {
				m = (Media) apc;
				folder = file.getParent();
				f = new File(m.getSource().getURI());
				f = new File(folder + File.separator + f.getName());
				addSongsToPlaylist(new File[] { f }, playlist);
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
			for (Song song : playlist) {
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
	public List<? extends Playlist> getAllPlaylists()
			throws DataAccessException {
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
	public void addSongsToPlaylist(File[] files, WritablePlaylist playlist)
			throws DataAccessException {
		List<Song> list = createSongs(files);

		for (Song s : list) {
			playlist.add(s);
		}

		updatePlaylist(playlist);
	}

	private List<Song> createSongs(File[] files) throws DataAccessException {
		List<Song> songs = new ArrayList<Song>();
		Song s;

		String[] userFileTypes = ss.getUserFileTypes();

		for (File file : files) {
			if (checkFileExtensionAccepted(file.getName(), userFileTypes)) {
				s = new Song("Unknown Artist", "Unknown Title", 0,
						file.getAbsolutePath());
				sd.create(s);
				sis.getMetaTags(s);

				songs.add(s);
			}
		}

		return songs;
	}

	@Override
	public void deleteSongs(List<Song> songs, WritablePlaylist playlist)
			throws DataAccessException {
		if (songs == null || playlist == null)
			throw new IllegalArgumentException(
					"Song list and playlist must not be null");

		if (songs.size() > 0) {
			for (Iterator<Song> iterator = playlist.iterator(); iterator
					.hasNext();) {
				Song s = iterator.next();
				if (songs.contains(s))
					iterator.remove();
			}

			updatePlaylist(playlist);
		}
	}

	@Override
	public WritablePlaylist createPlaylist(String name)
			throws DataAccessException {
		WritablePlaylist playlist = new WritablePlaylist(name);
		this.pd.create(playlist);
		return playlist;
	}

	@Override
	public void deletePlaylist(WritablePlaylist playlist)
			throws DataAccessException {
		this.pd.delete(playlist.getId());
	}

	@Override
	public void updatePlaylist(WritablePlaylist playlist)
			throws DataAccessException {
		this.pd.update(playlist);
	}

	@Override
	public void renamePlaylist(WritablePlaylist playlist, String name)
			throws DataAccessException {
		pd.rename(playlist, name);
	}

	@Override
	public Playlist getTopRated() throws DataAccessException {
		Playlist playlist = new Playlist("TopRated");
		playlist.addAll(sd.getTopRatedSongs(ss.getTopXXRatedCount()));

		return playlist;
	}

	@Override
	public Playlist getTopPlayed() throws DataAccessException {
		Playlist playlist = new Playlist("TopPlayed");
		playlist.addAll(sd.getTopPlayedSongs(ss.getTopXXPlayedCount()));

		return playlist;
	}

	@Override
	public Playlist globalSearch(String pattern) throws DataAccessException {
		Playlist plst = getLibrary();

		for (Iterator<Song> iterator = plst.iterator(); iterator.hasNext();) {
			Song s = iterator.next();
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
		pl.addAll(plst);

		return pl;
	}

	@Override
	public void checkSongPaths() throws DataAccessException {
		List<Song> songs = this.sd.readAll();
		for (Song song : songs) {
			song.setPathOk(new File(song.getPath()).isFile());
			sd.update(song);
		}
	}

	@Override
	public void reloadPlaylist(WritablePlaylist p) throws DataAccessException {
		Playlist np = pd.read(p.getId());
		p.setTitle(np.getTitle());
		p.addAll(np);
	}
}
