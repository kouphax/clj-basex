(defproject basex "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :java-source-paths  ["src/main/java"]
  :source-paths ["src/main/clojure"]

  :dependencies [[org.clojure/clojure "1.5.1"] ]

  :profiles { :dev { :repositories { "BaseX Maven Repository" "http://files.basex.org/maven" }
                     :dependencies [[org.basex/basex "7.8.2"]
                                    [midje "1.5.1"]
                                    [me.raynes/fs "1.4.4"]]}})

