# SmartTour Architecture

This project is moving toward a Kotlin-first tourism platform with:

- Android app in Kotlin using Jetpack Compose
- Spring Boot backend in Kotlin
- Geospatial POI search with PostgreSQL + PostGIS
- AR landmark overlays with ARCore + SceneView
- AI-powered narration as an asynchronous backend capability

The current repository already contains a basic Android POI flow. This document turns that into a concrete target architecture and implementation roadmap.

## Product Scope

SmartTour helps travelers:

- discover nearby points of interest
- visualize landmarks in AR
- follow curated tours and route suggestions
- listen to narrated historical and cultural context
- save preferences and personalize visits

## Target Android Architecture

The Android app should follow clean, feature-oriented modularization:

```text
:app
:feature:ar
:feature:map
:feature:tour
:core:domain
:core:data
:core:designsystem
:core:ml
:core:network
:core:database
```

### Module Responsibilities

`app`
- application entry point
- Navigation Compose graph
- Hilt setup
- top-level theme and shared app shell

`feature:ar`
- AR camera screen
- ARCore session management
- SceneView integration
- floating POI labels and landmark anchors

`feature:map`
- Mapbox map screen
- user location display
- nearby POI markers
- route preview

`feature:tour`
- tour list
- POI details
- narration player UI
- favorites and recently viewed content

`core:domain`
- pure Kotlin models
- repository interfaces
- use cases
- no Android framework dependencies

`core:data`
- repository implementations
- DTO to domain mapping
- cache coordination
- offline-first policies

`core:network`
- Retrofit APIs
- OkHttp interceptors
- auth token handling
- network DTOs

`core:database`
- Room entities
- DAOs
- local cache tables for POIs, tours, and saved content

`core:designsystem`
- Compose theme
- reusable cards, chips, buttons, and loading states

`core:ml`
- TensorFlow Lite wrappers
- optional on-device landmark classification or image assistance

## Android Technical Decisions

- UI: Jetpack Compose only
- Navigation: Navigation Compose with single-activity architecture
- DI: Hilt
- Async: Kotlin Coroutines, `Flow`, and `StateFlow`
- Networking: Retrofit + OkHttp + Moshi
- Storage: Room + DataStore
- Maps: Mapbox Maps SDK
- AR: ARCore + SceneView using `AndroidView` only where interop is required

### Presentation Pattern

Each feature screen should follow:

```text
Screen -> ViewModel -> UseCase -> Repository -> DataSource
```

Recommended state split:

- `UiState` for screen rendering
- `UiEvent` for user actions
- `UiEffect` for one-time events such as snackbars or navigation

### Example Android Flows

#### Nearby POIs

```text
Location update
-> Nearby POIs use case
-> Repository checks Room cache
-> Repository refreshes from backend if stale
-> ViewModel exposes StateFlow
-> Compose screen renders cards/map markers
```

#### AR Landmark Overlay

```text
Camera + device pose
-> nearby POIs from repository
-> filter by distance and bearing
-> SceneView nodes anchored in AR scene
-> tap node opens POI detail or narration
```

#### Narration Playback

```text
POI selected
-> request narration metadata/audio URL
-> stream or download audio
-> playback state exposed to Compose UI
```

## Target Backend Architecture

Recommended Gradle multi-module layout:

```text
smarttour-backend/
  |- common/
  |- user-service/
  |- poi-service/
  |- tour-service/
  `- narration-service/
