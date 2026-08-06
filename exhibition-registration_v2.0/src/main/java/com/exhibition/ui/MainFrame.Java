package com.exhibition.ui;

import com.exhibition.dao.ParticipantDAO;
import com.exhibition.model.Participant;
import com.exhibition.util.ValidationUtils;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;

public class MainFrame extends JFrame {
    private JTextField txtName, txtEmail, txtPhone, txtDate;
    private JComboBox<String> cmbCategory;
    private JButton btnRegister, btnClear, btnViewAll;
    private final ParticipantDAO participantDAO = new ParticipantDAO();

    public MainFrame() {
        super("Exhibition Registration Form");
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 320);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Form Fields
        txtName = new JTextField(20);
        txtEmail = new JTextField(20);
        txtPhone = new JTextField(20);
        
        String[] categories = {"Select Category", "Art", "Science", "Technology", "Literature", "Photography"};
        cmbCategory = new JComboBox<>(categories);
        
        txtDate = new JTextField(20);
        txtDate.setText(LocalDate.now().toString());
        txtDate.setEditable(false);

        // Layout Adding
        addLabelAndComponent(formPanel, "Full Name:", txtName, gbc, 0);
        addLabelAndComponent(formPanel, "Email:", txtEmail, gbc, 1);
        addLabelAndComponent(formPanel, "Contact Number:", txtPhone, gbc, 2);
        addLabelAndComponent(formPanel, "Category:", cmbCategory, gbc, 3);
        addLabelAndComponent(formPanel, "Registration Date:", txtDate, gbc, 4);

        add(formPanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnRegister = new JButton("Register");
        btnClear = new JButton("Clear");
        btnViewAll = new JButton("View All");

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnViewAll);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event Handling
        btnRegister.addActionListener(e -> registerParticipant());
        btnClear.addActionListener(e -> clearForm());
        btnViewAll.addActionListener(e -> new ViewParticipantsDialog(this).setVisible(true));
    }

    private void addLabelAndComponent(JPanel panel, String labelText, Component comp, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        panel.add(new JLabel(labelText), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(comp, gbc);
    }

    private void registerParticipant() {
        String name = txtName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String category = (String) cmbCategory.getSelectedItem();

        if (!ValidationUtils.isNotEmpty(name)) {
            JOptionPane.showMessageDialog(this, "Full Name cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!ValidationUtils.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email address (must contain '@' and '.').", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!ValidationUtils.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(this, "Contact number must contain at least 8 digits.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!ValidationUtils.isValidCategory(category)) {
            JOptionPane.showMessageDialog(this, "Please select a valid exhibition category.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Participant participant = new Participant(name, email, phone, category, LocalDate.now());
        try {
            participantDAO.insert(participant);
            JOptionPane.showMessageDialog(this, "Registration Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                JOptionPane.showMessageDialog(this, "Registration failed: This email address is already registered.", "Database Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Database operation failed: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        cmbCategory.setSelectedIndex(0);
        txtDate.setText(LocalDate.now().toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new MainFrame().setVisible(true);
        });
    }
}
