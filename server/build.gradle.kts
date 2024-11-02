plugins {
    application
    kotlin("jvm")
    alias(libs.plugins.docker.compose)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.flyway)
}

group = "io.kort.inbooks"
version = "0.0.1"

application {
    mainClass.set("io.kort.inbooks.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dockerCompose {
    isRequiredBy(project.tasks.test)
    forceRecreate = true
    removeVolumes = true
    useComposeFiles = listOf("docker/docker-compose-postgres-test.yml")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.register<JavaExec>("generateMigrationScript") {
    group = "application"
    description = "Generate migration script in the path exposed-migration/migrations"
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass = "GenerateMigrationScriptKt"
}

dependencies {
    implementation(project(":common"))
    implementation(libs.arrow.core)
    implementation(libs.bcrypt)
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.bundles.ktor.server)
    implementation(project.dependencies.platform(libs.exposed.bom))
    implementation(libs.bundles.exposed)
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)
    implementation(libs.logback)
    implementation(libs.postgres)
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.ktor)
    implementation(libs.resend)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.ktor.server.test.host.jvm)
    testImplementation(libs.kotest.assertions.arrow)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.extension.ktor)
    testImplementation(libs.bundles.ktor.client)
    testImplementation(libs.mockk.jvm)
}