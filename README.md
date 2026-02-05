# Money Manager Backend

A Spring Boot REST API for managing personal and business financial transactions, accounts, and transfers with MongoDB Atlas integration.

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Environment Variables](#environment-variables)
- [Contributing](#contributing)

## ✨ Features

- **Transaction Management**: Create, read, update, and delete financial transactions
- **Account Management**: Manage multiple bank accounts with balance tracking
- **Fund Transfers**: Transfer funds between user accounts with validation
- **Filtering**: Filter transactions by type, category, division, and date range
- **Edit Restrictions**: Transactions can only be edited within 12 hours of creation
- **Category Support**: Track expenses/income with predefined categories
- **Division Support**: Organize transactions into Office or Personal categories
- **MongoDB Integration**: Flexible NoSQL database with Atlas support
- **CORS Enabled**: Ready for frontend integration
- **Comprehensive Logging**: SLF4J with Log4j2 integration

## 🛠 Tech Stack

- **Framework**: Spring Boot 3.2.0
- **Java Version**: 17
- **Database**: MongoDB Atlas
- **Build Tool**: Maven 3.9+
- **Security**: Spring Security
- **Validation**: Jakarta Bean Validation
- **ORM**: Spring Data MongoDB
- **Mapping**: ModelMapper 3.2.0
- **JWT**: JJWT 0.12.3
- **Logging**: SLF4J with Log4j2

## 📁 Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/moneymanager/
│   │   │   ├── model/                  # Database models
│   │   │   │   ├── Transaction.java   # Transaction entity
│   │   │   │   ├── Account.java       # Account entity
│   │   │   │   ├── Transfer.java      # Transfer entity
│   │   │   │   └── User.java          # User entity
│   │   │   ├── dto/                   # Data Transfer Objects
│   │   │   │   ├── TransactionDTO.java
│   │   │   │   ├── AccountDTO.java
│   │   │   │   ├── TransferDTO.java
│   │   │   │   └── UserDTO.java
│   │   │   ├── controller/            # REST API endpoints
│   │   │   │   ├── TransactionController.java
│   │   │   │   ├── AccountController.java
│   │   │   │   ├── TransferController.java
│   │   │   │   └── HealthController.java
│   │   │   ├── service/               # Business logic
│   │   │   │   ├── TransactionService.java
│   │   │   │   ├── AccountService.java
│   │   │   │   └── TransferService.java
│   │   │   ├── repository/            # Data access layer
│   │   │   │   ├── TransactionRepository.java
│   │   │   │   ├── AccountRepository.java
│   │   │   │   ├── TransferRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── exception/             # Custom exceptions
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── UnauthorizedException.java
│   │   │   │   └── InvalidOperationException.java
│   │   │   ├── config/                # Configuration classes
│   │   │   │   └── AppConfig.java     # Spring configuration
│   │   │   └── MoneyManagerApplication.java  # Main application class
│   │   └── resources/
│   │       ├── application.yml        # Base configuration
│   │       └── application-dev.yml    # Development configuration
│   └── test/
│       └── java/com/moneymanager/    # Test classes
├── pom.xml                           # Maven dependencies and configuration
├── .gitignore                        # Git ignore rules
└── README.md                         # This file
```

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 17 or higher**: [Download JDK](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- **Maven 3.9+**: [Download Maven](https://maven.apache.org/download.cgi)
- **MongoDB Atlas Account**: [Create Account](https://www.mongodb.com/cloud/atlas)
- **Git**: [Download Git](https://git-scm.com/)

## 🚀 Installation & Setup

### Step 1: Clone Repository

```bash
git clone <repository-url>
cd Money\ Manager\ Frontend/backend
```

### Step 2: Verify Java Installation

```bash
java -version
javac -version
```

Should show Java 17 or higher.

### Step 3: Verify Maven Installation

```bash
mvn -version
```

Should show Maven 3.9 or higher.

### Step 4: Setup MongoDB Atlas

1. Create account at [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Create a new cluster
3. Create a database user with username and password
4. Get connection string from "Connect" button
5. Connection string format:
   ```
   mongodb+srv://username:password@cluster.mongodb.net/money-manager?retryWrites=true&w=majority
   ```

### Step 5: Build Project

```bash
mvn clean install
```

This will download all dependencies and compile the project.

## ⚙️ Configuration

### Application Properties

Edit `src/main/resources/application-dev.yml`:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb+srv://your-username:your-password@your-cluster.mongodb.net/money-manager

jwt:
  secret: your-secret-key-here
  expiration: 86400000

app:
  transaction:
    edit-limit-hours: 12

cors:
  allowed-origins: http://localhost:3000,http://localhost:5173
```

### Environment Variables

Create a `.env` file in the backend directory:

```
MONGO_USERNAME=your_username
MONGO_PASSWORD=your_password
MONGO_CLUSTER=your_cluster
MONGO_DATABASE=money-manager
JWT_SECRET=your_jwt_secret_key
```

## 🏃 Running the Application

### Development Mode

```bash
mvn spring-boot:run
```

Or with specific profile:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

The API will be available at: `http://localhost:8080/api`

### Production Build

```bash
mvn clean package
```

Run the JAR file:

```bash
java -jar target/money-manager-backend-1.0.0.jar
```

### Health Check

```bash
curl http://localhost:8080/api/health
```

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Authentication
Include header in all requests (except health):
```
X-User-Id: <user-id>
```

### Response Format

All endpoints return JSON with standard format:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... },
  "count": 0
}
```

### Transaction Endpoints

#### Create Transaction
```
POST /transactions
Content-Type: application/json
X-User-Id: user-123

