package com.hotelms.dao;

import com.hotelms.db.DatabaseManager;
import com.hotelms.model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public void addBooking(Booking booking) {
        String sql = "INSERT INTO bookings(room_id, guest_id, check_in_date, check_out_date, status) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, booking.getRoomId());
            pstmt.setInt(2, booking.getGuestId());
            pstmt.setString(3, booking.getCheckInDate());
            pstmt.setString(4, booking.getCheckOutDate());
            pstmt.setString(5, booking.getStatus());
            pstmt.executeUpdate();
            System.out.println("Booking added for room " + booking.getRoomId() + " and guest " + booking.getGuestId());
        } catch (SQLException e) {
            System.err.println("Error adding booking: " + e.getMessage());
        }
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT id, room_id, guest_id, check_in_date, check_out_date, status FROM bookings";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getInt("id"),
                        rs.getInt("room_id"),
                        rs.getInt("guest_id"),
                        rs.getString("check_in_date"),
                        rs.getString("check_out_date"),
                        rs.getString("status")
                );
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching bookings: " + e.getMessage());
        }
        return bookings;
    }

    public void updateBooking(Booking booking) {
        String sql = "UPDATE bookings SET room_id = ?, guest_id = ?, check_in_date = ?, check_out_date = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, booking.getRoomId());
            pstmt.setInt(2, booking.getGuestId());
            pstmt.setString(3, booking.getCheckInDate());
            pstmt.setString(4, booking.getCheckOutDate());
            pstmt.setString(5, booking.getStatus());
            pstmt.setInt(6, booking.getId());
            pstmt.executeUpdate();
            System.out.println("Booking updated for ID: " + booking.getId());
        } catch (SQLException e) {
            System.err.println("Error updating booking: " + e.getMessage());
        }
    }

    public void deleteBooking(int id) {
        String sql = "DELETE FROM bookings WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Booking deleted with ID: " + id);
        } catch (SQLException e) {
            System.err.println("Error deleting booking: " + e.getMessage());
        }
    }
}
