import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
	kotlin("multiplatform") version "1.7.0"
	application
	kotlin("plugin.serialization") version "1.7.0"
}

group = "net.bmuller"
version = "1.0-SNAPSHOT"

val arrowKtVersion = "1.1.2"
val ktorVersion = "2.0.2"
val logbackVersion = "1.2.11"
val kotlinVersion = "1.7.0"
val reactVersion = "18.1.0-pre.343"
val exposedVersion = "0.38.1"
val serializationVersion = "1.3.3"
val postgresVersion = "42.3.4"
val flywayVersion = "8.5.9"
val hikariVersion = "5.0.1"
val dotenvVersion = "6.2.2"
val koinVersion = "3.2.0"
val kotlinxDateTimeVersion = "0.3.3"

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
				implementation("io.ktor:ktor-client-serialization:$ktorVersion")
				implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
				implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
				implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDateTimeVersion")
				implementation("io.ktor:ktor-client-core:$ktorVersion")
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
			}
		}

		val jsMain by getting {
			dependencies {

				implementation("org.jetbrains.kotlin-wrappers:kotlin-react:$reactVersion")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:$reactVersion")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom:6.3.0-pre.343")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react-query:3.39.1-pre.343")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react-css:18.0.0-pre.331-kotlin-1.6.20")

				// NPM
				implementation(npm("react-transition-state", "1.1.4"))

				// tailwind
				implementation(npm("postcss", "8.4.13"))
				implementation(npm("postcss-loader", "4.2.0"))
				implementation(npm("autoprefixer", "10.4.7"))
				implementation(npm("tailwindcss", "3.0.24"))
				implementation(npm("@heroicons/react", "1.0.6"))
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