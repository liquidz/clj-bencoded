(ns bencoded.bencode
  (:require [clojure.string :as str]))

(declare decode*)

(defn- decode-string [s]
  (if-let [i (str/index-of s ":")]
    (let [len (Long/parseLong (subs s 0 i))
          end-index (+ i len 1)]
      {:value (subs s (inc i) end-index) :rest (subs s end-index)})
    (throw (ex-info "failed to decode string token" {:s s}))))

(defn- decode-number [s]
  (if-let [i (str/index-of s "e")]
    {:value (Long/parseLong (subs s 1 i)) :rest (subs s (inc i))}
    (throw (ex-info "failed to decode number token" {:s s}))))

(defn- decode-list [s]
  (loop [s (subs s 1) res []]
    (if (str/blank? s)
      (throw (ex-info "failed to decode list" {:s s}))
      (let [{:keys [value rest]} (decode* s)]
        (if (= \e (first rest))
          {:value (conj res value) :rest (subs rest 1)}
          (recur rest (conj res value)))))))

(defn- decode-dict [s]
  (loop [s (subs s 1) res {}]
    (if (str/blank? s)
      (throw (ex-info "failed to decode dict" {:s s}))
      (let [{k :value s :rest} (decode* s)
            {v :value s :rest} (decode* s)]
        (if (= \e (first s))
          {:value (assoc res k v) :rest (subs s 1)}
          (recur rest (assoc res k v)))))))

(defn- decode* [s]
  (try
    (case (first s)
      \i (decode-number s)
      \l (decode-list s)
      \d (decode-dict s)
      (\0 \1 \2 \3 \4 \5 \6 \7 \8 \9) (decode-string s)
      (throw (ex-info "Invalid token" {:s s})))
    (catch Exception ex
      {:value "BENCODE_DECODING_ERROR"})))

(defn decode [s]
  (loop [s s res []]
    (let [{v :value s :rest} (decode* s)]
      (if (str/blank? s)
        (let [res (conj res v)]
          (cond-> res (= 1 (count res)) first))
        (recur s (conj res v))))))
