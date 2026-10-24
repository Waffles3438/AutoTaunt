plugins {
    kotlin("jvm") version "2.4.20"
    id("net.fabricmc.fabric-loom-remap") version "1.18.4"
    id("ploceus") version "1.18.1"
}

version = "${property("mod_version")}+mc${property("minecraft_version")}-ornithe"
group = "me.waffles.autotaunt"
base.archivesName.set(property("mod_archives_name").toString())

repositories {
    mavenCentral()
    maven("https://api.modrinth.com/maven") { content { includeGroup("maven.modrinth") } }
    maven("https://repo.polyfrost.org/releases")
    maven("https://repo.hypixel.net/repository/Hypixel/")
    maven("https://maven.cloverclient.com/releases")
    maven("https://maven.deftu.dev/releases")
    maven("https://nexus.prsm.wtf/repository/maven-public/maven-repo/releases/")
    maven("https://redirector.kotlinlang.org/maven/compose-dev")
    google()
}

ploceus { setIntermediaryGeneration(2) }

val oneconfigVersion = property("oneconfig_version").toString()
val composeBundleVersion = property("compose_bundle_version").toString()

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings(ploceus.featherMappings(property("feather_build").toString()))
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")
    ploceus.dependOsl(property("osl_version").toString())
    modImplementation("pl.tomgirl:pylon:${property("pylon_version")}")

    // The API platform must be explicit: Loom removes nested jars from the bootstrap in dev.
    modImplementation("org.polyfrost.oneconfig:1.8.9-ornithe:$oneconfigVersion")
    modRuntimeOnly("maven.modrinth:oneconfig:${property("oneconfig_runtime_version")}") {
        isTransitive = false
    }
    modRuntimeOnly("org.polyfrost.oneconfig:compose-bundle:$composeBundleVersion") {
        isTransitive = false
    }

    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.14.0")
}

// OneConfig's Ornithe platform and Pylon use Java 25 and LWJGL 3.
java { toolchain.languageVersion.set(JavaLanguageVersion.of(25)); withSourcesJar() }
kotlin { jvmToolchain(25) }
configurations.configureEach { exclude(group = "org.lwjgl.lwjgl") }

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("fabric.mod.json") { expand("version" to project.version) }
}

tasks.test { useJUnitPlatform() }
