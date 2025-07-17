FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /build
COPY FindJob/pom.xml .
RUN mvn -B dependency:go-offline
COPY FindJob/src ./src
RUN mvn -B clean package -DskipTests

FROM tomcat:11.0-jdk17-temurin
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /build/target/FindJob*.war /usr/local/tomcat/webapps/FindJob.war
EXPOSE 8080
CMD ["catalina.sh", "run"]

