# --- STAGE 1: Build ---
# Utilizziamo un'immagine Maven con JDK 17 per la compilazione
FROM maven:3.9-eclipse-temurin-17-alpine AS build

# Copiamo i file del progetto [cite: 120]
COPY pom.xml /app/
COPY src /app/src/
WORKDIR /app

# Eseguiamo il package creando il Fat JAR di Spring Boot [cite: 146]
RUN mvn clean package -DskipTests

# --- STAGE 2: Runtime ---
# Usiamo l'immagine ufficiale AWS per il runtime Java 17 di Lambda
FROM public.ecr.aws/lambda/java:17

# Impostiamo la directory di lavoro standard di Lambda
WORKDIR ${LAMBDA_TASK_ROOT}

# Copiamo il JAR generato dallo stage di build
# Nota: usa il nome esatto del tuo file JAR (senza .original) [cite: 110]
COPY --from=build /app/target/resume-app-0.0.1-SNAPSHOT.jar app.jar

# ESTRAZIONE DEL JAR: Questo passaggio è fondamentale per evitare ClassNotFoundException.
# Esplodiamo il JAR direttamente nella root del task in modo che le classi siano visibili.
RUN jar -xf app.jar && rm app.jar

# Definiamo l'Handler: [PACKAGE].[CLASSE]::[METODO] [cite: 111, 118]
# Assicurati che il package nel tuo codice Java sia esattamente com.mlm.resume_app
CMD ["com.mlm.resume_app.StreamLambdaHandler::handleRequest"]