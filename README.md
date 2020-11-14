java-app-struct
===============
	java起手工程

# Develop
## server side
```
mvn spring-boot:run
```

## client side
```
cd src/main/webapp/admin/
npm run dev
```

## Building for production

### Packaging as jar

To build the final jar and optimize the jwt application for production, run:
```
mvn -Pprod clean verify
```    
### Packaging as war

To package your application as a war in order to deploy it to an application server, run:
```
mvn -Pwar clean verify
```

# References
- [jhipster](https://www.jhipster.tech/)
- [vue-element-admin](https://panjiachen.github.io/vue-element-admin/)