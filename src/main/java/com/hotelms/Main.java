package com.hotelms;

import com.hotelms.db.DatabaseManager;
import com.hotelms.gui.LoginScreen;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Initialize the database
        DatabaseManager.initializeDatabase();

        // Launch the Login Screen on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.setVisible(true);
        });
    }
}
