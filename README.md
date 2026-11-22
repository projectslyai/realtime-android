# Courseexe Realtime Android SDK (Kotlin)

Official Android Kotlin client SDK for **Courseexe Realtime Channels** (`https://courseexe.com`).

---

## Installation via JitPack

### 1. Add repository to your `settings.gradle.kts` (or root `build.gradle.kts`):

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### 2. Add dependency to your app's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.projectslyai:realtime-android:v1.0.0")
}
```

---

## Usage

```kotlin
import com.courseexe.realtime.CourseexeRealtimeClient

// Initialize client with your public key from courseexe.com/realtime/dashboard
val client = CourseexeRealtimeClient(
    key = "YOUR_APP_KEY",
    host = "courseexe.com",
    port = 443,
    secure = true
)

// Subscribe to a public channel
val channel = client.subscribe("orders")

// Listen to incoming live events
channel.on("order-created") { data ->
    println("Received real-time event: $data")
}
```

---

## Repository & Source
- **GitHub**: [https://github.com/projectslyai/realtime-android](https://github.com/projectslyai/realtime-android)
- **Documentation**: [https://courseexe.com/realtime/docs#sdk-mobile](https://courseexe.com/realtime/docs#sdk-mobile)

---

## License
MIT © [Courseexe](https://courseexe.com) & [PROJECTSLY AI SOLUTIONS](https://projectsly.in)
