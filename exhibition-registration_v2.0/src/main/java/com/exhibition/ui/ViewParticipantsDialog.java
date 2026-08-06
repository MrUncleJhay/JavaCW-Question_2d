package com.exhibition.ui;

import com.exhibition.dao.ParticipantDAO;
import com.exhibition.model.Participant;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ViewParticipantsDialog extends JDialog {
    private JTable table;
    private DefaultTableModel tableModel;
    private final ParticipantDAO participantDAO = new ParticipantDAO();

    public ViewParticipantsDialog(Frame owner) {
        super(owner, "Registered Participants", true);
        initializeUI();
        loadData();
    }

    private void initializeUI() {
        setSize(650, 400);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        String[] columns = {"ID", "Full Name", "Email", "Contact", "Category", "Reg Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        bottomPanel.add(closeButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        try {
            List<Participant> participants = participantDAO.getAll();
            for (Participant p : participants) {
                tableModel.addRow(new Object[]{
                    p.getId(), p.getFullName(), p.getEmail(), p.getPhone(), p.getCategory(), p.getRegistrationDate()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching records: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
