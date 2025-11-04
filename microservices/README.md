# OpenCms Microservices Architecture

This project represents the Spring Boot microservices architecture for the OpenCms content management system, migrating from the legacy servlet-based monolithic architecture to a modern, scalable microservices design.

## Architecture Overview

The OpenCms microservices architecture consists of the following services:

### Core Services

1. **API Gateway** (Port 8080)
   - Entry point for all client requests
   - Routes requests to appropriate microservices
   - Handles cross-cutting concerns (authentication, rate limiting, etc.)

2. **Config Service** (Port 8888)
   - Centralized configuration management
   - Provides configuration to all microservices
   - Supports environment-specific configurations

3. **Content Service** (Port 8081)
   - Manages VFS (Virtual File System) resources
   - Handles content CRUD operations
   - Document processing (PDF, Office documents)
   - Search integration (Apache Solr)
   - Image and media processing

4. **User Service** (Port 8082)
   - Authentication and authorization
   - User and group management
   - Session management with Redis
   - JWT token generation and validation
   - Role-based access control

5. **Publishing Service** (Port 8083)
   - Manages content publishing workflows
   - Offline/Online content synchronization
   - Publishing history and audit trails

6. **Rendering Service** (Port 8084)
   - Template rendering (JSP, Thymeleaf)
   - Page composition
   - Fragment caching

7. **Shared Library**
   - Common utilities and domain models
   - Shared across all microservices
   - No runtime deployment (library only)

### Supporting Infrastructure

- **PostgreSQL**: Primary database for all services
- **Redis**: Session storage and caching
- **Apache Solr**: Full-text search engine

## Project Structure

```
opencms-microservices/
├── pom.xml                       # Parent POM
├── docker-compose.yml            # Docker Compose configuration
├── README.md                     # This file
├── api-gateway/                  # API Gateway service
│   ├── pom.xml
│   └── src/main/
│       ├── java/
│       └── resources/
├── content-service/              # Content management service
│   ├── pom.xml
│   └── src/main/
│       ├── java/
│       └── resources/
├── user-service/                 # User management service
│   ├── pom.xml
│   └── src/main/
│       ├── java/
│       └── resources/
├── publishing-service/           # Publishing workflow service
│   ├── pom.xml
│   └── src/main/
│       ├── java/
│       └── resources/
├── rendering-service/            # Template rendering service
│   ├── pom.xml
│   └── src/main/
│       ├── java/
│       └── resources/
├── config-service/               # Configuration service
│   ├── pom.xml
│   └── src/main/
│       ├── java/
│       └── resources/
└── shared-lib/                   # Shared library
    ├── pom.xml
    └── src/main/
        ├── java/
        └── resources/
```

## Technology Stack

- **Java**: 17
- **Spring Boot**: 3.2.0
- **Spring Cloud**: 2023.0.0
- **Build Tool**: Maven
- **Database**: PostgreSQL 15
- **Cache/Session**: Redis 7
- **Search**: Apache Solr 9.8
- **Container Platform**: Docker

## Prerequisites

- Java 17 or higher
- Maven 3.8+
- Docker and Docker Compose (for local development)

## Building the Project

To build all microservices:

```bash
mvn clean install
```

To build a specific service:

```bash
cd <service-directory>
mvn clean package
```

## Running Services Locally

### Using Docker Compose (Recommended)

**First-time setup**: Copy the example environment file and configure your local environment:

```bash
cp .env.example .env
# Edit .env file to set secure passwords for production use
```

Start all services and infrastructure:

```bash
docker-compose up
```

Start services in detached mode:

```bash
docker-compose up -d
```

Stop all services:

```bash
docker-compose down
```

View logs for a specific service:

```bash
docker-compose logs -f <service-name>
```

### Running Individual Services

To run a service without Docker:

```bash
cd <service-directory>
mvn spring-boot:run
```

**Note**: Ensure PostgreSQL, Redis, and Solr are running and accessible before starting individual services.

## Service Ports

