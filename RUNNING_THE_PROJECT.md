# Running SmartTour MVP

This project now has two runnable parts:

- Android app in the root project
- Spring Boot backend in `backend/explore-service`

## 1. Start the Backend

Open a terminal in:

```text
C:\Users\PC\Desktop\Study\ENSIASD SDBDAI\S4\Mobile\SmartTour\backend\explore-service
```

Run:

```powershell
.\gradlew.bat bootRun
```

The backend starts on:

```text
http://localhost:8080
```

Available MVP endpoints:

- `GET /api/explore`
- `GET /api/pois/{poiId}`
- `GET /api/tours/active`
- `GET /api/ar`
- `GET /api/profile`

## 2. Start the Android App

Open a second terminal in:

```text
C:\Users\PC\Desktop\Study\ENSIASD SDBDAI\S4\Mobile\SmartTour
```

Build the app:

```powershell
.\gradlew.bat :app:assembleDebug
```

Or open the project in Android Studio and run the `app` module on an emulator.

## 3. Important Emulator Note

The app uses:

```text
http://10.0.2.2:8080
```

This is correct for the Android emulator because `10.0.2.2` points to your computer's localhost.

## 4. MVP Flow

Once both parts are running:

- `Explore` loads nearby highlights and tours from the backend
- `Tour Map` loads the active route
- `AR` loads the landmark overlay data
- `POI Detail` loads the selected landmark details
- `Profile` loads the traveler summary and settings
