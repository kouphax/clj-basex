(ns basex.notifications-test
  (:require [midje.sweet :refer :all]
            [basex.client :as client]
            [basex.notifications :as notify]
            [basex.query :as query]
            [basex.support :refer [with-test-server]]))

(with-test-server
  (fact "We can trigger and receive notifications"
    (let [session-1 (client/create-session)
          session-2 (client/create-session)]
      (client/execute session-1 "create event onmessage")
      (notify/watch session-2 "onmessage"
                    (fn [value] value => (exactly "Hello World!")))
      (-> (query/create-query session-2 "for $i in 1 to 1000000 where $i = 0 return $i")
          (query/execute))
      (-> (query/create-query session-1 "db:event('onmessage', 'Hello World!')")
          (query/execute))
      (notify/unwatch session-2 "onmessage")
      (client/execute session-1 "drop event onmessage"))))
