# java-app-struct
java起手工程

## Building for production

### Packaging as jar

To build the final jar and optimize the jwt application for production, run:

    mvn -Pprod clean verify
    
### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

    mvn -Pwar clean verify