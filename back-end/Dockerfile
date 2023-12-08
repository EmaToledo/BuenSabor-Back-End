FROM amazoncorretto:17-alpine

COPY build/libs/api-0.0.1-SNAPSHOT.jar /app.jar
COPY build/libs/api-0.0.1-SNAPSHOT-plain.jar /app-plain.jar

EXPOSE 4000

ENV SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/db_name
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=mysql
ENV AUTH0_AUDIENCE=localhost:8080
ENV OAUTH2_ISSUER_URI=https://dev-oytgouczyj6i7dr0.us.auth0.com/

ENTRYPOINT ["java", "-jar", "/app.jar"]