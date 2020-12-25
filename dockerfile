FROM openjdk:8-alpine


# Expose the http port
EXPOSE 8080

ADD /mlserving/spring_api/target/iris_spring_api-1.0-SNAPSHOT.jar /rest-app/iris_spring_api-1.0-SNAPSHOT.jar
ADD /model/iris_spark_model.zip /model/iris_spark_model.zip

ENTRYPOINT ["java","-jar","/rest-app/iris_spring_api-1.0-SNAPSHOT.jar"]