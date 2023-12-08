plugins {
	java
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
}

group = "com.elbuensabor"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation ("com.okta.spring:okta-spring-boot-starter:3.0.5")
	implementation ("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation ("org.springframework.boot:spring-boot-starter-security")
	implementation ("org.thymeleaf.extras:thymeleaf-extras-springsecurity6:3.1.1.RELEASE")
	implementation ("org.springframework.security:spring-security-test")
	implementation ("org.mapstruct:mapstruct:1.5.5.Final")
	compileOnly ("org.projectlombok:lombok:1.18.30")
	annotationProcessor ("org.projectlombok:lombok:1.18.30")
	annotationProcessor ("org.mapstruct:mapstruct-processor:1.5.5.Final")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.mysql:mysql-connector-j")
	testCompileOnly ("org.projectlombok:lombok:1.18.30")
	testAnnotationProcessor ("org.projectlombok:lombok:1.18.30")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
