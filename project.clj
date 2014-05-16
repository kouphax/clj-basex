(defproject basex "0.0.1"
  :description "A Clojure-ified wrapper around the BaseX Java Client"
  :url "https://github.com/kouphax/basex-clojure-client"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :java-source-paths  ["src/main/java"]
  :source-paths ["src/main/clojure"]

  :dependencies [[org.clojure/clojure "1.5.1"] ]

  :profiles { :dev { :repositories { "BaseX Maven Repository" "http://files.basex.org/maven" }
                     :plugins [[lein-midje "3.0.0"]]
                     :dependencies [[org.basex/basex "7.8.2"]
                                    [midje "1.5.1"]
                                    [me.raynes/fs "1.4.4"]]}})

