(defproject holston "0.1.0-SNAPSHOT"
  :description "Beer rating system"
  :min-lein-version "2.0.0"
  :url "http://holston.herokuapp.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.apache.httpcomponents/httpclient "4.3.3"]
                 [http-kit "2.1.16"]
                 [javax.servlet/servlet-api "2.5"]
                 [compojure "1.1.8"]
                 [org.clojure/java.jdbc "0.3.4"]
                 [postgresql "9.1-901.jdbc4"]
                 [clojure-csv/clojure-csv "2.0.1"]  ;;https://github.com/davidsantiago/clojure-csv
                 [com.cemerick/friend "0.2.0" :exclusions [ring/ring-core]]
                 [friend-oauth2 "0.1.1" :exclusions [org.apache.httpcomponents/httpcore]]
                 [cheshire "5.2.0"]
                 [ring-server "0.3.0" :exclusions [ring]]
                 [clj-http "0.9.2"]]
;; uberjar doesn't seem to work now...
  :uberjar-name "holston-standalone.jar"
  :main ^:skip-aot holston.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
