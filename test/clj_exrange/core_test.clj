(ns clj-exrange.core-test
  (:require [clojure.test :refer [deftest testing is are]]
            [clj-exrange.core :refer [exrange]]))

(deftest exrange-test
  (testing "Basic tests"
    (are [result pattern]
         (= result (exrange pattern))
      [""]                       ""
      [""]                       nil
      ["abc"]                    "abc"
      ["a1" "b2" "c3"]           "{a1,b2,c3}"
      ["a1,b2,c3"]               "a1,b2,c3"
      ["9" "10" "11"]            "[9-11]"
      ["09"]                     "[09-09]"
      ["09" "10" "11"]           "[09-11]"
      ["11" "10" "09"]           "[11-09]"
      ["9-11"]                   "9-11"
      ["09" "10" "15" "19" "20"] "[09-10,15,19-20]"
      ["a1" "a2" "b3" "b4"]      "{a{1,2},b{3,4}}"
      ["a1" "a2" "b3" "b4"]      "{a[1-2],b[3-4]}"
      ["axb" "ayb"]              "a{x,y}b"
      ["a1b" "a2b" "a3b"]        "a[1-3]b"))
  (testing "Error tests"
    (are [pattern]
         (instance? Exception (try
                                (exrange pattern)
                                (catch Exception e e)))
      "{a"
      "[1"
      "[a]"
      "[10-[11-12]]"
      "[10-a]"
      "{a{b}"
      "{a[1}"))
  (testing "test a exrange response"
    (is (= ["server-master.example.com"
            "server-master.example.jp"
            "server-01.example.com"
            "server-01.example.jp"
            "server-02.example.com"
            "server-02.example.jp"]
           (exrange "server-{master,[01-02]}.example.{com,jp}")))))
