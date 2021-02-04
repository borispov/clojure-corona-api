(ns corona.controllers
  (:gen-class)
  (:require
    [taoensso.carmine :as car :refer (wcar)]
    [clojure.data.json :as json]
    [clojure.string :as str]
    [compojure.core :refer :all]
    [compojure.route :as route]
    [corona.utils :as utils]))
(use 'corona.redis)


(require '[clojure.core.match :refer [match]])

; Source API URL
(def covid-api-url-old "https://corona.lmao.ninja/v3/covid-19/")
(def covid-api-url "https://disease.sh/v3/covid-19/")

; GET today/yester
(defn retrieve-daily-external [c, y-day]
  (match [c]
         ["world"] (utils/json-from-api (str covid-api-url "all"          (when y-day "?yesterday=true" )))
         :else     (utils/json-from-api (str covid-api-url "countries/" c (when y-day "?yesterday=true" )))))

; Save Today/Yesterday Stats For: Country/World
(defn save-country-stats-to-redis [country, yday] 
  (let [redis-key (utils/get-redis-key (if yday "countryYesterday" "countryToday" ))]
    (car-set 
      (str redis-key country) 
      (json/write-str (retrieve-daily-external country yday)))))

(defn get-country-stats
  ([country]
     (get-country-stats country nil))
  ([country yday]
    (let [redis-key (utils/get-redis-key (if yday "countryYesterday" "countryToday" ))]
      (or 
        (car-get (str redis-key country))
        (save-country-stats-to-redis country yday)))))

(defn save-countries []
  (try
    (car-set "covidapi:countryList" (utils/get-countries-list))
    (catch Exception e (prn "Handling Generic Error Saving Countries List"))))

(defn get-countries [req] (or (car-get "covidapi:countryList") (save-countries)))


; retrieve historical data for country
(defn get-historical-data
  ([] (get-historical-data "world"))
  ([country] (let [c (str/capitalize country)]
     (or
        (car-get (str (get-redis-key "historical") c))
        (utils/filter-location c (car-get "covidapi:historical"))))))

(defn save-all-historical [] (car-set "covidapi:historical" (utils/get-historical-data-all)))

(defn save-historical-israel []
  (car-set
      (str (get-redis-key "israelTime"))
      (utils/filter-location "Israel" (car-get "covidapi:historical"))))

(defn save-historical-world []
  (car-set 
      (str (get-redis-key "worldTime"))
      (utils/filter-location "World" (car-get "covidapi:historical"))))

(defn save-historical-country [cn]
  (car-set
           (str (get-redis-key "worldTime") (str/capitalize cn))
           (utils/filter-location (str/capitalize cn) (car-get "covidapi:historical"))))
