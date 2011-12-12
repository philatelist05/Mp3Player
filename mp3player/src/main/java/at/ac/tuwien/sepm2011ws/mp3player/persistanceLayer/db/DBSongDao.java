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

class DBSongDao implements SongDao {

	PreparedStatement createStmt;
	PreparedStatement createIsOnStmt;
	PreparedStatement readStmt;
	PreparedStatement readAllStmt;
	PreparedStatement updateStmt;
	PreparedStatement deleteStmt;

	DBSongDao(DataSource source) {

		try {

			Connection con = source.getConnection();
			createStmt = con.prepareStatement("INSERT INTO song ( "
					+ "title, artist, path, year, duration, "
					+ "playcount, rating, genre) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
					Statement.RETURN_GENERATED_KEYS);
			createIsOnStmt = con.prepareStatement("INSERT INTO is_on ( " +
					"song, album) " +
					"VALUES (?, ?);");
			readStmt = con.prepareStatement("SELECT "
					+ "title, artist, path, year, "
					+ "duration, playcount, rating, genre, "
					+ "album FROM song join is_on on id = song WHERE id=?;");

			readAllStmt = con.prepareStatement("SELECT id, "
					+ "title, artist, path, year, "
					+ "duration, playcount, rating, genre, "
					+ "album FROM song join is_on on id = song;");
			updateStmt = con.prepareStatement("UPDATE song SET "
					+ "title=?, artist=?, path=?, year=?, duration=?, " +
					"playcount=?, rating=?, genre=? "
					+ "WHERE id = ?;");
			deleteStmt = con.prepareStatement("DELETE FROM song "
					+ "WHERE id = ?;");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int create(Song s) throws IllegalArgumentException {
		int id = -1;

		if (s == null)
			throw new IllegalArgumentException();

		try {
			createStmt.setString(1, s.getTitle());
			createStmt.setString(2, s.getArtist());
			createStmt.setString(3, s.getPath());
			createStmt.setInt(4, s.getYear());
			createStmt.setInt(5, s.getDuration());
			createStmt.setInt(6, s.getPlaycount());
			createStmt.setInt(7, s.getRating());
			createStmt.setString(8, s.getGenre());
			
			createStmt.executeUpdate();

			ResultSet result = createStmt.getGeneratedKeys();
			if (!result.next()) {
				return -1;
			}
			id = result.getInt(1);
			result.close();
			
			if(s.getAlbum() != null) {
				createIsOnStmt.setInt(1, id);
				createIsOnStmt.setInt(2, s.getAlbum().getId());
				
				createIsOnStmt.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return id;
	}

	public void update(Song newS) throws IllegalArgumentException {
		// TODO Add update method

	}

	public void delete(int song) throws IllegalArgumentException {
		// TODO Add delete method

	}

	public Song read(int id) throws IllegalArgumentException {

		if (id < 0) {
			throw new IllegalArgumentException();
		}

		Song s = new Song();

		try {
			readStmt.setInt(1, id);
			ResultSet result = readStmt.executeQuery();
			if (!result.next()) {
				return null;
			}

			s.setId(id);
			s.setTitle(result.getString("title"));
			s.setArtist(result.getString("artist"));
			s.setPath(result.getString("path"));
			s.setYear(result.getInt("year"));
			s.setDuration(result.getInt("duration"));
			s.setPlaycount(result.getInt("playcount"));
			s.setRating(result.getInt("rating"));
			s.setGenre(result.getString("genre"));
			// TODO Add album of song as soon as AlbumDao is implemented
			// DaoFactory df = DaoFactory.getInstance();
			// AlbumDao ad = df.getAlbumDao();
			// s.setAlbum(ad.read(<with readIsOnStmt>));

			result.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		return s;
	}

	public List<Song> readAll() {
		ArrayList<Song> sList = new ArrayList<Song>();
		Song s;
		try {
			ResultSet result = readAllStmt.executeQuery();

			while (result.next()) {
				s = new Song();

				s.setId(result.getInt("id"));
				s.setTitle(result.getString("title"));
				s.setArtist(result.getString("artist"));
				s.setPath(result.getString("path"));
				s.setYear(result.getInt("year"));
				s.setDuration(result.getInt("duration"));
				s.setPlaycount(result.getInt("playcount"));
				s.setRating(result.getInt("rating"));
				s.setGenre(result.getString("genre"));
				// TODO Add album of song as soon as AlbumDao is implemented
				// DaoFactory df = DaoFactory.getInstance();
				// AlbumDao ad = df.getAlbumDao();
				// s.setAlbum(ad.read(<with readIsOnStmt>));

				sList.add(s);
			}
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return sList;
	}
}
