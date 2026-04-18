# SmartTour Modernization Guide

## Overview

This guide documents the transformation of SmartTour from a static, seed-data-driven prototype to a fully dynamic application using modern libraries and best practices.

---

## Backend Modernization (Spring Boot)

### 1. Database Layer (PostgreSQL + PostGIS + Hibernate Spatial)

**Location**: `backend/explore-service/src/main/kotlin/com/smarttour/explore/domain/`

**Files Created**:
- `entity/PointOfInterest.kt` - POI with geospatial location
- `entity/User.kt` - User profile and authentication
- `entity/Tour.kt` - Tour composition and metadata
- `entity/Narration.kt` - AI narration content
- `entity/RoutePoint.kt` - Tour waypoints with positions
- `entity/UserData.kt` - User preferences and saved tours

**Capabilities**:
- Geospatial queries: `ST_DWithin` for nearby POI search
- Relationship management: Tours contain multiple POIs
- Location-based filtering at the database level
- Cascading updates and deletions

**Configuration** (`application.yml`):
```yaml
spring.datasource.url: jdbc:postgresql://localhost:5432/smarttour
spring.jpa.properties.hibernate.dialect: org.hibernate.spatial.dialect.postgis.PostgisDialect
```

**Usage Example**:
```kotlin
// Find POIs within 500m radius
poiRepository.findNearby(userLocation, 500.0)
```

---

### 2. Repositories (Spring Data JPA)

**Location**: `backend/explore-service/src/main/kotlin/com/smarttour/explore/domain/repository/`

**Repositories Created**:
- `PointOfInterestRepository` - POI queries with geospatial filters
- `TourRepository` - Tour searches and recommendations
- `UserRepository` - User authentication and lookup
- `NarrationRepository` - Narration retrieval by language

**Example**:
```kotlin
interface PointOfInterestRepository : JpaRepository<PointOfInterest, String> {
    @Query("SELECT p FROM PointOfInterest p WHERE " +
           "ST_DWithin(p.location, :userLocation, :radiusMeters) = true")
    fun findNearby(userLocation: Point, radiusMeters: Double): List<PointOfInterest>
}
```

---

### 3. Security (Spring Security + JWT)

**Location**: `backend/explore-service/src/main/kotlin/com/smarttour/explore/security/`

**Files Created**:
- `JwtProvider.kt` - Token generation and validation with JJWT
- `JwtAuthenticationFilter.kt` - Request interceptor for token extraction

**Features**:
- Stateless authentication (no sessions)
- HS512 signing algorithm
- Configurable expiration (default: 24 hours)
- Automatic token validation on each request

**Configuration** (`application.yml`):
```yaml
jwt:
  secret: your-secret-key-here
  expiration: 86400000  # 24 hours in ms
```

**Usage**:
```bash
# Get token
POST /api/auth/login
{
  "email": "user@example.com",
  "password": "password"
}
# Response: { "token": "eyJhbGc..." }

# Use token in requests
GET /api/profile
Authorization: Bearer eyJhbGc...
```

---

### 4. Caching (Redis + Spring Cache)

**Location**: `backend/explore-service/src/main/kotlin/com/smarttour/explore/config/CacheConfig.kt`

**Cached Endpoints**:
- `getExploreFeed()` - 20 minutes
- `getPoiDetail(poiId)` - 1 hour per POI
- `getTourMap()` - 20 minutes
- `getArCamera(poiId)` - 1 hour per POI
- `getProfile()` - 30 minutes

**Usage**:
```kotlin
@Cacheable("pois", key = "#poiId")
fun getPoiDetail(poiId: String): PoiDetailResponse = ...
```

**Configuration** (`application.yml`):
```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
  cache:
    type: redis
```

---

### 5. Async/Reactive AI Narration (Spring WebFlux)

**Location**: `backend/explore-service/src/main/kotlin/com/smarttour/explore/api/service/NarrationService.kt`

**Features**:
- Non-blocking narration generation
- Reactive streams with `Mono` and `Flux`
- Background processing with `boundedElastic()` scheduler
- Language-specific narration support

**Usage**:
```kotlin
@PostMapping("/narrations/generate")
fun generateNarration(@RequestBody request: NarrationRequest): Mono<Map<String, String>> {
    return narrationService.generateNarrationAsync(
        request.poiId,
        request.language,
        request.text
    )
}
```

