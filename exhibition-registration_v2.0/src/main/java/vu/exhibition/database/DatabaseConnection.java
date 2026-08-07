package vu.exhibition.database;

import vu.exhibition.util.ConfigLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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