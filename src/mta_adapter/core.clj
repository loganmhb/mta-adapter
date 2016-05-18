(ns mta-adapter.core
  (:require [environ.core :refer [env]]
            [mta-adapter.handler :refer [app]]
            [ring.adapter.jetty :as jetty]))

(defn -main [& [port]]
  (jetty/run-jetty app {:port (Integer. (or port
                                            (env :port)
                                            5000))}))
