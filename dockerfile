FROM java:8
VOLUME /tmp
ADD spring-boot-docker-1.0.0.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profile.active=prod","-jar","/app.jar"]