# 🏋️ Gym Tracker

A full-stack gym tracking application built with **Spring Boot** that allows users to manage exercises, log workouts, track body metrics, report gym crowd levels, and analyze crowd patterns to find optimal gym time slots.

The application includes JWT-based authentication, user-specific data access, Docker containerization, GitHub Actions CI, and deployment to **Azure Container Apps** with **Azure Database for PostgreSQL** for persistent production data.

---

## ✨ Features

- 🔐 **Authentication & Authorization**
  - User registration and login
  - JWT-based authentication
  - Protected API endpoints
  - User-specific access to personal workout and body metric data
  - Role-based authorization for administrative crowd-report operations
  - Session persistence across page refreshes
  - Logout and invalid-session handling

- 🏋️ **Exercise Management**
  - Create, view, and delete exercises
  - Automatically delete associated workout logs when an exercise is deleted

- 💪 **Workout Tracking**
  - Log workout sessions
  - Track weight, reps, and sets
  - View exercise progress over time
  - Update and delete workout logs

- 📏 **Body Metrics**
  - Record weight, muscle mass, and body fat percentage
  - View body metric history
  - Update and delete measurements

- 👥 **Gym Crowd Tracking**
  - Report gym crowd levels
  - View crowd statistics by day
  - Update and delete crowd reports, subject to authorization

- 📊 **Crowd Analytics**
  - Analyze historical crowd data
  - Get optimal gym time-slot recommendations for a given day

- 📖 **API Documentation**
  - Interactive Swagger / OpenAPI documentation

- 🧪 **Automated Testing**
  - Automated tests for CRUD operations, validation, error handling, analytics, and entity relationships
  - H2 database configuration for lightweight CI testing

---

## 🛠️ Tech Stack

### Backend

- Java 21
- Spring Boot
- Spring Security
- Spring Web
- Spring Data JPA
- Hibernate
- Jakarta Bean Validation
- Maven
- Lombok
- JWT authentication
- Swagger / OpenAPI

### Frontend

- HTML
- CSS
- JavaScript
- Fetch API

The frontend is served as a static page by the Spring Boot application.

### Databases

| Environment | Database |
|---|---|
| Local Docker development | Oracle Database |
| Automated tests / CI | H2 |
| Production | Azure Database for PostgreSQL |

Database configuration is supplied through environment variables, allowing the application to use different databases in different environments.

### DevOps & Cloud

- Docker
- Docker Compose
- GitHub Actions
- GitHub Container Registry (GHCR)
- Azure Container Apps
- Azure Database for PostgreSQL

---

## 🏗️ Architecture

The application follows a layered Spring Boot architecture:

```text
                    REST API
                       │
                       ▼
                 ┌───────────┐
                 │ Controller│
                 └─────┬─────┘
                       │
                       ▼
                 ┌───────────┐
                 │  Service  │
                 └─────┬─────┘
                       │
                       ▼
                 ┌───────────┐
                 │ Repository│
                 └─────┬─────┘
                       │
                       ▼
                  ┌─────────┐
                  │Database │
                  └─────────┘
```

- **Controller** — exposes REST API endpoints and handles HTTP requests.
- **Service** — contains application and business logic.
- **Repository** — handles database access through Spring Data JPA.
- **Model** — represents persisted application entities.
- **DTO** — provides specialized request and response structures.
- **Security configuration** — defines public endpoints, authentication requirements, and authorization rules.
- **JWT components** — issue and validate authentication tokens.
- **Global exception handler** — provides consistent error responses for supported exceptions.
- **Static frontend** — provides the web interface served by Spring Boot.

---

# 🚀 Getting Started

## Prerequisites

Install the following for local development:

- Java 21
- Docker Desktop
- Git

The project includes the Maven Wrapper, so Maven does not need to be installed separately.

## 1. Clone the repository

```bash
git clone https://github.com/shiven-puri/gym-tracker-backend.git
cd gym-tracker-backend
```

## 2. Configure environment variables

Create a `.env` file using the example configuration.

**Windows:**

```powershell
copy .env.example .env
```

**Linux/macOS:**

```bash
cp .env.example .env
```

Configure the required database credentials and other required environment variables in `.env`.

> Never commit database passwords, JWT signing secrets, or other sensitive credentials to Git.

---

# 🐳 Running with Docker

The project includes a `docker-compose.yml` configuration for local development using Oracle Database.

Start the application and database:

```bash
docker compose up -d --build
```

Check the running containers:

```bash
docker compose ps
```

View application logs:

```bash
docker compose logs gym-tracker-app
```

Follow the application logs:

```bash
docker compose logs -f gym-tracker-app
```

Stop the containers:

```bash
docker compose down
```

The Oracle database uses a persistent Docker volume so database data can survive container stops and recreation.

