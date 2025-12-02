# ===========================
#   BUILD
# ===========================
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app
# 1. Copiamos el pom solo para aprovechar cache
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline -B

# 2. Copiamos el resto del proyecto
COPY src ./src
# 3. Build sin tests
RUN ./mvnw -B clean package -DskipTests

# --------------------------------------------------------
# ETAPA 2: Ejecución (Run)
# Usamos una imagen ligera de Java 21 para correr la app
# --------------------------------------------------------
# Etapa 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copia el JAR compilado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar
# Expone el puerto
EXPOSE 8080
# COMANDO DE INICIO:
# Aquí forzamos el perfil 'prod' para que lea application-prod.properties
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]