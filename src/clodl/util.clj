(ns clodl.util (:require [clojure.string :as string]))

; Try to clarify error handling here
(defn get-term-columns
  "Get the width of the terminal by reading the `COLUMNS` environment variable."
  []
  (let [columns (System/getenv "COLUMNS")]
    (if columns
      (try
        (Integer/parseInt columns)
        (catch NumberFormatException _ nil)) nil)))

(defn repeat-str
  "Create a string that repeats s n times."
  [s n]
  (apply str (repeat n s)))

(defn spaces
  "Create a string of n spaces."
  [n]
  (repeat-str \space n))

(defn center
  "Center s in padding to final size len"
  [s len & [custom-len]]
  (let [slen (if (not (nil? custom-len))
               (int custom-len)
               (count s))
        lpad (int (/ (- len slen) 2))
        rpad (- len slen lpad)]
    (str (spaces lpad) s (spaces rpad))))

(defn print-centered
  "Print string to the center of the terminal"
  [string & [custom-len]]
  (let [columns (get-term-columns)]
    (if (not (nil? custom-len))
      (println (center string columns custom-len))
      (println (center string columns)))))

; Kind of useless
(defn upper
  "Capitalize the given string"
  [string]
  (string/upper-case string))

; Kind of useless
(defn lower
  "Lowercase the given string"
  [string]
  (string/lower-case string))
