(defproject holston "0.1.0-SNAPSHOT"
  :description "Beer rating system"
  :min-lein-version "2.0.0"
  :url "http://holston.herokuapp.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [http-kit "2.1.16"]
                 [javax.servlet/servlet-api "2.5"]
                 [compojure "1.1.8"]
                 [org.clojure/java.jdbc "0.3.4"]
                 [postgresql "9.1-901.jdbc4"]]
;; uberjar doesn't seem to work now...
  :uberjar-name "holston-standalone.jar"
  :main ^:skip-aot holston.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
