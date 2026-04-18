# Frontend Explore Modifications

This file records the frontend changes made for the `Explore` section vertical slice.

## What Was Changed

- Replaced the broken Android entry point with a clean `MainActivity`
- Switched the app from a plain POI list to an Explore home feed
- Added new DTO, domain, repository, use case, and ViewModel layers for `Explore`
- Redesigned the Compose screen to match the preview structure:
  - greeting
  - title
  - search bar placeholder
  - nearby highlights cards
  - featured tours cards
- Added internet permission for backend calls
- Aligned the app namespace and application ID with the actual package name `com.smarttour`

## Files Added

- `app/src/main/java/com/smarttour/data/ExploreDto.kt`
- `app/src/main/java/com/smarttour/data/ExploreMappers.kt`
- `app/src/main/java/com/smarttour/data/ExploreRepository.kt`
- `app/src/main/java/com/smarttour/domain/ExploreFeed.kt`
- `app/src/main/java/com/smarttour/domain/GetExploreFeedUseCase.kt`
- `app/src/main/java/com/smarttour/ui/ExploreViewModel.kt`
- `app/src/main/java/com/smarttour/ui/ExploreScreen.kt`

## Files Updated

- `app/src/main/java/com/smarttour/MainActivity.kt`
- `app/src/main/java/com/smarttour/AppContainer.kt`
- `app/src/main/java/com/smarttour/network/POIApi.kt`
- `app/src/main/AndroidManifest.xml`
- `app/build.gradle.kts`

## Frontend Data Flow

```text
MainActivity
-> ExploreViewModel
-> GetExploreFeedUseCase
-> ExploreRepository
-> RetrofitInstance /api/explore
-> ExploreScreen renders UI state
```

## UI Decisions Made

- kept the design light and close to the preview colors
- used one aggregated feed instead of multiple screen requests
- treated `Explore` as the home surface for the app
- left navigation actions as visual structure for now, without implementing full screen routing yet

## Cleanup Included

- fixed a broken `MainActivity.kt` file that previously contained mapper code
- preserved the repo’s simple architecture style for now instead of forcing a large refactor in the same step

## Next Frontend Steps

- add click navigation from highlights to `POI detail`
- add featured tour navigation to `Tour map`
- add loading skeletons and retry action
- move from manual `AppContainer` wiring to Hilt
