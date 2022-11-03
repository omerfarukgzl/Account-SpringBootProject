FROM openjdk:17 AS build

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw package

FROM openjdk:17
WORKDIR Account
COPY --from=build target/*.jar Account.jar
ENTRYPOINT ["java", "-jar", "Account.jar"]