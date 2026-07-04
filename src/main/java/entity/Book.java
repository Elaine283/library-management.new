package com.example.library.entity;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private String status;

    // Getter and Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) throw new IllegalArgumentException("書名不能為空");
        this.title = title;
    }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}