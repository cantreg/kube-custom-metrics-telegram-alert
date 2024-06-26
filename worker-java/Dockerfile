FROM openjdk:17-alpine

WORKDIR /worker
COPY . .
RUN ./gradlew bootJar

WORKDIR ./build/libs
EXPOSE 8080
CMD java -jar worker-1.0.0.jar