# STAGE 1: Build
FROM maven:3.9-eclipse-temurin-17-alpine AS build
COPY pom.xml /app/
COPY src /app/src/
WORKDIR /app
RUN mvn clean package -DskipTests

# STAGE 2: Runtime
FROM public.ecr.aws/lambda/java:17
WORKDIR ${LAMBDA_TASK_ROOT}

# Copiamo il JAR, lo estraiamo e mettiamo le classi e le dipendenze direttamente nel task root
COPY --from=build /app/target/resume-app-0.0.1-SNAPSHOT.jar app.jar
RUN jar -xf app.jar \
    && mkdir -p lib \
    && cp -r BOOT-INF/lib/* lib/ || true \
    && cp -r BOOT-INF/classes/* . || true \
    && rm -rf BOOT-INF

# Il comando è già nel template, ma è buona norma lasciarlo qui come fallback
CMD ["com.mlm.resume_app.StreamLambdaHandler::handleRequest"]