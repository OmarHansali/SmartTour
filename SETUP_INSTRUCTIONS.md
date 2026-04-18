# SmartTour Setup Instructions

## Prerequisites

### System Requirements
- **Java 21** (for backend)
- **PostgreSQL 15+** (with PostGIS extension)
- **Redis 7+** (for caching)
- **Android Studio Iguana+** (for frontend)
- **Git** (for version control)
- **Gradle 8.x** (bundled with projects)

### Recommended IDEs
- **Backend**: IntelliJ IDEA Community/Ultimate
- **Android**: Android Studio

---

## Backend Setup (Spring Boot)

### Step 1: Install PostgreSQL with PostGIS

#### macOS (Homebrew)
```bash
brew install postgresql@15
brew install postgis
brew services start postgresql@15
```

#### Ubuntu/Debian
```bash
sudo apt-get install postgresql-15 postgresql-15-postgis
sudo systemctl start postgresql
```

#### Windows (Installer)
1. Download from https://www.postgresql.org/download/windows/
2. Run installer, remember the `postgres` password
3. Install PostGIS via Stack Builder (included in installer)

### Step 2: Create Database and User

```bash
# Connect to PostgreSQL
c

# Create database
CREATE DATABASE smarttour;

# Create user
CREATE USER smarttour_user WITH PASSWORD 'smarttour_password';

# Grant privileges
GRANT ALL PRIVILEGES ON DATABASE smarttour TO smarttour_user;

# Enable PostGIS extension
\c smarttour
CREATE EXTENSION postgis;
CREATE EXTENSION postgis_topology;

# Verify installation
SELECT PostGIS_Version();
-- Should show: 3.4.0

# Exit psql
\q
```

### Step 3: Install Redis

#### macOS (Homebrew)
```bash
brew install redis
brew services start redis
```

#### Ubuntu/Debian
```bash
sudo apt-get install redis-server
sudo systemctl start redis-server
```

#### Windows (Subsystem for Linux or Docker)
```bash
# Using WSL
wsl
sudo apt-get install redis-server
redis-server

# Or using Docker
docker run -d -p 6379:6379 redis:latest
```

### Step 4: Verify PostgreSQL & Redis

```bash
# Test PostgreSQL connection
psql -U smarttour_user -d smarttour -h localhost
\q

# Test Redis connection
redis-cli
> ping
# Should respond: PONG
> exit
```

### Step 5: Build and Run Backend

```bash
# Navigate to project
cd backend/explore-service

# Build
./gradlew build

# Run (development)
./gradlew bootRun

# You should see:
# Started ExploreServiceApplication in X.XXX seconds
# Tomcat started on port(s): 8080
```

**Verify Backend**:
```bash
# In a new terminal
curl http://localhost:8080/api/explore
# Should return JSON response
```

### Optional: Spring Boot DevTools

Edit `backend/explore-service/src/main/resources/application.yml`:
```yaml
spring:
  devtools:
    restart:
      enabled: true
```

This enables automatic restart on file changes.

---

## Android Setup

### Step 1: Clone/Open Project

```bash
# Clone (if needed)
git clone <repo-url>

# Open in Android Studio
open -a "Android Studio" app/
```

### Step 2: Update Dependencies

The `build.gradle.kts` files have been updated with:
- Hilt 2.48
- Retrofit 2.9.0 + Moshi 1.15.1
- Room 2.6.1
- DataStore Preferences 1.0.0
- Navigation Compose 2.7.7
- ARCore 1.42.0 + SceneView 2.1.1
- Mapbox Maps 11.0.0

```bash
# Sync Gradle (Automatic in Android Studio)
# Or manual:
./gradlew --refresh-dependencies
```

### Step 3: Create local.properties

Create file `local.properties` in project root:

```properties
# For Mapbox Maps
MAPBOX_DOWNLOADS_TOKEN=<your-mapbox-token>

# For API endpoints
API_BASE_URL=http://10.0.2.2:8080/
```

**Get Mapbox Token**:
1. Go to https://account.mapbox.com/tokens/
2. Create a "Public" token
3. Copy and paste into `local.properties`

### Step 4: Create AndroidManifest.xml Permissions

File: `app/src/main/AndroidManifest.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- Internet Access -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <!-- Location Services -->
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

    <!-- Camera (for ARCore) -->
    <uses-permission android:name="android.permission.CAMERA" />

    <!-- Storage (for offline maps) -->
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

    <application>
        <!-- Activities will be defined here -->
    </application>

</manifest>
```

