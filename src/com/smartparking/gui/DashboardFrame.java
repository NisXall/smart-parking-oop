package com.smartparking.gui;

import com.smartparking.model.DashboardStats;
import com.smartparking.model.VehicleRecord;
import com.smartparking.service.ParkingManager;
import com.smartparking.service.ParkingResult;
import com.smartparking.service.RemoveResult;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

public class DashboardFrame extends JFrame {
    private final ParkingManager parkingManager;

    private JLabel totalSlotsValue;
    private JLabel availableSlotsValue;
    private JLabel occupiedSlotsValue;

    private JTextField vehicleNumberField;
    private JTextField ownerNameField;
    private JTextField removeVehicleField;

    private DefaultTableModel tableModel;

    public DashboardFrame(ParkingManager parkingManager) {
        this.parkingManager = parkingManager;
        initializeUI();
        refreshDashboard();
        refreshVehicleTable();
    }

    private void initializeUI() {
        setTitle("Smart Parking System");
        setSize(950, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = createStatsPanel();
        JPanel centerPanel = createTablePanel();
        JPanel bottomPanel = createControlsPanel();

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        totalSlotsValue = createStatCard(panel, "Total Slots", new Color(30, 136, 229));
        availableSlotsValue = createStatCard(panel, "Available Slots", new Color(67, 160, 71));
        occupiedSlotsValue = createStatCard(panel, "Occupied Slots", new Color(229, 57, 53));

        return panel;
    }

    private JLabel createStatCard(JPanel parent, String title, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(color, 2));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(color);

        JLabel valueLabel = new JLabel("0", SwingConstants.CENTER);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 28));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        parent.add(card);
        return valueLabel;
    }

    private JPanel createTablePanel() {
        String[] columns = {"ID", "Vehicle No", "Owner", "Slot", "Entry Time", "Exit Time", "Fee (INR)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Vehicle Records"));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createControlsPanel() {
        JPanel wrapper = new JPanel(new GridLayout(1, 2, 10, 10));
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel parkPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        parkPanel.setBorder(BorderFactory.createTitledBorder("Park Vehicle"));

        vehicleNumberField = new JTextField();
        ownerNameField = new JTextField();
        JButton parkButton = new JButton("Park");
        parkButton.addActionListener(e -> handleParkVehicle());

        parkPanel.add(new JLabel("Vehicle Number:"));
        parkPanel.add(vehicleNumberField);
        parkPanel.add(new JLabel("Owner Name:"));
        parkPanel.add(ownerNameField);
        parkPanel.add(new JLabel(""));
        parkPanel.add(parkButton);

        JPanel removePanel = new JPanel(new GridLayout(3, 1, 8, 8));
        removePanel.setBorder(BorderFactory.createTitledBorder("Remove Vehicle"));

        removeVehicleField = new JTextField();
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> handleRemoveVehicle());
        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.addActionListener(e -> {
            refreshDashboard();
            refreshVehicleTable();
        });

        removePanel.add(removeVehicleField);
        removePanel.add(removeButton);
        removePanel.add(refreshButton);

        wrapper.add(parkPanel);
        wrapper.add(removePanel);
        return wrapper;
    }

    private void handleParkVehicle() {
        String vehicleNumber = vehicleNumberField.getText();
        String ownerName = ownerNameField.getText();

        ParkingResult result = parkingManager.parkVehicle(vehicleNumber, ownerName);
        JOptionPane.showMessageDialog(this,
                result.isSuccess() ? result.getMessage() + "\nAssigned Slot: " + result.getSlotNumber() : result.getMessage());

        if (result.isSuccess()) {
            vehicleNumberField.setText("");
            ownerNameField.setText("");
            refreshDashboard();
            refreshVehicleTable();
        }
    }

    private void handleRemoveVehicle() {
        String vehicleNumber = removeVehicleField.getText();
        RemoveResult result = parkingManager.removeVehicle(vehicleNumber);

        String message;
        if (result.isSuccess()) {
            message = result.getMessage() + "\nFreed Slot: " + result.getSlotNumber() + "\nBill: INR " + String.format("%.2f", result.getFee());
        } else {
            message = result.getMessage();
        }

        JOptionPane.showMessageDialog(this, message);

        if (result.isSuccess()) {
            removeVehicleField.setText("");
            refreshDashboard();
            refreshVehicleTable();
        }
    }

    private void refreshDashboard() {
        DashboardStats stats = parkingManager.getDashboardStats();
        totalSlotsValue.setText(String.valueOf(stats.getTotalSlots()));
        availableSlotsValue.setText(String.valueOf(stats.getAvailableSlots()));
        occupiedSlotsValue.setText(String.valueOf(stats.getOccupiedSlots()));
    }

    private void refreshVehicleTable() {
        List<VehicleRecord> records = parkingManager.getAllVehicleRecords();
        tableModel.setRowCount(0);

        for (VehicleRecord record : records) {
            tableModel.addRow(new Object[]{
                    record.getId(),
                    record.getVehicleNumber(),
                    record.getOwnerName(),
                    record.getSlotNumber(),
                    record.getEntryTime(),
                    record.getExitTime() == null ? "--" : record.getExitTime(),
                    record.getFee() == null ? "--" : String.format("%.2f", record.getFee())
            });
        }
    }

    public static void open(ParkingManager parkingManager) {
        SwingUtilities.invokeLater(() -> {
            DashboardFrame frame = new DashboardFrame(parkingManager);
            frame.setVisible(true);
        });
    }
}
