FROM maven:3.6.0-jdk-11-slim AS maven

WORKDIR /app

COPY ./pom.xml ./pom.xml

COPY ./src ./src

RUN mvn clean package -DskipTests && cp ./target/DummyProducts-*.jar app.jar

FROM openjdk:11-jre-slim

WORKDIR /app

COPY --from=maven /app/app.jar ./app.jar

EXPOSE 8090

CMD ["java", "-jar", "/app/app.jar"]