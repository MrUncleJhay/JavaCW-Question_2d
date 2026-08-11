package vu.exhibition.ui;

import vu.exhibition.util.ValidationUtils;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private static final String CATEGORY_PLACEHOLDER = "-- Select Category --";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox<String> categoryCombo;
    private JTextField dateField;

    private JButton registerButton;
    private JButton clearButton;
    private JButton viewAllButton;

    public MainFrame() {
        super("Exhibition Registration");
        initComponents();
        layoutComponents();
        configureFrame();
    }

    private void initComponents() {
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);

        List<String> categoryOptions = new ArrayList<>();
        categoryOptions.add(CATEGORY_PLACEHOLDER);
        categoryOptions.addAll(ValidationUtils.CATEGORIES);
        categoryCombo = new JComboBox<>(categoryOptions.toArray(new String[0]));

        dateField = new JTextField(LocalDate.now().format(DATE_FORMAT), 20);
        dateField.setEditable(false);
        dateField.setFocusable(false);
        dateField.setBackground(new Color(230, 230, 230));

        registerButton = new JButton("Register");
        clearButton = new JButton("Clear");
        viewAllButton = new JButton("View All");
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        add(buildFormPanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addFormRow(panel, gbc, row++, "Full Name:", nameField);
        addFormRow(panel, gbc, row++, "Email:", emailField);
        addFormRow(panel, gbc, row++, "Contact Number:", phoneField);
        addFormRow(panel, gbc, row++, "Exhibition Category:", categoryCombo);
        addFormRow(panel, gbc, row, "Registration Date:", dateField);

        return panel;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(field, gbc);
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        panel.add(registerButton);
        panel.add(clearButton);
        panel.add(viewAllButton);
        return panel;
    }

    private void configureFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }
}