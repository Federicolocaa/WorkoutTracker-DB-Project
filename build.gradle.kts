plugins {
    java
    application
    id("com.diffplug.spotless") version "6.25.0"
}

spotless {
    java {
        googleJavaFormat()
        removeUnusedImports()
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.mysql:mysql-connector-j:8.3.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass.set("it.unibo.workout.Main")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
