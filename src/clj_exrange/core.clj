(ns clj-exrange.core
  (:gen-class)
  (:require [clojure.string :refer [ends-with? starts-with?]]))

(defn- parse-range
  [s]
  (if-let [[_ d1 _ d2] (re-find #"^(\d+)(-(\d+))?$" s)]
    (if d2
      (let [c1 (count d1)
            c2 (count d2)
            i1 (Integer/parseInt d1)
            i2 (Integer/parseInt d2)
            fmt (if (= c1 c2)
                  (format "%%0%dd" c1)
                  "%d")
            step (if (< i1 i2) 1 -1)]
        (map #(format fmt %)
             (range i1 (+ i2 step) step)))
      (list d1))
    (throw (IllegalArgumentException. s))))

(defn- brackets-stack
  [coll ch]
  (case ch
    \[ (conj coll ch)
    \{ (conj coll ch)
    \] (pop coll)
    \} (pop coll)
    coll))

(defn- parse-comma
  [s]
  (->> (subs s 1 (dec (count s)))
       (reduce (fn [[xs ss st] ch]
                 (let [stack (brackets-stack st ch)]
                   (if (and (= ch \,) (empty? stack))
                     [(conj xs ss) "" stack]
                     [xs (str ss ch) stack])))
               [[] "" []])
       (apply (fn [xs ss _] (conj xs ss)))))

(defn- start-end-with?
  [s start end]
  (and (starts-with? s start) (ends-with? s end)))

(defn- pattern
  [s]
  (cond
    (start-end-with? s "{" "}") (parse-comma s)
    (start-end-with? s "[" "]") (reduce (fn [coll p] (into coll (parse-range p)))
                                        []
                                        (parse-comma s))))

(defn- find-closed-index
  [s]
  (->> (seq s)
       (reduce (fn [coll ch]
                 (let [[i st] coll
                       stack (brackets-stack st ch)]
                   (if (empty? stack)
                     (reduced coll)
                     [(inc i) stack])))
               [0 []])
       (first)))

(declare parse)

(defn- glob
  [s]
  (let [i (inc (find-closed-index s))
        h (subs s 0 i)
        t (subs s i)]
    (map #(parse "" (str % t))
         (pattern h))))

(defn- parse
  [head tail]
  (let [token {:ex head :tokens []}]
    (if (empty? tail)
      token
      (let [[_ h t :as all] (re-find #"(.*?)([\[\{].*)" tail)]
        (cond
          (nil? all) (update token :tokens conj (parse tail ""))
          (seq h) (update token :tokens conj (parse h t))
          :else (update token :tokens into (glob t)))))))

(defn- expand
  [xs s {:keys [ex tokens]}]
  (let [nstr (str s ex)]
    (if (empty? tokens)
      (conj xs nstr)
      (reduce #(expand %1 nstr %2) xs tokens))))

(defn exrange
  [s]
  (expand [] "" (parse "" s)))

(defn -main
  [arg & _]
  (run! println (exrange arg)))
