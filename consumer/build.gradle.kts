plugins {
    id("com.google.devtools.ksp") version "1.6.0-1.0.1"
    kotlin("jvm")
    application
}

group = "org.example.kspsample"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":processor"))
    implementation("io.realm.kotlin:library-base:0.7.0")
    ksp(project(":processor"))
}

application {
    mainClass.set("org.example.kspsample.consumer.TestKt")
}
