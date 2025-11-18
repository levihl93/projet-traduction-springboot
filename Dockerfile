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

# Créer un utilisateur non-root
RUN addgroup -S spring && adduser -S spring -G spring

# Donner les permissions
RUN chown -R spring:spring /app/uploads
USER spring:spring

# Copier le JAR avec le nom exact
COPY --from=builder /app/target/api-0.0.1-SNAPSHOT.jar app.jar

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]