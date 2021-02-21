# News Feed

News Feed is a small application that retrieves in an XML format the last news from [this portal](http://feeds.nos.nl/nosjournaal?format=xml) every 05 minutes. 
This information will be parsed using SAX Parser in order to persist the data or update it if they already exist into an [in-memory database called H2](https://www.h2database.com/html/main.html). 

We will use [GraphQL](https://graphql.org/) to expose the data via queries on a small playground called GraphiQL.


## Setting up - Environment Settings and Versions

IDE: IntelliJ IDEA Ultimate 2020.2.3

Project SDK: OpenJDK 15.0.1

Maven: Apache Maven 3.6.3

## Starting the App 

```bash
mvn install
mvn spring-boot:run
```
## GraphiQL Queries

You can find GraphiQL playground in your own localhost:8080/graphiql

Basic implemented queries:




Select a specific New by ID

```bash
{
  selectNewById(id: idLong) {
  	id,
    guid,
    title,
    description,
    image
    }
}
```

Select a specific New by GUID

```bash
{
  selectNewByGuid(guid: guidInteger) {
    id,
    guid,
    title,
    description,
    image
	}
}
```
Select All News

```bash
{
  selectAllNews {
    id,
    guid,
    title,
    description,
    image
	}
}
```
