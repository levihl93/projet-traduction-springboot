# Build stage avec Maven
FROM maven:3.9-eclipse-temurin-17 as builder
WORKDIR /app

# Copier seulement les fichiers essentiels
COPY pom.xml .
COPY src ./src

# Builder sans les properties problématiques
RUN mvn clean package -DskipTests -Dspring.profiles.active=prod

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN mkdir -p /app/uploads/documents
COPY --from=builder /app/target/api-0.0.1-SNAPSHOT.jar app.jar

# Variables d'environnement pour désactiver la BDD
ENV SPRING_AUTOCONFIGURE_EXCLUDE=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration

ENTRYPOINT ["java", "-jar", "app.jar"]