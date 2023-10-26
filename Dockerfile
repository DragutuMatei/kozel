FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/spring-boot-login-example-0.0.1-SNAPSHOT.jar fastlane.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "fastlane.jar"]

# CMD ["docker","pull","mongo"]

FROM mongo:latest
CMD ["docker","run","-d","-p","27017:27017","--name","mongo","mongo"]