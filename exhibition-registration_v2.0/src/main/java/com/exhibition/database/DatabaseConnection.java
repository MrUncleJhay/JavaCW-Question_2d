package com.exhibition.database;

import com.exhibition.util.ConfigLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private final String url;

    private DatabaseConnection() {
        String dbPath = ConfigLoader.getProperty("db.path", "participants.db");
        this.url = "jdbc:sqlite:" + dbPath;
        createTableIfMissing();
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    private void createTableIfMissing() {
        String sql = "CREATE TABLE IF NOT EXISTS participants ("
                   + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + "full_name TEXT NOT NULL,"
                   + "email TEXT NOT NULL UNIQUE,"
                   + "phone TEXT NOT NULL,"
                   + "category TEXT NOT NULL,"
                   + "registration_date TEXT NOT NULL"
                   + ");";
        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }
}