**Note:** `docker compose down` does not normally delete named volumes. Avoid using `docker compose down -v` unless you intentionally want to remove the associated volumes and their stored data.

---

# 💻 Running Without Docker

The application can be run directly with Spring Boot if a compatible database is available and the required environment variables are configured.

**Linux/macOS:**

```bash
./mvnw spring-boot:run
```

**Windows:**

```powershell
.\mvnw.cmd spring-boot:run
```

The application runs at:

```text
http://localhost:8080
```

Open the root URL or `/index.html` to access the Gym Tracker interface.

---

# 🔐 Authentication & Authorization

The application uses JSON Web Tokens (JWTs) to authenticate users accessing protected API endpoints.

## Authentication endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new account |
| POST | `/api/auth/login` | Authenticate an existing user |

Registration and login accept JSON containing the user's email and password.

Example request:

```json
{
  "email": "user@example.com",
  "password": "your-password"
}
```

On successful authentication, the backend returns a response containing the JWT, token type, user ID, and email.

The frontend stores the token and attaches it to protected API requests using the `Authorization` header:

```http
Authorization: Bearer <JWT>
```

## Session management

The frontend implements the following session behavior:

- The user must log in before accessing the dashboard.
- The JWT and current-user information are stored in `sessionStorage`.
- The session is restored when the page is refreshed in the same tab.
- Logging out clears the stored session and returns the user to the login page.
- An HTTP `401 Unauthorized` response from requests using the shared API wrapper clears the session and prompts the user to log in again.
- The JWT is configured to expire after one hour.

The browser-side session restoration does not itself validate the JWT. The backend remains responsible for validating the token and enforcing access controls on protected endpoints.

## Authorization

The API distinguishes between public operations, authenticated operations, and administrative operations.

- Authentication endpoints are public.
- Exercise retrieval and selected crowd analytics endpoints are public.
- Personal body metric and workout operations require authentication and are subject to user-ownership checks.
- Creating crowd reports requires authentication.
- Updating and deleting crowd reports are restricted to authorized administrative users.

The backend enforces authorization; hiding or showing frontend controls is not a substitute for server-side access checks.

---

# 🔌 API Endpoints

All REST endpoints use the `/api` base path.

## Body Metrics

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/body-metrics` | Record body metrics |
| GET | `/api/body-metrics/history` | Retrieve body metric history |
| PUT | `/api/body-metrics/{id}` | Update body metrics |
| DELETE | `/api/body-metrics/{id}` | Delete body metrics |

These endpoints require authentication and enforce user-specific access.

## Exercises

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/exercises` | Retrieve exercises |
| POST | `/api/exercises` | Create an exercise |
| DELETE | `/api/exercises/{id}` | Delete an exercise and its associated workout logs |

Exercise retrieval is public. Write operations are subject to the backend's security configuration.

## Workout Logs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/workouts/log` | Record a workout |
| GET | `/api/workouts/progress/{exerciseId}` | Retrieve progress for an exercise |
| PUT | `/api/workouts/log/{id}` | Update a workout log |
| DELETE | `/api/workouts/log/{id}` | Delete a workout log |

Workout operations require authentication and enforce ownership restrictions.

## Crowd Tracking

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/crowd/report` | Report gym crowd level |
| GET | `/api/crowd/stats/{day}` | Retrieve crowd statistics |
| PUT | `/api/crowd/report/{id}` | Update a crowd report |
| DELETE | `/api/crowd/report/{id}` | Delete a crowd report |

Write operations are subject to authentication and authorization rules. Updating and deleting crowd reports require the appropriate administrative role.

## Crowd Analytics

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/analytics/optimal-slots/{day}` | Retrieve optimal gym time slots |

## API Documentation

When the application is running, access Swagger UI at:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI specification:

```text
http://localhost:8080/api-docs
```

---

# 🧪 Testing

The project includes automated tests for CRUD operations, validation, error handling, workout progress, crowd analytics, and entity relationships. H2 is used for automated tests so CI does not need to start an Oracle database container.

Run the test suite:

**Linux/macOS:**

```bash
./mvnw clean verify
```

**Windows:**

```powershell
.\mvnw.cmd clean verify
```

The existing test suite was originally developed around the application's earlier, unauthenticated API behavior. Authentication and authorization changes may require updating integration tests to provide valid JWTs and test unauthorized access, forbidden operations, and user-ownership boundaries.

Run the suite and review the results before treating the current code as fully tested. Do not assume all tests pass solely because the application starts successfully.

GitHub Actions is configured to run the build and tests on pushes and pull requests targeting `main`.

---

# 🐳 Docker Image

The application uses a multi-stage Docker build.

### Build stage

```text
eclipse-temurin:21-jdk-alpine
```

This stage compiles the Spring Boot application.

### Runtime stage

```text
eclipse-temurin:21-jre-alpine
```

This stage runs the compiled JAR with a smaller Java runtime image.

