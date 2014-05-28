## BaseX Client for Clojure

[Build Status](https://travis-ci.org/kouphax/clj-basex)

A Clojure-ified wrapper around the BaseX Java client. It uses (and distributes under BSD) the [BaseXClient class](https://github.com/kouphax/basex-clojure-client/blob/master/src/main/java/basex/core/BaseXClient.java) and does not attempt to add a raft of additional features (save for some Clojure flavoured tweaks).

## Usage

### Dependency

> The library is not yet released as yet so this content is not strictly accurate.  Or, if you're feeling snarky - a lie.

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

[Generated API Documentation](https://rawgit.com/kouphax/basex-clojure-client/master/doc/index.html) (temporary location)

With the library you will be able to connect to a running BaseX server instance, execute database commands, perform queries or listen to events. There are 2 main forms of operation

- __Standard Mode__: connecting to a server, sending commands
- __Query Mode__: defining queries, binding variables, iterative evaluation

The library is broken up in a similar manner.

- `basex.session`: holds functions related to __Standard Mode__ operations
- `basex.query`: holds functions related to __Query Mode__ operations

#### `basex.session`

- `create-session`
- `with-session`
- `execute`
- `create`
- `add`
- `replace`
- `store`
- `info`
- `close`
- `watch`
- `unwatch`

#### `basex.query`

- `create-query`
- `with-query`
- `bind`
- `context`
- `more?`
- `next`
- `results`
- `execute`
- `info`
- `options`
- `close`

### Production Ready?

Its a small wrapper over a client that has been around for a while.  It's fairly simple and last time I checked the examples passed (codified as the test suite).  So honestly - yeah it probably is.  Have I used in it __production__? No, but I would.  Have a look at the code, it'll not take long, and decide for yourself. YMMV.

## License

Copyright © 2014 James Hughes

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
