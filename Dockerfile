# Étape 1 : Build de l'application
FROM eclipse-temurin:17-jdk-alpine as builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Étape 2 : Image finale
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Créer un utilisateur non-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Créer le dossier pour les fichiers uploadés
RUN mkdir -p /app/uploads/documents

# Copier le JAR depuis l'étape de build
COPY --from=builder /app/target/*.jar app.jar

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "/app.jar"]