package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB {

    private static Connection connection = null;

    public static Connection getConnection() {

        if (connection == null) {

            try {

                Properties properties = loadProperties();
                String url = properties.getProperty("dburl");
                connection = DriverManager.getConnection(url, properties);
            } catch (SQLException e) {

                throw new DBException(e.getMessage());
            }
        }

        return connection;
    }

    private static Properties loadProperties() {

        try (FileInputStream fileInputStream = new FileInputStream("db.properties")) {

            Properties properties = new Properties();
            properties.load(fileInputStream);

            return properties;
        } catch (IOException e) {

            throw new DBException(e.getMessage());
        }
    }
}
