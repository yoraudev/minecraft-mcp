import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.3.10"
    id("fabric-loom") version "1.15-SNAPSHOT"
    id("maven-publish")
}

version = project.property("mod_version") as String
group = project.property("maven_group") as String

base {
    archivesName.set(project.property("archives_base_name") as String)
}

val cliSourceSet = sourceSets.create("cli") {
    java.srcDir("src/cli/java")
    resources.srcDir("src/cli/resources")
}

configurations.named(cliSourceSet.implementationConfigurationName) {
    extendsFrom(configurations.implementation.get())
}

val targetJavaVersion = 21
val mcpVersion = "0.17.2"
val jettyVersion = "11.0.26"
java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    withSourcesJar()
}



repositories {
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${project.property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${project.property("kotlin_loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
    implementation(platform("io.modelcontextprotocol.sdk:mcp-bom:$mcpVersion"))
    implementation("io.modelcontextprotocol.sdk:mcp-core")
    implementation("io.modelcontextprotocol.sdk:mcp-json-jackson2")
    implementation("org.eclipse.jetty:jetty-server:$jettyVersion")
    implementation("org.eclipse.jetty:jetty-servlet:$jettyVersion")
    include("io.modelcontextprotocol.sdk:mcp-core:$mcpVersion")
    include("io.modelcontextprotocol.sdk:mcp-json-jackson2:$mcpVersion")
    include("org.eclipse.jetty:jetty-server:$jettyVersion")
    include("org.eclipse.jetty:jetty-servlet:$jettyVersion")
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", project.property("minecraft_version"))
    inputs.property("loader_version", project.property("loader_version"))
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version.toString(),
            "minecraft_version" to (project.property("minecraft_version") as String),
            "loader_version" to (project.property("loader_version") as String),
            "kotlin_loader_version" to (project.property("kotlin_loader_version") as String)
        )
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(targetJavaVersion)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.fromTarget(targetJavaVersion.toString()))
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName.get()}" }
    }
}

val mmcpCliJar = tasks.register<Jar>("mmcpCliJar") {
    dependsOn(tasks.named(cliSourceSet.classesTaskName))
    archiveFileName.set("mmcp.jar")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = "wtf.yoraudev.mmcp.cli.MmcpClientMain"
    }
    from(cliSourceSet.output)
    from(
        configurations[cliSourceSet.runtimeClasspathConfigurationName].filter { it.name.endsWith(".jar") }.map {
            zipTree(it)
        }
    )
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
}

tasks.named("build") {
    dependsOn(mmcpCliJar)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.property("archives_base_name") as String
            from(components["java"])
        }
    }

    repositories {
    }
}
