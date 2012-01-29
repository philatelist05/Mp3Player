package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import at.ac.tuwien.sepm2011ws.mp3player.domainObjects.Album;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.AlbumDao;
import at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.DataAccessException;

class DbAlbumDao implements AlbumDao {
	private Connection con;

	private PreparedStatement createStmt;
	private PreparedStatement readStmt;
	private PreparedStatement updateStmt;
	private PreparedStatement deleteStmt;

	private PreparedStatement sameStmt;

	DbAlbumDao(DataSource source) throws DataAccessException {

		try {

			con = source.getConnection();
			createStmt = con.prepareStatement("INSERT INTO album ( "
					+ "title, year, albumart_path) " + "VALUES (?, ?, ?);",
					Statement.RETURN_GENERATED_KEYS);
			readStmt = con.prepareStatement("SELECT "
					+ "title, year, albumart_path FROM album WHERE id=?;");
			updateStmt = con.prepareStatement("UPDATE album SET "
					+ "title=?, year=?, albumart_path=? WHERE id = ?;");
			deleteStmt = con.prepareStatement("DELETE FROM album "
					+ "WHERE id = ?;");
			sameStmt = con.prepareStatement("SELECT id FROM album WHERE "
					+ "title=? AND year=?;");

		} catch (SQLException e) {
			throw new DataAccessException(
					"Error initializing database commands");
		}
	}

	public void create(Album a) throws DataAccessException {
		ResultSet result = null;

		if (a == null)
			throw new IllegalArgumentException("Album must not be null");

		try {
			sameStmt.setString(1, a.getTitle());
			sameStmt.setInt(2, a.getYear());
			result = sameStmt.executeQuery();

//			if (result.next()) {
//				// Album already exists in db, so read it
//				Album a2 = read(result.getInt("id"));
//				a.setId(a2.getId());
//				a.setTitle(a2.getTitle());
//				a.setYear(a2.getYear());
//				a.setAlbumartPath(a2.getAlbumartPath());
//			} else {
				// Album doesn't exist in db, so create
				createStmt.setString(1, a.getTitle());
				createStmt.setInt(2, a.getYear());
				createStmt.setString(3, a.getAlbumartPath());

				createStmt.executeUpdate();

				result = createStmt.getGeneratedKeys();
				if (result.next()) {
					a.setId(result.getInt(1));
				} else {
					throw new DataAccessException(
							"Error creating album in database");
				}
//			}

		} catch (SQLException e) {
			throw new DataAccessException("Error creating album in database");
		} finally {
			try {
				if (result != null)
					result.close();
			} catch (SQLException e) {
			}
		}
	}

	public void update(Album a) throws DataAccessException {

		if (a == null) {
			throw new IllegalArgumentException("Album must not be null");
		}

		try {
			updateStmt.setString(1, a.getTitle());
			updateStmt.setInt(2, a.getYear());
			updateStmt.setString(3, a.getAlbumartPath());
			updateStmt.setInt(4, a.getId());

			updateStmt.executeUpdate();

		} catch (SQLException e) {
			throw new DataAccessException("Error updating album in database");
		}

	}

	public void delete(int id) throws DataAccessException {

		if (id < 0) {
			throw new IllegalArgumentException("ID must be greater or equal 0");
		}

		try {

			deleteStmt.setInt(1, id);
			deleteStmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException("Error deleting album in database");
		}

	}

	public Album read(int id) throws DataAccessException {
		ResultSet result = null;

		if (id < 0) {
			throw new IllegalArgumentException("ID must be greater or equal 0");
		}

		Album a;

		try {
			readStmt.setInt(1, id);
			result = readStmt.executeQuery();
			if (!result.next()) {
				result.close();
				return null;
			}

			a = new Album(id, result.getString("title"), result.getInt("year"),
					result.getString("albumart_path"));

			result.close();

		} catch (SQLException e) {
			throw new DataAccessException("Error reading album from database");
		} finally {
			try {
				if (result != null)
					result.close();
			} catch (SQLException e) {
			}
		}

		return a;
	}

	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return this.con;
	}
}
