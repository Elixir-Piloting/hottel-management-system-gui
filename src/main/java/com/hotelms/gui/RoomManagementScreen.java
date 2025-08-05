package com.hotelms.gui;

import com.hotelms.dao.RoomDAO;
import com.hotelms.model.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RoomManagementScreen extends JPanel implements ActionListener {
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, backButton;
    private JTextField roomNumberField, typeField, priceField;
    private JComboBox<String> statusComboBox;
    private RoomDAO roomDAO;

    private MainDashboard mainDashboard;

    public RoomManagementScreen(MainDashboard mainDashboard) {
        this.mainDashboard = mainDashboard;
        setLayout(new BorderLayout());
        

        roomDAO = new RoomDAO();

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"ID", "Room Number", "Type", "Price", "Status"}, 0);
        roomTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(roomTable);
        add(scrollPane, BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(new JLabel("Room Number:"));
        roomNumberField = new JTextField();
        inputPanel.add(roomNumberField);
        inputPanel.add(new JLabel("Type:"));
        typeField = new JTextField();
        inputPanel.add(typeField);
        inputPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Status:"));
        statusComboBox = new JComboBox<>(new String[]{"available", "occupied", "maintenance"});
        inputPanel.add(statusComboBox);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Add");
        addButton.addActionListener(this);
        editButton = new JButton("Edit");
        editButton.addActionListener(this);
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this);
        backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(this);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(inputPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        loadRooms();

        roomTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && roomTable.getSelectedRow() != -1) {
                int selectedRow = roomTable.getSelectedRow();
                roomNumberField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                typeField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                priceField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                statusComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 4).toString());
            }
        });
    }

    private void loadRooms() {
        tableModel.setRowCount(0); // Clear existing data
        List<Room> rooms = roomDAO.getAllRooms();
        for (Room room : rooms) {
            tableModel.addRow(new Object[]{room.getId(), room.getRoomNumber(), room.getType(), room.getPrice(), room.getStatus()});
        }
    }

    private void clearFields() {
        roomNumberField.setText("");
        typeField.setText("");
        priceField.setText("");
        statusComboBox.setSelectedIndex(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String roomNumber = roomNumberField.getText();
            String type = typeField.getText();
            double price = 0.0;
            try {
                price = Double.parseDouble(priceField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid price format.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String status = (String) statusComboBox.getSelectedItem();
            if (!roomNumber.isEmpty() && !type.isEmpty() && !status.isEmpty()) {
                roomDAO.addRoom(new Room(roomNumber, type, price, status));
                loadRooms();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == editButton) {
            int selectedRow = roomTable.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String roomNumber = roomNumberField.getText();
                String type = typeField.getText();
                double price = 0.0;
                try {
                    price = Double.parseDouble(priceField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid price format.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String status = (String) statusComboBox.getSelectedItem();
                if (!roomNumber.isEmpty() && !type.isEmpty() && !status.isEmpty()) {
                    roomDAO.updateRoom(new Room(id, roomNumber, type, price, status));
                    loadRooms();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a room to edit.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == deleteButton) {
            int selectedRow = roomTable.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this room?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    roomDAO.deleteRoom(id);
                    loadRooms();
                    clearFields();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a room to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == backButton) {
            mainDashboard.showCard("MainDashboard");
        }
    }
}
