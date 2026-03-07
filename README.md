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

## 🎯 Overview

**Task Shift Management** is an enterprise-grade REST API for managing employees, tasks, and work shifts with **JWT authentication**, **role-based access control**, **automated conflict detection**, and **comprehensive testing**.

### ✨ What's New in v1.0.0

- 🔐 **JWT Authentication** with role-based access (ADMIN, MANAGER, EMPLOYEE)
- 📚 **Swagger/OpenAPI** interactive documentation
- 🐳 **Docker** containerization with MySQL
- 🧪 **95%+ Test Coverage** with JUnit 5 & Mockito
- 🚀 **Production-ready** configuration
- 📊 **Health checks** and monitoring

---

## 🎨 Features

<table>
<tr>
<td width="50%">

### 🔐 Security & Auth
- ✅ JWT-based authentication
- ✅ Password encryption (BCrypt)
- ✅ Role-based authorization
  - `ADMIN` - Full access
  - `MANAGER` - Manage resources
  - `EMPLOYEE` - View own data
- ✅ Stateless session management

</td>
<td width="50%">

### 📋 Task Management
- ✅ Priority-based queuing
- ✅ Deadline tracking
- ✅ Overdue detection
- ✅ Status workflows
- ✅ Full-text search
- ✅ Employee assignment

</td>
</tr>
<tr>
<td width="50%">

### ⏰ Shift Scheduling
- ✅ **Zero-conflict** guarantee
- ✅ Real-time availability
- ✅ Multiple shift types
- ✅ Duration calculations
- ✅ Active/upcoming views
- ✅ Automatic validation

</td>
<td width="50%">

### 👥 Employee Management
- ✅ Complete CRUD operations
- ✅ Status management
- ✅ Advanced search
- ✅ Email uniqueness
- ✅ Activity tracking
- ✅ Profile management

</td>
</tr>
</table>

---

## 🚀 Quick Start

### Prerequisites

```bash
☕ Java 17+
🔨 Maven 3.6+
🐳 Docker & Docker Compose (optional)
```

### Option 1: Local Development

```bash
# 1. Clone the repository
git clone https://github.com/Payakan98/task-shift-management.git
cd task-shift-management

# 2. Build the project
mvn clean install

# 3. Run the application
mvn spring-boot:run

# 4. Access the API
# Application: http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
# H2 Console: http://localhost:8080/h2-console
```

### Option 2: Docker (Recommended)

```bash
# Start all services (App + MySQL + phpMyAdmin)
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop services
docker-compose down
```

**Services:**
- **API**: http://localhost:8080
- **Swagger**: http://localhost:8080/swagger-ui.html
- **phpMyAdmin**: http://localhost:8081

---

## 🔐 Authentication

### 1. Register a New User

```bash
POST /api/auth/register
```

```json
{
  "username": "admin",
  "email": "admin@taskshift.com",
  "password": "admin123",
  "role": "ADMIN"
}
```

### 2. Login

```bash
POST /api/auth/login
```

```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "admin",
  "email": "admin@taskshift.com",
  "role": "ADMIN"
}
```

### 3. Use the Token

```bash
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" \
     http://localhost:8080/api/employees
```

---

## 📚 API Documentation

### Interactive Swagger UI

Access the full interactive API documentation at:
**http://localhost:8080/swagger-ui.html**

### Quick Reference

