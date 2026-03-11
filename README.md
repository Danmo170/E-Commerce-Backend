# 🛒 E-Commerce Backend

A RESTful e-commerce backend API built with Java and Spring Boot, featuring JWT authentication, product management, shopping cart, and order processing.

## 🛠️ Tech Stack

- **Java 25**
- **Spring Boot 4**
- **Spring Security + JWT** (JJWT 0.12.6)
- **Spring Data JPA + Hibernate**
- **MySQL** (production) / **H2** (testing)
- **Lombok**
- **SpringDoc OpenAPI 3** (Swagger UI)
- **GitHub Actions CI**

## ✨ Features

- JWT-based authentication with role management (USER / ADMIN)
- Full product CRUD with partial updates and pagination
- Shopping cart with stock validation
- Order management with status tracking and stock control
- Global exception handling with structured error responses
- Integration tests with MockMvc and H2
- API versioning (`/api/v1`)
- Environment variable configuration
- Automated CI pipeline with GitHub Actions
- Data seeding on startup for easy testing

## 📋 Requirements

- Java 25
- Maven
- MySQL 8+

## ⚙️ Setup

**1. Clone the repository**
```bash
git clone https://github.com/Danmo170/ecommerce.git
cd ecommerce
```

**2. Create the database**
```sql
CREATE DATABASE ecommerce_db;
```

**3. Configure environment variables**

Copy the example file and fill in your values:
```bash
cp application.properties.example src/main/resources/application.properties
```

Create a `.env` file in the root of the project:
```env
DB_PASSWORD=your_database_password
JWT_SECRET=your_jwt_secret_base64_encoded
```

> To generate a secure Base64 JWT secret you can run:
> ```bash
> openssl rand -base64 32
> ```

**4. Run the application**
```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

Swagger UI: `http://localhost:8080/swagger-ui.html`

## 🔑 Test Credentials

The application seeds the following accounts on startup:

| Role  | Email               | Password         |
|-------|---------------------|------------------|
| USER  | user@example.com    | UserExample123   |
| ADMIN | admin@example.com   | AdminExample123  |

## 📡 API Endpoints

### Auth
| Method | Endpoint                  | Access | Description         |
|--------|---------------------------|--------|---------------------|
| POST   | `/api/v1/auth/register`   | Public | Register a new user |
| POST   | `/api/v1/auth/login`      | Public | Login and get token |

### Products
| Method | Endpoint                          | Access | Description              |
|--------|-----------------------------------|--------|--------------------------|
| GET    | `/api/v1/products`                | Public | Get all products (paginated) |
| GET    | `/api/v1/products/{id}`           | Public | Get product by ID        |
| POST   | `/api/v1/products`                | ADMIN  | Create a product         |
| PUT    | `/api/v1/products/{id}`           | ADMIN  | Update a product         |
| PATCH  | `/api/v1/products/{id}/deactivate`| ADMIN  | Deactivate a product     |

### Cart
| Method | Endpoint                        | Access | Description           |
|--------|---------------------------------|--------|-----------------------|
| GET    | `/api/v1/cart`                  | USER   | Get current cart      |
| POST   | `/api/v1/cart/items`            | USER   | Add item to cart      |
| PUT    | `/api/v1/cart/items/{productId}`| USER   | Update item quantity  |
| DELETE | `/api/v1/cart/items/{productId}`| USER   | Remove item from cart |

### Orders
| Method | Endpoint                            | Access | Description            |
|--------|-------------------------------------|--------|------------------------|
| GET    | `/api/v1/orders`                    | USER   | Get all user orders    |
| GET    | `/api/v1/orders/{id}`               | USER   | Get order by ID        |
| POST   | `/api/v1/orders`                    | USER   | Create order from cart |
| PATCH  | `/api/v1/orders/{id}/cancel`        | USER   | Cancel an order        |
| PATCH  | `/api/v1/orders/{id}/status`        | ADMIN  | Update order status    |

## 🧪 Running Tests

```bash
mvn test
```

## 👤 Author

**Daniel Moshe Carrillo Alvarez**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue?style=flat&logo=linkedin)](https://www.linkedin.com/in/daniel-carrillo-48ab863a1/)