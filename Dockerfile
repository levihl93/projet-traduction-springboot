FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Créer les dossiers nécessaires
RUN mkdir -p /app/uploads/documents

# Copier le JAR
COPY target/api-0.0.1-SNAPSHOT.jar app.jar

# Variables d'environnement par défaut
ENV SPRING_PROFILES_ACTIVE=prod,heroku
ENV SERVER_PORT=8080

# Démarrer avec debug
ENTRYPOINT ["java", "-Xdebug", "-jar", "app.jar"]