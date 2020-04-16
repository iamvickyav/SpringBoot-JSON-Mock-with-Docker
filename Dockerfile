FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /opt/app
COPY /mockFiles /opt/app/mockFiles
COPY target/MockService.jar MockService.jar
ENTRYPOINT ["java","-jar","MockService.jar"]