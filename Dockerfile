FROM openjdk:19-jdk-alpine AS build
WORKDIR /workspace/app
COPY . /workspace/app
RUN chmod +x ./gradlew
RUN --mount=type=cache,target=/root/.gradle ./gradlew clean bootJar --no-daemon
RUN mkdir -p dependency && (cd dependency; jar -xf ../application/build/libs/*.jar)

FROM openjdk:19-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 8081
ENTRYPOINT ["java","-cp","app:app/lib/*:app/classes","com.application.SingleDeviceLoginApplicationKt"]
