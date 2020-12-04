# **Criando uma imagem docker**

- [**Criando uma imagem docker**](#criando-uma-imagem-docker)
  - [**Dockerfile**](#dockerfile)
  - [**Criando a imagem**](#criando-a-imagem)
  - [**Rodando um container na Imagem**](#rodando-um-container-na-imagem)
    - [**Parâmetros**](#parâmetros)
    - [**Relatórios**](#relatórios)
  - [**Referências**](#referências)
  
- [**Código-fonte**](https://github.com/rwcosta/ScalaLearning/tree/main/Gatling%20Testes/Mvn%20Gatling%20Image)

## **Dockerfile**

Para a criação de uma imagem docker nós utilizamos o Dockerfile:

```Dockerfile
FROM maven:3-alpine
COPY . /user/src/myimage
WORKDIR /user/src/myimage

RUN apk update \
    && apk add python3

EXPOSE 8080

CMD ["echo", "Gatling-Maven criado."]
```

Utilizamos como base a imagem do maven `maven:3-alpine` que já contém o **openjdk** e utiliza a distro linux **Alpine** que é mais leve do que outras distribuições pesando 5mb em média. Também foi instalado o `python3` para auxiliar na visualização do arquivo `index.hmtl` utilizando a porta 8080 do container que foi indicado pelo `EXPOSE`.

## **Criando a imagem**

No Dockerfile foi especificado para copiar todos os arquivos do diretório atual para a imagem, no nosso caso foi copiado o arquivo `gatling-framework` que foi criado pelo `mvn archetype` já contendo as simulações. Para criar a imagem basta executar:

```console
$ docker build -t rwcosta/mvngtl:v1 .
```

Caso o Dockerfile não esteja no diretório atual, é necessário especificar após o nome da imagem. Com a imagem criada é possível visualizá-la:

```console
$ docker image ls

REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
rwcosta/mvngtl      v1                  00ae4e5eeb38        2 hours ago         197MB
```

## **Rodando um container na Imagem**

Para rodar um container na imagem criada utilizamos:

```console
$ docker container run -d -p 8080:8080 --name MyContainer -v gtl-volm:/user/src/myimage -it rwcosta/mvngtl:v1 /bin/bash
```

### **Parâmetros**

* **`-d`**: Entra em modo **daemon**.
* **`-p 8080:8080`**: Informa para o docker ligar a nossa porta 8080 do host (esquerda) com a porta 8080 do container (direita).
* **`--name MyContainer`**: Define uma nome para o nosso container, assim não precisamos ficar passando o seu ID.
* **`-v gtl-volm:/user/src/myimage`**: Definimos um volume para que os dados do container não sejam perdidos quando encerrar a execução, nesse local serão armazenados os dados do `WORKDIR` da imagem, `/user/src/myimage` no nosso caso. Para descobrir onde os dados do container estão salvos pode-se utilizar o comando `docker inspect MyContainer | grep "gtl-volm"`:

```console
"gtl-volm:/user/src/myimage"
"Name": "gtl-volm",
"Source": "/var/snap/docker/common/var-lib-docker/volumes/gtl-volm/_data",
```

O local do meu volume `gtl-vol` é em `/var/snap/docker/common/var-lib-docker/volumes/gtl-volm/_data`.

Para visualizar o container:

```console
$ docker container ls

CONTAINER ID        IMAGE                  COMMAND                  CREATED             STATUS              PORTS                    NAMES
9affc3b57550        rwcosta/mvngtl:v1      "/usr/local/bin/mvn-…"   12 minutes ago      Up 12 minutes       0.0.0.0:8080->8080/tcp   MyContainer
```

Para entrar no modo interativo no container utilizamos:

```console
$ docker container exec -it MyContainer /bin/bash
```

No bash temos acesso aos arquivos do container, para executar o teste basta ir para o diretório criado pelo `mvn archetype` e executar normalmente o `mvn gatling:test`:

```console
$ cd Gatling\ Maven/gatling-framework/
$ mvn gatling:test -Dgatling.simulationClass=simulations.ReqresSimulation
```

### **Relatórios**

Caso seja necessário visualizar o relatório gerado no `index.html`, basta ir para o diretório onde se encontra o arquivo e executar:

```console
pushd index.html; python3 -m http.server 8080; popd;
```

Para visualizar os relatórios basta acessar o `localhost:8080` em qualquer navegador.

## **Referências**

* [Install Scala and sbt on Alpine Linux](https://gist.github.com/gyndav/c8d65b59793566ee73ed2aa25aa10497)
