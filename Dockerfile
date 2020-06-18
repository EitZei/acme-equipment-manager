FROM maven:3-openjdk-14
COPY src src
COPY pom.xml pom.xml
RUN mvn package -DskipTests=true

FROM openjdk:14-alpine
COPY --from=0 target/equipmentmanager-*.jar equipmentmanager.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "equipmentmanager.jar"]