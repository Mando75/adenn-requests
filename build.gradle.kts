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
val exposedVersion = "0.38.1"
val serializationVersion = "1.3.2"
val postgresVersion = "42.3.4"

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
		val commonMain by getting {
			dependencies {
				implementation("io.arrow-kt:arrow-core:$arrowKtVersion")
				implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
			}
		}

		val commonTest by getting {
			dependencies {
				implementation(kotlin("test"))
			}
		}

		val jvmMain by getting {
			dependencies {
				implementation("ch.qos.logback:logback-classic:$logbackVersion")
				implementation("io.github.cdimascio:dotenv-kotlin:6.2.2")
				// KTOR
				implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
				implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
				implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
				implementation("io.ktor:ktor-server-compression:$ktorVersion")
				implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
				implementation("io.ktor:ktor-server-cors:$ktorVersion")
				implementation("io.ktor:ktor-server-netty:$ktorVersion")
				implementation("io.ktor:ktor-server-resources:$ktorVersion")
				implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
				// Database
				implementation("io.ktor:ktor-client-serialization:$ktorVersion")
				implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
				implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
				implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
				implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
				implementation("org.postgresql:postgresql:$postgresVersion")
				implementation("org.flywaydb:flyway-core:8.5.9")
				implementation("com.zaxxer:HikariCP:5.0.1")

			}
		}

		val jvmTest by getting {
			dependencies {
				implementation("io.ktor:ktor-server-test-host:$ktorVersion")
			}
		}

		val jsMain by getting {
			dependencies {
				implementation("io.ktor:ktor-client-js:$ktorVersion")
				implementation("io.ktor:ktor-client-json:$ktorVersion")
				implementation("io.ktor:ktor-client-serialization:$ktorVersion")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react:$reactVersion")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:$reactVersion")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react-css:$reactVersion")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom:6.3.0-pre.330-kotlin-$kotlinVersion")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react-query:3.34.19-pre.330-kotlin-$kotlinVersion")
			}
		}
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