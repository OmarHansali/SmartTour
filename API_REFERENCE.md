# SmartTour API Quick Reference

## Base URL
```
http://localhost:8080/api
```

## Authentication
All requests (except `/auth/**` and `/public/**`) require JWT token:
```
Authorization: Bearer <jwt_token>
```

---

## Endpoints

### Explore Feed
**Get nearby POIs and featured tours**

```http
GET /api/explore
```

**Response** (200 OK):
```json
{
  "city": "Marrakech",
  "greeting": "Good morning",
  "title": "Explore SmartTour",
  "searchPlaceholder": "Search places, monuments...",
  "nearbyHighlights": [
    {
      "id": "poi-001",
      "name": "Koutoubia Mosque",
      "description": "Historic mosque",
      "category": "Landmark",
      "distanceMeters": 250.5,
      "rating": 4.8,
      "imageUrl": "https://...",
      "latitude": 31.629,
      "longitude": -8.010
    }
  ],
  "featuredTours": [
    {
      "id": "tour-001",
      "title": "Medina Walking Tour",
      "description": "Explore the old city",
      "durationMinutes": 180,
      "stopCount": 5,
      "accentColorHex": "#FF6B35"
    }
  ]
}
```

---

### POI Detail
**Get full details for a single POI**

```http
GET /api/pois/{poiId}
```

**Path Parameters**:
- `poiId` (string, required): POI identifier

**Response** (200 OK):
```json
{
  "id": "poi-001",
  "name": "Koutoubia Mosque",
  "subtitle": "Landmark",
  "status": "Open",
  "ratingLabel": "4.8 ★",
  "distanceLabel": "250 m",
  "admissionLabel": "Free",
  "eraLabel": "12th Century",
  "description": "The Koutoubia Mosque is the largest mosque in Marrakech...",
  "narrationTitle": "AI narration ready",
  "narrationSubtitle": "Listen to facts in your language",
  "imageUrl": "https://...",
  "actions": {
    "canAddToTour": true,
    "canNavigate": true,
    "canPlayNarration": true
  }
}
```

**Error** (404 Not Found):
```json
{
  "error": "POI not found"
}
```

---

### Active Tour Map
**Get the active tour with all stops and route**

```http
GET /api/tours/active
```

**Response** (200 OK):
```json
{
  "title": "Tour map",
  "badge": "Active tour",
  "progressLabel": "1/5",
  "activeTourId": "tour-001",
  "nextStop": {
    "poiId": "poi-001",
    "name": "Koutoubia Mosque",
    "distanceLabel": "250 m",
    "walkTimeLabel": "~4 min walk",
    "isCurrent": true
  },
  "stops": [
    {
      "poiId": "poi-001",
      "name": "Koutoubia Mosque",
      "distanceLabel": "250 m",
      "walkTimeLabel": "~4 min walk",
      "isCurrent": true
    },
    {
      "poiId": "poi-002",
      "name": "Jemaa el-Fnaa",
      "distanceLabel": "500 m",
      "walkTimeLabel": "~6 min walk",
      "isCurrent": false
    }
  ],
  "routePoints": [
    { "x": 0.1, "y": 0.8, "type": "start" },
    { "x": 0.35, "y": 0.38, "type": "waypoint" },
    { "x": 0.9, "y": 0.2, "type": "destination" }
  ]
}
```

---

### AR Camera Overlays
**Get AR overlay data for a POI**

```http
GET /api/ar?poiId={poiId}
```

**Query Parameters**:
- `poiId` (string, optional): Focus on this POI. Defaults to nearest.

**Response** (200 OK):
```json
{
  "focusedPoiId": "poi-001",
  "focusedPoiName": "Koutoubia Mosque",
  "focusedPoiSubtitle": "Landmark",
  "actionHint": "The Koutoubia Mosque is the largest mosque...",
  "metrics": [
    { "label": "Distance", "value": "250 m" },
    { "label": "Rating", "value": "4.8 ★" },
    { "label": "Category", "value": "Landmark" }
  ],
  "overlays": [
    {
      "poiId": "poi-001",
      "name": "Koutoubia Mosque",
      "subtitle": "Landmark",
      "distanceMeters": 250.0,
      "rating": 4.8,
      "eraLabel": "Historic"
    }
  ],
  "nowPlaying": {
    "title": "Koutoubia Mosque",
    "subtitle": "Now playing narration",
    "isPlaying": true
  }
}
```

---

### User Profile
**Get logged-in user profile and statistics**

```http
GET /api/profile
Authorization: Bearer <jwt_token>
```

**Response** (200 OK):
```json
{
  "fullName": "John Traveler",
  "initials": "JT",
  "levelLabel": "Explorer Level 5",
  "stats": [
    { "value": "42", "label": "Tours Completed" },
    { "value": "156", "label": "POIs Visited" },
    { "value": "€2,450", "label": "Saved" }
  ],
  "settings": [
    { "label": "Notifications", "value": "Enabled" },
    { "label": "Offline Maps", "value": "Downloaded" },
    { "label": "Language", "value": "English" }
  ]
}
```

---

## Narration Endpoints (Async/Reactive)

### Generate Narration
**Asynchronously generate narration for a POI**

```http
POST /api/narrations/generate
Content-Type: application/json

{
  "poiId": "poi-001",
  "language": "en",
  "text": "The Koutoubia Mosque is the largest mosque in Marrakech..."
}
```

**Response** (202 Accepted):
```json
{
  "narrationId": "narr-001",
  "audioUrl": "https://example.com/narration/poi-001/en.mp3",
  "duration": "125"
}
```

