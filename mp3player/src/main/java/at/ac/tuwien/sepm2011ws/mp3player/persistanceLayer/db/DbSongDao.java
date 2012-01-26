package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Lyric;
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.AlbumDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.SongDao;

class DbSongDao implements SongDao {
	private Connection con;
	private AlbumDao ad;

	private PreparedStatement createStmt;
	private PreparedStatement createIsOnStmt;
	private PreparedStatement readStmt;
	private PreparedStatement readIsOnStmt;
	private PreparedStatement readAllStmt;
	private PreparedStatement updateStmt;
	private PreparedStatement deleteStmt;
	private PreparedStatement readRatedStmt;
	private PreparedStatement readPlayedStmt;
	private PreparedStatement sameStmt;

	DbSongDao(DataSource source, AlbumDao ad) throws DataAccessException {
		this.ad = ad;
		try {

			con = source.getConnection();
			createStmt = con.prepareStatement("INSERT INTO song ( "
					+ "title, artist, path, year, duration, "
					+ "playcount, rating, genre, pathOk, lyric) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);",
					Statement.RETURN_GENERATED_KEYS);
			createIsOnStmt = con.prepareStatement("INSERT INTO is_on ( "
					+ "song, album) " + "VALUES (?, ?);");
			readStmt = con.prepareStatement("SELECT "
					+ "title, artist, path, year, "
					+ "duration, playcount, rating, genre, pathOk, lyric, "
					+ "album FROM song LEFT JOIN is_on ON id=song WHERE id=?;");
			readIsOnStmt = con
					.prepareStatement("SELECT album FROM is_on WHERE song=?;");
			readAllStmt = con
					.prepareStatement("SELECT id, "
							+ "title, artist, path, year, "
							+ "duration, playcount, rating, genre, pathOk, lyric, "
							+ "album FROM song LEFT JOIN is_on ON id = song ORDER BY id;");
			updateStmt = con.prepareStatement("UPDATE song SET "
					+ "title=?, artist=?, path=?, year=?, duration=?, "
					+ "playcount=?, rating=?, genre=?, pathOk=?, lyric=? "
					+ "WHERE id = ?;");
			deleteStmt = con.prepareStatement("DELETE FROM song "
					+ "WHERE id = ?;");
			readRatedStmt = con
					.prepareStatement("SELECT id, "
							+ "title, artist, path, year, "
							+ "duration, playcount, rating, genre, pathOk, lyric, "
							+ "album FROM song LEFT JOIN is_on ON id = song ORDER BY rating DESC LIMIT ?;");
			readPlayedStmt = con
					.prepareStatement("SELECT id, "
							+ "title, artist, path, year, "
							+ "duration, playcount, rating, genre, pathOk, lyric, "
							+ "album FROM song LEFT JOIN is_on ON id = song ORDER BY playcount DESC LIMIT ?;");
			sameStmt = con
					.prepareStatement("SELECT id FROM song WHERE path=?;");

		} catch (SQLException e) {
			throw new DataAccessException(
					"Error initializing database commands");
		}
	}

	public void create(Song s) throws DataAccessException {
		ResultSet result = null;

		if (s == null)
			throw new IllegalArgumentException("Song must not be null");

		try {
			sameStmt.setString(1, s.getPath());
			result = sameStmt.executeQuery();

			if (result.next()) {
				// Song already exists in db, so read it
				Song s2 = read(result.getInt("id"));
				s.setId(s2.getId());
				s.setTitle(s2.getTitle());
				s.setArtist(s2.getArtist());
				s.setPath(s2.getPath());
				s.setYear(s2.getYear());
				s.setDuration(s2.getDuration());
				s.setPlaycount(s2.getPlaycount());
				s.setRating(s2.getRating());
				s.setGenre(s2.getGenre());
				s.setPathOk(s2.isPathOk());
				s.setLyric(s2.getLyric());
				s.setAlbum(s2.getAlbum());
			} else {
				// Song doesn't exist in db, so create
				createStmt.setString(1, s.getTitle());
				createStmt.setString(2, s.getArtist());
				createStmt.setString(3, s.getPath());
				createStmt.setInt(4, s.getYear());
				createStmt.setInt(5, s.getDuration());
				createStmt.setInt(6, s.getPlaycount());
				createStmt.setDouble(7, s.getRating());
				createStmt.setString(8, s.getGenre());
				createStmt.setBoolean(9, s.isPathOk());

				if (s.getLyric() != null)
					createStmt.setString(10, s.getLyric().getText());
				else
					createStmt.setString(10, null);

				createStmt.executeUpdate();

				result = createStmt.getGeneratedKeys();
				if (result.next()) {
					s.setId(result.getInt(1));

					if (s.getAlbum() != null) {
						// Create album if it doesn't exist
						ad.create(s.getAlbum());

						// Create album song association
						createIsOnStmt.setInt(1, s.getId());
						createIsOnStmt.setInt(2, s.getAlbum().getId());

						createIsOnStmt.executeUpdate();
					}

				} else {
					throw new DataAccessException(
							"Error creating song in database");
				}
			}
		} catch (SQLException e) {
			throw new DataAccessException("Error creating song in database");
		} finally {
			try {
				if (result != null)
					result.close();
			} catch (SQLException e) {
			}
		}
	}

