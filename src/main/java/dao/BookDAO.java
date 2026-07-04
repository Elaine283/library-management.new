package com.example.library.dao;

import com.example.library.entity.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class BookDAO {

    public List<Book> findByKeyword(Connection conn, String keyword) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String searchStr = "%" + (keyword == null ? "" : keyword) + "%";
            ps.setString(1, searchStr);
            ps.setString(2, searchStr);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getInt("id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setStatus(rs.getString("status"));
                    books.add(book);
                }
            }
        }
        return books;
    }

    public Book findById(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM books WHERE id = ? FOR UPDATE";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getInt("id"));
                    book.setTitle(rs.getString("title"));
                    book.setStatus(rs.getString("status"));
                    return book;
                }
            }
        }
        return null;
    }

    public void updateStatus(Connection conn, int id, String status) throws SQLException {
        String sql = "UPDATE books SET status = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }
}