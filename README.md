java-app-struct
===============
	java起手工程

# Develop
To start your application in the dev profile, run:
```
mvn
```


# Building for production

## Packaging as jar

To build the final jar and optimize the jwt application for production, run:
```
mvn -Pprod clean verify
```    

## Packaging as war

To package your application as a war in order to deploy it to an application server, run:
```
mvn -Pprod,war clean verify
```

# Doing API-First development using openapi-generator

[OpenAPI-Generator]() is configured for this application. You can generate API code from the `src/main/resources/swagger/api.yml` definition file by running:

```bash
mvn generate-sources
```

Then implements the generated delegate classes with `@Service` classes.

To edit the `api.yml` definition file, you can use a tool such as [Swagger-Editor](). Start a local instance of the swagger-editor using docker by running: `docker-compose -f src/main/docker/swagger-editor.yml up -d`. The editor will then be reachable at [http://localhost:7742](http://localhost:7742).

# References
- [jhipster](https://www.jhipster.tech/)
- [vue-admin](https://github.com/HiPhone-Chan/vue-admin)