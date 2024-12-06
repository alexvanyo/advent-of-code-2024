plugins {
    kotlin("jvm") version "2.1.0"
}

sourceSets {
    main {
        kotlin.srcDir("src")
        dependencies {
            implementation("org.jetbrains.compose.ui:ui-unit:1.7.1")
            implementation("org.jetbrains.compose.ui:ui-geometry:1.7.1")
        }
    }
}

tasks {
    wrapper {
        gradleVersion = "8.11.1"
    }
}
