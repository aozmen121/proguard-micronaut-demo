
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
    id("org.jetbrains.kotlin.kapt") version "1.6.21"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.micronaut.application") version "3.7.10"
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
    kapt("io.micronaut:micronaut-http-validation")
    kapt("io.micronaut.openapi:micronaut-openapi")
    kapt("io.micronaut.serde:micronaut-serde-processor")
    implementation("io.micronaut:micronaut-aop")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut.graphql:micronaut-graphql")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation(platform("com.fasterxml.jackson:jackson-bom:2.14.0"))
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("io.swagger.core.v3:swagger-annotations")
    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx3:1.6.1-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    runtimeOnly("ch.qos.logback:logback-classic")
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
graalvmNative.toolchainDetection.set(false)
micronaut {
    runtime("netty")
    testRuntime("kotest5")
    processing {
        incremental(true)
        annotations("com.example.*")
    }
}



configurations.all {
    resolutionStrategy.dependencySubstitution {
        substitute(module("io.micronaut:micronaut-jackson-databind"))
            .using(module("io.micronaut.serde:micronaut-serde-jackson:1.5.3"))
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

