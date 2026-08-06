package com.exhibition.dao;

import com.exhibition.database.DatabaseConnection;
import com.exhibition.model.Participant;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ParticipantDAO {
    
    public void insert(Participant participant) throws SQLException {
        String sql = "INSERT INTO participants (full_name, email, phone, category, registration_date) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, participant.getFullName());
            pstmt.setString(2, participant.getEmail());
            pstmt.setString(3, participant.getPhone());
            pstmt.setString(4, participant.getCategory());
            pstmt.setString(5, participant.getRegistrationDate().toString());
            
            pstmt.executeUpdate();
        }
    }

    public List<Participant> getAll() throws SQLException {
        List<Participant> list = new ArrayList<>();
        String sql = "SELECT id, full_name, email, phone, category, registration_date FROM participants";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Participant p = new Participant();
                p.setId(rs.getInt("id"));
                p.setFullName(rs.getString("full_name"));
                p.setEmail(rs.getString("email"));
                p.setPhone(rs.getString("phone"));
                p.setCategory(rs.getString("category"));
                p.setRegistrationDate(LocalDate.parse(rs.getString("registration_date")));
                list.add(p);
            }
        }
        return list;
    }
}
