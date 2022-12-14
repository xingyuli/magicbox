dependencies {
    // spring-data-jpa on hibernate need this
    implementation(kotlin("reflect"))

    implementation("com.sparkjava:spark-core:2.9.4")


    /* ***** joda ***** */

    implementation("joda-time:joda-time:2.12.2")


    /* ***** jwt ***** */

    // option: jjwt
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    // or jjwt-gson if Gson is preferred
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // option: jose jwt
    implementation("com.nimbusds:nimbus-jose-jwt:9.25.6")


    /* ***** logback ***** */

    // Java 8 Compatible
    implementation("ch.qos.logback:logback-classic:1.3.5")

    // for: smtp appender example
    runtimeOnly("javax.mail:javax.mail-api:1.6.2")
    runtimeOnly("com.sun.mail:javax.mail:1.6.2")


    /* ***** spring data jpa ***** */

    // Java 8 Compatible
    implementation(platform("org.springframework.data:spring-data-bom:2021.2.6"))
    implementation("org.springframework.data:spring-data-jpa")

    implementation("org.hibernate:hibernate-entitymanager:5.6.14.Final")
    implementation("org.hibernate:hibernate-jpamodelgen:5.6.14.Final")
    kapt("org.hibernate:hibernate-jpamodelgen:5.6.14.Final")

    implementation("com.h2database:h2:2.1.214")
    implementation("org.apache.commons:commons-dbcp2:2.9.0")

    // Java 8 Compatible
    testImplementation("org.springframework:spring-test:5.3.24")


    /* ***** junit ***** */

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.0")
    testImplementation("org.junit.platform:junit-platform-suite-engine:1.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")

    testImplementation("org.hamcrest:hamcrest:2.2")

    testImplementation("org.easymock:easymock:5.0.1")
}

tasks.test {
    useJUnitPlatform()
}

kapt {
    arguments {
        arg("persistenceXml", "META-INF/persistence-abc.xml")
    }
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
