package vu.exhibition.ui;

import vu.exhibition.dao.ParticipantDAO;
import vu.exhibition.database.DatabaseConnection;
import vu.exhibition.model.Participant;
import vu.exhibition.util.ValidationUtils;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Main window of the Exhibition Registration application.
 * <p>
 * Presents the registration form — name, email, phone, category, and an
 * auto-filled date — alongside Register, Clear, and View All actions.
 * Register validates every field in turn, showing a specific dialog for
 * whichever check fails first; once everything passes, the record is
 * saved via {@link ParticipantDAO} and the form resets. View All opens
 * {@link ViewParticipantsDialog} with every saved participant.
 * {@link #main} is the application entry point.
 */
public class MainFrame extends JFrame {

    private static final String CATEGORY_PLACEHOLDER = "-- Select Category --";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final ParticipantDAO participantDAO = new ParticipantDAO();

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
        initListeners();
        configureFrame();
    }

    private void initComponents() {
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);

        // Placeholder first, then the real options straight from
        // ValidationUtils so the dropdown can never drift out of sync
        // with what isValidCategory() actually accepts.
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

    private void initListeners() {
        registerButton.addActionListener(e -> handleRegister());
        clearButton.addActionListener(e -> clearForm());
        viewAllButton.addActionListener(e -> handleViewAll());
    }

    /**
     * Validates the form in spec order — name, email, phone, category —
     * stopping at the first failure with a specific dialog explaining
     * exactly what's wrong. Once everything passes, saves via the DAO
     * and shows the matching outcome: success, duplicate email, or any
     * other database error.
     */
    private void handleRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String category = (String) categoryCombo.getSelectedItem();

        if (!ValidationUtils.isValidName(name)) {
            showValidationError("Full name cannot be empty.", nameField);
            return;
        }
        if (!ValidationUtils.isValidEmail(email)) {
            showValidationError("Please enter a valid email address (it must contain '@' and '.').", emailField);
            return;
        }
        if (!ValidationUtils.isValidPhone(phone)) {
            showValidationError("Please enter a valid contact number (at least 8 digits).", phoneField);
            return;
        }
        if (!ValidationUtils.isValidCategory(category)) {
            showValidationError("Please select an exhibition category.", categoryCombo);
            return;
        }

        // Computed fresh rather than parsed back from dateField's display
        // text, so the saved value is genuinely "now" even if the form
        // has been sitting open since before midnight.
        Participant participant = new Participant(name, email, phone, category, LocalDate.now());

        try {
            participantDAO.insert(participant);
            JOptionPane.showMessageDialog(this,
                    participant.getFullName() + " has been registered successfully!\n"
                            + "Participant ID: " + participant.getId(),
                    "Registration Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("UNIQUE")) {
                JOptionPane.showMessageDialog(this,
                        "A participant with this email is already registered.",
                        "Duplicate Email",
                        JOptionPane.ERROR_MESSAGE);
                emailField.requestFocusInWindow();
                emailField.selectAll();
            } else {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Registration failed due to a database error:\n" + e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showValidationError(String message, JComponent fieldToFocus) {
        JOptionPane.showMessageDialog(this, message, "Invalid Input", JOptionPane.ERROR_MESSAGE);
        fieldToFocus.requestFocusInWindow();
        if (fieldToFocus instanceof JTextField textField) {
            textField.selectAll();
        }
    }

    /**
     * Resets every field to its just-opened state. Called after a
     * successful registration, and by the Clear button directly.
     */
    private void clearForm() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        categoryCombo.setSelectedIndex(0);
        dateField.setText(LocalDate.now().format(DATE_FORMAT));
        nameField.requestFocusInWindow();
    }

    /**
     * Fetches every participant and opens {@link ViewParticipantsDialog}
     * to display them. A fetch failure shows the same generic
     * database-error dialog style used by Register, and the dialog is
     * never opened in that case.
     */
    private void handleViewAll() {
        try {
            List<Participant> participants = participantDAO.getAll();
            new ViewParticipantsDialog(this, participants).setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Could not load the participant list:\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Application entry point.
     * <p>
     * Installs a JVM-wide safety net for any exception that isn't
     * already handled by a more specific catch block elsewhere in this
     * class — bad input, a duplicate email, and a database error each
     * already show their own specific dialog; this is only a backstop
     * for a genuinely unexpected bug, so a failure surfaces as a message
     * instead of a button that silently does nothing. Then initializes
     * the database on the EDT before constructing any UI, so a database
     * that fails to initialize is reported as a clear startup error
     * instead of surfacing later as a broken button click.
     */
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            throwable.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "An unexpected error occurred and the last action may "
                            + "not have completed:\n" + throwable,
                    "Unexpected Error",
                    JOptionPane.ERROR_MESSAGE);
        });

        SwingUtilities.invokeLater(() -> {
            try {
                DatabaseConnection.getInstance();
                new MainFrame().setVisible(true);
            } catch (IllegalStateException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "The application could not start because the database "
                                + "could not be initialized:\n" + e.getMessage(),
                        "Startup Error",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
