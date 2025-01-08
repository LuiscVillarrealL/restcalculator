FROM maven:3.9.8-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy entire multi-module project from the parent context
COPY . . 

# This means /app/pom.xml is the parent POM
RUN mvn clean package -pl rest -am -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/rest/target/rest-*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/app.jar"]
