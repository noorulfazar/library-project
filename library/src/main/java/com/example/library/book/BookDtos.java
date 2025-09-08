package com.example.library.book;

import jakarta.validation.constraints.NotBlank;

public class BookDtos {
	
	public record CreateBookRequest(@NotBlank String title, @NotBlank String author, @NotBlank String isbn) {}
//	public record BookView(Long id, String title, String author, String isbn, boolean available) {}
	public record BookView(
			  Long id, String title, String author, String isbn, boolean available, String borrowedByEmail
			) {}


}
