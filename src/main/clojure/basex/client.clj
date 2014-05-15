(ns basex.client
  (:refer-clojure :exclude [replace])
  (:import (basex.core BaseXClient)))

(def ^:private default-db-spec
  { :host     "localhost"
    :port     1984
    :username "admin"
    :password "admin" })


(defn create-session
  ([]
   (create-session {}))
  ([db-spec]
   (let [spec (merge default-db-spec db-spec)]
     (BaseXClient. (:host spec)
                   (:port spec)
                   (:username spec)
                   (:password spec)))))

(defn execute
  ([session command output-stream]
   (.execute session command output-stream))
  ([session command]
   (.execute session command)))

(defn create [session name xml-stream]
  (.create session name xml-stream))

(defn add [session path xml-stream]
  (.add session path xml-stream))

(defn replace [session path xml-stream]
  (.replace session path xml-stream))

(defn store [session path blob-stream]
  (.store session path blob-stream))

(defn info [session]
  (.info session))

(defn close [session]
  (.close session))
