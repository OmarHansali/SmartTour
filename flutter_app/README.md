# SmartTour – Flutter Frontend (Map Page)

This directory contains the **Flutter front-end** for the SmartTour app's **Map page** (1 of 5 screens).  
The Kotlin Android module in `../app/` remains the **backend** (REST API server); this Flutter app acts as the mobile client.

---

## Architecture

```
flutter_app/
├── lib/
│   ├── main.dart               ← App entry-point, theme, DI wiring
│   ├── app_container.dart      ← Manual DI (mirrors Kotlin AppContainer)
│   │
│   ├── data/
│   │   ├── api/
│   │   │   └── map_api_service.dart     ← HTTP client (mirrors Kotlin MapApiService)
│   │   ├── dto/
│   │   │   ├── tour_route_dto.dart      ← JSON → domain mapping
│   │   │   └── tour_stop_dto.dart
│   │   └── repository/
│   │       └── map_repository_impl.dart
│   │
│   ├── domain/
│   │   ├── model/
│   │   │   ├── tour_route.dart          ← Pure domain model
│   │   │   └── tour_stop.dart
│   │   ├── repository/
│   │   │   └── map_repository.dart      ← Abstract interface
│   │   └── use_case/
│   │       ├── get_active_tour_map_use_case.dart
│   │       └── mark_stop_visited_use_case.dart
│   │
│   └── ui/
│       └── map/
│           ├── map_screen.dart          ← Full map page UI
│           ├── map_view_model.dart      ← ChangeNotifier ViewModel
│           ├── map_ui_state.dart        ← Sealed state union
│           └── components/
│               ├── map_top_bar.dart     ← AppBar with tour name + back
│               ├── next_stop_card.dart  ← Bottom card (progress + mark visited)
│               └── tour_map_legend.dart ← Map legend overlay
│
└── test/
    └── tour_route_test.dart    ← Domain model unit tests
```

---

## Features (Map Page)

| Feature | Details |
|---|---|
| **OSM Map** | OpenStreetMap tiles via `flutter_map` – no API key required |
| **Route polyline** | Connects all stops in order |
| **Colour-coded markers** | 🔵 Current · 🟢 Visited · ⚫ Upcoming |
| **Map legend** | Top-left overlay showing the three states |
| **Bottom card** | Current stop name, progress bar, next stop, "Mark Visited" button |
| **Marker tap sheet** | Tap any marker to see stop details + mark-visited action |
| **Optimistic updates** | UI updates immediately; reverts on backend error |
| **Loading / Error states** | Spinner during fetch; error card with retry button |

---

## Backend API Contract

The Flutter app calls the **same endpoints** as the Kotlin Retrofit client:

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/tours/{tourId}/route` | Fetch full route + stops |
| `POST` | `/api/tours/{tourId}/stops/{stopId}/visit` | Mark a stop as visited |

Default base URL: `http://10.0.2.2:8080` (Android emulator → host machine).  
Override by passing `baseUrl` to `AppContainer(baseUrl: '...')` in `main.dart`.

---

## Running Locally

```bash
# 1. Install Flutter SDK (https://flutter.dev/docs/get-started/install)
cd flutter_app
flutter pub get
flutter run          # connects to emulator / physical device
```

Run unit tests:

```bash
flutter test
```

---

## Integrating with the other 4 pages

Replace the direct `home: MapScreen(...)` in `main.dart` with a proper
`GoRouter` (or `Navigator`) that routes between all 5 screens.  The `MapScreen`
widget accepts `tourId` and an optional `onBackClick` callback, making it
drop-in ready for any navigation setup.
