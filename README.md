# SmartTour

A smart tour guide mobile application built with **Flutter** frontend and **Kotlin/Spring Boot** backend. SmartTour helps travelers discover nearby places, experience augmented reality guidance, plan optimized trips, and manage their travel profile.

## Architecture

```
SmartTour/
├── backend/          # Kotlin/Spring Boot REST API
│   ├── src/main/kotlin/com/smarttour/
│   │   ├── model/    # JPA Entities (Place, User, Favorite, etc.)
│   │   ├── repository/  # Spring Data JPA Repositories
│   │   ├── service/     # Business Logic & External APIs
│   │   ├── controller/  # REST API Endpoints
│   │   └── dto/         # Request/Response Data Transfer Objects
│   └── src/main/resources/
│       └── data.sql     # Demo data seeding
│
└── frontend/         # Flutter Mobile App
    ├── lib/
    │   ├── models/      # Data models
    │   ├── services/    # API client
    │   ├── pages/       # UI Screens (Map, AR, Trips, Profile)
    │   └── main.dart    # App entry point
    └── android/         # Android-specific config
```

## Features

### 1. Maps Page
- Real-time user location tracking
- Google Maps integration with custom markers
- Nearby places discovery with distance calculation
- Place details with ratings, categories, and addresses
- Mark places as visited or add to favorites

### 2. AR Guide Page
- Live camera preview with AR overlay
- Object detection and scene description
- Compass heading display
- Nearest place finder with directional guidance
- Visual arrow pointing to points of interest

### 3. Trip Planner Page
- AI-powered trip generation from nearby places
- Route optimization using nearest-neighbor algorithm
- Customizable search radius, max places, and categories
- Step-by-step route visualization
- Estimated duration and walking distance

### 4. Profile Page
- User profile with avatar and statistics
- Favorite places management
- Visit history with ratings and notes
- Travel preferences tracking

## Technology Stack

### Backend
- **Kotlin** with Spring Boot 3.2
- **Spring Data JPA** for database access
- **H2 Database** (development) / easily switchable to PostgreSQL/MySQL
- **WebFlux** for async HTTP client (Google Maps API)
- **Google Maps API** integration (Places, Directions)
- **Gradle** build system

### Frontend
- **Flutter** 3.x with Dart
- **google_maps_flutter** for interactive maps
- **camera** for AR camera preview
- **geolocator** for GPS positioning
- **flutter_compass** for directional AR
- **http** for REST API communication

## API Endpoints

### Maps
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/maps/nearby` | Find nearby places |
| POST | `/api/maps/nearby/google` | Search via Google Places API |
| GET | `/api/maps/place/{id}` | Get place details |
| POST | `/api/maps/directions` | Get route directions |
| GET | `/api/maps/distance` | Calculate distance & bearing |

### AR
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/ar/analyze` | Analyze scene & find nearest place |

### Trips
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/trips` | Generate optimized trip |
| GET | `/api/trips/user/{userId}` | Get user's trips |
| GET | `/api/trips/{tripId}` | Get trip details |

### Profile
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/profile/{userId}` | Get user profile |
| GET | `/api/profile/{userId}/favorites` | List favorites |
| POST | `/api/profile/favorites` | Add favorite |
| DELETE | `/api/profile/{userId}/favorites/{placeId}` | Remove favorite |
| GET | `/api/profile/{userId}/history` | Visit history |
| POST | `/api/profile/history` | Record a visit |

## Setup Instructions

### Prerequisites
- Java 17+
- Flutter SDK 3.0+
- Android Studio / Xcode (for mobile emulators)
- Google Maps API Key

### Backend Setup

1. Navigate to the backend directory:
```bash
cd smarttour/backend
```

2. Set your Google Maps API key (optional, demo data works without it):
```bash
export GOOGLE_MAPS_API_KEY=your_api_key_here
```

3. Build and run:
```bash
./gradlew bootRun
```

The backend will start on `http://localhost:8081`

Access the H2 database console at: `http://localhost:8081/h2-console`
- JDBC URL: `jdbc:h2:mem:smarttourdb`
- Username: `sa`
- Password: (leave empty)

### Frontend Setup

1. Navigate to the frontend directory:
```bash
cd smarttour/frontend
```

2. Install dependencies:
```bash
flutter pub get
```

3. Update the Google Maps API key in `android/app/src/main/AndroidManifest.xml`:
```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_GOOGLE_MAPS_API_KEY" />
```

4. Update the backend URL in `lib/services/api_service.dart` if needed:
```dart
// For Android Emulator
static const String baseUrl = 'http://10.0.2.2:8081/api';
// For real device, use your computer's IP address
```

5. Run the app:
```bash
flutter run
```

## Demo Data

The application comes pre-seeded with:
- **1 Demo User** with profile
- **12 Places** around New York City (museums, parks, cafes, landmarks)
- **3 Favorites** (Museum, Cathedral, Tower)
- **4 Visit History** entries with ratings and notes

## Configuration

### application.yml
```yaml
server:
  port: 8081

spring:
  datasource:
    url: jdbc:h2:mem:smarttourdb
  jpa:
    hibernate:
      ddl-auto: create-drop

smarttour:
  google-maps:
    api-key: ${GOOGLE_MAPS_API_KEY:your_key}
  trip:
    max-places-per-trip: 5
    default-radius-meters: 5000
```

## Switching to Production Database

To use PostgreSQL or MySQL instead of H2:

1. Add the database driver to `build.gradle.kts`:
```kotlin
runtimeOnly("org.postgresql:postgresql")
```

2. Update `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/smarttour
    username: your_username
    password: your_password
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    defer-datasource-initialization: false
  sql:
    init:
      mode: never
```

## Future Enhancements
- User authentication (JWT/OAuth2)
- Real ML-based object detection integration
- Offline map caching
- Push notifications for nearby places
- Social sharing of trips
- Integration with ride-sharing services

## License
MIT
