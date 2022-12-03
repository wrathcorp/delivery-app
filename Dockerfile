FROM amazoncorretto:18
RUN mkdir /app

WORKDIR /app

ADD ./api/target/delivery-api-1.0.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "delivery-api-1.0.0-SNAPSHOT.jar"]
#ENTRYPOINT ["java", "-jar", "delivery-api-1.0.0-SNAPSHOT.jar"]
#CMD java -jar delivery-api-1.0.0-SNAPSHOT.jar
