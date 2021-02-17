# News Feed

News Feed is a small application that requests in an XML format the last news from [this portal](http://feeds.nos.nl/nosjournaal?format=xml) every 5 minutes. Later on, this information is parsed using SAX Parser into Java objects in order to persist new ones or update them if they already exist. [GraphQL](https://graphql.org/) is used as our API to store the data into an [in-memory database H2](https://www.h2database.com/html/main.html).


## Setting up - Environment Settings and Versions

IDE: IntelliJ IDEA Ultimate 2020.2.3

Project SDK: OpenJDK 15.0.1

Maven: Apache Maven 3.6.3

```bash
mvn install
mvn spring-boot:run
```

