# Etapa de compilación usando Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
# Copiamos los archivos necesarios para construir el proyecto
COPY pom.xml .
COPY src ./src
# Ejecutamos la compilación. Se omiten las pruebas para agilizar la imagen
RUN mvn clean package -DskipTests

# Imagen final basada en JRE
FROM eclipse-temurin:21-jre
WORKDIR /app
# Copiamos el jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar
# Exponemos el puerto por defecto de Spring Boot
EXPOSE 8080
# Comando de inicio de la aplicación
CMD ["java", "-jar", "app.jar"]
