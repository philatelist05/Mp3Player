package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.postgresql.ds.PGSimpleDataSource;

public class DBConnection{
    private static Connection instance;

    private DBConnection() {
    }

    public static synchronized Connection getConnection() {
	if (instance == null) {
	    // TODO: Properties reading from a properties file
	    try {
		PGSimpleDataSource ds = new PGSimpleDataSource();
		ds.setDatabaseName("mp3player");
		ds.setServerName("localhost");
		ds.setUser("postgres");
		ds.setPassword("12345");
		instance = ds.getConnection();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}

	return instance;
    }
    
}