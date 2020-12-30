import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    id("org.jetbrains.compose") version "0.3.0-build138"
}

group = "org.fuho.sheefra"
version = "1.0"

repositories {
    jcenter()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
    implementation(compose.desktop.currentOs)
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "15"
}

compose.desktop {
    application {
        mainClass = "org.fuho.sheefra.MainKt"
        nativeDistributions {
            packageName = "sheefra"
            description = "The all-powerful Sheefra"
            copyright = "©2020 fuho. All rights reserved."
            vendor = "I am the vendor"
            jvmArgs += listOf("-Xmx2G")
            args += listOf("-customArgument")
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            outputBaseDir.set(project.buildDir.resolve("sheefra"))
            macOS {
                iconFile.set(project.file("icon.icns"))
            }
            windows {
                iconFile.set(project.file("icon.ico"))
                menuGroup = "Sheefra"
            }
            linux {
                iconFile.set(project.file("icon.png"))
            }
        }
    }
}