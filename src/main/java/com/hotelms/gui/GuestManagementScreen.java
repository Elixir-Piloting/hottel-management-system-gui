package com.hotelms.gui;

import com.hotelms.dao.GuestDAO;
import com.hotelms.model.Guest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GuestManagementScreen extends JPanel implements ActionListener {
    private JTable guestTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, backButton;
    private JTextField firstNameField, lastNameField, phoneField, emailField;
    private GuestDAO guestDAO;

    private MainDashboard mainDashboard;

    public GuestManagementScreen(MainDashboard mainDashboard) {
        this.mainDashboard = mainDashboard;
        setLayout(new BorderLayout());
        

        guestDAO = new GuestDAO();

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"ID", "First Name", "Last Name", "Phone", "Email"}, 0);
        guestTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(guestTable);
        add(scrollPane, BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        inputPanel.add(firstNameField);
        inputPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        inputPanel.add(lastNameField);
        inputPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

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

        loadGuests();

        guestTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && guestTable.getSelectedRow() != -1) {
                int selectedRow = guestTable.getSelectedRow();
                firstNameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                lastNameField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                phoneField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                emailField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            }
        });
    }

    private void loadGuests() {
        tableModel.setRowCount(0); // Clear existing data
        List<Guest> guests = guestDAO.getAllGuests();
        for (Guest guest : guests) {
            tableModel.addRow(new Object[]{guest.getId(), guest.getFirstName(), guest.getLastName(), guest.getPhone(), guest.getEmail()});
        }
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        phoneField.setText("");
        emailField.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();
            if (!firstName.isEmpty() && !lastName.isEmpty()) {
                guestDAO.addGuest(new Guest(firstName, lastName, phone, email));
                loadGuests();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields (First Name, Last Name).", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == editButton) {
            int selectedRow = guestTable.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String phone = phoneField.getText();
                String email = emailField.getText();
                if (!firstName.isEmpty() && !lastName.isEmpty()) {
                    guestDAO.updateGuest(new Guest(id, firstName, lastName, phone, email));
                    loadGuests();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Please fill in all required fields (First Name, Last Name).", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a guest to edit.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == deleteButton) {
            int selectedRow = guestTable.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this guest?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    guestDAO.deleteGuest(id);
                    loadGuests();
                    clearFields();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a guest to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == backButton) {
            mainDashboard.showCard("MainDashboard");
        }
    }
}
