@file:Suppress("UNUSED_VARIABLE")

import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("multiplatform") version "1.7.10"
	application
	kotlin("plugin.serialization") version "1.7.10"
	id("io.github.turansky.seskar") version "0.7.0"
}

group = "net.bmuller"
version = "1.0-SNAPSHOT"

val arrowKtVersion = "1.1.2"
val ktorVersion = "2.0.3"
val logbackVersion = "1.2.11"
val kotlinVersion = "1.7.0"
val reactVersion = "18.1.0-pre.343"
val exposedVersion = "0.39.2"
val serializationVersion = "1.3.3"
val postgresVersion = "42.3.4"
val flywayVersion = "8.5.9"
val hikariVersion = "5.0.1"
val dotenvVersion = "6.2.2"
val koinVersion = "3.2.0"
val kotlinxDateTimeVersion = "0.3.3"
val kotestVersion = "5.3.2"

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
				implementation("io.arrow-kt:arrow-fx-coroutines:$arrowKtVersion")
				implementation("io.ktor:ktor-client-serialization:$ktorVersion")
				implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
				implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
				implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDateTimeVersion")
				implementation("io.ktor:ktor-client-resources:$ktorVersion")
				implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
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
				implementation("io.github.cdimascio:dotenv-kotlin:$dotenvVersion")
				// Removing this breaks idea... who know why?
				implementation("io.insert-koin:koin-ktor:$koinVersion")
				// KTOR
				implementation("io.ktor:ktor-client-cio-jvm:$ktorVersion")
				implementation("io.ktor:ktor-serialization-kotlinx-xml:$ktorVersion")
				implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
				implementation("io.ktor:ktor-server-compression:$ktorVersion")
				implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
				implementation("io.ktor:ktor-server-cors:$ktorVersion")
				implementation("io.ktor:ktor-server-netty:$ktorVersion")
				implementation("io.ktor:ktor-server-resources:$ktorVersion")
				implementation("io.ktor:ktor-server-auth:$ktorVersion")
				implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")
				implementation("io.ktor:ktor-server-sessions:$ktorVersion")
				implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")

				// Database
				implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
				implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
				implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
				implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
				implementation("org.postgresql:postgresql:$postgresVersion")
				implementation("org.flywaydb:flyway-core:$flywayVersion")
				implementation("com.zaxxer:HikariCP:$hikariVersion")

			}
		}

		val jvmTest by getting {
			dependencies {
				implementation("io.ktor:ktor-server-test-host:$ktorVersion")
				implementation("org.testcontainers:postgresql:1.17.3")
				implementation("io.kotest:kotest-framework-engine:$kotestVersion")
				implementation("io.kotest:kotest-property:$kotestVersion")
				implementation("io.kotest:kotest-runner-junit5:$kotestVersion")
				implementation("io.kotest.extensions:kotest-assertions-ktor:1.0.3")
				implementation("io.kotest.extensions:kotest-assertions-arrow:1.2.5")
				implementation("io.kotest.extensions:kotest-extensions-testcontainers:1.3.3")
				implementation("io.ktor:ktor-client-mock:$ktorVersion")
				implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
			}
		}

		val jsMain by getting {
			dependencies {

				implementation("io.github.turansky.seskar:seskar-core:0.7.0")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react:$reactVersion")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:$reactVersion")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom:6.3.0-pre.343")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react-query:3.39.1-pre.343")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react-css:18.0.0-pre.331-kotlin-1.6.20")
				implementation("io.ktor:ktor-client-core-js:$ktorVersion")

				// NPM
				implementation(npm("react-transition-state", "1.1.4"))
				implementation(npm("react-multi-carousel", "2.8.2"))

				// tailwind
				implementation(npm("postcss", "8.4.13"))
				implementation(npm("postcss-loader", "4.2.0"))
				implementation(npm("autoprefixer", "10.4.7"))
				implementation(npm("tailwindcss", "3.0.24"))
				implementation(npm("@tailwindcss/line-clamp", "0.4.0"))
				implementation(npm("@heroicons/react", "1.0.6"))
			}
		}
	}
}

application {
	mainClass.set("net.bmuller.application.ServerKt")
}

tasks {
	withType<KotlinCompile>().configureEach {
		kotlinOptions {
			freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
		}
	}
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
	val copyResourcesTask = tasks.getByName("processResources")
	dependsOn(copyResourcesTask)
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

val copyTailwindConfig = tasks.register<Copy>("copyTailwindConfig") {
	from("./tailwind.config.js")
	into("./build/js/packages/adenn-requests")

	dependsOn(":kotlinNpmInstall")
}

val copyPostCssConfig = tasks.register<Copy>("copyPostcssConfig") {
	from("./postcss.config.js")
	into("./build/js/packages/adenn-requests")

	dependsOn(":kotlinNpmInstall")
}

tasks.named("processResources") {
	dependsOn(copyTailwindConfig)
	dependsOn(copyPostCssConfig)
}