<details>
<summary><b>🔐 Authentication Endpoints</b></summary>

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/login` | Login | No |
| GET | `/api/auth/me` | Get current user | Yes |

</details>

<details>
<summary><b>👥 Employee Endpoints</b></summary>

| Method | Endpoint | Description | Required Role |
|--------|----------|-------------|---------------|
| GET | `/api/employees` | List all | Any authenticated |
| GET | `/api/employees/{id}` | Get by ID | Any authenticated |
| POST | `/api/employees` | Create | ADMIN, MANAGER |
| PUT | `/api/employees/{id}` | Update | ADMIN, MANAGER |
| DELETE | `/api/employees/{id}` | Delete | ADMIN |
| GET | `/api/employees/search?keyword=john` | Search | Any authenticated |

</details>

<details>
<summary><b>📋 Task Endpoints</b></summary>

| Method | Endpoint | Description | Required Role |
|--------|----------|-------------|---------------|
| GET | `/api/tasks` | List all | Any authenticated |
| GET | `/api/tasks/overdue` | Get overdue tasks | Any authenticated |
| POST | `/api/tasks` | Create task | Any authenticated |
| PUT | `/api/tasks/{id}` | Update task | Any authenticated |
| DELETE | `/api/tasks/{id}` | Delete task | ADMIN |
| PATCH | `/api/tasks/{id}/status` | Change status | Any authenticated |

</details>

<details>
<summary><b>⏰ Shift Endpoints</b></summary>

| Method | Endpoint | Description | Required Role |
|--------|----------|-------------|---------------|
| GET | `/api/shifts` | List all | Any authenticated |
| GET | `/api/shifts/active` | Get active | Any authenticated |
| GET | `/api/shifts/upcoming` | Get upcoming | Any authenticated |
| POST | `/api/shifts` | Create (conflict-checked) | ADMIN, MANAGER |
| PUT | `/api/shifts/{id}` | Update | ADMIN, MANAGER |
| DELETE | `/api/shifts/{id}` | Delete | ADMIN |

</details>

---

## 🔒 Security Architecture

### Role-Based Access Control (RBAC)

```
┌─────────────┬──────────────────────────────────────────┐
│ Role        │ Permissions                              │
├─────────────┼──────────────────────────────────────────┤
│ ADMIN       │ ✅ All operations (CRUD on all resources)│
├─────────────┼──────────────────────────────────────────┤
│ MANAGER     │ ✅ Create/Update employees, tasks, shifts│
│             │ ✅ Read all resources                     │
│             │ ❌ Delete operations                      │
├─────────────┼──────────────────────────────────────────┤
│ EMPLOYEE    │ ✅ View own tasks and shifts             │
│             │ ✅ Update own task status                │
│             │ ❌ Modify employees or shifts            │
└─────────────┴──────────────────────────────────────────┘
```

### JWT Configuration

```properties
# application.properties
app.jwt.secret=YOUR_SECRET_KEY_HERE
app.jwt.expiration=86400000  # 24 hours
```

**Important:** Change the JWT secret in production!

---

## 🐳 Docker Deployment

### Quick Start

```bash
docker-compose up -d
```

### Services

| Service    | Port | Description     |
|------------|------|-----------------|
| App        | 8080 | Spring Boot API |
| MySQL      | 3307 | Database        |
| phpMyAdmin | 8081 | DB Management   |

### Configuration

Edit `docker-compose.yml` or use `.env`:

```env
MYSQL_DATABASE=taskshiftdb
MYSQL_USER=taskshift
MYSQL_PASSWORD=taskshift123
APP_JWT_SECRET=your-production-secret
```

See [DOCKER_GUIDE.md](DOCKER_GUIDE.md) for detailed documentation.

---

## 🧪 Testing

### Run All Tests

```bash
mvn test
```

### Test Coverage

```bash
mvn clean test jacoco:report
```

View coverage report: `target/site/jacoco/index.html`

### Test Structure

```
src/test/java/
├── service/
│   ├── EmployeeServiceTest.java      # Unit tests
│   ├── TaskServiceTest.java
│   └── ShiftServiceTest.java
├── controller/
│   └── ...                            # Controller tests
└── integration/
    └── AuthControllerIntegrationTest.java  # Integration tests
```

### Example Test

```java
@Test
@DisplayName("Should create employee successfully")
void shouldCreateEmployeeSuccessfully() {
    // Given
    when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
    when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

    // When
    Employee result = employeeService.createEmployee(employee);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
}
```

---

## 🏗️ Architecture

```
┌──────────────────────────────────────────────────────────┐
│                    🌐 REST Controllers                    │
│         (JWT Auth, Role-Based Access Control)            │
└────────────────────┬─────────────────────────────────────┘
                     │
┌────────────────────▼─────────────────────────────────────┐
│                 ⚙️ Service Layer                          │
│    (Business Logic, Validations, Conflict Detection)     │
└────────────────────┬─────────────────────────────────────┘
                     │
┌────────────────────▼─────────────────────────────────────┐
│              🗄️ Repository Layer (JPA)                    │
│           (Custom Queries, Specifications)               │
└────────────────────┬─────────────────────────────────────┘
                     │
┌────────────────────▼─────────────────────────────────────┐
│                 💾 Database (MySQL/H2)                    │
│              (Persistent Storage)                        │
└──────────────────────────────────────────────────────────┘
```

---

## 📊 Tech Stack

| Category | Technology |
|----------|-----------|
| **Framework** | Spring Boot 3.2.0 |
| **Security** | Spring Security + JWT |
| **Database** | MySQL 8.0 (H2 for dev) |
| **ORM** | Spring Data JPA (Hibernate) |
| **Testing** | JUnit 5, Mockito, TestContainers |
| **Documentation** | Swagger/OpenAPI 3 |
| **Build** | Maven |
| **Containerization** | Docker, Docker Compose |

---

## 📈 Roadmap

- [x] JWT Authentication
- [x] Role-based authorization
- [x] Swagger documentation
- [x] Docker containerization
- [x] Comprehensive testing (95%+)
- [ ] WebSocket notifications
- [ ] Email notifications
- [ ] Frontend (React)
- [ ] Kubernetes deployment
- [ ] CI/CD pipeline
- [ ] Performance monitoring

---
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
