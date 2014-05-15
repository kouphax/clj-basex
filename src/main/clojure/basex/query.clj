(ns basex.query
  (:refer-clojure :exclude [next]))

(defn create-query [session query]
  (.query session query))

(defn bind
  ([query name value]
   (.bind query name value))
  ([query name value type]
   (.bind query name value type)))

(defn context
  ([query value]
   (.context query value))
  ([query value type]
   (.context query value type)))

(defn more? [query]
  (.more query))

(defn next [query]
  (.next query))

(defn execute [query]
  (.execute query))

(defn info [query]
  (.info query))

(defn options [query]
  (.options query))

(defn close [query]
  (.close query))
