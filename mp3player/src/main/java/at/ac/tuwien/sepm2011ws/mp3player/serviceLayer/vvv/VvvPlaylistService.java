package at.ac.tuwien.sepm2011ws.mp3player.serviceLayer.vvv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

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
		if (files == null)
			throw new IllegalArgumentException("File array must not be null");

		for (File file : files) {
			if(file == null) throw new IllegalArgumentException("File must not be null");
			
			if(FilenameUtils.isExtension(file.getName(), PlaylistFileTypes))
				importPlaylist(file);
		}
	}

	private WritablePlaylist importPlaylist(File file)
			throws DataAccessException {
		WritablePlaylist playlist;

		if (file == null || !file.isFile()) {
			throw new IllegalArgumentException(
					"The playlist to import is not a valid file");
		}

		// Initialize playlist
		playlist = createPlaylist(FilenameUtils.getBaseName(file.getName()));

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
				f = new File(m.getSource().getURL().getPath());
				f = new File(folder + File.separator + f.getName());
				addSongsToPlaylist(new File[] { f }, playlist);
			}

		} catch (IOException e) {
			throw new DataAccessException("Error reading playlist "
					+ file.getPath());
		}

		return playlist;
	}

	@Override
	public void exportPlaylist(File file, Playlist playlist) {
		FileWriter writer = null;
		BufferedWriter bwriter = null;

		if (file == null)
			throw new IllegalArgumentException("The file must not be null");
		if (playlist == null)
			throw new IllegalArgumentException("The playlist must not be null");

		try {
			String path = file.getAbsolutePath();
			if (!FilenameUtils.isExtension(path, "m3u"))
				path += ".m3u";

			writer = new FileWriter(path);
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
	public List<WritablePlaylist> getAllPlaylists() throws DataAccessException {
		return this.pd.readAll();
	}

	@Override
	public void addFolder(File folder) throws DataAccessException {
		if (folder == null || !folder.isDirectory())
			throw new IllegalArgumentException(
					"The folder is not a valid folder");

		addSongs(traverseFolderRecursively(folder).toArray(new File[] {}));
	}

	private List<File> traverseFolderRecursively(File folder) {
		if (folder == null || !folder.isDirectory())
			throw new IllegalArgumentException(
					"The folder is not a valid folder");

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
		if (files == null)
			throw new IllegalArgumentException("Files must not be null");

		createSongs(files);
	}

	@Override
	public void addSongsToPlaylist(File[] files, WritablePlaylist playlist)
			throws DataAccessException {
		if (files == null)
			throw new IllegalArgumentException("Files must not be null");
		if (playlist == null)
			throw new IllegalArgumentException("Playlist must not be null");

		List<Song> list = createSongs(files);

		for (Song s : list) {
			playlist.add(s);
		}

		updatePlaylist(playlist);
	}

	private List<Song> createSongs(File[] files) throws DataAccessException {
		if (files == null)
			throw new IllegalArgumentException("Files must not be null");

		List<Song> songs = new ArrayList<Song>();
		Song s;

		String[] userFileTypes = ss.getUserFileTypes();

		for (File file : files) {

			// for (File acceptedFile : file.listFiles(((FilenameFilter)new
			// SuffixFileFilter(userFileTypes)))) {
			if (FilenameUtils.isExtension(file.getName(), userFileTypes)) {
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
		if (songs == null)
			throw new IllegalArgumentException("The song list must not be null");
		if (playlist == null)
			throw new IllegalArgumentException("The playlist must not be null");

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
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException(
					"The name of the playlist must not be null or empty");

		WritablePlaylist playlist = new WritablePlaylist(name);
		this.pd.create(playlist);
		return playlist;
	}

	@Override
	public void deletePlaylist(WritablePlaylist playlist)
			throws DataAccessException {
		if (playlist == null)
			throw new IllegalArgumentException("The playlist must not be null");

		this.pd.delete(playlist.getId());
	}

	@Override
	public void updatePlaylist(WritablePlaylist playlist)
			throws DataAccessException {
		if (playlist == null)
			throw new IllegalArgumentException("The playlist must not be null");

		this.pd.update(playlist);
	}

	@Override
	public void renamePlaylist(WritablePlaylist playlist, String name)
			throws DataAccessException {
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException(
					"The new name of the playlist must not be null or empty");
		if (playlist == null)
			throw new IllegalArgumentException("The playlist must not be null");

		playlist.setTitle(name);
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
		if (pattern == null || pattern.isEmpty())
			throw new IllegalArgumentException(
					"The search pattern must not be null or empty");

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
		if (p == null)
			throw new IllegalArgumentException("The playlist must not be null");

		WritablePlaylist np = pd.read(p.getId());
		p.setTitle(np.getTitle());
		p.clear();
		p.addAll(np);
	}
}
