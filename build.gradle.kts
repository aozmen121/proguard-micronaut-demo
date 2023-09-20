
plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("kapt") version "1.6.10"
    kotlin("plugin.allopen") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"

    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("io.micronaut.application") version "3.6.4"

    jacoco
    `maven-publish`
}

version = "0.1"
group = "com.example"

val kotlinVersion=project.properties.get("kotlinVersion")

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.guardsquare:proguard-gradle:7.3.1")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Constraints
    constraints {
        kapt("org.jsoup:jsoup:1.15.3") {
            because("micronaut-openapi 4.5.2 pulls in v1.11.3 which has a high severity vulnerability")
        }
    }

    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("javax.annotation:javax.annotation-api:1.3.2")

    // Jackson
    implementation(platform("com.fasterxml.jackson:jackson-bom:2.14.0"))
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("com.auth0:java-jwt:3.19.1")

    implementation("io.lettuce:lettuce-core:6.1.8.RELEASE")

    implementation("org.apache.kafka:kafka-clients:3.2.3")

    // Micronaut
    kapt("io.micronaut:micronaut-inject-java")
    kapt("io.micronaut.openapi:micronaut-openapi")

    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-management")

    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")

    implementation("io.micronaut.reactor:micronaut-reactor")

    implementation("io.swagger.core.v3:swagger-annotations")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx3:1.6.1-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // externals
    implementation("com.google.guava:guava:30.0-jre")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("io.grpc:grpc-kotlin-stub:1.3.0")
    implementation("io.grpc:grpc-protobuf:1.50.2")

}

application {
    mainClass.set("com.example.Application")
}
java {
    sourceCompatibility = JavaVersion.toVersion("17")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}

micronaut {
    runtime("netty")
    testRuntime("kotest5")
    processing {
        incremental(true)
        annotations("com.example.*")
    }
}

tasks.register<Copy>("extractJar") {
    dependsOn(tasks.assemble)
    val zipFile = file("${buildDir}/libs/demo-${rootProject.version}-all.jar")
    val outputDir = file("${buildDir}/extracted/")

    from(zipTree(zipFile))
    into(outputDir)
}

tasks.register<Delete>("deleteClasses") {
    delete("${buildDir}/extracted/")
}

tasks.register<Copy>("copyObfuscatedClasses") {
    dependsOn("deleteClasses")

    from(zipTree("${buildDir}/obfuscatedClasses.jar"))
    into("${buildDir}/extracted/")
}

tasks.register<Delete>("deleteObfuscated") {
    delete("${buildDir}/obfuscatedClasses.jar")
}

tasks.register<Zip>("repackage") {
    dependsOn("deleteClasses")
    dependsOn("copyObfuscatedClasses")
    dependsOn("deleteObfuscated")

    from("${buildDir}/extracted")
    entryCompression = ZipEntryCompression.STORED
    archiveFileName.set("demo-${rootProject.version}-all-obfs.jar")
    destinationDirectory.set(file("${buildDir}/libs/"))
    isZip64 = true
}

tasks.register<proguard.gradle.ProGuardTask>("proguard") {
    dependsOn("shadowJar", "extractJar")
    verbose()

    injars("${buildDir}/extracted/")
    outjars("${buildDir}/obfuscatedClasses.jar")

    val javaHome = System.getProperty("java.home")

    for (module in listOf(
        "java.base", "jdk.xml.dom", "jdk.jsobject", "java.xml", "java.desktop",
        "java.datatransfer", "java.logging", "java.management", "java.naming", "java.net.http",
        "java.xml.crypto", "java.sql", "java.scripting"
    )) {
        libraryjars(
            mapOf("jarfilter" to "!**.jar", "filter" to "!module-info.class"),
            "$javaHome/jmods/$module.jmod"
        )
    }

    printmapping("$buildDir/proguard-mapping.txt")
    configuration("proguard-rules.pro")

    // After ProGuard has executed, repackage the app.
    finalizedBy("repackage")
}