The runtime container runs the application as a non-root user.

Build the image manually:

```bash
docker build -t gym-tracker-backend .
```

Run the image:

```bash
docker run -p 8080:8080 gym-tracker-backend
```

For a complete local environment, use Docker Compose so the application can connect to its configured database.

Ensure required environment variables are supplied when running the image directly.

---

# 🔐 Configuration & Security

Database configuration is supplied through environment variables:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

The `.env.example` file provides a template for local configuration. The actual `.env` file should be excluded from version control.

Never commit database credentials, production secrets, or JWT signing keys to the repository. Use environment variables or an appropriate secret-management system.

## JWT storage decision

The frontend currently stores its JWT and user information in browser `sessionStorage`.

This choice was made for the current learning and demonstration project because it is straightforward to implement, allows the session to survive page refreshes, and normally limits persistence to the current browser tab's session.

However, `sessionStorage` is accessible to JavaScript running on the page. If an attacker exploits a cross-site scripting (XSS) vulnerability, malicious JavaScript may be able to read the stored JWT and potentially use it to impersonate the user until the token expires.

For this reason, this implementation should **not be considered the strongest production-ready session design**.

### Why not use an HttpOnly, Secure cookie yet?

A stronger production approach would be to place the session credential in a cookie configured with appropriate attributes, including:

- `HttpOnly` — prevents client-side JavaScript from directly reading the cookie.
- `Secure` — restricts cookie transmission to HTTPS.
- `SameSite` — helps reduce cross-site request risks when configured appropriately.

This requires coordinated backend and frontend changes, including cookie issuance, cookie handling, logout behavior, and appropriate CSRF protections where applicable.

We are deferring that additional implementation complexity for now so that the current project can focus on completing and demonstrating the JWT authentication flow. The trade-off is documented intentionally and should be revisited before production use.

**Important:** An `HttpOnly` cookie reduces the risk of token theft through JavaScript, but it does not eliminate XSS or every session-related risk. Production deployments still need appropriate input handling, output encoding, HTTPS, cookie configuration, CSRF defenses where applicable, and other security controls.

### Logout and token expiration

Logging out currently removes the token from the browser's `sessionStorage`. It does not revoke the JWT on the server.

A copied or stolen token may therefore remain usable until it expires, unless another server-side control rejects it. The configured token lifetime is one hour.

A production implementation may require additional measures, such as token revocation or refresh-token rotation, depending on its session requirements.

---

# ☁️ Cloud Deployment

The production deployment uses:

- **Azure Container Apps** — application hosting
- **Azure Database for PostgreSQL** — persistent database
- **GitHub Container Registry** — Docker image registry

Production database configuration is supplied separately from the local Oracle configuration through:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

Sensitive credentials are stored using Azure Container Apps secrets rather than being committed to the repository.

The documented Container App configuration is:

```text
CPU:              0.25 vCPU
Memory:           0.5 GiB
Minimum replicas: 0
Maximum replicas: 1
```

The application can scale down when it is not being used while remaining available through its public HTTPS endpoint, subject to the configured scaling and availability behavior.

---

# 🔄 CI/CD

GitHub Actions is used for continuous integration and Docker image publishing.

The workflow is configured to run on pushes and pull requests targeting `main`:

```text
GitHub Push / Pull Request
          │
          ▼
     GitHub Actions
          │
          ▼
      Java 21 Setup
          │
          ▼
     Maven Build/Test
          │
          ▼
      Docker Build
          │
          ▼
   GitHub Container Registry
```

For pushes to `main`, Docker images are tagged with:

```text
latest
<git-commit-sha>
```

The commit SHA tag provides a specific reference to a build and can support reproducible deployments and rollbacks.

Azure Container Apps deployment is performed separately from the GitHub Actions image-publishing workflow.

---

# 🗄️ Database Strategy

Different databases are used depending on the environment:

```text
Local Development
       │
       ▼
Oracle Database


CI / Automated Tests
       │
       ▼
      H2


Production
       │
       ▼
Azure PostgreSQL
```

Using H2 for CI keeps automated testing lightweight and avoids requiring an Oracle database container during GitHub Actions runs.

Production data is stored in Azure PostgreSQL and persists independently of Container App revisions and restarts.

---

# 🔮 Future Enhancements

Potential improvements include:

- Updating integration tests for JWT authentication and authorization
- More comprehensive authentication and session-security testing
- Migrating to an `HttpOnly`, `Secure` cookie-based session design for production
- Refresh-token rotation or other session-lifecycle improvements, if required
- Pagination and filtering
- Automated deployment from GitHub Actions to Azure
- Improved analytics and visualizations
- More advanced workout progress tracking
- Database migration management with Flyway
- Expanded API DTOs as the application grows

---

# 👨‍💻 Author

**Shiven Puri**

GitHub:  
https://github.com/shiven-puri
