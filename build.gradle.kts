import org.gradle.api.JavaVersion
import java.util.Properties

plugins {
    java
    `maven-publish`
    id("net.kyori.indra.git") version "3.1.3" // Keep for Git info access
}

group = "ac.darkness.darknessac" // Or your desired group ID
// VersionUtil appends commit/branch metadata if not a release build
version = "1.0"
description = "DarknessAPI"

// --- Java Configuration ---
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    withSourcesJar()
    withJavadocJar()
    // disableAutoTargetJvm() // Keep if needed
}

// --- Environment Variable Loading for Publishing ---
val envProperties = Properties()
file(".env").takeIf { it.exists() }?.reader(Charsets.UTF_8)?.use(envProperties::load)
fun getEnvVar(name: String): String? =
    System.getenv(name) ?: envProperties.getProperty(name)

// --- Publishing Configuration ---
publishing {
    repositories {
        mavenLocal()
        getEnvVar("MAVEN_REPO_URL")?.let { repoUrl ->
            maven {
                name = getEnvVar("MAVEN_REPO_NAME") ?: "CustomMaven"
                url = uri(repoUrl)
                credentials {
                    username = getEnvVar("MAVEN_USERNAME") ?: ""
                    password = getEnvVar("MAVEN_PASSWORD") ?: ""
                }
            }
        }
    }
    publications.create<MavenPublication>("maven") {
        // Use the calculated version (EPOCH.MAJOR.MINOR.PATCH + optional metadata)
        version = project.version.toString()
        from(components["java"])
    }
}

// --- Repositories for Dependencies ---
repositories {
    mavenLocal()
    maven("https://jitpack.io/")
    maven("https://repo.viaversion.com")
    mavenCentral()
}

// --- Dependencies ---
dependencies {
    compileOnly("org.jetbrains:annotations:24.1.0")
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    testCompileOnly("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")
    // Add test framework if needed
}

// --- Test Task Configuration ---
tasks.test {
    useJUnitPlatform() // If using JUnit 5
}