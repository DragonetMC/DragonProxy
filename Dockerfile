FROM openjdk:8u151-jdk-alpine3.7

RUN addgroup proxy && \
    adduser -g "DragonProxy user" -s /bin/ash -D proxy -G proxy

WORKDIR /home/proxy

ADD proxy/target/dragonproxy-* dragonproxy.jar

CMD ["java", "-jar", "dragonproxy.jar"]