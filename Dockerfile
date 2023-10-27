FROM tomcat:10.1.15-jre21-temurin

COPY target/*.war /usr/local/tomcat/webapps

COPY src/main/resources/WEB-INF/*.xml /usr/local/tomcat/conf

EXPOSE 8080

CMD ["catalina.sh", "run"]