---

### 6. REST API Controllers

**Location**: `backend/explore-service/src/main/kotlin/com/smarttour/explore/api/web/SmartTourController.kt`

**Endpoints**:

| Method | Endpoint | Returns |
|--------|----------|---------|
| GET | `/api/explore` | ExploreFeedResponse (nearby POIs + featured tours) |
| GET | `/api/pois/{poiId}` | PoiDetailResponse (full POI details) |
| GET | `/api/tours/active` | TourMapResponse (active tour with stops) |
| GET | `/api/ar` | ArCameraResponse (AR overlays) |
| GET | `/api/profile` | ProfileResponse (user stats) |
| POST | `/api/narrations/generate` | Mono<NarrationResponse> (async) |
| GET | `/api/pois/{poiId}/narrations` | Flux<NarrationResponse> (stream) |
| GET | `/api/pois/{poiId}/narrations/{language}` | Mono<NarrationResponse> (specific) |

---

### 7. Build Configuration (Gradle Kotlin DSL)

**File**: `backend/explore-service/build.gradle.kts`

**Key Dependencies**:
- Spring Boot 3.3.4
- PostgreSQL Driver 42.7.1
- Hibernate Spatial 6.2.10.Final
- JJWT 0.12.3 (JWT)
- Spring WebFlux (Reactive)
- Lettuce (Redis client)
- Jackson for JSON serialization

---

## Android Modernization

### 1. Dependency Injection (Hilt)

**Location**: `app/src/main/java/com/smarttour/di/AppModule.kt`

**Modules Created**:
- `NetworkModule` - Retrofit/OkHttp/Moshi configuration
- `DatabaseModule` - Room database setup
- `RepositoryModule` - Repository implementations

**Usage**:
```kotlin
@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val poiRepository: PoiRepository
) : ViewModel() { }

// In Activity/Fragment
@Inject
lateinit var viewModel: ExploreViewModel
```

**build.gradle.kts**:
```kotlin
id("com.google.dagger.hilt.android")
id("kotlin-kapt")
```

---

### 2. Network Layer (Retrofit + OkHttp + Moshi)

**Location**: `app/src/main/java/com/smarttour/data/remote/SmartTourApiService.kt`

**API Interface**:
```kotlin
interface SmartTourApiService {
    @GET("api/explore")
    suspend fun getExploreFeed(): ExploreFeedResponse

    @GET("api/pois/{poiId}")
    suspend fun getPoiDetail(@Path("poiId") poiId: String): PoiDetailResponse
    
    @GET("api/ar")
    suspend fun getArCamera(@Query("poiId") poiId: String?): ArCameraResponse
}
```

**Features**:
- Moshi JSON deserialization with Kotlin adapter factory
- OkHttp logging interceptor for debugging
- Suspend functions for coroutine support
- Type-safe JSON models with `@JsonClass`

**Configuration** (`AppModule.kt`):
```kotlin
@Singleton
@Provides
fun provideMoshi(): Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
```

---

### 3. Local Database (Room)

**Location**: `app/src/main/java/com/smarttour/data/local/`

**Database** (`SmartTourDatabase.kt`):
```kotlin
@Database(
    entities = [PoiEntity::class, TourEntity::class, UserEntity::class],
    version = 1
)
abstract class SmartTourDatabase : RoomDatabase() {
    abstract fun poiDao(): PoiDao
    abstract fun tourDao(): TourDao
    abstract fun userDao(): UserDao
}
```

**Entities**:
- `PoiEntity` - Cached POI data with favorites
- `TourEntity` - Cached tour data with save status
- `UserEntity` - Local user profile

**DAOs** (`Daos.kt`):
- `PoiDao` - Query/insert/update POIs with Flow support
- `TourDao` - Tour management
- `UserDao` - User profile access

**Usage**:
```kotlin
@Dao
interface PoiDao {
    @Query("SELECT * FROM pois WHERE isFavorite = 1")
    fun getFavoritePois(): Flow<List<PoiEntity>>
}
```

---

### 4. Preferences (DataStore)

**Location**: `app/src/main/java/com/smarttour/data/preferences/PreferencesManager.kt`

