# Usa una imagen base de OpenJDK 17
FROM tomcat:10.1-jdk17

# Establece el directorio de trabajo en /app
WORKDIR /usr/local/tomcat/webapps

# Establece el directorio de trabajo en /usr/local/tomcat/webapps
WORKDIR /usr/local/tomcat/webapps

# Copia el archivo .war en el directorio webapps de Tomcat
COPY NoCountryAdopcion-0.0.1-SNAPSHOT.war ./ROOT.war

# Expone el puerto en el que Tomcat se ejecutará
EXPOSE 8080
