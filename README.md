[![Build Status](https://travis-ci.com/Want100Cookies/GradeMaster.svg?token=sKpyGyXRMBtmPh6qJBuM&branch=develop)](https://travis-ci.com/Want100Cookies/GradeMaster)

# GradeMaster
A small application to ease the grading of project groups for teachers.

### Features:
- Teachers can assign and deadline a grade to a group
- Students can grade fellow group members before the deadline ends
- Teachers can finalize the given grades and export it to CSV/PDF

### Documentation
Swagger is used to document the API.
To view the documentation, run the Spring boot app and visit [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Setup
##### Backend
Docker is being used to run all the needed backend services:
```
$ docker-compose up -d
```

To run the backend the following environment variables should exist:
- EMAIL_HOST
- EMAIL_PORT
- EMAIL_USERNAME
- EMAIL_PASSWORD

*For intellij; Edit configurations... > **Select configuration** > Environment variables*

##### Frontend
```
$ cd ./src/main/resources/public
$ npm install
```

##### Now you're ready to run the Spring boot app :)

### Requirements
- Java SDK          >= 8.x
- NPM               >= 5.x
- Node              >= 9.x
- Docker            >= 17.x
- Docker-compose    >= 1.18.x