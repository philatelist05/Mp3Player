package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

class DbConnection {

	private Connection sqlConnection;

	public DbConnection(DataSource dataSource) throws SQLException {
		this.sqlConnection = dataSource.getConnection();
	}

	public Connection getSqlConnection() {
		return sqlConnection;
	}
}
