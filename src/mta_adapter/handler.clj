(ns mta-adapter.handler
  (:require [compojure
             [core :refer :all]
             [route :as route]]
            [mta-adapter.mta-client :as mta]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [cheshire.core :as json]))

(defroutes app-routes
  (GET "/stops" [lat lon]
    {:status 200
     :body (json/generate-string (mta/stops-near-location {:lat (Float. lat)
                                                           :lon (Float. lon)}))})
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
