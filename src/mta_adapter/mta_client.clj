(ns mta-adapter.mta-client
  (:require [environ.core :refer [env]]
            [clj-http.client :as http]
            [cheshire.core :as json]))

(def mta-api-key (env :mta-api-key))

(def mta-siri-url "http://bustime.mta.info/api/siri/stop-monitoring.json")

(def mta-discovery-url "http://bustime.mta.info/api/where/")


(defn get-data-for-stop [stop-id]
  (http/get mta-siri-url {:query-params {:key mta-api-key
                                         :version 2
                                         :MonitoringRef stop-id}}))


(defn stops-near-location [{:keys [lat lon radius] :or {radius 0.005}}]
  (->> (http/get (str mta-discovery-url "stops-for-location.json")
                 {:query-params {:lat lat
                                 :lon lon
                                 :latSpan radius
                                 :lonSpan radius
                                 :key mta-api-key}})
       :body
       json/parse-string
       clojure.walk/keywordize-keys
       :data
       :stops
       (map (fn [stop] (update stop :routes #(map :id %))))
       (map (fn [stop] (select-keys stop #{:id :routes :lat :lon :name :direction})))
       (hash-map :stops)))


(comment (def stops (stops-near-location {:lat 40.748433 :lon -73.985656}))

         (map :id (:stops stops))

         (get-data-for-stop "MTA_400323")

         (ffirst (map #(get % "MonitoredStopVisit")
                      (get-in siri
                              ["Siri"
                               "ServiceDelivery"
                               "StopMonitoringDelivery"]))))
