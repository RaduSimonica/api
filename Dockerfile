FROM openjdk:17-jdk
RUN useradd -ms /bin/bash jdkUsr
WORKDIR /api
COPY ./build/libs/api.jar .
RUN chown jdkUsr:jdkUsr api.jar
RUN chmod +x api.jar
USER jdkUsr
CMD ["java", "-jar", "api.jar"]