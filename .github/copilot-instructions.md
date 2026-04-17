# SmartTour – Copilot Agent Instructions

This file gives GitHub Copilot's coding agent the project context it needs to work effectively in this repository.

---

## Project overview

**SmartTour** is an Android mobile application that acts as an intelligent tour guide. It connects to a Spring Boot REST backend backed by a PostgreSQL database.

---

## Technology stack

| Layer | Technology |
|---|---|
| Mobile app | Kotlin, Jetpack Compose, Material 3 |
| Dependency injection | Hilt (Dagger) |
| Networking | Retrofit 2 + Moshi |
| Async | Kotlin Coroutines |
| Backend | Spring Boot (REST API) |
| Database | PostgreSQL |

---

## Project structure

```
app/src/main/java/com/smarttour/
├── data/           # DTOs, repository implementations, mappers
├── domain/         # Domain models (POI, …) and use-case classes
├── network/        # Retrofit API interfaces and RetrofitInstance singleton
├── ui/             # Jetpack Compose screens and ViewModels
│   └── theme/      # Material 3 colour scheme, typography, shapes
├── AppContainer.kt # Manual DI object (no-Hilt path for simple wiring)
└── MainActivity.kt # Single-activity entry point
```

### Naming conventions

- **DTOs** live in `data/` and are named `<Entity>Dto` (e.g. `POIDto`).
- **Domain models** live in `domain/` and use plain names (e.g. `POI`).
- **Mappers** are extension functions in `data/<Entity>Mappers.kt`, mapping `Dto → Domain`.
- **Use cases** live in `domain/` and are named `Get<Something>UseCase`.
- **Screens** are `@Composable` functions in `ui/` named `<Feature>Screen`.
- **ViewModels** are in `ui/` named `<Feature>ViewModel`, using `StateFlow` / `collectAsState()`.

---

## Key configuration values

- **Backend base URL (emulator):** `http://10.0.2.2:8080`
- **Compile SDK:** 34 | **Min SDK:** 24 | **Target SDK:** 34
- **JVM target:** 1.8 | **Kotlin Compose compiler extension:** 1.5.14 (set in `app/build.gradle.kts → composeOptions`)
- **Package name:** `com.example.smarttour` (applicationId) / `com.smarttour` (source)

---

## How to build and test

```bash
# Make gradlew executable (first time only)
chmod +x gradlew

# Compile the app (debug build)
./gradlew assembleDebug

# Run unit tests
./gradlew test

# Run Android instrumented tests (requires a connected device/emulator)
./gradlew connectedAndroidTest

# Run all checks
./gradlew check
```

---

## Adding a new feature

Follow this layer pattern (mirrors the existing `POI` feature):

1. **`data/<Feature>Dto.kt`** – data class matching the JSON from the API.
2. **`data/<Feature>Mappers.kt`** – extension function `<Feature>Dto.toDomain()`.
3. **`domain/<Feature>.kt`** – clean domain model.
4. **`domain/Get<Feature>UseCase.kt`** – use-case class (injected repository).
5. **`network/<Feature>Api.kt`** – Retrofit `interface` with `@GET`/`@POST`/… annotations.
6. **`ui/<Feature>ViewModel.kt`** – `ViewModel` exposing `StateFlow<List<Feature>>`.
7. **`ui/<Feature>Screen.kt`** – `@Composable` screen consuming the ViewModel.

---

## Important constraints

- Do **not** change `compileSdk`, `minSdk`, or `targetSdk` without a clear reason.
- All new screens must use **Jetpack Compose** (no XML layouts).
- Use `StateFlow` + `collectAsState()` for UI state – avoid `LiveData`.
- New dependencies must be added to **`app/build.gradle.kts`** (not the root build file).
- The app uses **Hilt** for DI; annotate new ViewModels with `@HiltViewModel` and inject with `@Inject constructor(…)`.
- Map/location features require `ACCESS_FINE_LOCATION` and `ACCESS_COARSE_LOCATION` permissions in `AndroidManifest.xml`.
