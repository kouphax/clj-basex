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

(defn results [query]
  (lazy-seq (loop [has-more? (more? query)
                   results   []]
              (if-not has-more?
                results
                (let [memo (conj results (next query))]
                  (recur (more? query) memo))))))

(defn execute [query]
  (.execute query))

(defn info [query]
  (.info query))

(defn options [query]
  (.options query))

(defn close [query]
  (.close query))

(defmacro with-query
  "Simple macro that allows clean handling of queries. Wraps statements in a
   try block that will attempt to close the query in the finally block.

   Bindings should start with the query declaration but contain as many
   declarations as you need

       (with-query [query (create-query session query-str)]
         (println (basex.query/execute query)))

   In the case above the query binding will be available for use in the body
   of the form. The query will be closed when the form completes"
  [bindings & body]
  `(let ~bindings
     (try
       ~@body
      (finally
        (close ~(bindings 0))))))
