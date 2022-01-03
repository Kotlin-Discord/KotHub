import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application

    `maven-publish`

    kotlin("jvm")
    kotlin("plugin.serialization")

    id("com.github.jakemarsden.git-hooks")
    id("io.gitlab.arturbosch.detekt")
    id("org.cadixdev.licenser")
}

group = "com.kotlindiscord.kothub"
version = "1.0-SNAPSHOT"

repositories {
    // You can remove this if you're not testing locally-installed KordEx builds
    mavenLocal()

    maven {
        name = "Kotlin Discord"
        url = uri("https://maven.kotlindiscord.com/repository/maven-public/")
    }
}

dependencies {
    detektPlugins(libs.detekt)

    implementation(libs.kotlin.stdlib)
    implementation(libs.kx.ser)

    implementation(libs.bundles.ktor)

    // Logging dependencies
    testImplementation(libs.groovy)
    testImplementation(libs.jansi)
    testImplementation(libs.logback)
    testImplementation(libs.logging)

    testImplementation(libs.bundles.ktor.test)
}

gitHooks {
    setHooks(
        mapOf("pre-commit" to "detekt updateLicenses")
    )
}

tasks.withType<KotlinCompile> {
    // Current LTS version of Java
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}

val sourceJar = task("sourceJar", Jar::class) {
    dependsOn(tasks["classes"])
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

tasks {
    build {
        finalizedBy(sourceJar)
    }
}

java {
    // Current LTS version of Java
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    explicitApi()
}

detekt {
    buildUponDefaultConfig = true
    config = rootProject.files("detekt.yml")
}

license {
    setHeader(rootProject.file("LICENSE"))
    ignoreFailures(System.getenv()["CI"] == null)
}

publishing {
    repositories {
        maven {
            name = "KotDis"

            url = if (project.version.toString().contains("SNAPSHOT")) {
                uri("https://maven.kotlindiscord.com/repository/maven-snapshots/")
            } else {
                uri("https://maven.kotlindiscord.com/repository/maven-releases/")
            }

            credentials {
                username = project.findProperty("kotdis.user") as String? ?: System.getenv("KOTLIN_DISCORD_USER")
                password = project.findProperty("kotdis.password") as String?
                    ?: System.getenv("KOTLIN_DISCORD_PASSWORD")
            }

            version = project.version
        }
    }

    publications {
        create<MavenPublication>("maven") {
            from(components.getByName("java"))

            artifact(sourceJar)
        }
    }
}
