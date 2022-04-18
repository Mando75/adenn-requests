import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
	kotlin("multiplatform") version "1.6.20"
	application
	kotlin("plugin.serialization") version "1.6.20"
}

group = "net.bmuller"
version = "1.0-SNAPSHOT"

val arrowKtVersion = "1.0.1"
val ktorVersion = "2.0.0"
val logbackVersion = "1.2.11"
val kotlinVersion = "1.6.20"
val reactVersion = "18.0.0-pre.330-kotlin-$kotlinVersion"

repositories {
	mavenCentral()
	maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

kotlin {
	jvm {
		compilations.all {
			kotlinOptions.jvmTarget = "15"
		}
		withJava()
		testRuns["test"].executionTask.configure {
			useJUnitPlatform()
		}
	}
	js(IR) {
		binaries.executable()
		browser {
			commonWebpackConfig {
				cssSupport.enabled = true
			}
		}
	}
	sourceSets {
		val commonMain by getting

		val commonTest by getting {
			dependencies {
				implementation(kotlin("test"))
				implementation("io.arrow-kt:arrow-core:$arrowKtVersion")
			}
		}

		val jvmMain by getting {
			dependencies {
				implementation("io.ktor:ktor-server-netty:$ktorVersion")
				implementation("io.ktor:ktor-server-cors:$ktorVersion")
				implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
				implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
				implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
				implementation("ch.qos.logback:logback-classic:$logbackVersion")
				implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
				implementation("io.ktor:ktor-server-compression:$ktorVersion")
				implementation("io.ktor:ktor-server-resources:$ktorVersion")
			}
		}

		val jvmTest by getting

		val jsMain by getting {
			dependencies {
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react:$reactVersion")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:$reactVersion")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react-css:$reactVersion")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom:6.3.0-pre.330-kotlin-$kotlinVersion")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react-query:3.34.19-pre.330-kotlin-$kotlinVersion")
			}
		}

		val jsTest by getting
	}
}

application {
	mainClass.set("net.bmuller.application.ServerKt")
}

tasks.getByName<Jar>("jvmJar") {
	duplicatesStrategy = DuplicatesStrategy.WARN
	val taskName =
		if (project.hasProperty("isProduction")
			|| project.gradle.startParameter.taskNames.contains("installDist")
		) {
			"jsBrowserProductionWebpack"
		} else {
			"jsBrowserDevelopmentWebpack"
		}
	val webpackTask = tasks.getByName<KotlinWebpack>(taskName)
	dependsOn(webpackTask)
	from(File(webpackTask.destinationDirectory, webpackTask.outputFileName))
}

tasks.named<Copy>("jvmProcessResources") {
	val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
	from(jsBrowserDistribution)
}

tasks.named<JavaExec>("run") {
	dependsOn(tasks.named<Jar>("jvmJar"))
	classpath(tasks.getByName<Jar>("jvmJar"))
}