package vu.exhibition.ui;

import vu.exhibition.model.Participant;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Read-only dialog listing every registered participant in a sortable
 * table, wrapped in a scroll pane with a Close button. Constructed with
 * the participant list already fetched — see
 * {@code MainFrame.handleViewAll()} — so this class has no database
 * concerns of its own.
 */
public class ViewParticipantsDialog extends JDialog {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int[] COLUMN_WIDTHS = {40, 140, 180, 120, 100, 120};

    private final JTable participantsTable;

    public ViewParticipantsDialog(Frame owner, List<Participant> participants) {
        super(owner, "Registered Participants", true);
        this.participantsTable = buildTable(participants);
        layoutDialog();
        configureDialog(owner);
    }

    private JTable buildTable(List<Participant> participants) {
        JTable table = new JTable(new ParticipantTableModel(participants));
        table.setAutoCreateRowSorter(true);
        table.setPreferredScrollableViewportSize(new Dimension(700, 300));
        setColumnWidths(table);
        return table;
    }

    private void setColumnWidths(JTable table) {
        for (int i = 0; i < COLUMN_WIDTHS.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(COLUMN_WIDTHS[i]);
        }
    }

    private void layoutDialog() {
        JScrollPane scrollPane = new JScrollPane(participantsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel buildButtonPanel() {
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(closeButton);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        panel.add(closeButton);
        return panel;
    }

    private void configureDialog(Frame owner) {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(owner);
    }

    /**
     * Read-only table model backed directly by a {@code List<Participant>}
     * — no intermediate {@code Object[][]} copy. Every cell is
     * non-editable: there is no DAO {@code update()} method, so an
     * editable cell would let the user "change" a value that's silently
     * discarded the next time this dialog is opened.
     */
    private static class ParticipantTableModel extends AbstractTableModel {

        private static final String[] COLUMN_NAMES = {
                "ID", "Full Name", "Email", "Phone", "Category", "Registration Date"
        };

        private final List<Participant> participants;

        ParticipantTableModel(List<Participant> participants) {
            this.participants = participants;
        }

        @Override
        public int getRowCount() {
            return participants.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_NAMES.length;
        }

        @Override
        public String getColumnName(int column) {
            return COLUMN_NAMES[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnIndex == 0 ? Integer.class : String.class;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Participant p = participants.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> p.getId();
                case 1 -> p.getFullName();
                case 2 -> p.getEmail();
                case 3 -> p.getPhone();
                case 4 -> p.getCategory();
                case 5 -> p.getRegistrationDate().format(DATE_FORMAT);
                default -> throw new IllegalArgumentException("Unknown column: " + columnIndex);
            };
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }
}
