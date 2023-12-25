FROM eclipse-temurin:21-jre-alpine
COPY build/libs/strava-kudos-java-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
