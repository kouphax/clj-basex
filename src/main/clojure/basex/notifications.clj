(ns basex.notifications
  (:import (basex.core.EventNotifier)))

(defn- build-notifier [notifier-action]
  (reify EventNotifier
    (notify [this value]
      (notifier-action value))))

(defn watch [session event-name notifier-action]
  (let [notifier (build-notifier notifier-action)]
    (.watch session event-name notifier)))

(defn unwatch [session event-name]
  (.unwatch session event-name))
