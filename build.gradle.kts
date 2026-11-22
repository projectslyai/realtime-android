plugins {
    kotlin("jvm") version "1.9.22"
    `maven-publish`
}

group = "com.github.projectslyai"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.java-websocket:Java-WebSocket:1.5.6")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            groupId = "com.github.projectslyai"
            artifactId = "realtime-android"
            version = "1.0.0"
        }
    }
}
