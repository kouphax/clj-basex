(ns basex.core
  (:require [basex.client        :as client]
            [basex.query         :as query]
            [basex.notifications :as notify])
  (:import (org.basex BaseXServer))
  (:gen-class))


(defn simple-example [session]
  (println (client/execute session "xquery 1 to 10")))

(defn add-example
  "This example shows how documents can be added to databases, and how existing
   documents can be replaced."
  [session]
  ; create an empty database
  (client/execute session "create db database")
  (println (client/info session))

  ; add a document
  (let [bais (java.io.ByteArrayInputStream. (.getBytes "<x>Hello World!</x>"))]
    (client/add session "world/world.xml" bais)
    (println (client/info session)))

  ; add another document
  (let [bais (java.io.ByteArrayInputStream. (.getBytes "<x>Hello Universe!</x>"))]
    (client/add session "universe.xml" bais)
    (println (client/info session)))

  ; run query on database
  (println (client/execute session "xquery collection('database')"))

  ; replace a document
  (let [bais (java.io.ByteArrayInputStream. (.getBytes "<x>Hello Replacement!</x>"))]
    (client/replace session "universe.xml" bais)
    (println (client/info session)))

  ; drop database
  (client/execute session "drop db database"))

(defn query-example
  "This example shows how queries can be executed in an iterative manner.
   Iterative evaluation will be slower, as more server requests are performed."
  [session]

  ; create query instance
  (let [input "for $i in 1 to 10 return <xml>Text { $i }</xml>"
        query (query/create-query session input)]

    ; loop through all results
    (while (query/more? query)
      (println (query/next query)))

    ; print query info
    (println (query/info query))

    ; close query instance
    (query/close query)))


(defn query-bind-example
  [session]
  (let [input "declare variable $name external;
               for $i in 1 to 10 return element { $name } { $i }"
        query (query/create-query session input)]
    (query/bind query "$name" "number")
    (println (query/execute query))
    (query/close query)))

(defn event-example
  "This example demonstrates how to trigger and receive database events"
  [session-1]
  (let [session-2 (client/create-session)]
    (try
      (client/execute session-1 "create event messenger")
      (notify/watch session-2 "messenger" (fn [value] (println value)))
      (-> (query/create-query session-2 "for $i in 1 to 1000000 where $i = 0 return $i")
          (query/execute))
      (-> (query/create-query session-1 "db:event('messenger', 'Hello World!')")
          (query/execute))
      (notify/unwatch session-2 "messenger")
      (client/execute session-1 "drop event messenger")
      (finally
        (client/close session-2)))))

(defn binary-example
  [session]
  (client/execute session "create db database")
  (println (client/info session))
  (let [data (byte-array (map byte (range 128)))
        bais (java.io.ByteArrayInputStream. data)
        baos (java.io.ByteArrayOutputStream.)]
    (client/store session "test.bin" bais)
    (println (client/info session))
    (client/execute session "retrieve test.bin" baos)
    (if (java.util.Arrays/equals data (.toByteArray baos))
      (println "Stored and retrieved bytes are equal.")
      (println "Stored and retrieved bytes differ!")))
  (client/execute session "drop db database"))

(defn -main [& args]
  (System/setProperty "org.basex.DBPATH" "./.basex/data")
  (let [server  (BaseXServer. (into-array String []))
        session (client/create-session)]
    (try
      (simple-example session)
      (add-example    session)
      (query-example  session)
      (event-example  session)
      (binary-example session)
      (query-bind-example session)
      (finally
        (client/close session)
        (.stop server)))))

(-main)

