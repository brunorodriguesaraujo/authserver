plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	kotlin("plugin.jpa") version "1.9.25"
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "br.brunorodrigues"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	//Spring DOC
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.4")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	runtimeOnly("com.h2database:h2")
	implementation("org.springframework.boot:spring-boot-starter-security")

	val jjwt = "0.12.6"
	implementation("io.jsonwebtoken:jjwt-api:${jjwt}")
	implementation("io.jsonwebtoken:jjwt-jackson:${jjwt}")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:${jjwt}")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}