{
  "type": "INCOME",
  "description": "Monthly Salary",
  "amount": 3500.00,
  "category": "Salary",
  "division": "Office",
  "transactionDate": "2024-02-05T10:30:00",
  "status": "COMPLETED"
}
```

#### Get All Transactions
```
GET /transactions
X-User-Id: user-123
```

#### Get Transaction by ID
```
GET /transactions/{id}
X-User-Id: user-123
```

#### Get by Type
```
GET /transactions/type/INCOME
X-User-Id: user-123
```

#### Get by Category
```
GET /transactions/category/Food
X-User-Id: user-123
```

#### Get by Division
```
GET /transactions/division/Personal
X-User-Id: user-123
```

#### Get by Date Range
```
GET /transactions/filter/date-range?startDate=2024-01-01T00:00:00&endDate=2024-02-05T23:59:59
X-User-Id: user-123
```

#### Update Transaction
```
PUT /transactions/{id}
Content-Type: application/json
X-User-Id: user-123

{
  "description": "Updated description",
  "amount": 3600.00,
  "category": "Salary",
  "division": "Office",
  "transactionDate": "2024-02-05T10:30:00"
}
```

#### Delete Transaction
```
DELETE /transactions/{id}
X-User-Id: user-123
```

### Account Endpoints

#### Create Account
```
POST /accounts
Content-Type: application/json
X-User-Id: user-123

{
  "accountName": "Main Checking",
  "accountType": "CHECKING",
  "balance": 5000.00,
  "currency": "USD",
  "bankName": "Bank Name",
  "accountNumber": "****1234"
}
```

#### Get All Accounts
```
GET /accounts
X-User-Id: user-123
```

#### Get Active Accounts
```
GET /accounts/active
X-User-Id: user-123
```

#### Get Account by ID
```
GET /accounts/{id}
X-User-Id: user-123
```

#### Update Account
```
PUT /accounts/{id}
Content-Type: application/json
X-User-Id: user-123

{
  "accountName": "Updated Name",
  "balance": 5500.00
}
```

#### Deactivate Account
```
PATCH /accounts/{id}/deactivate
X-User-Id: user-123
```

#### Get Total Balance
```
GET /accounts/total-balance
X-User-Id: user-123
```

### Transfer Endpoints

#### Create Transfer
```
POST /accounts/transfer
Content-Type: application/json
X-User-Id: user-123

{
  "fromAccountId": "account-1",
  "toAccountId": "account-2",
  "amount": 1000.00,
  "description": "Transfer to savings",
  "transferDate": "2024-02-05T10:30:00"
}
```

#### Get All Transfers
```
GET /accounts/transfer
X-User-Id: user-123
```

#### Get Transfers by Account
```
GET /accounts/transfer/account/{accountId}
```

#### Get Transfers by Date Range
```
GET /accounts/transfer/filter/date-range?startDate=2024-01-01T00:00:00&endDate=2024-02-05T23:59:59
X-User-Id: user-123
```

## 📊 Database Schema

### Collections

#### Transactions
```
{
  _id: ObjectId,
  userId: String,
  type: String (INCOME/EXPENSE),
  description: String,
  amount: Double,
  category: String,
  division: String (Office/Personal),
  transactionDate: DateTime,
  createdAt: DateTime,
  updatedAt: DateTime,
  deletedAt: DateTime,
  isDeleted: Boolean,
  accountId: String,
  notes: String,
  status: String (PENDING/COMPLETED/FAILED)
}
```

#### Accounts
```
{
  _id: ObjectId,
  userId: String,
  accountName: String,
  accountType: String,
  balance: Double,
  currency: String (default: USD),
  createdAt: DateTime,
  updatedAt: DateTime,
  isActive: Boolean,
  bankName: String,
  accountNumber: String,
  initialBalance: Double
}
```

#### Transfers
```
{
  _id: ObjectId,
  userId: String,
  fromAccountId: String,
  toAccountId: String,
  amount: Double,
  description: String,
  transferDate: DateTime,
  createdAt: DateTime,
  status: String (PENDING/COMPLETED/FAILED),
  reference: String
}
```

#### Users
```
{
  _id: ObjectId,
  email: String (unique),
  password: String,
  firstName: String,
  lastName: String,
  createdAt: DateTime,
  updatedAt: DateTime,
  isActive: Boolean,
  phoneNumber: String,
  profilePicture: String,
  preferredCurrency: String (default: USD)
}
```

## 🔒 Security Features

- Transaction edit limit: 12 hours after creation
- Soft delete: Deleted records are marked as deleted but not physically removed
- User isolation: Users can only access their own data
- CORS enabled for specified origins

## 🐛 Troubleshooting

### MongoDB Connection Issues
- Verify MongoDB Atlas connection string
- Check IP whitelist in MongoDB Atlas
- Ensure credentials are correct
- Test connection using MongoDB Compass

### Port Already in Use
```bash
# Linux/Mac
lsof -i :8080
kill -9 <PID>

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Build Failures
```bash
mvn clean install -DskipTests
mvn dependency:tree  # Check dependencies
```

## 🤝 Contributing

1. Fork the repository
2. Create feature branch: `git checkout -b feature/AmazingFeature`
3. Commit changes: `git commit -m 'Add AmazingFeature'`
4. Push to branch: `git push origin feature/AmazingFeature`
5. Open Pull Request

## 📝 License

This project is licensed under the MIT License.

## 📞 Support

For issues and questions, please open an issue in the repository.

---

**Built with ❤️ using Spring Boot and MongoDB**
