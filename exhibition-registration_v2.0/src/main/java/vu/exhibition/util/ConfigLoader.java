package vu.exhibition.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigLoader {

    private static final String CONFIG_FILE = "config.properties";
    private static final String DEFAULT_DB_PATH = "participants.db";

    private static final Properties PROPERTIES = load();

    private ConfigLoader() {
        // Utility class — not instantiable
    }

    private static Properties load() {
        Properties props = new Properties();
        try (InputStream in = ConfigLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (in == null) {
                System.err.println(
                        "Warning: " + CONFIG_FILE + " not found on classpath; "
                                + "falling back to default db.path=" + DEFAULT_DB_PATH);
                return props;
            }
            props.load(in);
        } catch (IOException e) {
            System.err.println(
                    "Warning: failed to read " + CONFIG_FILE + " (" + e.getMessage() + "); "
                            + "falling back to default db.path=" + DEFAULT_DB_PATH);
        }
        return props;
    }

    public static String getDbPath() {
        return PROPERTIES.getProperty("db.path", DEFAULT_DB_PATH);
    }

    public static String getProperty(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }
}