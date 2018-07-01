FROM maven:3.5.2-jdk-10-alpine

MAINTAINER https://github.com/DragonetMC

# create the group and user
RUN addgroup proxy && adduser -g "DragonProxy user" -s /bin/ash -D proxy -G proxy

# application placed into /opt/app
RUN mkdir -p /home/proxy/compile
WORKDIR /home/proxy/compile

# Copy project sources
COPY pom.xml    /home/proxy/compile/pom.xml
COPY api        /home/proxy/compile/api
COPY commons    /home/proxy/compile/commons
COPY plugin     /home/proxy/compile/plugin
COPY protocol   /home/proxy/compile/protocol
COPY proxy      /home/proxy/compile/proxy
COPY .git       /home/proxy/compile/.git

# Build artifact
RUN mvn package -e

# Copy artifact
RUN cp /home/proxy/compile/proxy/target/dragonproxy-*-SNAPSHOT.jar /home/proxy/dragonproxy.jar

# Clean compile and maven cache
RUN rm -rf /home/proxy/compile

WORKDIR /home/proxy

CMD ["java", "-Dorg.dragonet.proxy.profile=container", "-jar", "/home/proxy/dragonproxy.jar"]
