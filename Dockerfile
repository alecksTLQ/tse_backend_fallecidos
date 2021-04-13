FROM java:8 AS build-env
MAINTAINER Jorge Santos "netneill@hotmail.com"

# update packages and install maven
RUN  export DEBIAN_FRONTEND=noninteractive 
RUN  sed -i 's/# \(.*multiverse$\)/\1/g' /etc/apt/sources.list 

RUN echo "deb [check-valid-until=no] http://archive.debian.org/debian jessie-backports main" > /etc/apt/sources.list.d/jessie-backports.list
# As suggested by a user, for some people this line works instead of the first one. Use whichever works for your case
# RUN echo "deb [check-valid-until=no] http://archive.debian.org/debian jessie main" > /etc/apt/sources.list.d/jessie.list
RUN sed -i '/deb http:\/\/deb.debian.org\/debian jessie-updates main/d' /etc/apt/sources.list
RUN apt-get -o Acquire::Check-Valid-Until=false update

RUN  apt-get -y upgrade 
RUN  apt-get install -y vim wget curl git maven

# create working directory
RUN mkdir -p /local/git

RUN mkdir app
COPY . app
WORKDIR /app

RUN mvn install:install-file -Dfile=lib/ojdbc7.jar -DgroupId=com.oracle -DartifactId=ojdbc7 -Dversion=12.1.0 -Dpackaging=jar -DgeneratePom=true
RUN mvn package

FROM openjdk:8-jdk-alpine AS builder
COPY --from=build-env /app/target target
ENTRYPOINT ["java","-jar","target/Ixmukane-api-0.0.1-SNAPSHOT.jar"]



