FROM gradle:7.3.3 AS GRADLE_BUILD_IMAGE
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle installDist --no-daemon



FROM openjdk:17-alpine
EXPOSE 8080:8080
RUN mkdir "/app"
COPY --from=GRADLE_BUILD_IMAGE /home/gradle/src/build/install/adenn-requests/ /app/
COPY --from=GRADLE_BUILD_IMAGE /home/gradle/src/build/distributions/ /app/bin/
COPY ./.env /app/bin/.env
WORKDIR /app/bin
CMD ["./adenn-requests"]