**Managed Preferences**:
- `language` - User language preference
- `is_logged_in` - Authentication status
- `user_id` - Current user ID
- `auth_token` - JWT token (stored securely)
- `dark_mode` - Theme preference
- `notifications_enabled` - Push notification settings
- `offline_mode` - Offline capability toggle

**Usage**:
```kotlin
@Inject
lateinit var prefsManager: PreferencesManager

// Reading
val language: Flow<String> = prefsManager.languageFlow

// Writing
viewModelScope.launch {
    prefsManager.setLanguage("fr")
}
```

---

### 5. Domain Models (Moshi Serialization)

**Location**: `app/src/main/java/com/smarttour/domain/model/ApiModels.kt`

**Models**:
- `ExploreFeedResponse` - Feed with nearby POIs + featured tours
- `PoiDetailResponse` - Full POI information
- `TourMapResponse` - Active tour with waypoints
- `ArCameraResponse` - AR overlay data
- `ProfileResponse` - User profile and stats

**Annotations**:
```kotlin
@JsonClass(generateAdapter = true)
data class PoiDetailResponse(
    val id: String,
    val name: String,
    val rating: Double,
    // ...
)
```

---

### 6. Repository Pattern

**Location**: `app/src/main/java/com/smarttour/domain/repository/`

**Interface** (`PoiRepository.kt`):
```kotlin
interface PoiRepository {
    suspend fun getExploreFeed(): Flow<List<PoiDetailResponse>>
    suspend fun getPoiDetail(poiId: String): PoiDetailResponse
    suspend fun getNearbyPois(lat: Double, lon: Double, radiusMeters: Double): Flow<List<PoiDetailResponse>>
    suspend fun searchPois(query: String): Flow<List<PoiDetailResponse>>
}
```

**Implementation** (`PoiRepositoryImpl.kt`):
- Fetches from remote API
- Caches in local Room database
- Falls back to cache on network failure

---

### 7. Navigation (Navigation Compose)

**To Implement**: Single Activity with Navigation Compose graph

**Planned Structure**:
```kotlin
@Composable
fun SmartTourNavigation(navController: NavHostController) {
    NavHost(navController, startDestination = "explore") {
        composable("explore") { ExploreScreen() }
        composable("poi/{poiId}") { PoiDetailScreen() }
        composable("tour/{tourId}") { TourMapScreen() }
        composable("ar/{poiId}") { ArCameraScreen() }
        composable("profile") { ProfileScreen() }
    }
}
```

---

### 8. ARCore + SceneView Integration

**Library**: ARCore + SceneView 2.1.1

**To Implement**:
- AR session initialization
- Landmark detection and rendering
- Proximity-based POI overlays
- Hit testing for interaction

**Usage Structure**:
```kotlin
@Composable
fun ArCameraScreen(viewModel: ArCameraViewModel) {
    ArSceneView(
        engine = engine,
        view = arView,
        onArSessionCreated = { session -> viewModel.setupSession(session) }
    )
}
```

---

### 9. Mapbox Maps Integration

**Library**: Mapbox Maps SDK 11.0.0 + Compose Extension

**To Implement**:
- User location display
- POI markers with clusters
- Route visualization
- Tap-to-navigate functionality

**Usage Structure**:
```kotlin
@Composable
fun TourMapScreen() {
    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        initialState = rememberMapViewState(startLocation = CameraOptions(...))
    ) {
        // Add markers, routes, user location
    }
}
```

---

### 10. TensorFlow Lite (Optional ML)

**Library**: TensorFlow Lite 2.14.0 + Support Library

**Potential Uses**:
- Landmark classification from camera frames
- On-device image preprocessing
- Gesture recognition in AR mode

---

### 11. Gradle Configuration

**File**: `app/build.gradle.kts`

**Key Dependencies Added**:
- Hilt 2.48 + Hilt Navigation Compose
- Retrofit 2.9.0 + Moshi 1.15.1
- OkHttp 4.12.0 + Logging Interceptor
- Room 2.6.1
- DataStore Preferences 1.0.0
- Navigation Compose 2.7.7
- ARCore 1.42.0 + SceneView 2.1.1
- Mapbox Maps 11.0.0 + Compose
- TensorFlow Lite 2.14.0

