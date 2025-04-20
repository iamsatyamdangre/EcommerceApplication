package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnUtil {
    public static Connection getConnection() {
        Properties props = DBPropertyUtil.loadProperties();
        String url = "jdbc:mysql://" + props.getProperty("host") + ":" +
                     props.getProperty("port") + "/" + props.getProperty("dbname");
        String user = props.getProperty("user");
        String password = props.getProperty("password");

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
