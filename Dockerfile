FROM adoptopenjdk:11-jre-hotspot
RUN mkdir /opt/app
ADD build/libs/platter-0.0.1-SNAPSHOT.jar /opt/app/app.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/opt/app/app.jar" ]
