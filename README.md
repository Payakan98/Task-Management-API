<div align="center">

```
╔════════════════════════════════════════════════════════════════╗
║                                                                ║
║   ████████╗ █████╗ ███████╗██╗  ██╗    ███████╗██╗  ██╗        ║
║   ╚══██╔══╝██╔══██╗██╔════╝██║ ██╔╝    ██╔════╝██║  ██║        ║
║      ██║   ███████║███████╗█████╔╝     ███████╗███████║        ║
║      ██║   ██╔══██║╚════██║██╔═██╗     ╚════██║██╔══██║        ║
║      ██║   ██║  ██║███████║██║  ██╗    ███████║██║  ██║        ║
║      ╚═╝   ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝    ╚══════╝╚═╝  ╚═╝        ║
║                                                                ║
║              🚀 Task & Shift Management System 🚀             ║
║          Where productivity meets intelligent scheduling       ║
║                                                                ║
╚════════════════════════════════════════════════════════════════╝
```

[![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen?style=for-the-badge&logo=spring)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)](LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=for-the-badge)](http://makeapullrequest.com)

**[🌟 Features](#-key-features) • [🚀 Quick Start](#-quick-start) • [📚 Documentation](#-api-documentation) • [💡 Examples](#-usage-examples) • [🤝 Contributing](#-contributing)**

---

</div>

## 🎯 What is Task-Shift?

**Task-Shift** is not just another task management system. It's your **intelligent workforce orchestrator** that seamlessly combines:

- 📋 **Smart Task Management** - Prioritize, track, and conquer
- ⏰ **Advanced Shift Scheduling** - Conflict-free, efficient planning
- 👥 **Employee Lifecycle** - Complete HR management
- 🔥 **Real-time Operations** - Know what's happening, right now

> **Built with Spring Boot 3.2.0, leveraging the power of modern Java to deliver enterprise-grade performance.**

---

## ✨ Key Features

<table>
<tr>
<td width="33%" valign="top">

### 🎯 Task Intelligence
- ✅ Priority-based task queue
- 📅 Smart deadline tracking
- ⚠️ Overdue detection
- 🔄 Real-time status updates
- 🔍 Full-text search

</td>
<td width="33%" valign="top">

### ⚡ Shift Optimization
- 🚫 **Zero conflicts** guarantee
- 📊 Active shift monitoring
- 🔮 Upcoming shift preview
- ⏱️ Duration calculations
- 📱 Multiple shift types

</td>
<td width="33%" valign="top">

### 👤 Employee Hub
- 📇 Complete profiles
- 📧 Email uniqueness
- 🏷️ Status management
- 🔎 Advanced search
- 📈 Activity tracking

</td>
</tr>
</table>

---

## 🚀 Quick Start

### Prerequisites

```bash
☕ Java 17+
🔨 Maven 3.6+
💻 Your favorite IDE (IntelliJ IDEA recommended)
```

### Installation

```bash
# 1️⃣ Clone the repository
git clone https://github.com/yourusername/task-shift-management.git
cd task-shift-management

# 2️⃣ Build the project
mvn clean install

# 3️⃣ Run the application
mvn spring-boot:run

# 🎉 You're live at http://localhost:8080
```

### Docker Quick Start

```bash
# Coming soon! 🐳
docker-compose up -d
```

---

## 🎮 Usage Examples

### Create Your First Employee

```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Sarah",
    "lastName": "Connor",
    "email": "sarah.connor@skynet.com",
    "position": "Resistance Leader",
    "status": "ACTIVE"
  }'
```

### Assign a High-Priority Task

```bash
curl -X POST http://localhost:8080/api/tasks/employee/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Stop Skynet",
    "description": "Prevent the robot apocalypse",
    "priority": "URGENT",
    "status": "IN_PROGRESS",
    "dueDate": "2024-12-31T23:59:59"
  }'
```

### Schedule a Shift (with automatic conflict detection!)

```bash
curl -X POST http://localhost:8080/api/shifts/employee/1 \
  -H "Content-Type: application/json" \
  -d '{
    "startTime": "2024-02-20T09:00:00",
    "endTime": "2024-02-20T17:00:00",
    "shiftType": "MORNING",
    "status": "SCHEDULED"
  }'
```

---

## 📚 API Documentation

### 🌐 Core Endpoints

<details>
<summary><b>👥 Employees API</b> - Click to expand</summary>

| Method | Endpoint | Description | Status Codes |
|--------|----------|-------------|--------------|
| `GET` | `/api/employees` | List all employees | 200 OK |
| `GET` | `/api/employees/{id}` | Get employee by ID | 200 OK, 404 Not Found |
| `POST` | `/api/employees` | Create new employee | 201 Created, 400 Bad Request |
| `PUT` | `/api/employees/{id}` | Update employee | 200 OK, 404 Not Found |
| `DELETE` | `/api/employees/{id}` | Delete employee | 204 No Content |
| `GET` | `/api/employees/search?keyword=john` | Search employees | 200 OK |
| `PATCH` | `/api/employees/{id}/status` | Change status | 200 OK |

</details>

<details>
<summary><b>📋 Tasks API</b> - Click to expand</summary>

| Method | Endpoint | Description | Status Codes |
|--------|----------|-------------|--------------|
| `GET` | `/api/tasks` | List all tasks | 200 OK |
| `GET` | `/api/tasks/overdue` | Get overdue tasks | 200 OK |
| `GET` | `/api/tasks/due-soon?days=7` | Tasks due soon | 200 OK |
| `POST` | `/api/tasks/employee/{id}` | Create & assign task | 201 Created |
| `PATCH` | `/api/tasks/{taskId}/assign/{empId}` | Reassign task | 200 OK |
| `PATCH` | `/api/tasks/{id}/status` | Update status | 200 OK |
| `GET` | `/api/tasks/search?keyword=urgent` | Search tasks | 200 OK |

</details>

<details>
<summary><b>⏰ Shifts API</b> - Click to expand</summary>

| Method | Endpoint | Description | Status Codes |
|--------|----------|-------------|--------------|
| `GET` | `/api/shifts` | List all shifts | 200 OK |
| `GET` | `/api/shifts/active` | Get active shifts | 200 OK |
| `GET` | `/api/shifts/upcoming` | Get upcoming shifts | 200 OK |
| `POST` | `/api/shifts/employee/{id}` | Create shift (conflict-checked) | 201 Created, 400 Conflict |
| `GET` | `/api/shifts/between?start=...&end=...` | Shifts in date range | 200 OK |
| `PATCH` | `/api/shifts/{id}/status` | Update shift status | 200 OK |

</details>

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        🌐 REST API Layer                     │
│              (Controllers - HTTP Endpoints)                  │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│                    ⚙️ Business Logic Layer                   │
│        (Services - Validations & Business Rules)            │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│                  🗄️ Data Access Layer                        │
│           (Repositories - JPA Queries)                       │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│                   💾 Database Layer                          │
│              (H2 / MySQL - Persistence)                      │
└─────────────────────────────────────────────────────────────┘
```

### Tech Stack

```yaml
Backend Framework: Spring Boot 3.2.0
Language: Java 17
ORM: Spring Data JPA (Hibernate)
Database: H2 (dev) / MySQL (prod)
Build Tool: Maven
Validation: Jakarta Bean Validation
Developer Tools: Lombok, Spring DevTools
```

---

## 🎨 Data Models

### 👤 Employee
```java
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "position": "Software Engineer",
  "status": "ACTIVE",  // ACTIVE | INACTIVE | ON_LEAVE
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### 📋 Task
```java
{
  "id": 1,
  "title": "Implement REST API",
  "description": "Build the employee management endpoints",
  "status": "IN_PROGRESS",  // TODO | IN_PROGRESS | COMPLETED | CANCELLED
  "priority": "HIGH",       // LOW | MEDIUM | HIGH | URGENT
  "dueDate": "2024-02-20T17:00:00",
  "employee": { "id": 1, "firstName": "John", "lastName": "Doe" }
}
```

### ⏰ Shift
```java
{
  "id": 1,
  "startTime": "2024-02-20T09:00:00",
  "endTime": "2024-02-20T17:00:00",
  "shiftType": "MORNING",  // Custom types
  "status": "SCHEDULED",   // SCHEDULED | IN_PROGRESS | COMPLETED | CANCELLED | NO_SHOW
  "notes": "Regular morning shift",
  "employee": { "id": 1, "firstName": "John", "lastName": "Doe" }
}
```

---

## 🔥 Advanced Features

### ⚡ Smart Conflict Detection

The system **automatically detects** scheduling conflicts:

```java
// ❌ This will fail if employee already has a shift at this time
POST /api/shifts/employee/1
{
  "startTime": "2024-02-20T09:00:00",
  "endTime": "2024-02-20T17:00:00"
}

// Response: 400 Bad Request
{
  "error": "Conflit détecté : l'employé a déjà un shift programmé pendant cette période"
}
```

### 📊 Real-Time Analytics

```bash
# Count active employees
GET /api/employees/count/active

# Count tasks by status
GET /api/tasks/count/status/COMPLETED

# Get overdue tasks
GET /api/tasks/overdue
```

---

## 🗄️ Database Configuration

### Development (H2 In-Memory)

```properties
# Auto-configured! Just run the app
# Access H2 Console: http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:taskshiftdb
# Username: sa
# Password: (leave blank)
```

### Production (MySQL)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/taskshiftdb
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
```

---

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run with coverage
mvn clean test jacoco:report

# Integration tests
mvn verify
```

---

## 🎯 Roadmap

- [x] Core CRUD operations
- [x] Shift conflict detection
- [x] Advanced search & filtering
- [ ] 🔐 Authentication & Authorization (JWT)
- [ ] 📧 Email notifications
- [ ] 📊 Dashboard & Analytics UI
- [ ] 🐳 Docker containerization
- [ ] ☁️ Cloud deployment (AWS/Azure)
- [ ] 📱 Mobile app (React Native)
- [ ] 🤖 AI-powered shift optimization

---

## 🤝 Contributing

We love contributions! Here's how you can help:

1. 🍴 Fork the repository
2. 🌿 Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. ✨ Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. 📤 Push to the branch (`git push origin feature/AmazingFeature`)
5. 🎉 Open a Pull Request

### Code Style

- Follow Google Java Style Guide
- Write meaningful commit messages
- Add tests for new features
- Update documentation

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🌟 Acknowledgments

- Built with ☕ and ❤️ using Spring Boot
- Inspired by modern workforce management needs
- Special thanks to the open-source community

---

<div align="center">

### ⭐ Star us on GitHub — it helps!

Made with ❤️ by [Islem CHOKRI](https://github.com/Payakan98)

**[⬆ Back to Top](#)**

</div>
