(ns basex.core
  (:require [basex.client        :as client]
            [basex.query         :as query]
            [basex.notifications :as notify])
  (:import (org.basex BaseXServer))
  (:gen-class))


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
