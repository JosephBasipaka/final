FROM openjdk:17
LABEL maintainers="jo"
ADD target/final-0.0.1-SNAPSHOT.jar final.jar
ENTRYPOINT ["java","-jar","final.jar"]