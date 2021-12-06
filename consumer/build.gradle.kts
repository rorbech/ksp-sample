plugins {
    id("com.google.devtools.ksp") version "1.6.0-1.0.1"
    kotlin("jvm")
}

group = "org.example.kspsample"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":processor"))
    ksp(project(":processor"))
}
