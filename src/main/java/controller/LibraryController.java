package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.service.LibraryService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books") // 定義共用前綴
@CrossOrigin(origins = "*")   // 解決跨域問題
public class LibraryController {

    private final LibraryService libraryService;

    // 使用建構子注入 Service
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    // 對應前端: GET /api/books 或 /api/books?keyword=xxx
    @GetMapping
    public ResponseEntity<List<Book>> getBooks(@RequestParam(required = false) String keyword) {
        try {
            List<Book> books = libraryService.searchBooks(keyword);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 對應前端: POST /api/books/borrow?bookId=1&userId=2
    @PostMapping("/borrow")
    public ResponseEntity<Map<String, String>> borrowBook(@RequestParam int bookId, @RequestParam int userId) {
        try {
            libraryService.borrowBook(bookId, userId);
            return ResponseEntity.ok(Collections.singletonMap("message", "借閱成功"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}