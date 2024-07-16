FROM openjdk:17-jdk-alpine

COPY target/genealogy-portal-0.0.1-SNAPSHOT.jar genealogy-portal-0.0.1-SNAPSHOT.jar

EXPOSE 8080:8080

ENTRYPOINT ["java","-jar","/genealogy-portal-0.0.1-SNAPSHOT.jar"]