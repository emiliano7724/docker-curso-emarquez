
ARG BASE_IMAGE=jelastic/maven:3.9.5-openjdk-21
# Usar la imagen base de Maven para la construcción
FROM $BASE_IMAGE AS build

WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN chmod +x mvnw
RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)


FROM $BASE_IMAGE
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

USER root
COPY startup.sh /etc/startup.sh
RUN chmod +x /etc/startup.sh
ENTRYPOINT ["/etc/startup.sh"]