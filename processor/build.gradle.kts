plugins {
    kotlin("jvm")
    `maven-publish`
}

group = "org.example.kspsample"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.6.0-1.0.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.6.0")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:1.4.6")
    testImplementation("io.realm.kotlin:library-base:0.7.0")
}

publishing {
    publications {
        register("processor", MavenPublication::class.java) {
            from(components["java"])
        }
    }
}
