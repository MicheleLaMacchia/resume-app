# --- STAGE 1: Build ---
# Usiamo un'immagine Maven leggera con JDK 17 per compilare il progetto
FROM maven:3.9-eclipse-temurin-17-alpine AS build

# Copiamo i file di configurazione delle dipendenze per sfruttare la cache di Docker
COPY pom.xml /app/
COPY src /app/src/

# Impostiamo la directory di lavoro e compiliamo il file .jar
WORKDIR /app
RUN mvn clean package -DskipTests

# --- STAGE 2: Runtime ---
# Usiamo un'immagine JRE (Java Runtime Environment) minimale basata su Alpine Linux
FROM eclipse-temurin:17-jre-alpine

# Creiamo una directory per l'applicazione
WORKDIR /app

# Copiamo solo il file JAR compilato dallo stage precedente
# Il nome del jar dipende da quanto definito nel tuo pom.xml
COPY --from=build /app/target/*.jar app.jar

# Esposizione della porta standard (Cloud Run usa la 8080 per default)
EXPOSE 8080

# Comando di avvio con ottimizzazioni per container con poca memoria (Free Tier)
ENTRYPOINT ["java", "-XX:+UseSerialGC", "-Xss512k", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]