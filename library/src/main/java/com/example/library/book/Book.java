// src/main/java/com/example/library/book/Book.java
package com.example.library.book;

import com.example.library.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "books", uniqueConstraints = @UniqueConstraint(columnNames = "isbn"))
public class Book {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @NotBlank
    private String isbn;

    @Column(nullable = false)
    private boolean available = true;

    // who borrowed (null if available)
    @ManyToOne(fetch = FetchType.LAZY)
    private User borrowedBy;

    public Book() {}

    public Book(Long id, String title, String author, String isbn, boolean available, User borrowedBy) {
        this.id = id; this.title = title; this.author = author; this.isbn = isbn;
        this.available = available; this.borrowedBy = borrowedBy;
    }

    // ---- getters / setters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public boolean isAvailable() { return available; }
    public User getBorrowedBy() { return borrowedBy; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setBorrowedBy(User borrowedBy) { this.borrowedBy = borrowedBy; }
}