```

### Backend Module Responsibilities

`common`
- shared DTOs
- exception handling
- security config
- base response models
- shared constants and utilities

`user-service`
- registration and login
- JWT issuing and validation
- user profile and preferences
- favorites and history

`poi-service`
- POI CRUD for admin workflows
- nearby POI search
- category filters
- geospatial lookup against PostGIS

`tour-service`
- curated tours
- route generation
- recommendation rules
- personalized itineraries

`narration-service`
- AI prompt orchestration
- text generation
- TTS provider integration
- streaming or asynchronous narration jobs

## Backend Technical Decisions

- Language: Kotlin
- Framework: Spring Boot 3.x
- REST API: Spring Web MVC for standard CRUD APIs
- Reactive/long-running operations: Spring WebFlux
- Security: Spring Security with stateless JWT
- Database: PostgreSQL + PostGIS
- ORM: Spring Data JPA + Hibernate Spatial
- Cache: Redis + Spring Cache
- Async style: Kotlin coroutines with `kotlinx-coroutines-reactor`

## Core Domain Model

These are the main entities we should preserve across Android and backend:

```kotlin
data class Poi(
    val id: Long,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val category: String,
    val imageUrl: String?,
    val audioUrl: String? = null,
    val distanceMeters: Double? = null
)

data class Tour(
    val id: Long,
    val title: String,
    val description: String,
    val poiIds: List<Long>,
    val durationMinutes: Int,
    val coverImageUrl: String?
)

data class UserProfile(
    val id: Long,
    val fullName: String,
    val email: String,
    val preferredLanguage: String,
    val interests: List<String>
)
```

For MVP, the Android app and backend can keep separate model definitions. After stabilization, a shared Kotlin Multiplatform module can hold DTO contracts to reduce drift.

## API Design

Suggested initial endpoints:

### Authentication

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/users/me`

### POIs

- `GET /api/pois`
- `GET /api/pois/nearby?lat={lat}&lng={lng}&radius={meters}`
- `GET /api/pois/{id}`
- `GET /api/pois/categories`

### Tours

- `GET /api/tours`
- `GET /api/tours/{id}`
- `GET /api/tours/recommended`

### Narration

- `POST /api/narration/poi/{id}`
- `GET /api/narration/jobs/{jobId}`

## Geospatial Query Design

Nearby search should be performed in PostGIS using geography-aware distance functions.

Typical approach:

```sql
SELECT id, name, description, category, image_url
FROM pois
WHERE ST_DWithin(
  location,
  ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography,
  :radiusMeters
)
ORDER BY ST_Distance(
  location,
  ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography
);
```

This supports:

- nearby discovery
- AR candidate filtering
- route building around the user

## Security Model

- mobile user logs in and receives JWT access token
- token is sent in `Authorization: Bearer <token>`
- Spring Security validates JWT on protected endpoints
- role separation can be added later for admin POI management

## Current Repository Mapping

The repository currently contains a simple single-module Android app with:

- `POIRepository`
- `GetPOIsUseCase`
- `POIViewModel`
- a Compose `POIScreen`

That is a good MVP seed, but it should evolve as follows:

### Short-Term Refactor

- keep `:app` running
- move domain models and use cases into `:core:domain`
- move repository and DTO mapping into `:core:data`
- move Retrofit APIs into `:core:network`
- introduce Hilt instead of `AppContainer`
- add Room cache before adding AR

### Medium-Term Feature Expansion

- add `:feature:map` for nearby map exploration
- add `:feature:tour` for tour and POI details
- add authentication and profile preferences
- add narration playback support

### Advanced Layer

- add `:feature:ar`
- add backend narration jobs and AI integration
- optionally add `:core:ml` for local classification support

## MVP Delivery Plan

### Phase 1

- nearby POI list
- POI detail screen
- backend POI API
- static or seeded demo data

### Phase 2

- map view with nearby search
- favorites
- login and profile
- Room caching

### Phase 3

- curated tours
- route suggestions
- narration generation and playback

### Phase 4

- AR labels over landmarks
- optional on-device model support
- recommendation tuning and analytics

## Recommended Next Implementation Step

The best next step in this repository is:

1. convert the current single-module Android app into a feature-based structure while preserving the existing POI flow
2. replace `AppContainer` with Hilt
3. add a backend contract for `GET /api/pois/nearby`
4. introduce a POI detail screen and map screen before starting AR work

This sequence gives the project a stable base for the more advanced AR and narration layers.
