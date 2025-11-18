# Build stage
FROM eclipse-temurin:17-jdk-alpine as builder
WORKDIR /app

# Copier les fichiers de configuration Maven d'abord
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
# Télécharger les dépendances (cache si pom.xml ne change pas)
RUN ./mvnw dependency:go-offline

# Copier le code source et builder
COPY src/ src/
RUN ./mvnw clean package -DskipTests

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