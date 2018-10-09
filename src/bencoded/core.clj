(ns bencoded.core
  (:require [bencoded.bencode :as bc]
            [clojure.data.json :as json])
  (:gen-class))

(defn response [x]
  (println (json/write-str x)))

(defn -main []
  (response "BENCODED_READY")
  (loop [s (read-line)]
    (when (not= s "quit")
      (response (bc/decode s))
      (recur (read-line)))))
