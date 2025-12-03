# Bidly

A Spring Boot web application for online bidding/auction platform.

## Tech Stack

- **Java 21**
- **Spring Boot 4.0.0**
- **PostgreSQL 15**
- **Thymeleaf** (templating engine)
- **Spring Data JPA** (data persistence)
- **Flyway** (database migrations)
- **Docker & Docker Compose**

## Prerequisites

- Java 21 or higher
- Maven 3.6+
- Docker & Docker Compose (for containerized deployment)

## Features

- User registration and authentication
- Item listing and management
- Bidding system
- User profile management
- Contact form
- File upload support (max 5MB)

## Project Structure

```
src/
├── main/
│   ├── java/com/example/bidly/
│   │   ├── controllers/     # REST controllers
│   │   ├── entity/          # JPA entities
│   │   ├── repositories/    # Data repositories
│   │   └── services/        # Business logic
│   └── resources/
│       ├── application.properties
│       ├── db/migration/    # Flyway migrations
│       ├── static/css/      # Stylesheets
│       └── templates/       # Thymeleaf templates
```

## Database Migrations

Flyway automatically runs migrations on application startup. Migration files are located in `src/main/resources/db/migration/`.

## Quick Start

1. **Build the application:**
   ```bash
   mvn clean package -DskipTests
   ```

2. **Start the application with Docker Compose:**
   ```bash
   docker-compose up -d --build
   ```

3. **Access the application:**
    - Open your browser and navigate to: `http://localhost:8080`

4. **Stop the application:**
   ```bash
   docker-compose down
   ```
