plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:1.6.3")
    implementation("com.charleskorn.kaml:kaml:0.91.0")

    // 테스트 의존성
    testImplementation("io.kotest:kotest-runner-junit5:5.8.1")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}