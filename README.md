#### Structure
      + config/
          All app setups. Javalin, Koin and Database
      + domain/
        + repository/
            Persistence layer and tables definition
        + service/
            Logic layer and transformation data
      + ext/
          Extension of String for email validation
      + utils/
          Jwt and Encrypt classes
      + web/
        + controllers
            Classes and methods to mapping actions of routes
        Router definition to features and exceptions
      - App.kt <- The main class

#### Database

It uses a H2 in memory database

# Getting started

Need JVM installed.

The server is configured to start on [7000](http://localhost:7000/api) with `api` context, which can be changed in `koin.properties`.