**Note**: This is reactive. Polling `/api/pois/{poiId}/narrations/{language}` to check status.

---

### Get Narrations by POI
**Stream all narrations for a POI**

```http
GET /api/pois/{poiId}/narrations
```

**Response** (200 OK - Stream):
```json
[
  {
    "id": "narr-001",
    "language": "en",
    "audioUrl": "https://example.com/narration/poi-001/en.mp3",
    "duration": "125"
  },
  {
    "id": "narr-002",
    "language": "fr",
    "audioUrl": "https://example.com/narration/poi-001/fr.mp3",
    "duration": "140"
  }
]
```

---

### Get Narration by Language
**Get narration for a POI in specific language**

```http
GET /api/pois/{poiId}/narrations/{language}
```

**Path Parameters**:
- `poiId` (string): POI identifier
- `language` (string): Language code (e.g., `en`, `fr`, `es`, `ar`)

**Response** (200 OK):
```json
{
  "id": "narr-001",
  "language": "en",
  "audioUrl": "https://example.com/narration/poi-001/en.mp3",
  "duration": "125"
}
```

**Error** (404 Not Found):
```json
{
  "error": "Narration not found for language: de"
}
```

---

## Authentication (Placeholder for Phase 2)

### Login
**Authenticate user and get JWT token**

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response** (200 OK):
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "expiresIn": 86400,
  "user": {
    "id": "user-001",
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Traveler"
  }
}
```

**Error** (401 Unauthorized):
```json
{
  "error": "Invalid credentials"
}
```

---

## Error Responses

### 400 Bad Request
```json
{
  "error": "Invalid request format",
  "details": "poiId parameter is required"
}
```

### 401 Unauthorized
```json
{
  "error": "Invalid or expired token"
}
```

### 404 Not Found
```json
{
  "error": "POI not found"
}
```

### 500 Internal Server Error
```json
{
  "error": "An unexpected error occurred",
  "timestamp": "2026-04-16T10:30:00Z"
}
```

---

## Caching Behavior

| Endpoint | Cache Key | TTL | Invalidation |
|----------|-----------|-----|--------------|
| `GET /api/explore` | `exploreFeed` | 20 min | Manual |
| `GET /api/pois/{poiId}` | `poiDetail:{poiId}` | 1 hour | Manual |
| `GET /api/tours/active` | `tourMap` | 20 min | Manual |
| `GET /api/ar` | `arCamera:{poiId}` | 1 hour | Manual |
| `GET /api/profile` | `profile` | 30 min | Manual |

**Note**: Cache is stored in Redis. Clear manually by:
```bash
# Via Redis CLI
FLUSHALL
```

---

## Rate Limiting (Recommended for Production)

```
- Unauthenticated: 10 requests/minute
- Authenticated: 100 requests/minute
- Auth endpoints: 5 requests/minute
```

---

## CORS Configuration

**Allowed Origins**: Configured in `WebFluxConfig.kt`
- Current: `*` (all origins)
- For production: Restrict to specific domains

**Allowed Methods**: GET, POST, PUT, DELETE, OPTIONS

**Allowed Headers**: Content-Type, Authorization

**Max Age**: 3600 seconds (1 hour)

---

## Testing Endpoints

### cURL Examples

```bash
# Get explore feed
curl http://localhost:8080/api/explore

# Get POI details
curl http://localhost:8080/api/pois/poi-001

# Get active tour
curl http://localhost:8080/api/tours/active

# Get AR camera overlays
curl http://localhost:8080/api/ar?poiId=poi-001

# Get profile (requires auth)
curl http://localhost:8080/api/profile \
  -H "Authorization: Bearer eyJhbGc..."

# Generate narration (async)
curl -X POST http://localhost:8080/api/narrations/generate \
  -H "Content-Type: application/json" \
  -d '{"poiId":"poi-001","language":"en","text":"Description..."}'

# Get narrations by POI
curl http://localhost:8080/api/pois/poi-001/narrations

# Get narration in specific language
curl http://localhost:8080/api/pois/poi-001/narrations/en
```

### Postman Collection

Import this as a Postman collection:
```json
{
  "info": {
    "name": "SmartTour API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Explore",
      "request": {
        "method": "GET",
        "url": "{{base_url}}/api/explore"
      }
    }
  ],
  "variable": [
    {
      "key": "base_url",
      "value": "http://localhost:8080"
    },
    {
      "key": "jwt_token",
      "value": ""
    }
  ]
}
```

---

## Performance Metrics

**Typical Response Times** (with warm Redis cache):
- GET /api/explore: ~50-100ms
- GET /api/pois/{id}: ~30-50ms
- GET /api/tours/active: ~40-80ms
- GET /api/profile: ~20-40ms
- POST /api/narrations/generate: ~1000-5000ms (background job)

**Cold Cache** (first request):
- Add ~200-500ms for database query
- Add ~100ms for Redis write

---

## Pagination (Future Enhancement)

```http
GET /api/explore?page=0&size=20&sort=rating,desc
```

```json
{
  "content": [ ... ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "totalElements": 456,
    "totalPages": 23
  }
}
```

---

## Filtering (Future Enhancement)

```http
GET /api/pois?category=landmark&minRating=4.0&maxDistance=1000
```

---

## API Versioning (Future)

```http
GET /api/v1/explore
GET /api/v2/explore
```

Current endpoints are `/api/**` (will become `/api/v1/**`)
