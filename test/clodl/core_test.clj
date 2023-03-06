(ns clodl.core-test
  (:require [clojure.test :refer :all]
            [clodl.core :as core]))

(deftest get-letter-matches-test
  (testing "Test get-letter-matches with valid args"
    (is (= [(str "\033[1;37mH\u001B[0m") (str "\033[1;32mO\u001B[0m") (str "\033[1;32mU\u001B[0m") (str "\033[1;32mS\u001B[0m") (str "\033[1;32mE\u001B[0m")] (core/get-letter-matches "house" "mouse")))
    (is (= [(str "\033[1;37mH\u001B[0m") (str "\033[1;33mO\u001B[0m") (str "\033[1;37mU\u001B[0m") (str "\033[1;37mS\u001B[0m") (str "\033[1;37mE\u001B[0m")] (core/get-letter-matches "house" "clown")))))

(deftest evaluate-guess-test
  (testing "Test evaluate-guess with valid args"
    (is (= [false "\033[1;37mH\u001B[0m\033[1;32mO\u001B[0m\033[1;32mU\u001B[0m\033[1;32mS\u001B[0m\033[1;32mE\u001B[0m"] (core/evaluate-guess "house" "mouse")))
    (is (= [true "\033[1;32mH\u001B[0m\033[1;32mO\u001B[0m\033[1;32mU\u001B[0m\033[1;32mS\u001B[0m\033[1;32mE\u001B[0m"] (core/evaluate-guess "house" "house")))))