	public void update(Song s) throws DataAccessException {

		if (s == null) {
			throw new IllegalArgumentException("Song must not be null");
		}

		try {
			updateStmt.setString(1, s.getTitle());
			updateStmt.setString(2, s.getArtist());
			updateStmt.setString(3, s.getPath());
			updateStmt.setInt(4, s.getYear());
			updateStmt.setInt(5, s.getDuration());
			updateStmt.setInt(6, s.getPlaycount());
			updateStmt.setDouble(7, s.getRating());
			updateStmt.setString(8, s.getGenre());
			updateStmt.setBoolean(9, s.isPathOk());

			if (s.getLyric() != null)
				updateStmt.setString(10, s.getLyric().getText());
			else
				updateStmt.setString(10, null);

			updateStmt.setInt(11, s.getId());

			if (s.getAlbum() != null) {
				ad.update(s.getAlbum());
			}

			updateStmt.executeUpdate();

		} catch (SQLException e) {
			throw new DataAccessException("Error updating song in database");
		}

	}

	public void delete(int id) throws DataAccessException {

		if (id < 0) {
			throw new IllegalArgumentException("ID must be greater or equal 0");
		}

		try {

			deleteStmt.setInt(1, id);
			deleteStmt.executeUpdate();

			// TODO: Delete album too if there are no more songs of it;
			// I guess this should actually do ON DELETE CASCASE ????? <- NO!
		} catch (SQLException e) {
			throw new DataAccessException("Error deleting song in database");
		}

	}

	public Song read(int id) throws DataAccessException {
		ResultSet result = null;

		if (id < 0) {
			throw new IllegalArgumentException("ID must be greater or equal 0");
		}

		Song s;
		String lyric;

		try {
			readStmt.setInt(1, id);
			result = readStmt.executeQuery();
			if (!result.next()) {
				result.close();
				return null;
			}

			s = new Song(result.getString("artist"), result.getString("title"),
					result.getInt("duration"), result.getString("path"));
			s.setId(id);
			s.setYear(result.getInt("year"));
			s.setPlaycount(result.getInt("playcount"));
			s.setRating(result.getInt("rating"));
			s.setGenre(result.getString("genre"));
			s.setPathOk(result.getBoolean("pathOk"));

			// Read lyric
			lyric = result.getString("lyric");
			if (lyric != null) {
				s.setLyric(new Lyric(lyric));
			} else {
				s.setLyric(null);
			}

			// Read album
			readIsOnStmt.setInt(1, id);
			result = readIsOnStmt.executeQuery();

			if (!result.next()) {
				s.setAlbum(null);
			} else {
				int albumId = result.getInt("album");
				s.setAlbum(ad.read(albumId));
			}

		} catch (SQLException e) {
			throw new DataAccessException("Error reading song from database");
		} finally {
			try {
				if (result != null)
					result.close();
			} catch (SQLException e) {
			}
		}

		return s;
	}

	public List<Song> readAll() throws DataAccessException {
		return executeSelect(readAllStmt);
	}

	private List<Song> executeSelect(PreparedStatement statement)
			throws DataAccessException {
		ResultSet result = null;
		ResultSet result2 = null;
		ArrayList<Song> sList = new ArrayList<Song>();
		Song s;
		String lyric;
		try {
			result = statement.executeQuery();

			while (result.next()) {
				s = new Song(result.getString("artist"),
						result.getString("title"), result.getInt("duration"),
						result.getString("path"));

				s.setId(result.getInt("id"));
				s.setYear(result.getInt("year"));
				s.setPlaycount(result.getInt("playcount"));
				s.setRating(result.getInt("rating"));
				s.setGenre(result.getString("genre"));
				s.setPathOk(result.getBoolean("pathOk"));

				// Read lyric
				lyric = result.getString("lyric");
				if (lyric != null) {
					s.setLyric(new Lyric(lyric));
				} else {
					s.setLyric(null);
				}

				// Read album
				readIsOnStmt.setInt(1, s.getId());
				result2 = readIsOnStmt.executeQuery();

				if (!result2.next()) {
					s.setAlbum(null);
				} else {
					int albumId = result2.getInt("album");
					s.setAlbum(ad.read(albumId));
				}

				sList.add(s);
			}

		} catch (SQLException e) {
			throw new DataAccessException(
					"Error reading all songs from database");
		} finally {
			try {
				if (result != null)
					result.close();
				if (result2 != null)
					result2.close();
			} catch (SQLException e) {
			}
		}
		return sList;
	}

	@Override
	public List<Song> getTopRatedSongs(int number) throws DataAccessException {
		if (number < 1)
			throw new IllegalArgumentException(
					"The number XX stands for in TopXX... playlist must be greater than zero");

		try {
			readRatedStmt.setInt(1, number);
			return executeSelect(readRatedStmt);
		} catch (SQLException e) {
			throw new DataAccessException("Error reading song from database");
		}
	}

	@Override
	public List<Song> getTopPlayedSongs(int number) throws DataAccessException {
		if (number < 1)
			throw new IllegalArgumentException(
					"The number XX stands for in TopXX... playlist must be greater than zero");

		try {
			readPlayedStmt.setInt(1, number);
			return executeSelect(readPlayedStmt);
		} catch (SQLException e) {
			throw new DataAccessException("Error reading song from database");
		}
	}

	public Connection getConnection() {
		return this.con;
	}

}
