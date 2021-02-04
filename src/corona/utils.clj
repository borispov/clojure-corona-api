(ns corona.utils
  (:gen-class)
  (:require 
    [clojure.data.json :as json]
    [taoensso.carmine :as car :refer (wcar)]))
(require '[ultra-csv.core :as ultra])

; This is currently the .csv sheet used by *ourworldindata* organization. When it changes I have to change it manually because the hyperlink changes as well
; IF THIS CHANGE OCCURS TOO OFTEN - MIGHT CONSIDER WORKING WITH AN API FOR HISTORICAL DATA
(def jsonconfig (json/read-str (slurp "./config.json") :key-fn keyword))
(def owid-data-url "https://covid.ourworldindata.org/data/owid-covid-data.csv")

(defn csv-to-clj [csv-file] (ultra/read-csv csv-file {:header? true :keywordize-keys? true}))

; get only a list of all countries.
(defn get-countries-list [] (apply sorted-set (set (map :location (csv-to-clj owid-data-url)))))

(defn graph-keys 
  [{:keys [location total_cases new_cases date total_deaths new_deaths iso_code]}]
  {:location location :date date :total_cases total_cases :new_cases new_cases :total_deaths total_deaths :new_deaths new_deaths :iso_code iso_code})

(defn get-historical-data-all
  ([] (map graph-keys (csv-to-clj owid-data-url)))
  ([url] (map graph-keys (csv-to-clj url))))

(defn filter-location [loc dataset] (filter #(= (:location %) loc) dataset))

(def d [{:location "Israel" :cases 5123}{:location "Israel" :cases 1023}{:location "Israel" :cases 92}])
(defn json-from-api [url] (json/read-str (slurp url) :key-fn keyword))
(defn get-redis-key [k1] (get jsonconfig (keyword k1)))
