FROM public.ecr.aws/docker/library/gradle:7.6.1-jdk17-alpine as builder
WORKDIR /build

COPY ./item-service /build
RUN gradle build -x test --parallel

FROM public.ecr.aws/docker/library/amazoncorretto:17-alpine3.17-jdk as deps
#FROM gcr.io/distroless/java11-debian10 as distroless
# required for strip-debug to work
#RUN apk add --no-cache binutils
COPY --from=builder /build/build/libs/item-service.jar /app/item-service.jar

RUN mkdir /app/unpacked && \
    cd /app/unpacked && \
    unzip ../item-service.jar && \
    cd .. && \
    $JAVA_HOME/bin/jdeps \
    --ignore-missing-deps \
    --print-module-deps \
    -q \
    --recursive \
    --multi-release 17 \
    --class-path="./unpacked/BOOT-INF/lib/*" \
    --module-path="./unpacked/BOOT-INF/lib/*" \
    ./item-service.jar > /deps.info

FROM public.ecr.aws/docker/library/amazoncorretto:17-alpine3.17-jdk as corretto-jdk

RUN apk add --no-cache binutils

COPY --from=deps /deps.info /deps.info
# Build small JRE image
RUN $JAVA_HOME/bin/jlink \
         --verbose \
         --add-modules $(cat /deps.info) \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /customjre

# main app image
FROM public.ecr.aws/docker/library/alpine:3.20.3
ENV JAVA_HOME=/jre
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# copy JRE from the base image
COPY --from=corretto-jdk /customjre $JAVA_HOME
#COPY --from=distroless /customjre $JAVA_HOME

# Add app user
ARG APPLICATION_USER=nobody
#ARG APPLICATION_USER=appuser
#RUN adduser --no-create-home -u nobody -D $APPLICATION_USER

# Configure working directory
RUN mkdir /app && \
    chown -R $APPLICATION_USER /app


COPY --chown=nobody:nobody --from=builder /build/build/libs/item-service.jar /app/item-service.jar
WORKDIR /app

EXPOSE 8080
# root 대신 nobody 권한으로 실행
USER nobody
ENTRYPOINT ["java", "-jar", "-Djava.security.egd=file:/dev/./urandom", "-Dsun.net.inetaddr.ttl=0", "-Dspring.profiles.active=prod", "/app/item-service.jar" ]
