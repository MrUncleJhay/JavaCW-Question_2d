package vu.exhibition.database;

import vu.exhibition.util.ConfigLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Single point of access to the SQLite connection URL and schema setup.
 * <p>
 * This class does <b>not</b> hold one long-lived {@link Connection}.
 * SQLite connections are cheap to open against a local file, and the DAO
 * layer relies on try-with-resources to open-and-close a connection per
 * operation — so {@link #getConnection()} hands back a fresh connection
 * every time it is called. What <i>is</i> a singleton is the
 * configuration and one-time table-initialization logic: exactly one
 * {@code DatabaseConnection} exists per JVM, and it guarantees the
 * {@code participants} table exists before anything else runs.
 */
public final class DatabaseConnection {

    private static final String CREATE_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS participants (
                id                INTEGER PRIMARY KEY AUTOINCREMENT,
                full_name         TEXT    NOT NULL,
                email             TEXT    NOT NULL UNIQUE,
                phone             TEXT    NOT NULL,
                category          TEXT    NOT NULL,
                registration_date TEXT    NOT NULL
            )
            """;

    private static volatile DatabaseConnection instance;

    private final String jdbcUrl;

    private DatabaseConnection() {
        this.jdbcUrl = "jdbc:sqlite:" + ConfigLoader.getDbPath();
        initializeSchema();
    }

    /**
     * Returns the single {@code DatabaseConnection} instance, creating it
     * (and the {@code participants} table, if missing) on first call.
     * Thread-safe via double-checked locking.
     */
    public static DatabaseConnection getInstance() {
        DatabaseConnection result = instance;
        if (result == null) {
            synchronized (DatabaseConnection.class) {
                result = instance;
                if (result == null) {
                    instance = result = new DatabaseConnection();
                }
            }
        }
        return result;
    }

    /**
     * Opens and returns a brand-new SQLite connection. Callers own it and
     * are responsible for closing it — typically via try-with-resources
     * in the DAO layer.
     *
     * @throws SQLException if the connection cannot be opened
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl);
    }

    private void initializeSchema() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE_SQL);
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Failed to initialize the participants table at " + jdbcUrl, e);
        }
    }
}
