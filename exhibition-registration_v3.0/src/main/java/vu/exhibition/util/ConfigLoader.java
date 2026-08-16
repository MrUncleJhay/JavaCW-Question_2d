package vu.exhibition.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads application configuration from {@code config.properties} on the
 * classpath ({@code src/main/resources/config.properties}). The file is
 * read once, the first time this class is touched, and cached for the
 * life of the JVM.
 */
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

    /**
     * @return the configured SQLite database path ({@code db.path}), or
     *         {@code "participants.db"} if the property or file is missing.
     */
    public static String getDbPath() {
        return PROPERTIES.getProperty("db.path", DEFAULT_DB_PATH);
    }

    /**
     * General-purpose accessor for any other property that might get
     * added to {@code config.properties} later.
     */
    public static String getProperty(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }
}
