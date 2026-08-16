package vu.exhibition.dao;

import vu.exhibition.database.DatabaseConnection;
import vu.exhibition.model.Participant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data-access layer for {@link Participant} records.
 * <p>
 * Every method opens its own connection (via
 * {@link DatabaseConnection#getConnection()}) and closes it — along with
 * any statement or result set — through try-with-resources. SQLExceptions
 * are intentionally left to propagate rather than being caught here:
 * deciding how a failure should be presented to the user is the UI
 * layer's job, not the DAO's.
 */
public class ParticipantDAO {

    private static final String INSERT_SQL =
            "INSERT INTO participants (full_name, email, phone, category, registration_date) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_SQL =
            "SELECT id, full_name, email, phone, category, registration_date " +
            "FROM participants ORDER BY id";

    /**
     * Inserts a new participant. On success, the database-generated
     * {@code id} is written back onto {@code participant}.
     *
     * @throws SQLException if the insert fails — including a UNIQUE
     *         constraint violation when {@code participant.getEmail()}
     *         is already registered, thrown by the driver as
     *         {@code org.sqlite.SQLiteException} with a message
     *         containing "UNIQUE" (e.g. {@code "[SQLITE_CONSTRAINT_UNIQUE]
     *         A UNIQUE constraint failed (UNIQUE constraint failed:
     *         participants.email)"}). Note: sqlite-jdbc leaves
     *         {@code getSQLState()} {@code null}, so callers must check
     *         {@code e.getMessage().contains("UNIQUE")} rather than the
     *         SQL state to distinguish this case.
     */
    public void insert(Participant participant) throws SQLException {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, participant.getFullName());
            stmt.setString(2, participant.getEmail());
            stmt.setString(3, participant.getPhone());
            stmt.setString(4, participant.getCategory());
            stmt.setString(5, participant.getRegistrationDate().toString());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    participant.setId(keys.getInt(1));
                }
            }
        }
    }

    /**
     * @return every registered participant, ordered by id (oldest first).
     * @throws SQLException if the query fails.
     */
    public List<Participant> getAll() throws SQLException {
        List<Participant> participants = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                participants.add(new Participant(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("category"),
                        LocalDate.parse(rs.getString("registration_date"))
                ));
            }
        }

        return participants;
    }
}
