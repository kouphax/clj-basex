(ns basex.session-test
  (:require [midje.sweet   :refer :all]
            [basex.support :refer [with-test-server]]
            [me.raynes.fs  :refer [delete-dir temp-dir]]
            [basex.session :as basex]
            [basex.query   :as query]))

(with-test-server

    (fact "We can create a session with or without a spec"
      (basex/create-session)                => truthy
      (basex/create-session {})             => truthy
      (basex/create-session { :port 1984 }) => truthy)

    (fact "Exceptions be thrown when config isn't right when creating a session"
      (basex/create-session { :password "invalid" }) => (throws Exception)
      (basex/create-session { :username "invalid" }) => (throws Exception)
      (basex/create-session { :port 1990 })          => (throws Exception))

    (fact "We can execute arbitrary xquery commands"
      (basex/with-session [session (basex/create-session)]
        (basex/execute session "xquery 1 to 10") => (exactly "1\n2\n3\n4\n5\n6\n7\n8\n9\n10")
        (basex/execute session "create db database")
        (basex/info session) => (contains #"Database 'database' created in")
        (basex/execute session "drop db database")
        (basex/info session) => (contains #"Database 'database' was dropped")))

    (fact "We can add and query documents"
      (basex/with-session [session (basex/create-session)
                            doc-1   (java.io.ByteArrayInputStream. (.getBytes "<x>Hello World 1!</x>"))
                            doc-2   (java.io.ByteArrayInputStream. (.getBytes "<x>Hello World 2!</x>"))
                            doc-3   (java.io.ByteArrayInputStream. (.getBytes "<x>Hello World 3!</x>"))]
        (basex/execute session "create db database")
        (basex/add session "doc-1" doc-1)
        (basex/info session) => (contains #" added in ")
        (basex/add session "doc-2" doc-2)
        (basex/info session) => (contains #"Resource\(s\) added in ")
        (basex/execute session "xquery collection('database')") => (exactly "<x>Hello World 1!</x>\n<x>Hello World 2!</x>")
        (basex/replace session "doc-2" doc-3)
        (basex/execute session "xquery collection('database')") => (exactly "<x>Hello World 1!</x>\n<x>Hello World 3!</x>")
        (basex/execute session "drop db database")))

    (fact "We can store binary files"
      (basex/with-session [session  (basex/create-session)
                            data    (byte-array (map byte (range 128)))
                            doc-in  (java.io.ByteArrayInputStream. data)
                            doc-out (java.io.ByteArrayOutputStream.)]
        (basex/execute session "create db database")
        (basex/store session "test.bin" doc-in)
        (basex/info session) => (contains #"Query executed in")
        (basex/execute session "retrieve test.bin" doc-out)
        (java.util.Arrays/equals data (.toByteArray doc-out)) => true
        (basex/execute session "drop db database"))))
