java-app-struct
===============
	java起手工程(带页面)

# init
拉取子模块
```
git submodule update --init --recursive
```

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

# References
- [jhipster](https://www.jhipster.tech/)
- [vue-admin](https://github.com/HiPhone-Chan/vue-admin)