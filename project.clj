(defproject corona "0.1.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 ; Patter Matchking
                 [org.clojure/core.match "1.0.0"]
                 ; Compojure - basic routing lib
                 [compojure "1.6.2"]
                 ; http kit for client/server
                 [http-kit "2.5.1"]
                 ; Ring defaults - for query params etc
                 [ring/ring-defaults "0.3.2"]
                 ; Clojure data.JSON library
                 [org.clojure/data.json "1.0.0"]
                 ; Immutant schedule
                 [org.immutant/scheduling "2.1.10"]
                 ; Redis driver
                 [com.taoensso/carmine "3.1.0"]
                 ; clj-http
                 [clj-http "3.12.0"]
                 ; CSV Library
                 [ultra-csv "0.2.3"]]
  :repl-options {:init-ns corona.core}
  :main corona.core)
