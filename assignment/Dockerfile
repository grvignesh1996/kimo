FROM maven:3-jdk-8-alpine

COPY ./target/assignment-0.0.1-SNAPSHOT.jar assignment.jar


ENTRYPOINT ["java","-jar","assignment.jar"]

ENV PORT 8080
EXPOSE $PORT
