# Backend MVP Modifications

This file records the full backend work for the SmartTour MVP pages.

## Pages Covered

- Explore
- Tour Map
- AR Camera
- POI Detail
- Profile

## What Was Added

- Replaced the one-screen backend slice with a shared MVP backend contract
- Added a common seed catalog so all pages use the same POIs, tours, and profile data
- Added backend models for all five screens
- Added one controller exposing the MVP API surface

## API Endpoints

- `GET /api/explore`
- `GET /api/pois/{poiId}`
- `GET /api/tours/active`
- `GET /api/ar`
- `GET /api/profile`

## Why This Structure

- It keeps the MVP coherent across all screens
- It lets the frontend behave like a real app instead of isolated mock screens
- It provides a smooth path from seeded data to a real database later

## Data Design

- Shared POI records power Explore, POI Detail, AR, and Tour Map
- Shared tour records power Explore and Tour Map
- Shared profile settings power the Profile page

## Next Evolution Path

- move seed data into PostgreSQL + PostGIS
- split controller/service packages by domain if the backend grows
- add authentication and persistence for favorites, downloaded packs, and tour history
