plugins {
    id("org.springframework.boot").apply(false)
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

group = "com.framework"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-security")
    api("org.springframework.boot:spring-boot-starter-validation")
    api("org.apache.commons:commons-lang3")
    implementation("jakarta.validation:jakarta.validation-api")

    implementation("io.jsonwebtoken:jjwt:+")
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)

    }
}