### Step 5: Update API Base URL

Edit `app/src/main/java/com/smarttour/di/AppModule.kt`:

```kotlin
@Singleton
@Provides
fun provideSmartTourApiService(
    moshi: Moshi,
    okHttpClient: OkHttpClient
): SmartTourApiService {
    return Retrofit.Builder()
        // For emulator:
        .baseUrl("http://10.0.2.2:8080/")
        // For physical device on same network:
        // .baseUrl("http://192.168.1.XXX:8080/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(SmartTourApiService::class.java)
}
```

### Step 6: Configure Emulator (if using)

```bash
# List available devices
emulator -list-avds

# Start emulator with internet access
emulator -avd <device-name> -netspeed full -netdelay none

# Enable internet access on emulator
adb shell setprop net.hostname smarttour-emulator
```

### Step 7: Build and Run App

```bash
# Build
./gradlew assembleDebug

# Install and run
./gradlew installDebug

# Or in Android Studio:
# Run > Run 'app'
```

**Expected Result**: App launches, can call backend API successfully

---

## Verify Everything Works

### Checklist

- [ ] PostgreSQL is running and accepts connections
- [ ] PostGIS extension is installed
- [ ] Redis server is running
- [ ] Backend built successfully
- [ ] Backend running on http://localhost:8080
- [ ] `curl http://localhost:8080/api/explore` returns JSON
- [ ] Android Studio project opens without errors
- [ ] Gradle sync completes successfully
- [ ] Emulator starts and app installs
- [ ] App can call backend successfully

### Test Requests

```bash
# Terminal 1: Start backend
cd backend/explore-service
./gradlew bootRun

# Terminal 2: Test API
curl -X GET http://localhost:8080/api/explore \
  -H "Content-Type: application/json"

# Expected: JSON response with exploreFeed data

# Test Redis caching
redis-cli
> KEYS *
# Should show cache keys after first request

# Test database
psql -U smarttour_user -d smarttour
SELECT * FROM pois;
# Should show seeded data (if seeds added)
```

---

## Environment Configuration

### Backend (application.yml)

```yaml
server:
  port: 8080

spring:
  application:
    name: explore-service

  datasource:
    url: jdbc:postgresql://localhost:5432/smarttour
    username: smarttour_user
    password: smarttour_password
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: validate  # Use 'create' for first run, then 'validate'
    properties:
      hibernate:
        dialect: org.hibernate.spatial.dialect.postgis.PostgisDialect
        format_sql: true

  data:
    redis:
      host: localhost
      port: 6379
      timeout: 2000

  cache:
    type: redis

jwt:
  secret: your-secret-key-here-change-in-production
  expiration: 86400000  # 24 hours

logging:
  level:
    org.springframework.web: INFO
    org.hibernate.spatial: DEBUG
    com.smarttour: DEBUG
```

### Android (build.gradle.kts)

Ensure these are present:

```kotlin
kapt {
    correctErrorTypes = true
}

android {
    // ... other config ...
    
    buildTypes {
        debug {
            buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:8080/\"")
        }
        release {
            buildConfigField("String", "API_BASE_URL", "\"https://api.smarttour.com/\"")
        }
    }
}
```

---

## Troubleshooting

### PostgreSQL Connection Failed

```
error: connection to server at "localhost" (127.0.0.1), port 5432 failed
```

**Solution**:
```bash
# Check if PostgreSQL is running
pg_isready

# Start PostgreSQL
brew services start postgresql@15  # macOS
sudo systemctl start postgresql   # Linux
```

### PostGIS Not Available

```
ERROR: relation "spatial_ref_sys" does not exist
```

**Solution**:
```bash
psql -U smarttour_user -d smarttour
CREATE EXTENSION postgis;
CREATE EXTENSION postgis_topology;
```

### Redis Connection Failed

```
Failed to get a resource from the pool
```

**Solution**:
```bash
# Check if Redis is running
redis-cli ping

# Start Redis
brew services start redis  # macOS
sudo systemctl start redis-server  # Linux
```

### Port 8080 Already in Use

```
Address already in use
```

**Solution**:
```bash
# Find process on port 8080
lsof -i :8080

# Kill process
kill -9 <PID>

# Or change port in application.yml
server.port: 8081
```

