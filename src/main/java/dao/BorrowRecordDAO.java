package com.example.library.dao;

import java.sql.*;
import org.springframework.stereotype.Repository;

@Repository
public class BorrowRecordDAO {
    public void insertRecord(Connection conn, int bookId, int userId) throws SQLException {
        String sql = "INSERT INTO borrow_records (book_id, user_id, borrow_date, status) VALUES (?, ?, NOW(), 'BORROWING')";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }
}