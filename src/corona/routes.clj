(ns corona.routes
  (:gen-class)
  (:require 
    [compojure.core :refer :all]
    [compojure.route :as route]
    [corona.controllers :as c]
    [clojure.data.json :as json]))

(defroutes app-routes
  (GET "/api/v1/today/:country" [country] (c/get-country-stats country))
  (GET "/api/v1/alltime" [] c/get-historical-data)
  (GET "/api/v1/alltime/:country" [country] (c/get-historical-data country))
  (GET "/api/v1/countries" [] c/get-countries))
