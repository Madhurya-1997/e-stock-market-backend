#Start with a base image containing Java runtime
FROM openjdk:15-slim as build

#Information around who maintains the image
MAINTAINER heritage.com

# Add the application's jar to the container
COPY target/api-gateway.jar api-gateway.jar

#execute the application
ENTRYPOINT ["java","-jar","/api-gateway.jar"]