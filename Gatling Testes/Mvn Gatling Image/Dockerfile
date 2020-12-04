FROM maven:3-alpine
COPY . /user/src/myimage
WORKDIR /user/src/myimage

RUN apk update \
    && apk add python3

EXPOSE 8080

CMD ["echo", "Gatling-Maven criado."]