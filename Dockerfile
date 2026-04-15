# Imagen base con Java 21
FROM eclipse-temurin:21-jre-jammy

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el JAR generado al contenedor
# el primer argumento es la ruta en tu máquina
# el segundo es la ruta dentro del contenedor
COPY target/Resourceserver-0.0.1-SNAPSHOT.jar app.jar

# Puerto que expone la app — solo documentación, no abre el puerto
EXPOSE 8081

# Comando que se ejecuta cuando arranca el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]