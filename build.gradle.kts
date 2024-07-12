plugins {
    java
    application
    id("io.freefair.lombok") version "8.6"
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "top.xearthlydust"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // https://mvnrepository.com/artifact/com.esotericsoftware/kryo
    implementation("com.esotericsoftware:kryo:5.6.0")

    implementation ("io.github.mkpaz:atlantafx-base:2.0.1")
}

javafx {
    version = "22"
    modules("javafx.controls", "javafx.fxml")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainClass = "top.xearthlydust.Main"
}
