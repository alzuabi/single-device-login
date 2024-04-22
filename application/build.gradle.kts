plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

group = "com.application"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation(project(":framework"))
}
//val bootJar: BootJar by tasks
//
//tasks.bootJar {
//    exclude("*.properties", "*.xml", "*.yml", "*.sql")
//}