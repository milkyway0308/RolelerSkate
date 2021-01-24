plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "2.0.4"
}


buildscript {
    repositories {
        jcenter()
    }
}


group = "skywolf46"
version = properties["version"] as String

repositories {
    jcenter()
    maven("https://maven.pkg.github.com/milkyway0308/CommandAnnotation") {
        credentials {
            username = properties["gpr.user"] as String
            password = properties["gpr.key"] as String
        }
    }

}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    // options.compilerArgs.add("-Xlint:unchecked")
}

dependencies {
    implementation ("com.discord4j:discord4j-core:3.1.3");
    implementation ("dnsjava:dnsjava:2.1.8");
    implementation ("net.md-5:bungeecord-chat:1.16-R0.4");
    implementation ("com.google.code.gson:gson:2.8.6");
    implementation ("org.apache.httpcomponents:httpclient:4.5.13");
    implementation ("org.apache.httpcomponents:fluent-hc:4.5.13");
    implementation ("org.jsoup:jsoup:1.13.1")
    implementation ("com.sedmelluq:lavaplayer:1.3.66")
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "skywolf46.rolelerskate.RolelerSkate"
    }
}

publishing {
    repositories {
        maven {
            name = "Github"
            url = uri("https://github.com/milkyway0308/RolelerSkate")
            credentials {
                username = properties["gpr.user"] as String
                password = properties["gpr.key"] as String
            }
        }
    }
    publications {
        create<MavenPublication>("jar") {
            from(components["java"])
            groupId = "skywolf46"
            artifactId = "globaltimer"
            version = properties["version"] as String
            pom {
                url.set("https://github.com/milkyway0308/RolelerSkate.git")
            }
        }
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}