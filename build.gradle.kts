plugins {
	java
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.dvo"
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
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4")

	compileOnly("org.projectlombok:lombok:1.18.30")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok:1.18.30")
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")


	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.testcontainers:junit-jupiter:1.19.8")
	testImplementation("org.testcontainers:postgresql:1.19.8")
	testImplementation("org.springframework.boot:spring-boot-testcontainers:3.2.5")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
