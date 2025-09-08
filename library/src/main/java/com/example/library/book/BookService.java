// src/main/java/com/example/library/book/BookService.java
package com.example.library.book;

import com.example.library.book.BookDtos.BookView;
import com.example.library.book.BookDtos.CreateBookRequest;
import com.example.library.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@Service
public class BookService {

    private final BookRepository repo;
    private final UserRepository users;

    public BookService(BookRepository r, UserRepository u) {
        this.repo = r; this.users = u;
    }

    public BookView create(CreateBookRequest in) {
        Book b = new Book();
        b.setTitle(in.title());
        b.setAuthor(in.author());
        b.setIsbn(in.isbn());
        b.setAvailable(true);
        b.setBorrowedBy(null);
        b = repo.save(b);
        return view(b);
    }

    public List<BookView> list(Boolean available) {
        List<Book> books;
        if (available == null) {
            books = repo.findAll();
        } else if (available) {
            books = repo.findByAvailableTrue();
        } else {
            books = repo.findByAvailableFalse();
        }
        return books.stream().map(BookService::view).toList();
    }

    @Transactional
    public BookView borrow(Long id, String email) {
        Book b = repo.findById(id).orElseThrow();
        if (!b.isAvailable()) throw new IllegalStateException("Book not available");
        var u = users.findByEmail(email).orElseThrow();
        b.setAvailable(false);
        b.setBorrowedBy(u);
        return view(b);
    }

    @Transactional
    public BookView _return(Long id, String email) {
        Book b = repo.findById(id).orElseThrow();
        if (b.isAvailable()) throw new IllegalStateException("Book is not borrowed");
        // controller should already check that the caller is the borrower or an admin
        b.setAvailable(true);
        b.setBorrowedBy(null);
        return view(b);
    }

//    static BookView view(Book b) {
//        return new BookView(
//                b.getId(),
//                b.getTitle(),
//                b.getAuthor(),
//                b.getIsbn(),
//                b.isAvailable()
//        );
//    }
    
    static BookView view(Book b){
    	  String borrower = (b.getBorrowedBy() == null) ? null : b.getBorrowedBy().getEmail();
    	  return new BookView(b.getId(), b.getTitle(), b.getAuthor(), b.getIsbn(), b.isAvailable(), borrower);
    	}

    
    @Transactional
    public BookView giveBack(Long id, String email, boolean isAdmin){
      var b = repo.findById(id).orElseThrow();
      if (b.isAvailable()) throw new IllegalStateException("Book is not borrowed");
      if (!isAdmin) {
        if (b.getBorrowedBy() == null || !b.getBorrowedBy().getEmail().equals(email)) {
        	throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only return books you borrowed");
//          throw new AccessDeniedException("You can only return books you borrowed");
        }
      }
      b.setAvailable(true);
      b.setBorrowedBy(null);
      return view(b);
    }
}








