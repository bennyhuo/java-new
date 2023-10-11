plugins {
    id("java")
}

group = "com.bennyhuo.java"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(listOf(
        "--enable-preview",
        "--add-modules=jdk.incubator.vector"
    ))
}

tasks.withType<JavaExec>() {
    jvmArgs("--enable-preview", "-Djava.library.path=./lib", "--add-modules=jdk.incubator.vector")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
