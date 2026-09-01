# 🏋️ Gym Tracker

A full-stack gym tracking application built with **Spring Boot** that allows users to manage exercises, log workouts, track body metrics, report gym crowd levels, and analyze crowd patterns to find optimal gym time slots.

The application is containerized with Docker, tested through GitHub Actions, and deployed to **Azure Container Apps** with **Azure Database for PostgreSQL** for persistent production data.

---

## ✨ Features

- 🏋️ **Exercise Management**
    - Create exercises
    - View all exercises
    - Delete exercises

- 💪 **Workout Tracking**
    - Log workout sessions
    - Track weight, reps, and sets
    - View exercise progress over time
    - Update and delete workout logs

- 📏 **Body Metrics**
    - Record weight
    - Record muscle mass
    - Record body fat percentage
    - View body metric history
    - Update and delete measurements

- 👥 **Gym Crowd Tracking**
    - Report gym crowd levels
    - View crowd statistics by day
    - Update and delete crowd reports

- 📊 **Crowd Analytics**
    - Analyze historical crowd data
    - Get optimal gym time-slot recommendations for a given day

- 📖 **API Documentation**
    - Interactive Swagger / OpenAPI documentation

---

## 🛠️ Tech Stack

### Backend

- **Java 21**
- **Spring Boot**
- **Spring Web**
- **Spring Data JPA**
- **Hibernate**
- **Maven**
- **Lombok**
- **Swagger / OpenAPI**

### Databases

| Environment | Database |
|---|---|
| Local Docker development | Oracle Database |
| Automated tests / CI | H2 |
| Production | Azure Database for PostgreSQL |

The application uses environment variables for database configuration, allowing the same application to run against different databases in different environments.

### DevOps & Cloud

- **Docker**
- **Docker Compose**
- **GitHub Actions**
- **GitHub Container Registry (GHCR)**
- **Azure Container Apps**
- **Azure Database for PostgreSQL**

---

## 🏗️ Architecture

The application follows a layered Spring Boot architecture:

- **Controller** — exposes REST API endpoints
- **Repository** — handles database access through Spring Data JPA
- **Model** — represents application entities
- **DTO** — transfers data for analytics responses
- **Static Frontend** — provides the web UI served by Spring Boot

---

# 🚀 Getting Started

## Prerequisites

For local development, install:

- Java 21
- Docker Desktop
- Git

The project includes the Maven Wrapper, so Maven does not need to be installed separately.

---

## 1. Clone the repository

```bash
git clone https://github.com/shiven-puri/gym-tracker-backend.git
cd gym-tracker-backend
```

---

## 2. Configure environment variables

Create a `.env` file based on the provided example:

```bash
copy .env.example .env
```

On Linux/macOS:

```bash
cp .env.example .env
```

Configure the required database credentials in `.env`.

> `.env` is intentionally excluded from version control. Never commit database passwords or other secrets.

---

# 🐳 Running with Docker

The project includes a `docker-compose.yml` configuration for local development.

The local Docker setup uses **Oracle Database** as the application database.

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

Follow the logs:

```bash
docker compose logs -f gym-tracker-app
```

Stop the containers:

```bash
docker compose down
```

The Oracle database uses a persistent Docker volume so that application data is retained when the containers are stopped or recreated.

---

# 💻 Running Without Docker

The application can also be run directly with Spring Boot if an appropriate database is available.

```bash
./mvnw spring-boot:run
```

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

---

# 🧪 Testing

The project uses **H2** for automated tests so that CI does not need to start the heavier Oracle database container.

Run the test suite:

```bash
./mvnw clean verify
```

Windows:

```powershell
.\mvnw.cmd clean verify
```

The GitHub Actions workflow automatically:

1. Sets up Java 21
2. Restores Maven dependencies
3. Runs the Maven test/build process
4. Builds the Docker image
5. Publishes the image to GHCR on pushes to `main`

