(ns basex.client
  (:refer-clojure :exclude [replace])
  (:import (basex.core BaseXClient)))

(def ^:private default-db-spec
  "Default BaseX specification settings for connecting to a typical BaseX server"
  { :host     "localhost"
    :port     1984
    :username "admin"
    :password "admin" })

(defn create-session
  "Creates a new TCP session to an existing BaseX server. Can be called with or
   without a db-spec. If a db-spec is provided it doesn't need to be complete
   as the values will be merged with the default settings:

       { :host     \"localhost\"
         :port     1984
         :username \"admin\"
         :password \"admin\" }

   Returns the connected session. Throws IOException if it can't connect or the
   username or password is incorrect."
  ([] (create-session {}))
  ([db-spec]
   (let [spec (merge default-db-spec db-spec)]
     (BaseXClient. (:host spec)
                   (:port spec)
                   (:username spec)
                   (:password spec)))))

(defn execute
  "Sends commands to the BaseX server via the provided session. Can be passed
   an output stream to collect the server response or, if one is not provided,
   it will simply return the server response as a string from the call."
  ([session command output-stream]
   (.execute session command output-stream))
  ([session command]
   (.execute session command)))

(defn create
  "Creates the database name with an input and opens it. An existing database
   will be overwritten."
  [session name xml-stream]
  (.create session name xml-stream))

(defn add
  "Adds the XML stream specified by [input] to the currently opened database at
   the specified [path]. A document with the same path may occur than once in a
   database. If this is unwanted, replace can be used."
  [session path xml-stream]
  (.add session path xml-stream))

(defn replace
  "Replaces a document in the currently opened database, addressed by [path],
   with the XML stream specified by [xml-stream], or adds a new document if the
   resource does not exist yet."
  [session path xml-stream]
  (.replace session path xml-stream))

(defn store
  "Stores a raw file in the opened database to the specified [path]"
  [session path blob-stream]
  (.store session path blob-stream))

(defn info
  "Shows global information."
  [session]
  (.info session))

(defn close
  "Closes the currently open session."
  [session]
  (.close session))
