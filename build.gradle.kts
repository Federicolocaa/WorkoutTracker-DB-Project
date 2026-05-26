plugins {
    java
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.mysql:mysql-connector-j:8.3.0")
    
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

application {
    mainClass.set("it.unibo.workout.Main")
}

tasks.withType<Test> {
    useJUnitPlatform()
}