---

# 🐳 Docker Image

The application uses a **multi-stage Docker build**.

### Build stage

Uses:

```text
eclipse-temurin:21-jdk-alpine
```

to compile the Spring Boot application.

### Runtime stage

Uses:

```text
eclipse-temurin:21-jre-alpine
```

to run the compiled JAR with a smaller runtime image.

The runtime container runs the application as a non-root user.

Build the image manually:

```bash
docker build -t gym-tracker-backend .
```

Run it:

```bash
docker run -p 8080:8080 gym-tracker-backend
```

---

# 📖 API Documentation

Swagger / OpenAPI documentation is available when the application is running.

### Swagger UI

```text
http://localhost:8080/swagger-ui.html
```

### OpenAPI specification

```text
http://localhost:8080/api-docs
```

---

# 🔌 API Endpoints

All REST endpoints use the `/api` base path.

## Body Metrics

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/body-metrics` | Record body metrics |
| GET | `/api/body-metrics/history` | Get body metric history |
| PUT | `/api/body-metrics/{id}` | Update body metrics |
| DELETE | `/api/body-metrics/{id}` | Delete body metrics |

---

## Exercises

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/exercises` | Create an exercise |
| GET | `/api/exercises` | Get all exercises |
| DELETE | `/api/exercises/{id}` | Delete an exercise |

---

## Workout Logs

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/workouts/log` | Record a workout |
| GET | `/api/workouts/progress/{exerciseId}` | Get progress for an exercise |
| PUT | `/api/workouts/log/{id}` | Update a workout log |
| DELETE | `/api/workouts/log/{id}` | Delete a workout log |

---

## Crowd Tracking

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/crowd/report` | Report gym crowd level |
| GET | `/api/crowd/stats/{day}` | Get crowd statistics for a day |
| PUT | `/api/crowd/report/{id}` | Update a crowd report |
| DELETE | `/api/crowd/report/{id}` | Delete a crowd report |

---

## Crowd Analytics

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/analytics/optimal-slots/{day}` | Get optimal gym time slots for a day |

---

# ☁️ Cloud Deployment

The production application is deployed using:

- **Azure Container Apps** — application hosting
- **Azure Database for PostgreSQL** — persistent database
- **GitHub Container Registry** — Docker image registry

The production database is configured separately from the local Oracle database using environment variables.

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

Sensitive credentials are stored using Azure Container Apps secrets rather than being committed to the repository.

The Container App is configured with:

```text
CPU:              0.25 vCPU
Memory:           0.5 GiB
Minimum replicas: 0
Maximum replicas: 1
```

The application can therefore scale down when it is not being used while remaining available through its public HTTPS endpoint.

---

# 🔄 CI/CD

GitHub Actions is used for continuous integration and Docker image publishing.

On every push or pull request to `main`:

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

For pushes to `main`, Docker images are tagged with both:

```text
latest
<git-commit-sha>
```

The commit SHA tag provides an immutable reference to a specific build and can be used for reproducible deployments and rollbacks.

---

# 🔐 Configuration & Security

Database configuration is supplied through environment variables:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Secrets should never be committed to Git.

The repository includes:

```text
.env.example
```

as a template for required environment variables.

The actual:

```text
.env
```

file is excluded from version control.

---

# 🗄️ Database Strategy

Different databases are used depending on the environment:

```text
Local Development
       │
       ▼
Oracle Database
       │
       │
       ├───────────────┐
       │               │
       ▼               ▼
   Application       Docker
                       
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

Potential future improvements include:

- JWT-based authentication
- User accounts
- Role-based authorization
- Pagination and filtering
- More comprehensive unit and integration tests
- Automated deployment from GitHub Actions to Azure
- Improved analytics and visualizations
- More advanced workout progress tracking

---

# 👨‍💻 Author

**Shiven Puri**

GitHub:  
https://github.com/shiven-puri