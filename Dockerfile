FROM maven:3.9.7-sapmachine-21 as build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

RUN mvn package -DskipTests


FROM openjdk:21-slim

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar","/app/app.jar"]