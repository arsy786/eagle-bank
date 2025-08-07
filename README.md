# Eagle Bank Backend

A comprehensive Spring Boot REST API for a banking application that provides account management, transaction tracking, and secure authentication capabilities.

> **Frontend Repository**: The backend API for this application is available at [eagle-bank-frontend](https://github.com/arsy786/eagle-bank-frontend)

## Features

Eagle Bank API is a secure banking application built with Spring Boot that allows users to:

- Register and authenticate with JWT tokens
- Manage their bank accounts (create, read, update, delete)
- Perform transactions (deposits and withdrawals)
- View transaction history
- Secure access to only their own resources

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6.3+

### Cloning the Repo

1. Open your terminal or command prompt.

2. Clone the repository using Git:

   ```bash
   git clone https://github.com/arsy786/eagle-bank-backend.git
   ```

3. Navigate to the cloned repository's root directory

   ```bash
   cd eagle-bank-backend
   ```

### Database Configuration

The application supports both **H2 in-memory database** (for development) and **PostgreSQL** (for production). Configure your choice in `src/main/resources/application.properties`:

#### Option A: H2 In-Memory Database (Development)

Uncomment the H2 configuration and comment out PostgreSQL:

```properties
# H2 Database Configuration (for development)
spring.datasource.url=jdbc:h2:mem:eaglebankdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# PostgreSQL Configuration (comment out for H2)
# spring.datasource.url=${PG_URL}
# spring.datasource.username=${PG_USERNAME}
# spring.datasource.password=${PG_PASSWORD}
# spring.datasource.driverClassName=org.postgresql.Driver
# spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

#### Option B: PostgreSQL Database (Production)

Comment out H2 configuration and uncomment PostgreSQL:

```properties
# H2 Database Configuration (comment out for PostgreSQL)
# spring.datasource.url=jdbc:h2:mem:eaglebankdb
# spring.datasource.driver-class-name=org.h2.Driver
# spring.datasource.username=sa
# spring.datasource.password=
# spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
# spring.h2.console.enabled=true
# spring.h2.console.path=/h2-console

# PostgreSQL Configuration (for production)
spring.datasource.url=${PG_URL}
spring.datasource.username=${PG_USERNAME}
spring.datasource.password=${PG_PASSWORD}
spring.datasource.driverClassName=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

### Environment Variables Setup

Create a `.env` file in the root directory with your configuration:

```env
# JWT Configuration
JWT_SECRET=your-super-secret-jwt-key-here-make-it-long-and-secure-512bits64chars
JWT_EXPIRATION_IN_MS=3600000

# PostgreSQL Configuration (only needed if using PostgreSQL)
PG_URL=jdbc:postgresql://localhost:5432/eaglebank
PG_USERNAME=your_postgres_username
PG_PASSWORD=your_postgres_password
```

### Build and Run

1. Build the project:

   ```bash
   mvn clean install
   ```

2. Run the Application:

   ```bash
   mvn spring-boot:run
   ```

The backend should now be running on `http://localhost:8080`.

### Accessing the Application

After starting both the backend and frontend servers, you can access the web application by navigating to `http://localhost:3000` in your web browser. Ensure both servers are running concurrently to allow the frontend to communicate with the backend effectively.

> **Frontend Repository**: The backend API for this application is available at [eagle-bank-frontend](https://github.com/arsy786/eagle-bank-frontend)

## API Documentation

### Base URL

```
http://localhost:8080
```

### Auth Header

All protected endpoints require a JWT token:

```
Authorization: Bearer <token>
```

### Swagger

Once the server is running, you can access the API documentation at:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Testing

Use any of the following tools:

- Swagger UI (built-in)
- Postman
- cURL

## CORS

The API is configured to accept requests from:

- `http://localhost:3000` (React development server)

## 🏗️ Project Structure

```
src/main/java/dev/arsalaan/eagle_bank/
├── config/
│   └── SecurityConfig.java          # Spring Security & CORS configuration
│   └── SwaggerConfig.java           # Swagger OpenAPI configuration
├── controller/
│   ├── UserController.java          # User management endpoints
│   ├── AccountController.java       # Account management endpoints
│   └── TransactionController.java   # Transaction endpoints
├── dto/
│   ├── RegisterRequest.java         # User creation DTO
│   ├── LoginRequest.java            # User login request DTO
│   ├── JwtResponse.java             # User login response DTO
│   ├── UserRequest.java             # User update request DTO
│   ├── UserResponse.java            # User response DTO
│   ├── AccountRequest.java          # Account creation/update DTO
│   ├── AccountResponse.java         # Account response DTO
│   └── TransactionRequest.java      # Transaction creation DTO
│   └── TransactionResponse.java     # Transaction response DTO
├── enums/
│   └── TransactionType.java         # Transaction type enum
├── exception/
│   ├── ApiException.java            # Error response model
│   ├── ApiRequestException.java     # Custom exception
│   └── ApiExceptionHandler.java     # Global exception handler
├── model/
│   ├── User.java                    # User entity
│   ├── Account.java                 # Account entity
│   └── Transaction.java             # Transaction entity
├── repository/
│   ├── UserRepository.java          # User data access
│   ├── AccountRepository.java       # Account data access
│   └── TransactionRepository.java   # Transaction data access
├── security/
│   ├── JwtTokenUtil.java            # JWT token utilities
│   ├── JwtRequestFilter.java        # JWT authentication filter
│   └── JwtUserDetailsService.java   # User details service
└── service/
    ├── UserService.java             # User business logic
    ├── AccountService.java          # Account business logic
    └── TransactionService.java      # Transaction business logic
```
