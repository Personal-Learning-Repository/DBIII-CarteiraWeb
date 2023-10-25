FROM tomcat:10.1.15-jdk21-temurin
FROM maven:3.8.1 as build

COPY pom.xml .

RUN mvn clean package

COPY target/*.war /usr/local/tomcat/webapps

COPY src/main/resources/WEB-INF/*.xml /usr/local/tomcat/conf

EXPOSE 8080

CMD ["catalina.sh", "run"]