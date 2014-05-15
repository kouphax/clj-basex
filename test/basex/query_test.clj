(ns basex.query-test
  (:require [midje.sweet :refer :all]
            [basex.client :as client]
            [basex.query :as query]
            [basex.support :refer [with-test-server]]))

(with-test-server

  (defn query-results [query]
    (loop [more?   (query/more? query)
           results []]
      (if-not more?
        results
        (let [memo (conj results (query/next query))]
          (recur (query/more? query) memo)))))

  (fact "We can build and execute queries"
    (let [session (client/create-session)
          input   "for $i in 1 to 10 return <xml>Text { $i }</xml>"
          query   (query/create-query session input)]
      (query-results query) => (just ["<xml>Text 1</xml>"
                                      "<xml>Text 2</xml>"
                                      "<xml>Text 3</xml>"
                                      "<xml>Text 4</xml>"
                                      "<xml>Text 5</xml>"
                                      "<xml>Text 6</xml>"
                                      "<xml>Text 7</xml>"
                                      "<xml>Text 8</xml>"
                                      "<xml>Text 9</xml>"
                                      "<xml>Text 10</xml>"])
      (query/info query) => (contains #"Query executed in")
      (query/close query)))

  (fact "We can bind external variables to queries"
    (let [session (client/create-session)
          input "declare variable $name external;
               for $i in 1 to 10 return element { $name } { $i }"
          query (query/create-query session input)]
      (query/bind query "$name" "number")
      (query-results query) => (just ["<number>1</number>"
                                      "<number>2</number>"
                                      "<number>3</number>"
                                      "<number>4</number>"
                                      "<number>5</number>"
                                      "<number>6</number>"
                                      "<number>7</number>"
                                      "<number>8</number>"
                                      "<number>9</number>"
                                      "<number>10</number>"]))))
