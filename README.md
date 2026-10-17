[readme_md.md](https://github.com/user-attachments/files/33601916/readme_md.md)
# Sales Savvy — Enterprise Sales & Inventory Management System

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Hibernate](https://img.shields.io/badge/Hibernate-JPA-59666C?style=for-the-badge&logo=hibernate&logoColor=white)](https://hibernate.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](https://opensource.org/licenses/MIT)

Sales Savvy is a modular, full-stack sales and customer management platform architected using Java, Spring Boot, Spring Security, Hibernate/JPA, and MySQL. The system streamlines catalog browsing, cart operations, order fulfillment, and administrative inventory management under an enterprise-grade MVC architecture.

---

## Table of Contents

- [Key Features](#key-features)
- [Architecture & Tech Stack](#architecture--tech-stack)
- [Database Schema Overview](#database-schema-overview)
- [API Specification](#api-specification)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation & Setup](#installation--setup)
  - [Environment Configuration](#environment-configuration)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [Project Directory Structure](#project-directory-structure)
- [Future Enhancements](#future-enhancements)
- [Author & License](#author--license)

---

## Key Features

### Customer & User Operations
- **Account Management & Authentication:** Session-based authentication with secure credential hashing and profile management.
- **Dynamic Catalog:** Browse and filter products by category, pricing, and stock status.
- **Cart Management:** Real-time item additions, deletions, and quantity recalculations.
- **Checkout & Orders:** Order placement flow with transaction persistence, status tracking, and order history.

### Administrative Control
- **Product & Inventory Management:** Complete CRUD controls for stock maintenance, price modifications, and catalog updates.
- **Order Oversight:** Review, process, and transition customer order lifecycles.
- **Customer Oversight:** View registered users and related purchase behaviors.

---

## Architecture & Tech Stack

The application follows standard Layered Architecture principles to enforce separation of concerns:
- **Presentation Layer:** Controller endpoints mapping REST/MVC requests with DTO validations.
- **Business/Service Layer:** Core business logic, pricing computations, and `@Transactional` state changes.
- **Persistence Layer:** Spring Data JPA repositories with Hibernate executing optimized queries on MySQL.

### Core Technologies
- **Backend:** Java 17, Spring Boot 3.x (Spring MVC, Spring Data JPA, Spring Security)
- **Database:** MySQL 8.0+
- **Persistence:** Hibernate ORM
- **Build Tool:** Maven
- **API Testing:** Postman
- **Version Control:** Git / GitHub

---

## Database Schema Overview

```
 [USERS] 1 ──── ∞ [ORDERS] 1 ──── ∞ [ORDER_ITEMS] ∞ ──── 1 [PRODUCTS]
    1
    │
    └─── 1 [CART] 1 ──── ∞ [CART_ITEMS] ∞ ──── 1 [PRODUCTS]
```

- **Users:** Stores authentication credentials, contact details, and role assignments (`ROLE_USER`, `ROLE_ADMIN`).
- **Products:** Stores SKU, title, descriptions, unit prices, and available inventory.
- **Orders & Order Items:** Captures immutable point-in-time transaction snapshots with reference to products and purchasing users.
- **Cart & Cart Items:** Manages transient shopping session states per user.

---

## API Specification

| HTTP Method | Endpoint | Description | Access Level |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register a new user account | Public |
| `POST` | `/api/auth/login` | Authenticate user and initiate session | Public |
| `GET` | `/api/products` | Retrieve all active products | Public |
| `GET` | `/api/products/{id}` | Retrieve specific product details | Public |
| `POST` | `/api/cart/add` | Add items to the authenticated cart | Authenticated |
| `GET` | `/api/cart` | View current user cart | Authenticated |
| `POST` | `/api/orders/checkout` | Process active cart into confirmed order | Authenticated |
| `GET` | `/api/orders/history` | List current user order history | Authenticated |
| `POST` | `/api/admin/products` | Create new inventory items | Admin |
| `PUT` | `/api/admin/products/{id}` | Update product information/stock | Admin |
| `DELETE`| `/api/admin/products/{id}` | Archive or delete a product | Admin |

---

## Getting Started

### Prerequisites

Ensure the following tools are installed on your workstation:
- **Java Development Kit (JDK):** Version 17 or higher
- **Build Tool:** Apache Maven 3.8+
- **Database Server:** MySQL Server 8.0+
- **Git:** Version 2.30+

### Installation & Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/obaid-majeed/salessavvy.git
   cd salessavvy
   ```

2. **Configure MySQL Database:**
   Log into your MySQL shell and create the database:
   ```sql
   CREATE DATABASE salessavvy_db;
   ```

### Environment Configuration

Update your database credentials in `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/salessavvy_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate Properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

---

## Running the Application

### Via Maven Wrapper
```bash
./mvnw clean spring-boot:run
```

### Via Built JAR Package
```bash
./mvnw clean package -DskipTests
java -jar target/salessavvy-0.0.1-SNAPSHOT.jar
```

The application will launch on `http://localhost:8080`.

---

## Testing

Run unit and integration test suites:
```bash
./mvnw test
```

For manual endpoint testing, import the provided endpoints into **Postman** and verify status codes, payload structures, and authorization constraints.

---

## Project Directory Structure

```text
salessavvy/
├── src/
│   ├── main/
│   │   ├── java/com/application/
│   │   │   ├── config/          # Security, Web, and Bean configurations
│   │   │   ├── controller/      # REST & MVC Endpoints
│   │   │   ├── dto/             # Request & Response Data Transfer Objects
│   │   │   ├── exception/       # Global exception handlers & custom errors
│   │   │   ├── model/           # JPA Entities (User, Product, Order, Cart)
│   │   │   ├── repository/      # Spring Data JPA Data Access Objects
│   │   │   └── service/         # Business logic and transaction workflows
│   │   └── resources/
│   │       ├── static/          # Static web assets (CSS, JS, images)
│   │       ├── templates/       # Server-rendered templates (if applicable)
│   │       └── application.properties
│   └── test/                    # Unit and integration test suites
├── pom.xml
└── README.md
```

---

## Future Enhancements

- [ ] Transition from session authentication to stateless JWT token infrastructure.
- [ ] Integrate third-party payment gateways (Stripe / Razorpay).
- [ ] Implement Redis cache for high-frequency product catalog queries.
- [ ] Containerize application with Docker & Docker Compose.

---

## Author & License

- **Developer:** [Obaid Majeed](https://github.com/obaid-majeed)
- **License:** Distributed under the [MIT License](LICENSE).
