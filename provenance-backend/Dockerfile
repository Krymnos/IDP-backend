FROM openjdk:8-jdk-alpine
ADD src/test/resources/conf.xml conf.xml
ADD target/provenance-backend-1.0.0-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENV ARGUMENTS="./conf.xml"

ENV CONF_LOC=EVAR
ENV DB_IP=cassandra
ENV DB_PORT=9042
ENV KEYSPACE=provenancekey
ENV TABLE=provenancetable

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar $ARGUMENTS" ]
