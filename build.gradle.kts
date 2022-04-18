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

repositories {
	jcenter()
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
			}
		}
		val jvmTest by getting
		val jsMain by getting {
			dependencies {
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react:17.0.2-pre.290-kotlin-1.6.10")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:17.0.2-pre.290-kotlin-1.6.10")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react-css:17.0.2-pre.290-kotlin-1.6.10")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react-router-dom:6.2.1-pre.290-kotlin-1.6.10")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-redux:4.1.2-pre.290-kotlin-1.6.10")
				implementation("org.jetbrains.kotlin-wrappers:kotlin-react-redux:7.2.6-pre.290-kotlin-1.6.10")
			}
		}
		val jsTest by getting
	}
}

application {
	mainClass.set("net.bmuller.application.ServerKt")
}

tasks.named<Copy>("jvmProcessResources") {
	val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
	from(jsBrowserDistribution)
}

tasks.named<JavaExec>("run") {
	dependsOn(tasks.named<Jar>("jvmJar"))
	classpath(tasks.named<Jar>("jvmJar"))
}