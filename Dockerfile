# Build stage
FROM eclipse-temurin:17-jdk-alpine as builder
WORKDIR /app

# Copier tout le projet
COPY . .

# Utiliser Maven directement au lieu de mvnw
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

# Copier le JAR
COPY --from=builder /app/target/*.jar app.jar

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "/app.jar"]