# Usa una imagen base de OpenJDK 17
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo en /app
WORKDIR /app

# Copia el archivo .jar en el contenedor
COPY NoCountryAdopcion-0.0.1-SNAPSHOT.jar /app/service.jar

# Expone el puerto en el que la aplicación se ejecutará
EXPOSE 9192

# Define el comando por defecto para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/service.jar"]
