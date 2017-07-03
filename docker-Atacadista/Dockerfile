FROM frolvlad/alpine-oraclejdk8:slim
COPY orcamentos.json /Orcamentos.json 
COPY produto.json /produto.json
COPY Solicitacoes.json /Solicitacoes.json 
ADD asdof07-abe-final-atacadista-0.1.0.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
EXPOSE 8091