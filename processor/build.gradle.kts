plugins {
    kotlin("jvm")
}

group = "org.example.kspsample"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.devtools.ksp:symbol-processing-api:1.6.0-1.0.1")
}
