# Frontend MVP Modifications

This file records the Android frontend work for the full SmartTour MVP.

## Pages Covered

- Explore
- Tour Map
- AR Camera
- POI Detail
- Profile

## What Changed

- Replaced the single Explore-only implementation with a full app state model
- Added API DTOs and domain models for all five pages
- Added one repository and one Retrofit API interface for the complete MVP
- Added a root ViewModel to manage:
  - initial data loading
  - bottom navigation
  - POI detail opening
  - AR opening from different entry points
- Added a full Compose app shell with:
  - Explore screen
  - Tour Map screen
  - AR Camera screen
  - POI Detail screen
  - Profile screen

## MVP Behavior Implemented

- Explore cards open POI detail
- Featured tours open Tour Map
- Tour Map stops open POI detail
- Tour Map can open AR
- AR overlays open POI detail
- POI Detail can navigate to Tour Map
- POI Detail can open AR narration
- Bottom navigation switches between Explore, AR, Map, and Profile

## Architecture Used

```text
MainActivity
-> SmartTourViewModel
-> use cases
-> SmartTourRepository
-> Retrofit API
-> Compose app shell and page composables
```

## Notes

- The MVP uses a single-activity Compose architecture
- Navigation is state-driven inside the ViewModel instead of adding a full navigation library layer
- The app is functional against the seeded backend API and can be evolved later without changing the user flow
