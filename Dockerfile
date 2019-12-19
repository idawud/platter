FROM adoptopenjdk:11-jre-hotspot

COPY build/libs/platter-0.0.1-SNAPSHOT.jar /batch.jar
RUN chmod 777 batch.jar
CMD ["java","-jar", "batch.jar"]