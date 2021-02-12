(ns corona.core
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [clojure.pprint :as pp]
            [clojure.string :as str]
            [clojure.data.json :as json])
  (:gen-class))

(require '[immutant.scheduling :refer :all])
(require '[corona.routes :as routes])
(require '[corona.utils :as utils])
(require '[corona.controllers :as c])


(defn isra-world
  []
  (println "Beginning to Fetch Israel & World's Today and Yesterday statistics!")
  (c/save-country-stats-to-redis "israel" false)
  (c/save-country-stats-to-redis "israel" true)
  (c/save-country-stats-to-redis "world" false)
  (c/save-country-stats-to-redis "world" true))

(defn job-all-history []
  (println "Fetching ALL Historical Entries")
  (c/save-all-historical))


(defn -main
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8000"))]
    (schedule isra-world :every [20 :minutes])
    (schedule job-all-history :in [1 :minutes] :every :day)
    (server/run-server (wrap-defaults #'routes/app-routes site-defaults) {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))