| Service | Port | Description |
|---------|------|-------------|
| API Gateway | 8080 | Main entry point |
| Content Service | 8081 | Content management |
| User Service | 8082 | User management |
| Publishing Service | 8083 | Publishing workflows |
| Rendering Service | 8084 | Template rendering |
| Config Service | 8888 | Configuration server |
| PostgreSQL | 5432 | Database |
| Redis | 6379 | Cache/Session store |
| Apache Solr | 8983 | Search engine |

## Configuration

### Environment Variables

#### Docker Compose Environment

For local development with Docker Compose, create a `.env` file in the project root:

```bash
cp .env.example .env
```

Required variables (defined in `.env`):
- `POSTGRES_USER`: PostgreSQL database username
- `POSTGRES_PASSWORD`: PostgreSQL database password (change from default in production!)
- `POSTGRES_DB`: PostgreSQL database name

**Security Note**: The `.env` file is gitignored and should never be committed. Always use strong passwords in production environments.

#### Spring Boot Service Configuration

Each service can be configured using environment variables. Common variables include:

- `SPRING_PROFILES_ACTIVE`: Active Spring profile (dev, test, prod)
- `SPRING_DATASOURCE_URL`: Database connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password

### Application Properties

Service-specific configuration can be placed in:
- `src/main/resources/application.yml`
- `src/main/resources/application-{profile}.yml`

## Database Schema

The microservices architecture maintains compatibility with the OpenCms database schema:

- **Content Service**: Uses `CMS_OFFLINE_*` and `CMS_ONLINE_*` tables for VFS operations
- **User Service**: Uses `CMS_USERS`, `CMS_GROUPS`, `CMS_GROUPUSERS` tables
- **Publishing Service**: Uses `CMS_PUBLISH_HISTORY` and related tables

Database migration scripts will be provided in future updates.

## Migration from Legacy OpenCms

This microservices architecture is designed to gradually replace the legacy servlet-based OpenCms application (from the `Infosys-BNY/opencms-core` repository). Key differences:

### Legacy Architecture
- Gradle 8.4 build system
- Servlet-based (OpenCmsServlet)
- Monolithic WAR deployment
- GWT/Vaadin frontend

### New Architecture
- Maven build system
- Spring Boot microservices
- Independent service deployment
- RESTful APIs
- Modern frontend (to be implemented separately)

## Development Workflow

1. **Setup**: Run `mvn clean install` to build all modules
2. **Local Testing**: Use `docker-compose up` to start services
3. **Development**: Make changes in service modules
4. **Rebuild**: Run `mvn clean package` for modified service
5. **Test**: Restart affected service with Docker Compose

## API Documentation

API documentation will be available via Swagger/OpenAPI at:
- API Gateway: `http://localhost:8080/swagger-ui.html`
- Individual services: `http://localhost:808X/swagger-ui.html`

## Dependencies Mapping

Dependencies have been mapped from the legacy `dependencies.gradle` file:

### Content Service
- Document processing: Apache POI, Tika, PDFBox
- Image processing: JAI ImageIO, metadata-extractor
- XML/HTML parsing: dom4j, jsoup, nekohtml
- Search: Apache Solr
- Caching: Caffeine

### User Service
- Security: Spring Security, Bouncy Castle
- JWT: jjwt
- Session: Spring Session with Redis
- 2FA: TOTP library

### Shared Library
- Utilities: Guava, Apache Commons (lang3, io, codec, text, collections4)

## Testing

Run tests for all services:

```bash
mvn test
```

Run tests for a specific service:

```bash
cd <service-directory>
mvn test
```

## Future Enhancements

- Service discovery with Eureka
- Distributed tracing with Zipkin/Jaeger
- Circuit breakers with Resilience4j
- API rate limiting
- Modern SPA frontend (React/Vue/Angular)
- Kubernetes deployment manifests
- CI/CD pipeline configuration

## Contributing

1. Create a feature branch
2. Make your changes
3. Write/update tests
4. Submit a pull request

## License

This project follows the same license as the original OpenCms project.

## Support

For issues and questions, please refer to the OpenCms documentation or contact the development team.

## Related Repositories

- Legacy OpenCms Core: `Infosys-BNY/opencms-core`
- Frontend (coming soon): TBD

---

**Migration Status**: This is the initial scaffolding phase. Implementation of business logic from the legacy OpenCms application is in progress.
