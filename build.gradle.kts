plugins {
    val kotlinVersion = "1.8.0"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    id("net.mamoe.mirai-console") version "2.13.2"
}

dependencies {
    val overflow_version = "2.16.0-db61867-SNAPSHOT"
    implementation("org.json:json:20230227")
    implementation("cn.hutool:hutool-http:5.8.26")
    implementation("org.apache.httpcomponents:httpclient:4.5.14")

    compileOnly("top.mrxiaom:overflow-core-api:$overflow_version")
    compileOnly("top.mrxiaom:overflow-core:$overflow_version")

    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}

group = "ltd.guimc.plugin"
version = "0.1.0"

repositories {
    if (System.getenv("CI")?.toBoolean() != true) {
        maven("https://maven.aliyun.com/repository/public") // 阿里云国内代理仓库
    }
    maven { url = uri("https://repo.repsy.io/mvn/chrynan/public") }
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
    mavenCentral()
}
