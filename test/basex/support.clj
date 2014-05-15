(ns basex.support
  (:require [me.raynes.fs :refer [delete-dir temp-dir]])
  (:import (org.basex BaseXServer)))


(defmacro with-test-server [& code]
  `(let [data-path# (.getAbsolutePath (temp-dir "basex"))]
     (System/setProperty "org.basex.DBPATH" data-path#)
     (let [server# (BaseXServer. (into-array String []))]
       (try
         ~@code
         (finally
           (.stop server#)
           (delete-dir data-path#))))))
