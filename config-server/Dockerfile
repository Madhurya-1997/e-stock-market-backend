#Start with a base image containing Java runtime
FROM openjdk:15-slim as build

#Information around who maintains the image
MAINTAINER heritage.com

# Add the application's jar to the container
COPY target/config-server-0.0.1-SNAPSHOT.jar config-server-0.0.1-SNAPSHOT.jar

#execute the application
ENTRYPOINT ["java","-jar","/config-server-0.0.1-SNAPSHOT.jar"]