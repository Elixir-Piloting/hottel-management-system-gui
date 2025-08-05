package com.hotelms.gui;

import com.hotelms.dao.UserDAO;
import com.hotelms.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserManagementScreen extends JPanel implements ActionListener {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, backButton;
    private JTextField usernameField;
    private JComboBox<String> roleComboBox;
    private JPasswordField passwordField;
    private UserDAO userDAO;

    private MainDashboard mainDashboard;

    public UserManagementScreen(MainDashboard mainDashboard) {
        this.mainDashboard = mainDashboard;
        setLayout(new BorderLayout());
        

        userDAO = new UserDAO();

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"ID", "Username", "Password", "Role"}, 0);
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);
        inputPanel.add(new JLabel("Role:"));
        roleComboBox = new JComboBox<>(new String[]{"admin", "staff", "guest"});
        inputPanel.add(roleComboBox);

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

        loadUsers();

        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && userTable.getSelectedRow() != -1) {
                int selectedRow = userTable.getSelectedRow();
                usernameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                
                roleComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 3).toString());
            }
        });
    }

    private void loadUsers() {
        tableModel.setRowCount(0); // Clear existing data
        List<User> users = userDAO.getAllUsers();
        for (User user : users) {
            tableModel.addRow(new Object[]{user.getId(), user.getUsername(), user.getPassword(), user.getRole()});
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        roleComboBox.setSelectedIndex(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();
            if (!username.isEmpty() && !password.isEmpty() && !role.isEmpty()) {
                userDAO.addUser(new User(username, password, role));
                loadUsers();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == editButton) {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String role = (String) roleComboBox.getSelectedItem();
                if (!username.isEmpty() && !password.isEmpty() && !role.isEmpty()) {
                    userDAO.updateUser(new User(id, username, password, role));
                    loadUsers();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a user to edit.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == deleteButton) {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    userDAO.deleteUser(id);
                    loadUsers();
                    clearFields();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a user to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == backButton) {
            mainDashboard.showCard("MainDashboard");
        }
    }
}
