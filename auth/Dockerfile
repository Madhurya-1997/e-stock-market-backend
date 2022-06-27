#Start with a base image containing Java runtime
FROM openjdk:15-slim as build

#Information around who maintains the image
MAINTAINER heritage.com

EXPOSE 9090

# Add the application's jar to the container
COPY target/auth-0.0.1-SNAPSHOT.jar auth-0.0.1-SNAPSHOT.jar

#execute the application
ENTRYPOINT ["java","-jar","/auth-0.0.1-SNAPSHOT.jar"]