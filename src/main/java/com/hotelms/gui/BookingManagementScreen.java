package com.hotelms.gui;

import com.hotelms.dao.BookingDAO;
import com.hotelms.dao.GuestDAO;
import com.hotelms.dao.RoomDAO;
import com.hotelms.model.Booking;
import com.hotelms.model.Guest;
import com.hotelms.model.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BookingManagementScreen extends JPanel implements ActionListener {
    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, backButton, checkInButton, checkOutButton, generateInvoiceButton;
    
    private JTextField checkInDateField, checkOutDateField, stayDaysField;
    private JComboBox<String> statusComboBox;
    private JComboBox<String> roomComboBox, guestComboBox;
    private BookingDAO bookingDAO;
    private RoomDAO roomDAO;
    private GuestDAO guestDAO;

    private MainDashboard mainDashboard;

    public BookingManagementScreen(MainDashboard mainDashboard) {
        this.mainDashboard = mainDashboard;
        setLayout(new BorderLayout());

        bookingDAO = new BookingDAO();
        roomDAO = new RoomDAO();
        guestDAO = new GuestDAO();

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"ID", "Room Number", "Guest Name", "Check-in Date", "Check-out Date", "Status", "Stay Days"}, 0);
        bookingTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookingTable);
        add(scrollPane, BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Room:"));
        roomComboBox = new JComboBox<>();
        inputPanel.add(roomComboBox);
        populateRoomComboBox();

        inputPanel.add(new JLabel("Guest:"));
        guestComboBox = new JComboBox<>();
        inputPanel.add(guestComboBox);
        populateGuestComboBox();

        inputPanel.add(new JLabel("Check-in Date (YYYY-MM-DD):"));
        checkInDateField = new JTextField();
        inputPanel.add(checkInDateField);

        inputPanel.add(new JLabel("Check-out Date (YYYY-MM-DD):"));
        checkOutDateField = new JTextField();
        inputPanel.add(checkOutDateField);

        inputPanel.add(new JLabel("Stay Days:"));
        stayDaysField = new JTextField();
        inputPanel.add(stayDaysField);

        inputPanel.add(new JLabel("Status:"));
        statusComboBox = new JComboBox<>(new String[]{"pending", "confirmed", "cancelled", "checked-in", "checked-out"});
        inputPanel.add(statusComboBox);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Add");
        addButton.addActionListener(this);
        editButton = new JButton("Edit");
        editButton.addActionListener(this);
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this);
        checkInButton = new JButton("Check In");
        checkInButton.addActionListener(this);
        checkOutButton = new JButton("Check Out");
        checkOutButton.addActionListener(this);
        generateInvoiceButton = new JButton("Generate Invoice");
        generateInvoiceButton.addActionListener(this);
        backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(this);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(checkInButton);
        buttonPanel.add(checkOutButton);
        buttonPanel.add(generateInvoiceButton);
        buttonPanel.add(backButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(inputPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        loadBookings();

        bookingTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && bookingTable.getSelectedRow() != -1) {
                int selectedRow = bookingTable.getSelectedRow();
                // Room Number is at index 1, Guest Name at index 2
                roomComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 1).toString());
                guestComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 2).toString());
                checkInDateField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                checkOutDateField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                statusComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 5).toString());
                stayDaysField.setText(tableModel.getValueAt(selectedRow, 6).toString());
            }
        });
    }

    private void populateRoomComboBox() {
        roomComboBox.removeAllItems();
        List<Room> rooms = roomDAO.getAllRooms();
        for (Room room : rooms) {
            roomComboBox.addItem(room.getRoomNumber());
        }
    }

    private void populateGuestComboBox() {
        guestComboBox.removeAllItems();
        List<Guest> guests = guestDAO.getAllGuests();
        for (Guest guest : guests) {
            guestComboBox.addItem(guest.getFirstName() + " " + guest.getLastName());
        }
    }

    private void loadBookings() {
        tableModel.setRowCount(0); // Clear existing data
        List<Booking> bookings = bookingDAO.getAllBookings();
        for (Booking booking : bookings) {
            // Fetch room number and guest name for display
            String roomNumber = "N/A";
            List<Room> rooms = roomDAO.getAllRooms();
            for (Room room : rooms) {
                if (room.getId() == booking.getRoomId()) {
                    roomNumber = room.getRoomNumber();
                    break;
                }
            }

            String guestName = "N/A";
            List<Guest> guests = guestDAO.getAllGuests();
            for (Guest guest : guests) {
                if (guest.getId() == booking.getGuestId()) {
                    guestName = guest.getFirstName() + " " + guest.getLastName();
                    break;
                }
            }
            tableModel.addRow(new Object[]{booking.getId(), roomNumber, guestName, booking.getCheckInDate(), booking.getCheckOutDate(), booking.getStatus(), booking.getStayDays()});
        }
    }

    private void clearFields() {
        roomComboBox.setSelectedIndex(-1);
        guestComboBox.setSelectedIndex(-1);
        checkInDateField.setText("");
        checkOutDateField.setText("");
        stayDaysField.setText("");
        statusComboBox.setSelectedIndex(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            int roomId = -1;
            String selectedRoomNumber = (String) roomComboBox.getSelectedItem();
            if (selectedRoomNumber != null) {
                List<Room> rooms = roomDAO.getAllRooms();
                for (Room room : rooms) {
                    if (room.getRoomNumber().equals(selectedRoomNumber)) {
                        roomId = room.getId();
                        break;
                    }
                }
            }

            int guestId = -1;
            String selectedGuestName = (String) guestComboBox.getSelectedItem();
            if (selectedGuestName != null) {
                List<Guest> guests = guestDAO.getAllGuests();
                for (Guest guest : guests) {
                    if ((guest.getFirstName() + " " + guest.getLastName()).equals(selectedGuestName)) {
                        guestId = guest.getId();
                        break;
                    }
                }
            }

            String checkInDate = checkInDateField.getText();
            String checkOutDate = checkOutDateField.getText();
            String status = (String) statusComboBox.getSelectedItem();
            int stayDays = Integer.parseInt(stayDaysField.getText());

            if (roomId != -1 && guestId != -1 && !checkInDate.isEmpty() && !checkOutDate.isEmpty() && status != null) {
                bookingDAO.addBooking(new Booking(roomId, guestId, checkInDate, checkOutDate, status, stayDays));
                roomDAO.updateRoomStatus(roomId, "Reserved");
                loadBookings();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields and select a Room, Guest, and Dates.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == editButton) {
            int selectedRow = bookingTable.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);

                int roomId = -1;
                String selectedRoomNumber = (String) roomComboBox.getSelectedItem();
                if (selectedRoomNumber != null) {
                    List<Room> rooms = roomDAO.getAllRooms();
                    for (Room room : rooms) {
                        if (room.getRoomNumber().equals(selectedRoomNumber)) {
                            roomId = room.getId();
                            break;
                        }
                    }
                }

                int guestId = -1;
                String selectedGuestName = (String) guestComboBox.getSelectedItem();
                if (selectedGuestName != null) {
                    List<Guest> guests = guestDAO.getAllGuests();
                    for (Guest guest : guests) {
                        if ((guest.getFirstName() + " " + guest.getLastName()).equals(selectedGuestName)) {
                            guestId = guest.getId();
                            break;
                        }
                    }
                }

                String checkInDate = checkInDateField.getText();
                String checkOutDate = checkOutDateField.getText();
                String status = (String) statusComboBox.getSelectedItem();
                int stayDays = Integer.parseInt(stayDaysField.getText());

                if (roomId != -1 && guestId != -1 && !checkInDate.isEmpty() && !checkOutDate.isEmpty() && status != null) {
                    bookingDAO.updateBooking(new Booking(id, roomId, guestId, checkInDate, checkOutDate, status, stayDays));
                    loadBookings();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields and select a Room, Guest, and Dates.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a booking to edit.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == deleteButton) {
            int selectedRow = bookingTable.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                int roomId = -1;
                String selectedRoomNumber = (String) tableModel.getValueAt(selectedRow, 1);
                if (selectedRoomNumber != null) {
                    List<Room> rooms = roomDAO.getAllRooms();
                    for (Room room : rooms) {
                        if (room.getRoomNumber().equals(selectedRoomNumber)) {
                            roomId = room.getId();
                            break;
                        }
                    }
                }

                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this booking?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    bookingDAO.deleteBooking(id);
                    roomDAO.updateRoomStatus(roomId, "Available");
                    loadBookings();
                    clearFields();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a booking to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == checkInButton) {
            int selectedRow = bookingTable.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                int roomId = -1;
                String selectedRoomNumber = (String) tableModel.getValueAt(selectedRow, 1);
                if (selectedRoomNumber != null) {
                    List<Room> rooms = roomDAO.getAllRooms();
                    for (Room room : rooms) {
                        if (room.getRoomNumber().equals(selectedRoomNumber)) {
                            roomId = room.getId();
                            break;
                        }
                    }
                }

                int guestId = -1;
                String selectedGuestName = (String) tableModel.getValueAt(selectedRow, 2);
                if (selectedGuestName != null) {
                    List<Guest> guests = guestDAO.getAllGuests();
                    for (Guest guest : guests) {
                        if ((guest.getFirstName() + " " + guest.getLastName()).equals(selectedGuestName)) {
                            guestId = guest.getId();
                            break;
                        }
                    }
                }

                bookingDAO.updateBooking(new Booking(id, roomId, guestId, (String) tableModel.getValueAt(selectedRow, 3), (String) tableModel.getValueAt(selectedRow, 4), "checked-in", (int) tableModel.getValueAt(selectedRow, 6)));
                roomDAO.updateRoomStatus(roomId, "Occupied");
                loadBookings();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a booking to check in.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == checkOutButton) {
            int selectedRow = bookingTable.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                int roomId = -1;
                String selectedRoomNumber = (String) tableModel.getValueAt(selectedRow, 1);
                if (selectedRoomNumber != null) {
                    List<Room> rooms = roomDAO.getAllRooms();
                    for (Room room : rooms) {
                        if (room.getRoomNumber().equals(selectedRoomNumber)) {
                            roomId = room.getId();
                            break;
                        }
                    }
                }

                int guestId = -1;
                String selectedGuestName = (String) tableModel.getValueAt(selectedRow, 2);
                if (selectedGuestName != null) {
                    List<Guest> guests = guestDAO.getAllGuests();
                    for (Guest guest : guests) {
                        if ((guest.getFirstName() + " " + guest.getLastName()).equals(selectedGuestName)) {
                            guestId = guest.getId();
                            break;
                        }
                    }
                }

                bookingDAO.updateBooking(new Booking(id, roomId, guestId, (String) tableModel.getValueAt(selectedRow, 3), (String) tableModel.getValueAt(selectedRow, 4), "checked-out", (int) tableModel.getValueAt(selectedRow, 6)));
                roomDAO.updateRoomStatus(roomId, "Available");
                loadBookings();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a booking to check out.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == generateInvoiceButton) {
            int selectedRow = bookingTable.getSelectedRow();
            if (selectedRow != -1) {
                int roomId = -1;
                String selectedRoomNumber = (String) tableModel.getValueAt(selectedRow, 1);
                double roomPrice = 0;
                if (selectedRoomNumber != null) {
                    List<Room> rooms = roomDAO.getAllRooms();
                    for (Room room : rooms) {
                        if (room.getRoomNumber().equals(selectedRoomNumber)) {
                            roomId = room.getId();
                            roomPrice = room.getPrice();
                            break;
                        }
                    }
                }

                int stayDays = (int) tableModel.getValueAt(selectedRow, 6);
                double totalPrice = roomPrice * stayDays;

                String guestName = (String) tableModel.getValueAt(selectedRow, 2);
                String checkInDate = (String) tableModel.getValueAt(selectedRow, 3);
                String checkOutDate = (String) tableModel.getValueAt(selectedRow, 4);

                JTextArea invoiceTextArea = new JTextArea();
                invoiceTextArea.setEditable(false);
                invoiceTextArea.setText("Invoice\n\n" +
                        "Guest: " + guestName + "\n" +
                        "Room: " + selectedRoomNumber + "\n" +
                        "Check-in: " + checkInDate + "\n" +
                        "Check-out: " + checkOutDate + "\n" +
                        "Stay Days: " + stayDays + "\n" +
                        "Room Price: " + roomPrice + "\n" +
                        "Total Price: " + totalPrice);

                JOptionPane.showMessageDialog(this, new JScrollPane(invoiceTextArea), "Invoice", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a booking to generate an invoice.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == backButton) {
            mainDashboard.showCard("MainDashboard");
        }
    }
}
