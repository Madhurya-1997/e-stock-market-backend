#Start with a base image containing Java runtime
FROM openjdk:15

#Information around who maintains the image
MAINTAINER heritage.com

# Add the application's jar to the container
ADD target/eureka-discovery-server.jar eureka-discovery-server.jar

#execute the application
ENTRYPOINT ["java","-jar","eureka-discovery-server.jar"]