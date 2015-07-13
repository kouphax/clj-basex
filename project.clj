(defproject basex "1.0.0"
  :description "A Clojure-ified wrapper around the BaseX Java Client"
  :url "https://github.com/kouphax/basex-clojure-client"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :java-source-paths  ["src/main/java"]
  :source-paths ["src/main/clojure"]

  :dependencies [[org.clojure/clojure "1.6.0"] ]

  :scm { :name "git"
         :url  "https://github.com/kouphax/clj-basex" }

  :profiles { :dev { :repositories { "BaseX Maven Repository" "http://files.basex.org/maven" }
                     :plugins [[lein-midje "3.1.3"]]
                     :dependencies [[org.basex/basex "8.2.2"]
                                    [midje "1.7.0"]
                                    [me.raynes/fs "1.4.6"]]}})

