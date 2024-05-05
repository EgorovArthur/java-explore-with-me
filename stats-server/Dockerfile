FROM amazoncorretto:11-alpine-jdk
COPY stat-client/target/*.jar stat-client.jar
COPY stat-dto/target/*.jar stat-dto.jar
COPY stat-service/target/*.jar stat-service.jar
ENTRYPOINT ["java","-jar","/stat-client.jar"]
ENTRYPOINT ["java","-jar","/stat-dto.jar"]
ENTRYPOINT ["java","-jar","/stat-service.jar"]