package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static Connection instance;

    private DBConnection() {  }

    public static synchronized Connection getInstance() {
    	if (instance == null) {
    		// TODO: Properties reading from a properties file
			String driver = "org.postgresql.Driver";
			String dsn = "jdbc:postgresql://192.168.3.109/mp3player";
			String usr = "postgres";
			String pwd = "12345";

			try {
				Class.forName(driver).newInstance();
				instance = DriverManager.getConnection(dsn, usr, pwd);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return instance;
    }
}