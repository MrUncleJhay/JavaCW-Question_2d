package vu.exhibition.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import vu.exhibition.database.DatabaseConnection;
import vu.exhibition.model.Participant;

public class ParticipantDAO {

    private static final String INSERT_SQL =
            "INSERT INTO participants (full_name, email, phone, category, registration_date) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_SQL =
            "SELECT id, full_name, email, phone, category, registration_date " +
            "FROM participants ORDER BY id";

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