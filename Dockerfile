# Build stage avec Maven
FROM maven:3.9-eclipse-temurin-17 as builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Créer les dossiers nécessaires
RUN mkdir -p /app/uploads/documents

# Copier le JAR depuis le build stage
COPY --from=builder /app/target/api-0.0.1-SNAPSHOT.jar app.jar

# Variables d'environnement
ENV SPRING_PROFILES_ACTIVE=prod

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]