# SmartTour Conception Graphs

These graphs are derived from the preview in [smarttour_app_screens.html](C:/Users/PC/Desktop/smarttour_app_screens.html) and the current SmartTour Kotlin conception.

## 1. App Screen Map

This graph captures the primary navigation structure visible in the preview.

```mermaid
graph TD
    Explore["Explore"]
    AR["AR Camera"]
    Map["Tour Map"]
    Detail["POI Detail"]
    Profile["Profile"]

    Explore -->|"Tap nearby highlight"| Detail
    Explore -->|"Start AR"| AR
    Explore -->|"Open featured tour"| Map
    Explore -->|"Bottom nav"| Profile

    AR -->|"Tap landmark overlay"| Detail
    AR -->|"Open active route"| Map
    AR -->|"Bottom nav"| Explore

    Map -->|"Tap next stop"| Detail
    Map -->|"Open AR for landmark view"| AR
    Map -->|"Bottom nav"| Explore

    Detail -->|"Navigate"| Map
    Detail -->|"Play AI narration"| AR
    Detail -->|"Add to tour"| Map

    Profile -->|"Saved tours or preferences influence"| Explore
```

## 2. User Journey Graph

This graph shows the main user journey suggested by the mockups.

```mermaid
flowchart LR
    A["Launch app"] --> B["Explore nearby highlights"]
    B --> C["Open POI detail"]
    B --> D["Open featured tour"]
    B --> E["Open AR camera"]

    C --> F["Listen to AI narration"]
    C --> G["Add POI to tour"]
    C --> H["Navigate to stop"]

    D --> I["Follow route on tour map"]
    I --> C

    E --> J["See live landmark overlay"]
    J --> C
    J --> F

    H --> I
    G --> I

    B --> K["Open profile"]
    K --> L["Adjust language, voice, offline packs"]
    L --> B
```

## 3. Feature Capability Graph

This graph organizes the preview into feature domains.

```mermaid
graph TB
    SmartTour["SmartTour Mobile App"]

    Explore["Explore Feature"]
    Tour["Tour Feature"]
    AR["AR Feature"]
    Narration["Narration Feature"]
    Profile["Profile Feature"]

    SmartTour --> Explore
    SmartTour --> Tour
    SmartTour --> AR
    SmartTour --> Narration
    SmartTour --> Profile

    Explore --> ExploreA["Nearby highlights"]
    Explore --> ExploreB["Featured tours"]
    Explore --> ExploreC["Search places"]

    Tour --> TourA["Active route"]
    Tour --> TourB["Waypoints"]
    Tour --> TourC["Next stop guidance"]

    AR --> ARA["Camera view"]
    AR --> ARB["Landmark overlay cards"]
    AR --> ARC["Distance and era context"]

    Narration --> N1["AI story generation"]
    Narration --> N2["Audio playback"]
    Narration --> N3["Narration entry from AR and detail"]

    Profile --> P1["Traveler stats"]
    Profile --> P2["Language and voice"]
    Profile --> P3["Offline packs"]
```

## 4. Android Conception Graph

This graph connects the visible screens to the Kotlin Android implementation direction.

```mermaid
graph TD
    UI["Jetpack Compose UI"]
    Nav["Navigation Compose"]
    VM["ViewModels with StateFlow"]
    Domain["Use Cases"]
    Repo["Repositories"]
    Local["Room + DataStore"]
    Remote["Retrofit APIs"]
    ARCore["ARCore + SceneView"]
    Mapbox["Mapbox SDK"]
    Audio["Narration Player"]

    UI --> Nav
    Nav --> VM
    VM --> Domain
    Domain --> Repo
    Repo --> Local
    Repo --> Remote

    UI --> ARCore
    UI --> Mapbox
    UI --> Audio
```

## 5. Backend Conception Graph

This graph mirrors the mobile experience with Spring Boot services.

```mermaid
graph TD
    Mobile["Android App"]

    Gateway["Spring Boot API"]
    User["User Service"]
    Poi["POI Service"]
    Tour["Tour Service"]
    Narration["Narration Service"]
    DB["PostgreSQL + PostGIS"]
    Cache["Redis"]
    AI["AI + TTS Providers"]

    Mobile --> Gateway
    Gateway --> User
    Gateway --> Poi
    Gateway --> Tour
    Gateway --> Narration

    Poi --> DB
    Tour --> DB
    User --> DB

    Poi --> Cache
    Tour --> Cache

    Narration --> AI
    Narration --> Cache
```

## 6. MVP Conception Sequence

This graph suggests the safest build order from the preview.

```mermaid
flowchart TD
    P1["Phase 1: Explore + POI Detail"] --> P2["Phase 2: Tour Map + Nearby search"]
    P2 --> P3["Phase 3: AI Narration"]
    P3 --> P4["Phase 4: AR Camera overlays"]
    P4 --> P5["Phase 5: Personalization + offline polish"]
```

## Notes

- The preview indicates `POI detail` is the central pivot screen between discovery, route guidance, and narration.
- `AR camera` should be treated as an advanced surface layered on top of existing POI and tour data, not as the first implemented feature.
- `Profile` is mostly a settings and personalization boundary rather than a discovery surface.
- `Narration` is a cross-cutting capability triggered from both AR and POI detail.
