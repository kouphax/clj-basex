(defproject basex "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :java-source-paths  ["src/main/java"]
  :source-paths ["src/main/clojure"]

  :main ^:skip-aot basex.core

  :repositories { "BaseX Maven Repository" "http://files.basex.org/maven"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.basex/basex "7.8.2"]])
