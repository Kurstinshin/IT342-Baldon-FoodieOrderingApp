# Foodie Ordering App — Android (Kotlin)

Native Android app built with **Kotlin + Jetpack Compose**, using the same Spring Boot backend as the web app.

## Project Location
```
IT342_BALDON/
├── backend/            ← Spring Boot (shared)
├── frontend/           ← React web app
└── FoodieApp(Mobile)/  ← This Android project
```

## Tech Stack
| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Networking | Retrofit 2 + OkHttp |
| Images | Coil |
| Navigation | Navigation Compose |
| State | ViewModel + StateFlow |
| Storage | SharedPreferences |

## Screens
- **Login / Register** — Auth with role-based navigation
- **Dashboard** — "Popular Food" grid matching the web UI
- **Cart** — Items with +/− qty controls
- **Checkout** — Payment form → success + order history
- **Order History** — Live status badges (PENDING/COMPLETED/CANCELLED)
- **Admin Dashboard** — Products CRUD + order status updates

## Setup in Android Studio

1. Open Android Studio
2. **File → Open** → select `IT342_BALDON/FoodieApp(Mobile)/`
3. Let Gradle sync complete (downloads dependencies automatically)
4. Start the backend:
   ```powershell
   # In IT342_BALDON/backend/
   $env:JAVA_HOME = "C:\Users\Administrator\.jdks\openjdk-25.0.2"
   $env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
   .\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local" "-DskipTests"
   ```
5. Run on emulator (Pixel 6, API 34 recommended)

## API Base URL
Edit `ApiClient.kt` to change the server:
```kotlin
// Emulator → host localhost
private const val BASE_URL = "http://10.0.2.2:8080/api/v1/"

// Real device on same WiFi → use your PC's local IP
// private const val BASE_URL = "http://192.168.x.x:8080/api/v1/"

// Production (Render)
// private const val BASE_URL = "https://your-app.onrender.com/api/v1/"
```

## Default Admin Account
- **Email**: `admin@foodie.com`
- **Password**: `admin123`

## Project Structure
```
app/src/main/java/edu/cit/baldon/foodiemobile/
├── MainActivity.kt                  ← Entry point + navigation
├── data/
│   ├── model/Models.kt              ← All data classes
│   ├── api/ApiService.kt            ← Retrofit interface
│   ├── api/ApiClient.kt             ← OkHttp + Retrofit singleton
│   └── local/SessionManager.kt     ← JWT + user info storage
├── ui/
│   ├── theme/                       ← Colors, Typography, Theme
│   └── screens/
│       ├── LoginScreen.kt
│       ├── RegisterScreen.kt
│       ├── DashboardScreen.kt       ← Food grid
│       ├── CartScreen.kt
│       ├── CheckoutScreen.kt        ← Payment + order history
│       ├── OrderHistoryScreen.kt
│       └── AdminDashboardScreen.kt  ← Products CRUD + status updates
└── viewmodel/
    ├── AuthViewModel.kt
    ├── DashboardViewModel.kt
    ├── CartViewModel.kt
    └── OrderViewModel.kt
```