---

## Data Flow

### Explore Screen
1. **Repository** calls `SmartTourApiService.getExploreFeed()`
2. **API** queries database: HOT POIs + Featured Tours
3. **Response** cached in Redis (~20 min)
4. **Local cache** (Room) stores for offline access
5. **UI** binds via `Flow<State>`

### POI Detail Screen
1. **User** taps POI from list
2. **Repository** calls `getPoiDetail(poiId)`
3. **API** queries database + fetches narrations
4. **Backend** generates AI narration reactively if missing
5. **UI** displays details + plays narration

### AR Screen
1. **User** taps "View in AR"
2. **Controller** gets ARCore overlay data
3. **Backend** returns nearby POIs + metrics
4. **ARCore** renders 3D models + overlays
5. **SceneView** handles touch interactions

### Authentication Flow
1. **User** enters email + password
2. **LoginViewModel** calls `authService.login()`
3. **Backend** validates credentials, returns JWT
4. **DataStore** saves token securely
5. **JwtAuthenticationFilter** includes token in all requests
6. **Backend** validates token expiration

---

## Setup Instructions

### Backend Setup

```bash
# 1. Install PostgreSQL with PostGIS extension
# Create database
createdb smarttour
createuser smarttour_user --password smarttour_password

# 2. Enable PostGIS
psql -d smarttour -c "CREATE EXTENSION postgis;"

# 3. Start Redis
redis-server

# 4. Build and run
cd backend/explore-service
./gradlew bootRun

# Runs on http://localhost:8080
```

### Android Setup

```bash
# 1. Add base URL to AppModule
val baseUrl = BuildConfig.API_BASE_URL  // "http://10.0.2.2:8080/" for emulator

# 2. Update AndroidManifest.xml with permissions
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.CAMERA" />

# 3. Build and run
./gradlew assembleDebug

# Test login
POST /api/auth/login
{
  "email": "user@example.com",
  "password": "password"
}
```

---

## Migration Checklist

- [x] Backend: Gradle multi-module setup with Kotlin DSL
- [x] Backend: PostgreSQL + PostGIS + Hibernate Spatial
- [x] Backend: Spring Data JPA repositories
- [x] Backend: Spring Security + JWT authentication
- [x] Backend: Redis caching configuration
- [x] Backend: Spring WebFlux for reactive narration
- [x] Backend: Updated REST controllers
- [ ] Android: Setup Hilt DI *(in progress)*
- [ ] Android: Configure Retrofit + OkHttp + Moshi
- [ ] Android: Implement Room database
- [ ] Android: Add DataStore preferences
- [ ] Android: Create Navigation Compose graph
- [ ] Android: Integrate ARCore + SceneView
- [ ] Android: Add Mapbox Maps
- [ ] Android: Setup TensorFlow Lite models
- [ ] Integration: End-to-end testing
- [ ] Deployment: Docker containers for backend
- [ ] Deployment: Firebase/Play Store for Android

---

## Performance Considerations

1. **Database Indexes**: POI location (`idx_location` on `geometry` column)
2. **Redis TTLs**: Varies by endpoint (30 min - 1 day)
3. **Room Pagination**: Implement for large POI lists
4. **Coroutine Scopes**: Use `viewModelScope` for lifecycle safety
5. **LazyColumn**: For infinite scroll in Compose lists
6. **Image Optimization**: Compress/cache downloaded images

---

## Security Checklist

- [x] JWT stateless authentication
- [ ] HTTPS/TLS enforcement (prod)
- [ ] Rate limiting on auth endpoints
- [ ] Secure token storage (DataStore, not SharedPreferences)
- [ ] SQL injection prevention (JPA parameterized queries)
- [ ] CORS configuration (restrict origins in prod)
- [ ] API key rotation for MapBox/ARCore

---

## Next Steps

1. **Database Seeding**: Add sample POIs, users, and tours
2. **Login UI**: Implement authentication screen
3. **Error Handling**: Add proper exception handling + user feedback
4. **Testing**: Add unit tests for repositories + ViewModels
5. **Analytics**: Track user behavior and POI engagement
6. **Offline Support**: Expand Room database usage
7. **Push Notifications**: Firebase Cloud Messaging integration

