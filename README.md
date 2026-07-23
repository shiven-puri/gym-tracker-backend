# 🏋️ Gym Tracker Backend

A RESTful backend application built with **Spring Boot** that helps users track workouts, monitor body metrics, analyze gym crowd patterns, and determine the best time to visit the gym using crowd analytics.

## ✨ Features

- Exercise management
- Workout logging and progress tracking
- Body metrics tracking (weight, body fat %, muscle mass)
- Gym crowd reporting
- Crowd analytics and optimal gym timing recommendations
- RESTful API architecture
- Interactive API documentation using Swagger

---

## 🛠️ Tech Stack

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- Oracle Database
- Maven
- Lombok
- Swagger / OpenAPI

---

## 📂 Project Structure

```
src
 ├── controller
 ├── dto
 ├── entity
 ├── repository
 ├── service
 └── config
```

---

## 🚀 Getting Started

### Clone the repository

```bash
git clone https://github.com/shiven-puri/gym-tracker-backend.git
```

### Configure the database

Update the database configuration in `application.properties` according to your Oracle database setup.

### Run the application

```bash
mvn spring-boot:run
```

---

## 📖 API Documentation

Once the application is running:

```
http://localhost:8080/swagger-ui.html
```

---

## 📊 Main Functionalities

- Manage exercises
- Record workout sessions
- Track body measurements
- Report gym crowd levels
- Analyze crowd trends
- Recommend optimal workout time slots

---

## 🔮 Future Enhancements

- JWT Authentication
- User accounts
- Role-based authorization
- Docker support
- Unit & integration tests
- CI/CD pipeline
- Pagination & filtering

---

## 👨‍💻 Author

**Shiven Puri**