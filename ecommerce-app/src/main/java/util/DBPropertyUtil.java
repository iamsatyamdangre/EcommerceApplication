package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBPropertyUtil {
    private static final String DB_PROPERTIES_FILE = "src/main/resources/db.properties";

    public static Properties loadProperties() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(DB_PROPERTIES_FILE)) {
            props.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }
}
