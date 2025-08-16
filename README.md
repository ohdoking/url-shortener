# URL Shortener Project

## Project Description
This project implements a URL shortening service that converts long URLs into short, easy-to-share links. 
The backend is built with Kotlin and Spring Boot, following Hexagonal Architecture principles.

#### Key features:
- Shorten long URLs with a unique 7-character identifier.
- Redirect shortened URLs to the original long URL.

## Tech Stack
- **Backend:** Kotlin, Spring Boot
- **Architecture:** Hexagonal Architecture
- **API Documentation:** Springdoc OpenAPI (Swagger UI)
- **Database:** PostgreSQL
- **Cache:** Inmemory cache(potentially redis)
- **Containerization:** Docker, Docker Compose

## How to Run Locally

### Build Docker Image
```bash
# Build the Docker image
docker build -t url-shortener:latest .
```

### Run with Docker Compose

Navigate to the local folder containing docker-compose.yml and execute:
```commandline
docker-compose up -d
```
This will start the backend service along with any dependencies (e.g., PostgreSQL).

### Run Project Directly
```
./gradlew build
./gradlew bootRun
```
The application will start on http://localhost:8080.


## API Documentation

The REST API is documented using Swagger UI:

- Access Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## How to Test

Unit and integration tests are included in the project:
```commandline
./gradlew test
```
Test reports will be generated under `build/reports/tests/`.

