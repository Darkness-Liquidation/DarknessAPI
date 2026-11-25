plugins {
    `java-library`
    `maven-publish`
}

group = "com.github.Darkness-Liquidation"
version = "1.2.2"
description = "DarknessAPI"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(22))
    withSourcesJar()
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:24.1.0")
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(17)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.Darkness-Liquidation"
            artifactId = "DarknessAPI"
            version = "1.2.2"
            from(components["java"])
        }
    }
}
