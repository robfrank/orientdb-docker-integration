############################################################
# Dockerfile to run an OrientDB (Graph) Container
############################################################

FROM openjdk:8-jdk-alpine

MAINTAINER OrientDB LTD (info@orientdb.com)

ENV ORIENTDB_VERSION 3.0.0-SNAPSHOT

ENV ORIENTDB_DOWNLOAD_URL  "http://oss.sonatype.org/service/local/artifact/maven/content?r=snapshots&g=com.orientechnologies&a=orientdb-community-spatial&e=tar.gz&v=$ORIENTDB_VERSION"

RUN apk add --update tar curl openssl \
    && rm -rf /var/cache/apk/*

#download distribution tar, untar and delete databases
RUN mkdir /orientdb && \
  wget  $ORIENTDB_DOWNLOAD_URL -O orientdb-community-spatial-$ORIENTDB_VERSION.tar.gz \
  && tar -xvzf orientdb-community-spatial-$ORIENTDB_VERSION.tar.gz -C /orientdb --strip-components=1 \
  && rm orientdb-community-spatial-$ORIENTDB_VERSION.tar.gz \
  && rm -rf /orientdb/databases/*


ENV PATH /orientdb/bin:$PATH

VOLUME ["/orientdb/backup", "/orientdb/databases", "/orientdb/config"]

WORKDIR /orientdb

#OrientDb binary
EXPOSE 2424

#OrientDb http
EXPOSE 2480

# Default command start the server
CMD ["server.sh"]

