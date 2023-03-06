(ns clodl.keyboard-test (:require [clojure.test :refer :all]
                                  [clodl.keyboard :as keyboard]))

(deftest create-guess-color-map-test
  (testing "Test create-guess-color-map-test with valid args"
    (is (= {:H "GREEN_BOLD", :E "GRAY_BOLD", :L "GREEN_BOLD", :O "GREEN_BOLD"} (keyboard/create-guess-color-map "hello" "hallo")))
    (is (= {:H "GRAY_BOLD", :E "YELLOW_BOLD", :L "GRAY_BOLD", :O "YELLOW_BOLD"} (keyboard/create-guess-color-map "hello" "mouse")))))

(deftest update-keyboard-key-colors
  (testing "Test update-keyboard-key colors with valid args"
    (is (= {:H "GRAY_BOLD", :E "YELLOW_BOLD", :L "GRAY_BOLD", :O "YELLOW_BOLD" :K nil} (keyboard/update-keyboard-key-colors {:H nil, :E nil, :L nil, :O nil :K nil} "hello" "mouse")))))