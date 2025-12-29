rootProject.name = "DarknessAPI"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

include(":darkness-bukkit-api")