### Android App Can't Connect to Backend

**Problem**: `GrpcWebCallCredentials error`

**Solution**:
1. Check emulator has internet: `adb shell ping 8.8.8.8`
2. Use `10.0.2.2` instead of `localhost` (emulator special IP)
3. Or use physical device on same network: `192.168.x.x`

```kotlin
val baseUrl = if (BuildConfig.DEBUG) {
    "http://10.0.2.2:8080/"  // Emulator
} else {
    "https://api.smarttour.com/"  // Production
}
```

### Gradle Build Fails

```
Could not find org.gradle.api:gradle-api:X.X.X
```

**Solution**:
```bash
# Clear Gradle cache
rm -rf ~/.gradle/caches/

# Rebuild
./gradlew clean build
```

---

## Next Steps After Setup

1. **Add Seed Data**:
   ```sql
   INSERT INTO pois (id, name, description, location) VALUES 
   ('poi-001', 'Koutoubia Mosque', 'Largest mosque in Marrakech', 
    ST_SetSRID(ST_MakePoint(-8.010, 31.629), 4326));
   ```

2. **Create Authentication Endpoints**:
   - Implement login/register in `AuthController.kt`
   - Add user creation logic

3. **Build UI Screens** (with Compose Navigation):
   - Explore screen (with POI list)
   - POI detail screen
   - Tour map screen
   - Login screen

4. **Integrate AR & Maps**:
   - ARCore surface in AR screen
   - Mapbox in Tour map screen

5. **Add Tests**:
   - Unit tests for repositories
   - Integration tests for API
   - UI tests with Compose testing

---

## Useful Commands

```bash
# Backend
./gradlew bootRun                    # Start backend
./gradlew test                       # Run tests
./gradlew build                      # Full build
./gradlew clean                      # Clean build

# Android
./gradlew assembleDebug              # Build APK
./gradlew installDebug               # Install on emulator/device
./gradlew connectedAndroidTest       # Run tests on device
./gradlew detekt                     # Code quality analysis

# Database
psql -U smarttour_user -d smarttour  # Connect to DB
\dt                                  # List tables
\d pois                              # Describe table
SELECT count(*) FROM pois;           # Count records

# Redis
redis-cli                            # Connect to Redis
KEYS *                               # List all keys
FLUSHALL                             # Clear all cache
```

---

## IDE Configuration

### IntelliJ IDEA (Backend)

1. **Open Project**: File > Open > `backend/explore-service`
2. **SDK Setup**: File > Project Structure > Project > JDK 21
3. **Gradle**: Settings > Build, Execution, Deployment > Gradle
   - Select "Gradle wrapper" option
4. **Run Configuration**: Run > Edit Configurations
   - Add "Gradle" task: `bootRun`
5. **Database**: Database tool window > + > PostgreSQL
   - Host: localhost, Port: 5432, User: smarttour_user

### Android Studio (Frontend)

1. **Open Project**: Open `app` folder
2. **SDK Manager**: Tools > SDK Manager
   - Install SDK 34, Build Tools 34.x
3. **Emulator**: Tools > Device Manager > Create Device
   - Select Pixel 6, Android 14
4. **Run**: Run > Run 'app'
5. **Debugging**: Debug > Debug 'app'

---

## Production Checklist

Before deploying to production:

### Backend
- [ ] Change JWT secret to strong random value
- [ ] Enable HTTPS/TLS
- [ ] Setup rate limiting
- [ ] Configure CORS for specific origins
- [ ] Enable SQL query logging (disable in production)
- [ ] Setup database backups
- [ ] Configure monitoring/logging (ELK, Datadog)
- [ ] Use connection pooling (HikariCP)
- [ ] Enable Spring Security headers

### Android
- [ ] Disable logging interceptors
- [ ] Remove hardcoded API base URLs
- [ ] Enable ProGuard/R8 obfuscation
- [ ] Setup Firebase Analytics
- [ ] Configure API key restrictions
- [ ] Test on multiple devices/Android versions
- [ ] Setup crash reporting (Firebase Crashlytics)
- [ ] Code signing with production keystore

### DevOps
- [ ] Containerize backend (Docker)
- [ ] Setup CI/CD pipeline (GitHub Actions)
- [ ] Configure environment-specific configs
- [ ] Load balancer for backend
- [ ] CDN for static assets
- [ ] Database replication/failover
- [ ] Monitoring and alerting

