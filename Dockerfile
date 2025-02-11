FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
ENV SPRING_DATASOURCE_URL: jdbc:postgresql://postgres_db/ChallengeDB
ENV SPRING_DATASOURCE_USERNAME: postgres
ENV SPRING_DATASOURCE_PASSWORD: mypassword
ENV SPRING_JPA_HIBERNATE_DDL_AUTO: update
ENV SPRING_REDIS_HOST: redis
ENV SPRING_REDIS_PASSWORD: 1234
ENV SPRING_REDIS_PORT: 6379