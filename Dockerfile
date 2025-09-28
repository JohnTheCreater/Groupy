FROM openjdk:24-jdk-slim


WORKDIR /Groupy

COPY out/artifacts/Groupy_jar/Groupy.jar .

EXPOSE 5500

CMD ["java","-jar","Groupy.jar"]
