package com.hotelms.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InvoicePaymentScreen extends JPanel implements ActionListener {
    private MainDashboard mainDashboard;
    private JTextArea invoiceDetailsArea;
    private JButton generateInvoiceButton;
    private JButton processPaymentButton;
    private JButton backButton;

    public InvoicePaymentScreen(MainDashboard mainDashboard) {
        this.mainDashboard = mainDashboard;
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Invoice and Payment Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Invoice Details Area
        invoiceDetailsArea = new JTextArea(15, 40);
        invoiceDetailsArea.setEditable(false);
        invoiceDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(invoiceDetailsArea);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        generateInvoiceButton = new JButton("Generate Invoice");
        generateInvoiceButton.addActionListener(this);
        processPaymentButton = new JButton("Process Payment");
        processPaymentButton.addActionListener(this);
        backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(this);

        buttonPanel.add(generateInvoiceButton);
        buttonPanel.add(processPaymentButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Mock Invoice Data
        generateMockInvoice();
    }

    private void generateMockInvoice() {
        StringBuilder invoice = new StringBuilder();
        invoice.append("---------------------------------------------------\n");
        invoice.append("                 HOTEL INVOICE                     \n");
        invoice.append("---------------------------------------------------\n");
        invoice.append(String.format("Invoice ID: %s\n", "INV" + System.currentTimeMillis()));
        invoice.append(String.format("Date: %s\n", java.time.LocalDate.now()));
        invoice.append("---------------------------------------------------\n");
        invoice.append("Guest Name: John Doe\n");
        invoice.append("Room Number: 101 (Deluxe)\n");
        invoice.append("Check-in: 2025-08-01\n");
        invoice.append("Check-out: 2025-08-05\n");
        invoice.append("---------------------------------------------------\n");
        invoice.append(String.format("Room Rate (4 nights @ $100/night): $%.2f\n", 4 * 100.00));
        invoice.append(String.format("Service Charge (10%%): $%.2f\n", 400.00 * 0.10));
        invoice.append(String.format("Tax (5%%): $%.2f\n", 400.00 * 0.05));
        invoice.append("---------------------------------------------------\n");
        invoice.append(String.format("Total Amount Due: $%.2f\n", 400.00 + 40.00 + 20.00));
        invoice.append("---------------------------------------------------\n");
        invoice.append("Thank you for your stay!\n");
        invoice.append("---------------------------------------------------\n");
        invoiceDetailsArea.setText(invoice.toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == generateInvoiceButton) {
            generateMockInvoice();
            JOptionPane.showMessageDialog(this, "Invoice Generated! (Mock Data)");
        } else if (e.getSource() == processPaymentButton) {
            JOptionPane.showMessageDialog(this, "Payment Processed! (Mock Action)");
        } else if (e.getSource() == backButton) {
            mainDashboard.showCard("MainDashboard");
        }
    }
}
