(ns clodl.keyboard (:require [clodl.util :as util]
                             [clojure.string :as str]))

(def ^:private ^:const keyboard-keys [[\Q \W \E \R \T \Y \U \I \O \P]
                    [\A \S \D \F \G \H \J \K \L]
                    [\Z \X \C \V \B \N \M]])

(defn create-guess-color-map
  "Create map containing updated values for one guess"
  [guess target]
  (let [guess (str/upper-case guess)
        target (str/upper-case target)]
    (reduce (fn [result [guess-char target-char]]
              (assoc result (keyword (str guess-char))
                     (cond
                       (= guess-char target-char) (str "GREEN_BOLD")
                       (not (nil? (str/index-of (str/upper-case target) guess-char))) (str "YELLOW_BOLD")
                       :else (str "GRAY_BOLD"))))
            {}
            (map vector guess target))))

(defn print-keyboard
  "Todo"
  [colors]
  (doseq [key keyboard-keys]
    (as-> key x
      (map #(util/colorize-string % (get colors (keyword (str %)))) x)
      (clojure.string/join " " x)
      (util/print-centered x (util/get-printed-len key)))))

(defn update-keyboard-key-colors
  "Update the colors of the keyboard keys"
  [colors guess target]
  (merge colors
         (create-guess-color-map guess target)))
