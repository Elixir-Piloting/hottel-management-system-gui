package com.hotelms.dao;

import com.hotelms.db.DatabaseManager;
import com.hotelms.model.Guest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {

    public void addGuest(Guest guest) {
        String sql = "INSERT INTO guests(first_name, last_name, phone, email) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, guest.getFirstName());
            pstmt.setString(2, guest.getLastName());
            pstmt.setString(3, guest.getPhone());
            pstmt.setString(4, guest.getEmail());
            pstmt.executeUpdate();
            System.out.println("Guest added: " + guest.getFirstName() + " " + guest.getLastName());
        } catch (SQLException e) {
            System.err.println("Error adding guest: " + e.getMessage());
        }
    }

    public List<Guest> getAllGuests() {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT id, first_name, last_name, phone, email FROM guests";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Guest guest = new Guest(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("phone"),
                        rs.getString("email")
                );
                guests.add(guest);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching guests: " + e.getMessage());
        }
        return guests;
    }

    public void updateGuest(Guest guest) {
        String sql = "UPDATE guests SET first_name = ?, last_name = ?, phone = ?, email = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, guest.getFirstName());
            pstmt.setString(2, guest.getLastName());
            pstmt.setString(3, guest.getPhone());
            pstmt.setString(4, guest.getEmail());
            pstmt.setInt(5, guest.getId());
            pstmt.executeUpdate();
            System.out.println("Guest updated: " + guest.getFirstName() + " " + guest.getLastName());
        } catch (SQLException e) {
            System.err.println("Error updating guest: " + e.getMessage());
        }
    }

    public void deleteGuest(int id) {
        String sql = "DELETE FROM guests WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Guest deleted with ID: " + id);
        } catch (SQLException e) {
            System.err.println("Error deleting guest: " + e.getMessage());
        }
    }
}
