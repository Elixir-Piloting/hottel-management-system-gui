package com.hotelms.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:hotel_management.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");

                createTables(conn);
                insertDefaultAdmin(conn);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void createTables(Connection conn) throws SQLException {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                                + "username TEXT NOT NULL UNIQUE,"
                                + "password TEXT NOT NULL,"
                                + "role TEXT NOT NULL"
                                + ");";

        String createRoomsTable = "CREATE TABLE IF NOT EXISTS rooms ("
                                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                                + "room_number TEXT NOT NULL UNIQUE,"
                                + "type TEXT NOT NULL,"
                                + "price REAL NOT NULL,"
                                + "status TEXT NOT NULL"
                                + ");";

        String createGuestsTable = "CREATE TABLE IF NOT EXISTS guests ("
                                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                                + "first_name TEXT NOT NULL,"
                                + "last_name TEXT NOT NULL,"
                                + "phone TEXT,"
                                + "email TEXT"
                                + ");";

        String createBookingsTable = "CREATE TABLE IF NOT EXISTS bookings ("
                                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                                + "room_id INTEGER NOT NULL,"
                                + "guest_id INTEGER NOT NULL,"
                                + "check_in_date TEXT NOT NULL,"
                                + "check_out_date TEXT NOT NULL,"
                                + "status TEXT NOT NULL,"
                                + "stay_days INTEGER NOT NULL,"
                                + "FOREIGN KEY (room_id) REFERENCES rooms(id),"
                                + "FOREIGN KEY (guest_id) REFERENCES guests(id)"
                                + ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            stmt.execute(createRoomsTable);
            stmt.execute(createGuestsTable);
            stmt.execute(createBookingsTable);
            System.out.println("Tables created successfully.");
        }

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("ALTER TABLE bookings ADD COLUMN stay_days INTEGER NOT NULL DEFAULT 0");
            System.out.println("stay_days column added to bookings table.");
        } catch (SQLException e) {
            // Ignore if the column already exists
            if (!e.getMessage().contains("duplicate column name")) {
                throw e;
            }
        }
    }

    private static void insertDefaultAdmin(Connection conn) throws SQLException {
        String checkAdminExists = "SELECT COUNT(*) FROM users WHERE username = 'admin'";
        try (Statement stmt = conn.createStatement()) {
            if (stmt.executeQuery(checkAdminExists).getInt(1) == 0) {
                String insertAdmin = "INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'admin')";
                stmt.execute(insertAdmin);
                System.out.println("Default admin user created.");
            } else {
                System.out.println("Admin user already exists.");
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
