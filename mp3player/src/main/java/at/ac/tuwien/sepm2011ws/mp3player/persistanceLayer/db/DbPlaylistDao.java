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
import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.WritablePlaylist;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.PlaylistDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.SongDao;

/**
 * @author klaus
 * 
 */

class DbPlaylistDao implements PlaylistDao {
	private Connection con;
	private SongDao sd;

	private PreparedStatement createStmt;
	private PreparedStatement createContainsStmt;
	private PreparedStatement readStmt;
	private PreparedStatement readContainsStmt;
	private PreparedStatement readAllStmt;
	private PreparedStatement updateStmt;
	private PreparedStatement deleteContainsStmt;
	private PreparedStatement deleteStmt;

	DbPlaylistDao(DataSource source, SongDao sd) throws DataAccessException {
		try {
			this.sd = sd;

			con = source.getConnection();
			createStmt = con.prepareStatement("INSERT INTO playlist (name) "
					+ "VALUES (?);", Statement.RETURN_GENERATED_KEYS);
			createContainsStmt = con.prepareStatement("INSERT INTO contains ( "
					+ "position, playlist, song) " + "VALUES (?, ?, ?);");
			readStmt = con
					.prepareStatement("SELECT name FROM playlist WHERE id=?;");
			readContainsStmt = con
					.prepareStatement("SELECT song FROM contains WHERE playlist=? ORDER BY position;");
			readAllStmt = con.prepareStatement("SELECT id FROM playlist;");
			updateStmt = con.prepareStatement("UPDATE playlist SET "
					+ "name=? WHERE id = ?;");
			deleteContainsStmt = con
					.prepareStatement("DELETE FROM contains WHERE playlist=?;");
			deleteStmt = con.prepareStatement("DELETE FROM playlist "
					+ "WHERE id = ?;");

		} catch (SQLException e) {
			throw new DataAccessException(
					"Error initializing database commands");
		}
	}

	@Override
	public void create(WritablePlaylist playlist) throws DataAccessException {
		ResultSet result = null;

		if (playlist.getTitle() == null)
			throw new IllegalArgumentException("Title of Playlist must not be null");

		try {
			createStmt.setString(1, playlist.getTitle());
			createStmt.executeUpdate();

			result = createStmt.getGeneratedKeys();
			if (result.next()) {
				playlist.setId(result.getInt(1));

				int i = 0;
				for (Song s : playlist) {
					// Create song if it doesn't exist
					// sd.create(s); Should not be necessary

					// Create playlist song association
					createContainsStmt.setInt(1, i);
					createContainsStmt.setInt(2, playlist.getId());
					createContainsStmt.setInt(3, s.getId());
					createContainsStmt.executeUpdate();
					i++;
				}

			} else {
				throw new DataAccessException("Error creating playlist in database");
			}
		} catch (SQLException e) {
			throw new DataAccessException("Error creating playlist in database");
		} finally {
			try {
				if (result != null)
					result.close();
			} catch (SQLException e) {
			}
		}
	}

	@Override
	public void update(WritablePlaylist playlist) throws DataAccessException {

		if (playlist == null) {
			throw new IllegalArgumentException("Playlist must not be null");
		}
		
		try {
			updateStmt.setString(1, playlist.getTitle());
			updateStmt.setInt(2, playlist.getId());
			updateStmt.executeUpdate();
			
			deleteContainsStmt.setInt(1, playlist.getId());
			deleteContainsStmt.executeUpdate();
			int i = 0;
			
			for (Song s : playlist) {
				// Update song
				// sd.update(s); Should not be necessary
				
				// Recreate playlist song association
				createContainsStmt.setInt(1, i);
				createContainsStmt.setInt(2, playlist.getId());
				createContainsStmt.setInt(3, s.getId());
				createContainsStmt.executeUpdate();
				i++;
			}
			
			

		} catch (SQLException e) {
			throw new DataAccessException("Error updating playlist in database");
		}

	}

	@Override
	public void delete(int id) throws DataAccessException {

		if (id < 0) {
			throw new IllegalArgumentException("ID must be greater or equal 0");
		}

		try {

			deleteStmt.setInt(1, id);
			deleteStmt.executeUpdate();

			// TODO: Delete all songs from library which were only on this
			// playlist (maybe with a database trigger)
		} catch (SQLException e) {
			throw new DataAccessException("Error deleting playlist in database");
		}

	}

	@Override
	public WritablePlaylist read(int id) throws DataAccessException {
		ResultSet result = null;
		WritablePlaylist p;
		List<Song> songs;

		if (id < 0) {
			throw new IllegalArgumentException("ID must be greater or equal 0");
		}

		try {
			readStmt.setInt(1, id);
			result = readStmt.executeQuery();
			if (!result.next()) {
				result.close();
				return null;
			}

			p = new WritablePlaylist(result.getString("name"));
			p.setId(id);

			// Reading songs
			readContainsStmt.setInt(1, p.getId());
			result = readContainsStmt.executeQuery();

			songs = new ArrayList<Song>();
			while (result.next()) {
				songs.add(sd.read(result.getInt("song")));
			}

			p.addAll(songs);

		} catch (SQLException e) {
			throw new DataAccessException(
					"Error reading playlist from database");
		} finally {
			try {
				if (result != null)
					result.close();
			} catch (SQLException e) {
			}
		}

		return p;
	}

	@Override
	public List<WritablePlaylist> readAll() throws DataAccessException {
		ResultSet result = null;
		List<WritablePlaylist> playlists = new ArrayList<WritablePlaylist>();
		try {
			result = readAllStmt.executeQuery();

			while (result.next()) {
				playlists.add(read(result.getInt("id")));
			}

		} catch (SQLException e) {
			throw new DataAccessException(
					"Error reading all playlists from database");
		} finally {
			try {
				if (result != null)
					result.close();
			} catch (SQLException e) {
			}
		}
		return playlists;
	}

	@Override
	public void rename(WritablePlaylist p, String name) throws DataAccessException {

		if (p == null || name == null || name.isEmpty()) {
			throw new IllegalArgumentException(
					"Playlist and name must not be null and name must not be empty");
		}

		try {
			updateStmt.setString(1, name);
			updateStmt.setInt(2, p.getId());
			updateStmt.executeUpdate();

		} catch (SQLException e) {
			throw new DataAccessException("Error renaming playlist in database");
		}
	}

	/**
	 * @return the connection
	 */
	@Override
	public Connection getConnection() {
		return this.con;
	}
}
