package com.example.library.service;

import com.example.library.config.DatabaseConfig;
import com.example.library.dao.BookDAO;
import com.example.library.dao.BorrowRecordDAO;
import com.example.library.entity.Book;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LibraryService {

    private final DatabaseConfig dbConfig;
    private final BookDAO bookDAO;
    private final BorrowRecordDAO borrowRecordDAO;

    public LibraryService(DatabaseConfig dbConfig, BookDAO bookDAO, BorrowRecordDAO borrowRecordDAO) {
        this.dbConfig = dbConfig;
        this.bookDAO = bookDAO;
        this.borrowRecordDAO = borrowRecordDAO;
    }

    public List<Book> searchBooks(String keyword) throws SQLException {
        try (Connection conn = dbConfig.getConnection()) {
            return bookDAO.findByKeyword(conn, keyword);
        }
    }

    public void borrowBook(int bookId, int userId) throws SQLException {
        Connection conn = null;
        try {
            conn = dbConfig.getConnection();
            conn.setAutoCommit(false); // 關閉自動提交，開啟手動交易控制

            // 1. 檢查庫存並鎖定資料
            Book book = bookDAO.findById(conn, bookId);
            if (book == null) throw new IllegalArgumentException("找不到該書籍");
            if (!"AVAILABLE".equals(book.getStatus())) throw new IllegalStateException("該書已被借出或無法借閱");

            // 2. 插入借閱紀錄
            borrowRecordDAO.insertRecord(conn, bookId, userId);

            // 3. 修改書籍狀態
            bookDAO.updateStatus(conn, bookId, "BORROWED");

            conn.commit(); // 全部成功，手動提交交易
        } catch (Exception e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            throw new SQLException("借閱失敗，交易已安全回滾: " + e.getMessage(), e);
        } finally {
            if (conn != null) { try { conn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
    }
}