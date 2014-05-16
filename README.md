## BaseX Client for Clojure

A Clojure-ified wrapper around the BaseX Java client. It uses (and distributes under BSD) the [BaseXClient class](https://github.com/BaseXdb/basex/blob/master/basex-examples/src/main/java/org/basex/examples/api/BaseXClient.java).

## Usage

### Dependency

The library is available via [Clojars](#TODO)

With Leiningen

```clojure
[TODO "TODO"]
```

Or with Maven

```xml
<dependency>
  <groupId>???</groupId>
  <artifactId>???</artifactId>
  <version>???</version>
</dependency>
```

### In Code

With the library you will be able to connect to a running BaseX server instance, execute database commands, perform queries or listen to events. There are 2 main forms of operation

- __Standard Mode__: connecting to a server, sending commands
- __Query Mode__: defining queries, binding variables, iterative evaluation

The library is broken up in a similar manner.

- `basex.client`: holds functions related to __Standard Mode__ operations
- `basex.query`: holds functions related to __Query Mode__ operations
- `basex.notifications`: holds functions that relate to events handling

## License

Copyright © 2014 James Hughes

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
