# Eagle Bank API

A comprehensive Spring Boot REST API for a banking application that provides user authentication, account management, and transaction processing capabilities.

## 📑 Table of Contents

- [🏦 Overview](#-overview)
- [🚀 Features](#-features)
  - [Authentication & Authorization](#authentication--authorization)
  - [User Management](#user-management)
  - [Account Management](#account-management)
  - [Transaction Processing](#transaction-processing)
- [🛠️ Technology Stack](#️-technology-stack)
- [📋 Prerequisites](#-prerequisites)
- [🔧 Installation & Setup](#-installation--setup)
  - [Cloning the Repo](#cloning-the-repo)
  - [Database Configuration](#database-configuration)
  - [Environment Variables Setup](#environment-variables-setup)
  - [Build and Run](#build-and-run)
  - [Database Access](#database-access)
- [📚 API Documentation](#-api-documentation)
  - [Base URL](#base-url)
  - [Authentication](#authentication)
  - [Interactive API Documentation](#interactive-api-documentation)
- [🧪 Testing](#-testing)
  - [Manual Testing](#manual-testing)
- [🔧 Configuration](#-configuration)
  - [CORS Configuration](#cors-configuration)
- [🏗️ Project Structure](#️-project-structure)
- [🔒 Security Features](#-security-features)

---

## 🏦 Overview

Eagle Bank API is a secure banking application built with Spring Boot that allows users to:

- Register and authenticate with JWT tokens
- Manage their bank accounts (create, read, update, delete)
- Perform transactions (deposits and withdrawals)
- View transaction history
- Secure access to only their own resources

## 🚀 Features

### Authentication & Authorization

- **JWT-based authentication** with secure token generation and validation
- **User registration** with email and password validation
- **User login** with token-based session management
- **Role-based access control** ensuring users can only access their own resources

### User Management

- **User registration** with email validation
- **User profile management** (view, update, delete)
- **Secure password handling** with BCrypt encryption
- **Conflict handling** for users with existing bank accounts

### Account Management

- **Bank account creation** with unique account numbers
- **Account details retrieval** with ownership verification
- **Account updates** with partial update support
- **Account deletion** with conflict checking
- **Multiple account support** per user

### Transaction Processing

- **Deposit transactions** with automatic balance updates
- **Withdrawal transactions** with insufficient funds validation
- **Transaction history** with detailed records
- **Real-time balance updates** after transactions
- **Transaction type validation** (deposit/withdrawal only)

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 3.5.4**
- **Spring Security** with JWT authentication
- **Spring Data JPA** with Hibernate
- **H2 Database** (in-memory for development)
- **Lombok** for boilerplate reduction
- **MapStruct** for object mapping
- **JJWT** for JWT token handling
- **Spring Validation** for request validation

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6.3+
- IDE (IntelliJ IDEA, Eclipse, or VS Code recommended)

## 🔧 Installation & Setup

### Cloning the Repo

### 1. Clone the Repository

1. Open your terminal or command prompt.

2. Clone the repository using Git:

   ```bash
   git clone https://github.com/arsy786/eagle-bank-backend.git
   ```

3. Navigate to the cloned repository's root directory

   ```bash
   cd eagle-bank-backend
   ```

### 2. Database Configuration

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

### 3. Environment Variables Setup

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

### 4. Build and Run

1. Build the project:

   ```bash
   mvn clean install
   ```

2. Run the Application:

   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

### 5. Database Access

#### H2 Console (Development)

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:eaglebankdb`
- Username: `sa`
- Password: (leave empty)

#### PostgreSQL (Production)

- Ensure PostgreSQL is running
- Create database: `createdb eaglebank`
- Update `.env` with your PostgreSQL credentials

## 📚 API Documentation

### Base URL

```
http://localhost:8080
```

### Authentication

All protected endpoints require a JWT token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

### Interactive API Documentation

Once the server is running, you can access the interactive API documentation at:

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

These provide comprehensive, interactive documentation for all endpoints including:

- Request/response schemas
- Authentication requirements
- Example requests and responses
- Try-it-out functionality

## 🧪 Testing

### Manual Testing

You can test the API using tools like:

- **Postman**
- **cURL**
- **Swagger UI** (built-in testing interface)

## 🔧 Configuration

### CORS Configuration

The API is configured to accept requests from:

- `http://localhost:3000` (React development server)

## 🏗️ Project Structure

```
src/main/java/dev/arsalaan/eagle_bank/
├── config/
│   └── SecurityConfig.java          # Spring Security & CORS configuration
├── controller/
│   ├── UserController.java          # User management endpoints
│   ├── AccountController.java       # Account management endpoints
│   └── TransactionController.java   # Transaction endpoints
├── dto/
│   ├── RegisterRequest.java         # User registration DTO
│   ├── LoginRequest.java           # User login DTO
│   ├── UserRequest.java            # User update DTO
│   ├── UserResponse.java           # User response DTO
│   ├── AccountRequest.java         # Account creation/update DTO
│   ├── AccountResponse.java        # Account response DTO
│   └── TransactionRequest.java     # Transaction creation DTO
├── enums/
│   └── TransactionType.java        # Transaction type enum
├── exception/
│   ├── ApiException.java           # Error response model
│   ├── ApiRequestException.java    # Custom exception
│   └── ApiExceptionHandler.java    # Global exception handler
├── model/
│   ├── User.java                   # User entity
│   ├── Account.java                # Account entity
│   └── Transaction.java            # Transaction entity
├── repository/
│   ├── UserRepository.java         # User data access
│   ├── AccountRepository.java      # Account data access
│   └── TransactionRepository.java  # Transaction data access
├── security/
│   ├── JwtTokenUtil.java          # JWT token utilities
│   ├── JwtRequestFilter.java      # JWT authentication filter
│   └── JwtUserDetailsService.java # User details service
└── service/
    ├── UserService.java            # User business logic
    ├── AccountService.java         # Account business logic
    └── TransactionService.java     # Transaction business logic
```

## 🔒 Security Features

- **JWT Token Authentication**: Secure token-based authentication
- **Password Encryption**: BCrypt password hashing
- **Authorization**: Users can only access their own resources
- **Input Validation**: Comprehensive request validation
- **Error Handling**: Secure error responses without sensitive data exposure
- **CORS Support**: Configured for frontend integration

**Note**: This is a development version using H2 in-memory database. For production deployment, configure a persistent database like PostgreSQL or MySQL.
