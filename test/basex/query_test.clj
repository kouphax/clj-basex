(ns basex.query-test
  (:require [midje.sweet :refer :all]
            [basex.client :as client]
            [basex.query :as query]
            [basex.support :refer [with-test-server]]))

(with-test-server)
