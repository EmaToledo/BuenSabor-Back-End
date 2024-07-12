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
	implementation("com.mercadopago:sdk-java:2.1.24")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation ("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.security:spring-security-oauth2-jose:6.0.2")
	implementation ("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation ("org.thymeleaf.extras:thymeleaf-extras-springsecurity6:3.1.1.RELEASE")
	implementation ("org.springframework.security:spring-security-test")
	implementation ("com.squareup.okhttp:okhttp:2.7.5")
	implementation ("org.mapstruct:mapstruct:1.5.5.Final")
	compileOnly ("org.projectlombok:lombok:1.18.30")
	annotationProcessor ("org.projectlombok:lombok:1.18.30")
	annotationProcessor ("org.mapstruct:mapstruct-processor:1.5.5.Final")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.mysql:mysql-connector-j")
	testCompileOnly ("org.projectlombok:lombok:1.18.30")
	testAnnotationProcessor ("org.projectlombok:lombok:1.18.30")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
