package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Song;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.SongDao;

/**
 * @author klaus
 * 
 */

class DbSongDao implements SongDao {
	private Connection con;

	private PreparedStatement createStmt;
	private PreparedStatement createIsOnStmt;
	private PreparedStatement readStmt;
	private PreparedStatement readAllStmt;
	private PreparedStatement updateStmt;
	private PreparedStatement deleteStmt;

	DbSongDao(DataSource source) {

		try {

			con = source.getConnection();
			createStmt = con.prepareStatement("INSERT INTO song ( "
					+ "title, artist, path, year, duration, "
					+ "playcount, rating, genre, pathOk) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);",
					Statement.RETURN_GENERATED_KEYS);
			createIsOnStmt = con.prepareStatement("INSERT INTO is_on ( "
					+ "song, album) " + "VALUES (?, ?);");
			readStmt = con
					.prepareStatement("SELECT "
							+ "title, artist, path, year, "
							+ "duration, playcount, rating, genre, pathOk, "
							+ "album FROM song left join is_on on id = song WHERE id=?;");
			readAllStmt = con.prepareStatement("SELECT id, "
					+ "title, artist, path, year, "
					+ "duration, playcount, rating, genre, pathOk, "
					+ "album FROM song left join is_on on id = song;");
			updateStmt = con.prepareStatement("UPDATE song SET "
					+ "title=?, artist=?, path=?, year=?, duration=?, "
					+ "playcount=?, rating=?, genre=?, pathOk=? "
					+ "WHERE id = ?;");
			deleteStmt = con.prepareStatement("DELETE FROM song "
					+ "WHERE id = ?;");

		} catch (SQLException e) {
			// TODO Exception throwing instead of outputting
			e.printStackTrace();
		}
	}

	public int create(Song s) {
		ResultSet result = null;
		int id = -1;

		if (s == null)
			throw new IllegalArgumentException("Song must not be null");

		try {
			createStmt.setString(1, s.getTitle());
			createStmt.setString(2, s.getArtist());
			createStmt.setString(3, s.getPath());
			createStmt.setInt(4, s.getYear());
			createStmt.setInt(5, s.getDuration());
			createStmt.setInt(6, s.getPlaycount());
			createStmt.setInt(7, s.getRating());
			createStmt.setString(8, s.getGenre());
			createStmt.setBoolean(9, s.isPathOk());

			createStmt.executeUpdate();

			result = createStmt.getGeneratedKeys();
			if (!result.next()) {
				return -1;
			}
			id = result.getInt(1);

			if (s.getAlbum() != null) {
				// TODO What should we do if album doesn't already exist?
				createIsOnStmt.setInt(1, id);
				createIsOnStmt.setInt(2, s.getAlbum().getId());

				createIsOnStmt.executeUpdate();
			}

		} catch (SQLException e) {
			// TODO Exception throwing instead of outputting
			e.printStackTrace();
		} finally {
			try {
				if (result != null)
					result.close();
			} catch (SQLException e) {
			}
		}

		return id;
	}

	public void update(Song s) {

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
			updateStmt.setInt(7, s.getRating());
			updateStmt.setString(8, s.getGenre());
			updateStmt.setBoolean(9, s.isPathOk());
			updateStmt.setInt(10, s.getId());

			updateStmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void delete(int id) {

		if (id < 0) {
			throw new IllegalArgumentException("ID must be greater or equal 0");
		}

		try {

			deleteStmt.setInt(1, id);
			deleteStmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Exception throwing instead of outputting
			e.printStackTrace();
		}

	}

	public Song read(int id) {
		ResultSet result = null;

		if (id < 0) {
			throw new IllegalArgumentException("ID must be greater or equal 0");
		}

		Song s;

		try {
			readStmt.setInt(1, id);
			result = readStmt.executeQuery();
			if (!result.next()) {
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
			// TODO Add album of song as soon as AlbumDao is implemented
			// DaoFactory df = DaoFactory.getInstance();
			// AlbumDao ad = df.getAlbumDao();
			// s.setAlbum(ad.read(<with readIsOnStmt>));

			result.close();

		} catch (SQLException e) {
			// TODO Exception throwing instead of outputting
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (result != null)
					result.close();
			} catch (SQLException e) {
			}
		}

		return s;
	}

	public List<Song> readAll() {
		ResultSet result = null;
		ArrayList<Song> sList = new ArrayList<Song>();
		Song s;
		try {
			result = readAllStmt.executeQuery();

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
				// TODO Add album of song as soon as AlbumDao is implemented
				// DaoFactory df = DaoFactory.getInstance();
				// AlbumDao ad = df.getAlbumDao();
				// s.setAlbum(ad.read(<with readIsOnStmt>));

				sList.add(s);
			}
			result.close();
		} catch (SQLException e) {
			// TODO Exception throwing instead of outputting
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (result != null)
					result.close();
			} catch (SQLException e) {
			}
		}
		return sList;
	}

	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return this.con;
	}
}
