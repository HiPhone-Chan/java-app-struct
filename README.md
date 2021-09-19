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

# rbac
```
	api需要以/api/staff开始的才会启用rbac功能
```

# References
- [jhipster](https://www.jhipster.tech/)
- [vue-admin](https://github.com/HiPhone-Chan/vue-admin)