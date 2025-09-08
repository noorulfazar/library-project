“Library Management — Spring Boot + Angular + PostgreSQL (+ JWT & Roles)”

Backend: Spring Boot 3, Java 17, JPA/Hibernate, PostgreSQL, Spring Security (JWT), Bean Validation, Swagger (springdoc).

Frontend: Angular 19 (standalone), Tailwind CSS, Http Interceptor for JWT, simple in-app store via BehaviorSubject.

Roles: ADMIN (add books, view all), MEMBER (view available, borrow/return own).

Endpoints:

POST /api/auth/register, POST /api/auth/login

GET /api/books?available=true|false

POST /api/books (ADMIN)

POST /api/books/{id}/borrow (MEMBER)

POST /api/books/{id}/return (MEMBER/ADMIN)

Constraints: unique ISBN, cannot borrow if unavailable, cannot return if not borrowed.

Docs: Swagger UI at /swagger-ui.html.

Postman: included collection with sample flows (register → login → borrow → return).