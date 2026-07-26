# JWT Authentication API

A complete Spring Boot REST API demonstrating secure user authentication and authorization using JSON Web Tokens (JWT).

## Tech Stack
- Java 21
- Spring Boot 3.3.0
- Spring Security
- Spring Data JPA
- MySQL
- JJWT

## Prerequisites
- Java 21
- Maven
- Docker or MySQL installed locally

## Installation & Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd jwt-auth-api
   ```

2. **Start MySQL via Docker**
   You can quickly start a MySQL instance using Docker:
   ```bash
   docker run --name jwt-mysql -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_DATABASE=jwt_auth_db -p 3306:3306 -d mysql:8
   ```

3. **Set Environment Variables**
   Configure the following environment variables (or refer to the `.env.example` file):
   - `DB_URL`: `jdbc:mysql://localhost:3306/jwt_auth_db`
   - `DB_USERNAME`: `root`
   - `DB_PASSWORD`: `secret`
   - `JWT_SECRET`: `your_super_secret_key_that_is_at_least_256_bits_long`
   - `JWT_EXPIRATION`: `3600000` (1 hour)

4. **Run the Application**
   Use Maven to run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

## API Endpoints

| Method | Path | Description | Auth Required | Example Request Body | Example Response |
|---|---|---|---|---|---|
| POST | `/api/auth/register` | Register a new user | No | `{"username":"john", "password":"password123"}` | `{"message":"User registered successfully"}` |
| POST | `/api/auth/login` | Login and get JWT | No | `{"username":"john", "password":"password123"}` | `{"token":"eyJhb...", "type":"Bearer"}` |
| POST | `/api/auth/refresh` | Refresh JWT | Yes | *N/A* (Requires valid token in Header) | `{"token":"eyJhb...", "type":"Bearer"}` |
| GET | `/api/user/profile` | Get user profile | Yes | *N/A* | `{"username":"john", "role":"USER"}` |
| GET | `/api/admin/dashboard`| Get admin dashboard | Yes (ADMIN) | *N/A* | `{"message":"Welcome to the admin dashboard!"}` |

## API Documentation (Swagger UI)

Interactive API documentation is automatically generated. Once the application is running, you can access the Swagger UI at:
- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Understanding 401 vs 403 Errors

This project specifically handles and distinguishes between `401 Unauthorized` and `403 Forbidden` responses:
- **401 Unauthorized:** Returned when the user provides no valid authentication credentials. This occurs if the JWT token is missing, malformed, expired, or invalid.
- **403 Forbidden:** Returned when the authentication is valid (the JWT token is accepted), but the authenticated user lacks the required role or permission to access the resource (e.g., a `USER` trying to access an `ADMIN` endpoint).
