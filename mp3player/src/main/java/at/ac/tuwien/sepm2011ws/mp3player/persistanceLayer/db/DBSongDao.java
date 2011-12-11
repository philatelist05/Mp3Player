package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

	PreparedStatement readStmt;
	PreparedStatement readAllStmt;

	DBSongDao(DataSource source) {

		try {

			Connection con = source.getConnection();
			readStmt = con.prepareStatement("SELECT "
					+ "title, artist, path, year, "
					+ "duration, playcount, rating, genre, "
					+ "album FROM song join is_on on id = song WHERE id=?;");

			readAllStmt = con.prepareStatement("SELECT id, "
					+ "title, artist, path, year, "
					+ "duration, playcount, rating, genre, "
					+ "album FROM song join is_on on id = song;");

		} catch (SQLException e) {
			e.printStackTrace();
		}
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
			// TODO: Add album of song as soon as AlbumDao is implemented
			// DaoFactory df = DaoFactory.getInstance();
			// AlbumDao ad = df.getAlbumDao();
			// s.setAlbum(ad.read(result.getInt("album")));

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
				// TODO: Add album of song as soon as AlbumDao is implemented
				// DaoFactory df = DaoFactory.getInstance();
				// AlbumDao ad = df.getAlbumDao();
				// s.setAlbum(ad.read(result.getInt("album")));

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
