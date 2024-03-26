plugins {
    java
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
}

group = "com.io"
version = "0.0.1-SNAPSHOT"
val queryDslVersion = "5.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

extra["snippetsDir"] = file("build/generated-snippets")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3")

    // security 추가
    implementation("org.springframework.boot:spring-boot-starter-security:3.2.4")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6:3.1.2.RELEASE")

    // mariaDB
    // https://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client
    implementation("org.mariadb.jdbc:mariadb-java-client:3.2.0")

    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter-test:3.0.3")
    testImplementation("org.projectlombok:lombok:1.18.28")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.28")

    // 파일 업로드
    implementation ("commons-io:commons-io:2.7")
    implementation("commons-fileupload:commons-fileupload:1.5")

    // 저장될 파일명을 랜덤 문자열로 만들기 위해 사용
    implementation ("org.apache.commons:commons-lang3")

    /* 메일 발송 */
    implementation("javax.mail:mail:1.4.7")

    // json 데이터 처리
    // https://mvnrpository.com/artifact/com.googlecode.json-simple/json-simple
    implementation("com.googlecode.json-simple:json-simple:1.1.1")

    // Spring Boot Starter Validation
    implementation ("org.springframework.boot:spring-boot-starter-validation")

    // model mapper
    // https://mvnrepository.com/artifact/org.modelmapper/modelmapper
    implementation("org.modelmapper:modelmapper:3.0.0")

    // QueryDSL Implementation
    implementation ("com.querydsl:querydsl-jpa:${queryDslVersion}:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:${queryDslVersion}:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    //비밀번호 찾기
    implementation ("org.springframework.boot:spring-boot-starter-mail")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

/**
 * QueryDSL Build Options
 */
val querydslDir = "src/main/generated"

sourceSets {
    getByName("main").java.srcDirs(querydslDir)
}

tasks.withType<JavaCompile> {
    options.generatedSourceOutputDirectory = file(querydslDir)

    // 위의 설정이 안되면 아래 설정 사용
    // options.generatedSourceOutputDirectory.set(file(querydslDir))
}

tasks.named("clean") {
    doLast {
        file(querydslDir).deleteRecursively()
    }
}
