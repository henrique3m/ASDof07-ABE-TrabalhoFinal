FROM frolvlad/alpine-oraclejdk8:slim
WORKDIR /app
ADD asdof07-abe-final-lojista-0.1.0.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
EXPOSE 80
