(ns basex.client-test
  (:require [midje.sweet :refer :all]
            [basex.client :as client]
            [me.raynes.fs :refer [delete-dir temp-dir]])
  (:import (org.basex BaseXServer)))

(def data-path
  (.getAbsolutePath
    (temp-dir "basex")))

(System/setProperty "org.basex.DBPATH" data-path)
(let [server (BaseXServer. (into-array String []))]
  (try

    (fact "We can create a session with or without a spec"
      (client/create-session)                => truthy
      (client/create-session {})             => truthy
      (client/create-session { :port 1984 }) => truthy)

    (fact "Exceptions be thrown when config isn't right when creating a session"
      (client/create-session { :password "invalid" }) => (throws Exception)
      (client/create-session { :username "invalid" }) => (throws Exception)
      (client/create-session { :port 1990 })          => (throws Exception))

    (fact "We can execute arbitrary xquery commands"
      (let [session (client/create-session)]
        (client/execute session "xquery 1 to 10") => (exactly "1 2 3 4 5 6 7 8 9 10")
        (client/execute session "create db database")
        (client/info session) => (contains #"Database 'database' created in")
        (client/execute session "drop db database")
        (client/info session) => (contains #"Database 'database' was dropped")))

    (fact "We can add and query documents"
      (let [session (client/create-session)
            doc-1   (java.io.ByteArrayInputStream. (.getBytes "<x>Hello World 1!</x>"))
            doc-2   (java.io.ByteArrayInputStream. (.getBytes "<x>Hello World 2!</x>"))
            doc-3   (java.io.ByteArrayInputStream. (.getBytes "<x>Hello World 3!</x>"))]
        (client/execute session "create db database")
        (client/add session "doc-1" doc-1)
        (client/info session) => (contains #"Path \"doc-1\" added in")
        (client/add session "doc-2" doc-2)
        (client/info session) => (contains #"Path \"doc-2\" added in")
        (client/execute session "xquery collection('database')") => (exactly "<x>Hello World 1!</x><x>Hello World 2!</x>")
        (client/replace session "doc-2" doc-3)
        (client/execute session "xquery collection('database')") => (exactly "<x>Hello World 1!</x><x>Hello World 3!</x>")
        (client/execute session "drop db database")))

    (fact "We can store binary files"
      (let [session (client/create-session)
            data    (byte-array (map byte (range 128)))
            doc-in  (java.io.ByteArrayInputStream. data)
            doc-out (java.io.ByteArrayOutputStream.)]
        (client/execute session "create db database")
        (client/store session "test.bin" doc-in)
        (client/info session) => (contains #"Query executed in")
        (client/execute session "retrieve test.bin" doc-out)
        (java.util.Arrays/equals data (.toByteArray doc-out)) => true
        (client/execute session "drop db database")))

    (finally
      (.stop server)
      (delete-dir data-path))))
