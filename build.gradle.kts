// Top-level build file where you can add configuration options common to all sub-projects/modules.

/*
plugins {
    id("com.github.ben-manes.versions") version Versions.gradleVersionsPluginVersion
}
 */

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(ClassPaths.gradle)
        classpath(ClassPaths.kotlinGradlePlugin)
        classpath(ClassPaths.daggerHiltGradkePlugin)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}
