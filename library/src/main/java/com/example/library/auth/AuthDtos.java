package com.example.library.auth;

public class AuthDtos {
	
	public record AuthRequest(String email, String password) {}
	public record AuthResponse(String token, String role) {}

}
