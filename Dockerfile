FROM openjdk:17-alpine
EXPOSE 8080:8080
RUN mkdir "/app"
COPY ./build/install/adenn-requests/ /app/
COPY ./build/distributions/ /app/bin/
COPY ./.env /app/bin/.env
WORKDIR /app/bin
CMD ["./adenn-requests"]