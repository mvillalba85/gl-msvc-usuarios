# Usar una imagen base de OpenJDK 8
FROM openjdk:8-jdk-alpine

# Establecer el directorio de trabajo en el contenedor
WORKDIR /msvc-usuarios

ARG JAR_FILE=target/msvc-usuarios-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} msvc-usuarios.jar

# Exponer el puerto 8080, ya que es el puerto por defecto en Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "msvc-usuarios.jar"]