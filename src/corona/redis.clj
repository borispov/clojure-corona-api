(ns corona.redis
  (:gen-class)
  (:require
    [taoensso.carmine :as car :refer (wcar)]
    [clojure.data.json :as json]))

; json config with redis keys
;; (def jsonconfig (json/read-str (slurp "./config.json") :key-fn keyword))

(def jsonconfig {
                 :worldToday "covidapi:worldToday",
                 :countryToday "covidapi:countryToday:",
                 :countryYesterday "covidapi:countryYesterday:",
                 :countries "covidapi:countries",
                 :resources "covidapi:resources",
                 :newsHeb "covidapi:newsHeb",
                 :worldYesterday "covidapi:worldYesterday",
                 :countriesList "covidapi:countriesList",
                 :historical "covidapi:historical:",
                 :israelTime "covidapi:historical:israel",
                 :worldTime "covidapi:historical:world"})

; Redis connection
(def server1-conn {:pool {} :spec {:uri "redis://127.0.0.1:6379/"}})
(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

; REDIS utilities/functions for ease of use
(defn car-set [k v] (wcar* (car/set k v)))
(defn car-get [k] (wcar* (car/get k)))
(defn get-redis-key [k1] (get jsonconfig (keyword k1)))
