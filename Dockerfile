# Build stage avec cache Maven optimisé
FROM maven:3.9-eclipse-temurin-17 as builder
WORKDIR /app

# Copier d'abord les fichiers de configuration pour mieux utiliser le cache Docker
COPY pom.xml .
COPY src ./src

# Télécharger les dépendances d'abord (cache si pom.xml ne change pas)
RUN mvn dependency:go-offline -B

# Builder l'application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN mkdir -p /app/uploads/documents
COPY --from=builder /app/target/api-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]