(ns basex.query
  (:refer-clojure :exclude [next]))

(defn create-query [session query]
  "Creates a new query instance for the given session based on the passed in
   query string.
   
   This will cause the query to be prepared but not processed."
  (.query session query))

(defn bind
  "Bind a particular value (and optionally a type) to a named variable in the 
   context of the query"
  ([query name value]
   (.bind query name value))
  ([query name value type]
   (.bind query name value type)))

(defn context
  "Bind a value (and optionally type) to the global context of the query"
  ([query value]
   (.context query value))
  ([query value type]
   (.context query value type)))

(defn more? 
  "Returns an indication if there are any more items left in the queries
   result set"
  [query]
  (.more query))

(defn next 
  "Returns the next result in the result set.  If we are past the end of 
   the results nil will simply be returned. This is a stepped alternative 
  to the execute method."
  [query]
  (.next query))

(defn results 
  "A more Clojure-y way to walk the result set of the executed query.
   
   Lazily executes the `next` function on the query until the query 
   returns nil.  This does not rely on the `more?` method."
  [query]
  (take-while #(not (nil? %))
              (repeatedly #(next query))))

(defn execute 
  "Execute the query as a single batch and return the result as a 
   single string.  Similar to the `session/execute` function."
  [query]
  (.execute query))

(defn info 
  "Returns info/status about the query"
  [query]
  (.info query))

(defn options 
  "Returns a string with all query serialization parameters."
  [query]
  (.options query))

(defn close 
  "Closes and unregisters the query"
  [query]
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
