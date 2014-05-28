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

With the library you will be able to connect to a running BaseX server instance, execute database commands, perform queries or listen to events. There are 2 main forms of operation (as per the BaseX documentation itself)

- __Standard Mode__: connecting to a server, sending commands
- __Query Mode__: defining queries, binding variables, iterative evaluation

The library is broken up in a similar manner.

- `basex.session`: holds functions related to __Standard Mode__ operations
- `basex.query`: holds functions related to __Query Mode__ operations

#### `basex.session`

We can import `basex.session` where we want to use it

```clojure
(:require [basex.session :as basex])
```

##### `create-session`

`create-session` is used to establish a connection and setup a session with a running BaseX server.  It can either use the default `db-spec` (a map of BaseX server properties) or provide its own settings via the call.

```clojure
; create a session with the default db-spec
(def session (basex/create-session))

; create a session but override the port
(def session (basex/create-session { :port 1985 }))

; create a session with a complete db-spec
(def session (basex/create-session     
  { :host     "localhost"
    :port     1984
    :username "admin"
    :password "admin" }))
```

As demonstrated above the `db-spec` is made up of `:host`, `:port`, `:username` and `:password` keys.  All other keys will be ignored.

If the `db-spec` points to an available BaseX server instance a session will be created and returned.  If the server is not available an exception will be thrown.

##### `with-session`

`with-session` is a macro that ensures the created session is correctly closed regardless of how the wrapped code behaves.  You can specify any number of bindings (as per `let`) however the first binding __must be__ the session declaration.

```clojure
(basex/with-session [session (basex/create-session)
                     data    (get-data)]
  (do-some-work-1 session data)
  (do-some-work-2 session data))
```

When the form completes or an exception is thrown it will attempt to close the declared `session` to avoid leaking of resources.

##### `execute`

`execute` allows you to send commands to the sessions connected BaseX server.  Commands are sent as a string and output can either be returned diretly from the execute method as a string or pumped into a stream.

```clojure
(basex/with-session [session (basex/create-session)]
  (basex/execute session "xquery 1 to 10"))
  
; 1 2 3 4 5 6 7 8 9 10
```

This is the lowest form of server command communication and can be used to achieve other _higher level_ methods.  But at the end of the day its all just strings over the wire.

##### `create`

`create` is used to create a new database using the passed in stream of XML as a basis. It will also then implicitly switch to the databse within the current session.  

```clojure
(basex/with-session [session (basex/create-session)
                     doc     (java.io.ByteArrayInputStream. (.getBytes "<x>Hello World 1!</x>"))]
  (basex/create session doc))
```

__IMPORTANT__: `create` will __override__ any existing database with the supplied name.

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
