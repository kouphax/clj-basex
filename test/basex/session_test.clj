(ns basex.session-test
  (:require [midje.sweet   :refer :all]
            [basex.support :refer [with-test-server]]
            [me.raynes.fs  :refer [delete-dir temp-dir]]
            [basex.session :as basex]
            [basex.query   :as query]))

(with-test-server

    (fact "We can create a session with or without a spec"
      (basex/create-session)                => truthy
      (basex/create-session {})             => truthy
      (basex/create-session { :port 1984 }) => truthy)

    (fact "Exceptions be thrown when config isn't right when creating a session"
      (basex/create-session { :password "invalid" }) => (throws Exception)
      (basex/create-session { :username "invalid" }) => (throws Exception)
      (basex/create-session { :port 1990 })          => (throws Exception))

    (fact "We can execute arbitrary xquery commands"
      (basex/with-session [session (basex/create-session)]
        (basex/execute session "xquery 1 to 10") => (exactly "1 2 3 4 5 6 7 8 9 10")
        (basex/execute session "create db database")
        (basex/info session) => (contains #"Database 'database' created in")
        (basex/execute session "drop db database")
        (basex/info session) => (contains #"Database 'database' was dropped")))

    (fact "We can add and query documents"
      (basex/with-session [session (basex/create-session)
                            doc-1   (java.io.ByteArrayInputStream. (.getBytes "<x>Hello World 1!</x>"))
                            doc-2   (java.io.ByteArrayInputStream. (.getBytes "<x>Hello World 2!</x>"))
                            doc-3   (java.io.ByteArrayInputStream. (.getBytes "<x>Hello World 3!</x>"))]
        (basex/execute session "create db database")
        (basex/add session "doc-1" doc-1)
        (basex/info session) => (contains #"Path \"doc-1\" added in")
        (basex/add session "doc-2" doc-2)
        (basex/info session) => (contains #"Path \"doc-2\" added in")
        (basex/execute session "xquery collection('database')") => (exactly "<x>Hello World 1!</x><x>Hello World 2!</x>")
        (basex/replace session "doc-2" doc-3)
        (basex/execute session "xquery collection('database')") => (exactly "<x>Hello World 1!</x><x>Hello World 3!</x>")
        (basex/execute session "drop db database")))

    (fact "We can store binary files"
      (basex/with-session [session  (basex/create-session)
                            data    (byte-array (map byte (range 128)))
                            doc-in  (java.io.ByteArrayInputStream. data)
                            doc-out (java.io.ByteArrayOutputStream.)]
        (basex/execute session "create db database")
        (basex/store session "test.bin" doc-in)
        (basex/info session) => (contains #"Query executed in")
        (basex/execute session "retrieve test.bin" doc-out)
        (java.util.Arrays/equals data (.toByteArray doc-out)) => true
        (basex/execute session "drop db database")))

   (fact "We can trigger and receive notifications"
    (let [session-1 (basex/create-session)
          session-2 (basex/create-session)]
      (basex/execute session-1 "create event onmessage")
      (basex/watch session-2 "onmessage"
                    (fn [value] value => (exactly "Hello World!")))
      (-> (query/create-query session-2 "for $i in 1 to 1000000 where $i = 0 return $i")
          (query/execute))
      (-> (query/create-query session-1 "db:event('onmessage', 'Hello World!')")
          (query/execute))
      (basex/unwatch session-2 "onmessage")
      (basex/execute session-1 "drop event onmessage"))))
