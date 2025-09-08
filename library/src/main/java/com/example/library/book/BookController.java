// book/BookController.java
package com.example.library.book;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.library.book.BookDtos.BookView;
import com.example.library.book.BookDtos.CreateBookRequest;

import jakarta.validation.Valid;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
@RestController @RequestMapping("/api/books")
public class BookController {
  private final BookService service;
  public BookController(BookService s){ this.service=s; }

  @GetMapping public List<BookView> list(@RequestParam(required=false) Boolean available){
    return service.list(available);
  }

  @PostMapping @PreAuthorize("hasRole('ADMIN')")
  public BookView create(@Valid @RequestBody CreateBookRequest in){
    return service.create(in);
  }

  @PostMapping("/{id}/borrow") @PreAuthorize("hasAnyRole('MEMBER','ADMIN')")
  public BookView borrow(@PathVariable Long id, Authentication auth){
    return service.borrow(id, auth.getName());
  }

//  @PostMapping("/{id}/return") @PreAuthorize("hasAnyRole('MEMBER','ADMIN')")
//  public BookView giveBack(@PathVariable Long id, Authentication auth){
//    return service._return(id, auth.getName());
//  }
  
  @PostMapping("/{id}/return")
  @PreAuthorize("hasAnyRole('MEMBER','ADMIN')")
  public BookView giveBack(@PathVariable Long id, Authentication auth) {
    boolean isAdmin = auth.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    return service.giveBack(id, auth.getName(), isAdmin);
  }

}
