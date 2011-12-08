package at.ac.tuwien.sepm2011ws.mp3player.persistanceLayer.jdbc;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DBConnection{
    private static Connection instance;

    private DBConnection() {
    }

    public static Connection getConnection() {
	if (instance == null) {
	    try {
		ApplicationContext context = new ClassPathXmlApplicationContext("settings.xml");
		DataSource source = (DataSource) context.getBean("DataSource");
		instance = source.getConnection();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	return instance;
    }
    
}