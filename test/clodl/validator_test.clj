(ns clodl.validator-test (:require [clojure.test :refer :all]
                                   [clodl.validator :as validator]))

(deftest in-wordlist-test?
  (testing "Test in-wordlist? with valid args"
    (is (true? (validator/in-wordlist? "cat" ["dog" "cat" "mouse"])))
    (is (false? (validator/in-wordlist? "cat" ["dog" "mouse"])))))

(deftest valid?-test
  (testing "Test valid with valid arguments"
    (is (= "mouse" (validator/valid? "mouse" ["hello" "table" "mouse"])))
    (is (thrown-with-msg? Exception #"Invalid word." (validator/valid? "elf" ["hello" "table" "mouse"])))))