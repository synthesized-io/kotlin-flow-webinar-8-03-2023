import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.10"
    application
}

group = "io.synthesized.kotlin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.testcontainers:postgresql:1.17.6")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.jooq:jooq:3.17.7")
    implementation("org.slf4j:slf4j-api:2.0.6")
    implementation("ch.qos.logback:logback-classic:1.4.5")
    implementation("org.postgresql:postgresql:42.5.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("io.github.microutils:kotlin-logging:3.0.5")
}

application {
    mainClass.set("io.synthesized.kotlin.AppKt")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
