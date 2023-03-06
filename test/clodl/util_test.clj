(ns clodl.util-test (:require [clojure.test :refer :all]
                     [clodl.util :as util]))

(deftest repeat-str-test
  (testing "Testing repeat-str with valid args"
    (is (= "testtest" (util/repeat-str "test" 2)))
    (is (= "strstrstrstr" (util/repeat-str "str" 4))))
  (testing "Testing repeat-str with invalid args"))
    ;; (is (= "FIXME" (util/repeat-str 1 "test")))
    ;; (is (= "FIXME" (util/repeat-str 1 1)))
    ;; (is (= "FIXME" (util/repeat-str "test" "test")))))

(deftest spaces-test
  (testing "Testing spaces with valid args"
    (is (= "  " (util/spaces 2)))
    (is (= "" (util/spaces 0)))))

(deftest colorize-string-test
  (testing "Testing colorize-string with valid args"
    (is (= (str "\u001B[32mtest\u001B[0m") (util/colorize-string "test" "GREEN")))
    (is (= (str "\033[1;37mtest\u001B[0m") (util/colorize-string "test" "GRAY_BOLD")))
    (is (= (str "test") (util/colorize-string "test")))))

(deftest get-printed-len-test
  (testing "Testing get-printed-len with valid args"
    (is (= 7 (util/get-printed-len "test")))
    (is (= 13 (util/get-printed-len "glasses")))))