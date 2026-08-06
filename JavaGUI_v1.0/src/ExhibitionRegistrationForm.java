package vu.exhibition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;

public class ExhibitionRegistrationForm extends JFrame {

    private JLabel lblImagePreview;
    private JButton btnUploadImage;
    private JButton btnRegister;
    private JTextField txtName, txtRegID, txtEmail;
    private String imagePath = "";

    public ExhibitionRegistrationForm() {
        setTitle("Exhibition Registration");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Input Fields
        txtRegID = new JTextField(20);
        txtName = new JTextField(20);
        txtEmail = new JTextField(20);

        add(new JLabel("Registration ID:"));
        add(txtRegID);
        add(new JLabel("Name:"));
        add(txtName);
        add(new JLabel("Email:"));
        add(txtEmail);

        // Image Upload UI
        btnUploadImage = new JButton("Upload Project Image");
        lblImagePreview = new JLabel();
        lblImagePreview.setPreferredSize(new Dimension(150, 150));
        add(btnUploadImage);
        add(lblImagePreview);

        // Register Button
        btnRegister = new JButton("Register");
        add(btnRegister);

        // Image Upload Logic
        btnUploadImage.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                imagePath = file.getAbsolutePath();

                // Display preview
                ImageIcon icon = new ImageIcon(imagePath);
                Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                lblImagePreview.setIcon(new ImageIcon(img));
            }
        });

        // Register participant and save image path
        btnRegister.addActionListener(e -> registerParticipant());

        setVisible(true);
    }

    private void registerParticipant() {
        String regID = txtRegID.getText().trim();
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();

        if (regID.isEmpty() || name.isEmpty() || email.isEmpty() || imagePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields and image must be filled.");
            return;
        }

        try {
            // Load UCanAccess Driver
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            // Update the path to your actual database file
            String dbPath = "C:/Users/Admin/Documents/NetBeansProjects/JavaGUI/src/Participants.accdb";
            Connection conn = DriverManager.getConnection("jdbc:ucanaccess://" + dbPath);

            String sql = "INSERT INTO Participants (RegID, Name, Email, imagePath) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, regID);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, imagePath);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Participant registered successfully.");
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExhibitionRegistrationForm::new);
    }
}
