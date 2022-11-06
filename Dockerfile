FROM eclipse-temurin:17-jdk-alpine as stage1
WORKDIR /opt/app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean install

FROM  eclipse-temurin:17-jre-alpine
WORKDIR /opt/app
COPY --from=stage1 /opt/app/target*.jar /opt/app/*.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]