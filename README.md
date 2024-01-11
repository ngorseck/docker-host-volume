![work status](https://img.shields.io/badge/work-on%20progress-red.svg)
![Ngor Seck](https://img.shields.io/badge/Ngor%20SECK-Java-green)
![Java](https://img.shields.io/badge/Ngor%20SECK-Spring-yellowgreen)
![Jetty](https://img.shields.io/badge/Ngor%20SECK-TomcatWebServer-blue)
# Documentation

Ce projet vous montre comment mettre en place un volume docker pour une application spring boot piloter par maven.


## Configuration du projet

> J'ai d'abord créé un projet spring boot de base.

> J'ai ensuite créé un repertoire ***datas*** a la racine du projet pour y monter un volume

> Puis j'ai ajouté cette configuration dans le fichier application.properties : 
```bash
app.data.folder = ${directoryDatas:./datas/}
directoryDatas est juste une varibale d'environnement et si cette varibale nexiste pas alors ***./datas/*** sera la valeur de app.data.folder
```

## Mise en place d'un endpoint de création d'un fichier dans le répertoire de notre variable d'environnement
```bash
    private String directory_name ;
    ***La valeur de directory_name vient de la variable d'environnement ou par défaut c'est le répertoire datas***
    public FileController(@Value(("${app.data.folder}"))String directory_name) {
        this.directory_name = directory_name;
    }

    @GetMapping("/{fileName}")
    public String create(@PathVariable("fileName") String fileName) throws IOException {
        Path newFilePath = Paths.get(directory_name + fileName + ".txt");
        Files.createFile(newFilePath);
        return "File created";
    }
```

## Conteneurisation
Ici un repertoire ***/app/data*** sera créé lors de la création des conteneurs 
et ce dernier est défini comme ***volume dans le docker compose***
```bash
FROM openjdk:17-jdk-slim

LABEL maintainer="Ngor SECK"

EXPOSE 8080

RUN mkdir -p /app/data

ADD target/dockervolume.jar dockervolume.jar

ENTRYPOINT ["java", "-jar", "dockervolume.jar"]
```

```bash
version: '3.9'

services:

  app-dockervolume-backend:
    image: dockervolume-backend:1.0
    container_name: container-dockervolume-backend
    ports:
      - 8080:8080
    restart: unless-stopped
    build:
      context: ./
      dockerfile: Dockerfile
    environment:
      directoryDatas: /app/data/
    volumes:
      - ./datas:/app/data

volumes:
  datas:
```
## Installation

Clonez juste le projet comme ceci

```bash
git clone https://github.com/ngorseck/docker-host-volume.git

cd docker-host-volume/

mvn clean install

docker compose up -d

Pour entrer dans le conteneur:

docker exec -it [CONTAINER_ID] bash
ls
ls  app/data/
```

## Usage

Et pour lancer le projet avec le port 8080 puis visualisation des endpoints puis test :

```bash
Pour la création dun fichier clients.txt qui sera visible dans le conteneur (app/data) et en local (dans le répertoire datas)
http://localhost:8080/create/clients
Pour la création dun fichier fournisseurs.txt qui sera visible dans le conteneur (app/data) et en local (dans le répertoire datas)
http://localhost:8080/create/fournisseurs
Pour la création dun fichier commandes.txt qui sera visible dans le conteneur (app/data) et en local (dans le répertoire datas)
http://localhost:8080/create/commandes
```

## Usage des volumes
```bash
Vous pouvez ajouter manuellement un fichier dans le répertoire datas puis 
vérifier dans la conteneur, vous allez voir qu'il y sera.
```

## github action
```bash
Un plugin est ajouter dans le fichier pom.xml
un fichier build.yml est aussi ajouté dans .github/workflows pour les jobs
```
