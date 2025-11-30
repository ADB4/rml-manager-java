# RML Manager Backend

Spring Boot application for managing RML assets.

Currently has no actual link to RML prod. Mainly using this to acquaint myself with java/spring and things like RBAC,

## Prerequisites

- Java 21
- PostgreSQL 16
- Docker (for PostgreSQL)

## Environment Variables

### Development
```bash
export JWT_SECRET=your-secret-key
export MASTER_USERNAME=master
export MASTER_PASSWORD=your-password
```

### Production
TODO
```bash
export SPRING_PROFILES_ACTIVE=prod
export DATABASE_URL=jdbc:postgresql://host:5432/dbname
export DATABASE_USERNAME=username
export DATABASE_PASSWORD=password
export JWT_SECRET=your-production-secret
export MASTER_USERNAME=admin
export MASTER_PASSWORD=secure-password
```

## Running

### Development
```bash
# Start PostgreSQL
docker-compose up -d

# Run application
./gradlew bootRun
```

### Production
```bash
./gradlew build
java -jar build/libs/rml-manager-*.jar --spring.profiles.active=prod
```

## API Endpoints

### Authentication
- POST `/api/auth/login` - Login
- GET `/api/auth/status` - Check auth status
- POST `/api/auth/logout` - Logout

### Assets
- GET `/api/assets` - Get all assets
- GET `/api/assets/{id}` - Get asset by ID
- POST `/api/assets` - Create asset
- PUT `/api/assets/{id}` - Update asset
- DELETE `/api/assets/{id}` - Delete asset
- GET `/api/assets/search?name={name}` - Search by name
- GET `/api/assets/category/{category}` - Get by category

## Security

- JWT-based authentication
- BCrypt password hashing
- CORS configured for localhost:3000
- Stateless sessions