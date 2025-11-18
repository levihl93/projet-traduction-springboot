# Étape 1 : Build de l'application
FROM eclipse-temurin:17-jdk-alpine as builder
WORKDIR /app
COPY . .

# Donner les permissions d'exécution à mvnw
RUN chmod +x ./mvnw

RUN ./mvnw clean package -DskipTests

# Étape 2 : Image finale
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Créer les dossiers AVANT de changer d'utilisateur
RUN mkdir -p /app/uploads/documents

# Créer un utilisateur non-root
RUN addgroup -S spring && adduser -S spring -G spring

# Donner les permissions à l'utilisateur spring
RUN chown -R spring:spring /app/uploads

USER spring:spring

# Copier le JAR depuis l'étape de build
COPY --from=builder /app/target/*.jar app.jar

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "/app.jar"]