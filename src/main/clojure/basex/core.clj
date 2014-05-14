(ns basex.core
  (:require [basex.client :as client])
  (:import (org.basex BaseXServer))
  (:gen-class))

(defn -main [& args]
  (let [server  (BaseXServer. (into-array String []))
        session (client/create-session)]
    (try
      (simple-example session)
      (finally
        (client/close session)
        (.stop server)))))

(defn simple-example [session]
  (client/execute session "xquery 1 to 10"))

(defn add-example [session]
  (client/execute session "create db database"))

;(-main)
