# Finance Data Application

A Quarkus-based application for managing finance data, specifically exchange rates between currencies. Built with MySQL database integration and Hibernate ORM for data persistence.

## Features

* Real-time currency exchange rate management
* MySQL database integration
* High-performance with Quarkus framework
* Cloud-native architecture
* Low memory consumption
* Quick startup times

## Prerequisites

* MySQL Database
* Quarkus CLI (for local development)
* Maven

## Database Setup

Create the MySQL database:

```sql
CREATE DATABASE IF NOT EXISTS finance_db;
```

Configure database connection in `application.properties`:

```properties
# Database configuration
quarkus.datasource.db-kind=mysql
quarkus.datasource.username=root
quarkus.datasource.password=
quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/finance_db

# Hibernate ORM settings
quarkus.hibernate-orm.dialect=org.hibernate.dialect.MySQLDialect
quarkus.hibernate-orm.log.sql=true
quarkus.hibernate-orm.sql-load-script=import.sql
```

## Hibernate Schema Management

### Configuration Options

The `hibernate-orm.database.generation` property controls schema management:

* `none`: Manual schema management
* `update`: Automatic schema updates without data loss
* `validate`: Schema validation only
* `create`: Drop and recreate schema on startup
* `create-drop`: Drop schema on shutdown

### Environment-Specific Configurations

Development:
```properties
quarkus.hibernate-orm.database.generation=update
```

Production:
```properties
quarkus.hibernate-orm.database.generation=validate
```

## Troubleshooting Database Issues

### Handling "Table Already Exists" Error

If you encounter this error, try one of these solutions:

1. Use update mode:
```properties
quarkus.hibernate-orm.database.generation=update
```

2. Drop existing table:
```sql
DROP TABLE IF EXISTS finance_data;
```

3. Enable validation mode:
```properties
quarkus.hibernate-orm.database.generation=validate
```

4. Manually align table schema with entity class

## Application Configuration

### Database Connection

```properties
quarkus.datasource.db-kind=mysql
quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/finance_db
quarkus.datasource.username=root
quarkus.datasource.password=
```

### Hibernate Settings

```properties
quarkus.hibernate-orm.dialect=org.hibernate.dialect.MySQLDialect
quarkus.hibernate-orm.log.sql=true
quarkus.hibernate-orm.sql-load-script=import.sql
```

### OpenAPI Documentation

```properties
quarkus.smallrye-openapi.info.title=Finance Data API
quarkus.smallrye-openapi.info.version=1.0.0
quarkus.smallrye-openapi.path=/openapi
```

## Running the Application

Start the development server:
```bash
./mvnw quarkus:dev
```

Access points:
* Application: `http://localhost:8080`
* OpenAPI documentation: `http://localhost:8080/openapi`
* OpenAPI Swagger: `http://localhost:8080/q/swagger-ui/`

## Initial Data Import

Create `import.sql` in the resources directory:

```sql
-- Initial exchange rates
INSERT INTO finance_data (base_currency, target_currency, exchange_rate)
VALUES 
    ('USD', 'EUR', 0.85),
    ('EUR', 'GBP', 0.75);
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           ├── entity/
│   │           │   └── FinanceData.java
│   │           ├── repository/
│   │           │   └── FinanceDataRepository.java
│   │           ├── service/
│   │           │   └── FinanceDataService.java
│   │           └── resource/
│   │               └── FinanceDataResource.java
│   └── resources/
│       ├── application.properties
│       └── import.sql
└── test/
    └── java/
        └── com/
            └── example/
                └── FinanceDataTest.java
```

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.