# Backend Explore Modifications

This file records the backend changes made for the `Explore` section vertical slice.

## What Was Added

- Created a new Spring Boot Kotlin backend in `backend/explore-service`
- Added a dedicated `Explore` API endpoint at `GET /api/explore`
- Added response models for:
  - explore feed container
  - nearby highlight POIs
  - featured tours
- Added an application bootstrap class and configuration

## Backend Structure Added

```text
backend/explore-service/
  |- build.gradle.kts
  |- settings.gradle.kts
  `- src/main/
      |- kotlin/com/smarttour/explore/
      |   |- ExploreServiceApplication.kt
      |   `- explore/
      |       |- model/ExploreFeedResponse.kt
      |       |- service/ExploreCatalogService.kt
      |       `- web/ExploreController.kt
      `- resources/application.yml
```

## API Introduced

### `GET /api/explore`

Purpose:
- return the Explore home screen payload in one request

Payload includes:
- greeting and home title
- search placeholder
- nearby highlights
- featured tours

## Data Strategy Used

- Seeded in-memory sample data for Marrakech landmarks and tours
- Chosen intentionally so the frontend can be built immediately without waiting for a database

## Why This Design

- The Explore screen in the preview is feed-oriented
- A single aggregated endpoint reduces frontend orchestration complexity
- This keeps the MVP fast while still matching a future service split

## Next Backend Steps

- move seed data to PostgreSQL + PostGIS
- add `GET /api/pois/{id}`
- add radius-based nearby search
- connect featured tours to stored route definitions
