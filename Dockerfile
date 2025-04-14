# Primera etapa: Compilar la aplicación
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

# Segunda etapa: Imagen de ejecución
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiar el JAR desde la etapa de build
COPY --from=build /app/target/vitalapp-1.0-SNAPSHOT.jar ./vitalapp-1.0-SNAPSHOT.jar

# Crear la estructura de directorios y copiar model.xml
RUN mkdir -p src/main/resources/persistencia
COPY --from=build /app/src/main/resources/persistencia/model.xml ./src/main/resources/persistencia/model.xml

# Crear un archivo con opciones predefinidas
RUN echo "0" > input.txt

# Puerto en el que la aplicación escucha
EXPOSE 8080

# Ejecutar con entrada simulada
ENTRYPOINT ["sh", "-c", "cat input.txt | java -jar vitalapp-1.0-SNAPSHOT.jar"]