(defproject cheffy "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [ring "1.9.6"]
                 [integrant "0.8.0"]
                 [environ "1.2.0"]
                 [metosin/reitit-ring "0.5.18"]
                 [seancorfield/next.jdbc "1.2.659"]
                 [postgresql "9.3-1102.jdbc41"]
                 [clj-http "3.10.0"]
                 [ovotech/ring-jwt "1.3.0"]
                 [metosin/reitit-swagger "0.5.18"]
                 [metosin/reitit-swagger-ui "0.5.18"]
                 [metosin/reitit "0.5.18"]]
  :main ^:skip-aot cheffy.server
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}
             :dev {:source-paths ["dev/src"]
                   :resource-paths ["dev/resources"]
                   :dependencies [[ring/ring-mock "0.4.0"]
                                  [integrant/repl "0.3.2"]]}}
  :uberjar-name "cheffy.jar")
