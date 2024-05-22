FROM openjdk:17
ARG JAR_FILE=target/Retailer-App.jar
COPY ${JAR_FILE} retailer-app.jar
EXPOSE 9000
RUN mkdir /logs
RUN chmod 777 /logs
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -jar /retailer-app.jar