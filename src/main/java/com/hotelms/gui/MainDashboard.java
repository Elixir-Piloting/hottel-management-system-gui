package com.hotelms.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainDashboard extends JFrame implements ActionListener {
    private JButton userManagementButton;
    private JButton roomManagementButton;
    private JButton guestManagementButton;
    private JButton bookingManagementButton;
    private JButton invoicePaymentButton;
    private JButton logoutButton;

    private String userRole;

    public MainDashboard(String role) {
        this.userRole = role;
        setTitle("Hotel Management System - Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        setLayout(new BorderLayout());

        // Sidebar Panel
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new GridLayout(0, 1, 0, 10)); // One column, vertical gap
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sidebarPanel.setBackground(new Color(70, 130, 180)); // SteelBlue

        userManagementButton = new JButton("User Management");
        roomManagementButton = new JButton("Room Management");
        guestManagementButton = new JButton("Guest Management");
        bookingManagementButton = new JButton("Booking Management");
        invoicePaymentButton = new JButton("Invoice/Payment");
        logoutButton = new JButton("Logout");

        styleButton(userManagementButton);
        styleButton(roomManagementButton);
        styleButton(guestManagementButton);
        styleButton(bookingManagementButton);
        styleButton(invoicePaymentButton);
        styleButton(logoutButton);

        sidebarPanel.add(userManagementButton);
        sidebarPanel.add(roomManagementButton);
        sidebarPanel.add(guestManagementButton);
        sidebarPanel.add(bookingManagementButton);
        sidebarPanel.add(invoicePaymentButton);
        sidebarPanel.add(logoutButton);

        add(sidebarPanel, BorderLayout.WEST);

        // Content Panel with CardLayout
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        userManagementScreen = new UserManagementScreen(this);
        roomManagementScreen = new RoomManagementScreen(this);
        guestManagementScreen = new GuestManagementScreen(this);
        bookingManagementScreen = new BookingManagementScreen(this);
        invoicePaymentScreen = new InvoicePaymentScreen(this);

        contentPanel.add(userManagementScreen, "UserManagement");
        contentPanel.add(roomManagementScreen, "RoomManagement");
        contentPanel.add(guestManagementScreen, "GuestManagement");
        contentPanel.add(bookingManagementScreen, "BookingManagement");
        contentPanel.add(invoicePaymentScreen, "InvoicePayment");

        // Welcome Panel
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.add(new JLabel("Welcome to Hotel Management System!", SwingConstants.CENTER), BorderLayout.CENTER);
        contentPanel.add(welcomePanel, "Welcome");

        add(contentPanel, BorderLayout.CENTER);

        // Initial view
        cardLayout.show(contentPanel, "Welcome");

        // Role-based access control
        applyRoleBasedAccess();
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(100, 149, 237)); // CornflowerBlue
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.addActionListener(this);
    }

    private void applyRoleBasedAccess() {
        userManagementButton.setEnabled(false);
        roomManagementButton.setEnabled(false);
        guestManagementButton.setEnabled(false);
        bookingManagementButton.setEnabled(false);
        invoicePaymentButton.setEnabled(false);

        if (userRole.equals("admin")) {
            userManagementButton.setEnabled(true);
            roomManagementButton.setEnabled(true);
            guestManagementButton.setEnabled(true);
            bookingManagementButton.setEnabled(true);
        } else if (userRole.equals("staff")) {
            roomManagementButton.setEnabled(true);
            guestManagementButton.setEnabled(true);
            bookingManagementButton.setEnabled(true);
            invoicePaymentButton.setEnabled(true);
        }
    }

    public void showCard(String cardName) {
        cardLayout.show(contentPanel, cardName);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == userManagementButton) {
            showCard("UserManagement");
        } else if (e.getSource() == roomManagementButton) {
            showCard("RoomManagement");
        } else if (e.getSource() == guestManagementButton) {
            showCard("GuestManagement");
        } else if (e.getSource() == bookingManagementButton) {
            showCard("BookingManagement");
        } else if (e.getSource() == invoicePaymentButton) {
            showCard("InvoicePayment");
        } else if (e.getSource() == logoutButton) {
            new LoginScreen().setVisible(true);
            dispose();
        }
    }

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private UserManagementScreen userManagementScreen;
    private RoomManagementScreen roomManagementScreen;
    private GuestManagementScreen guestManagementScreen;
    private BookingManagementScreen bookingManagementScreen;
    private InvoicePaymentScreen invoicePaymentScreen;
}
