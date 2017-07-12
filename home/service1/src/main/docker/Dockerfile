FROM klauss42/scs-poc-java8-filebeat

ENV LOGSTASH_HOST elk
ENV LOGSTASH_PORT 5044
ARG ARTIFACT_ID
ARG ENABLE_FILEBEAT

ADD ./maven /maven

RUN echo "export ARTIFACT_ID=${ARTIFACT_ID}" > /scripts/vars.sh
