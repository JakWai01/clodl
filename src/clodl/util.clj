(ns clodl.util (:require [clojure.string :as string]
                         [clodl.util :as util]))

(defn get-term-columns
  "Get the width of the terminal by reading the `COLUMNS` environment variable."
  []
  (let [columns (System/getenv "COLUMNS")]
    (if columns
      (try
        (Integer/parseInt columns)
        (catch NumberFormatException _ "Failed to parse COLUMNS shell variable"))
      (throw (Exception. "In order to enjoy clodl at it's full potential, make sure to set the COLUMNS shell variable!")))))

(defn repeat-str
  "Create a string that repeats s n times."
  [s n]
  (apply str
         (repeat n s)))

(defn spaces
  "Create a string of n spaces."
  [n]
  (repeat-str \space n))

(defn center
  "Center s in padding to final size len"
  [s & [custom-len]]
  (let [len (try
              (get-term-columns)
              (catch Exception e
                (println (str "Error: " (.toString e)))
                ; Terminate program since the output can't be game can't be displayed properly
                (System/exit 1)))
        slen (if (not (nil? custom-len))
               (int custom-len)
               (count s))
        lpad (int (/ (- len slen) 2))
        rpad (- len slen lpad)]
    (str (spaces lpad) s (spaces rpad))))

(defn print-centered
  "Print string to the center of the terminal"
  [string & [custom-len]]
  (if (not (nil? custom-len))
    (println (center string custom-len))
    (println (center string))))

(defn colorize-string
  "Print the given string in the given color"
  [string & [color]]
  (let [cap-str (string/upper-case string)]
    (case color
      "GREEN" (str "\u001B[32m" cap-str "\u001B[0m")
      "YELLOW" (str "\u001B[33m" cap-str "\u001B[0m")
      "GRAY" (str "\u001B[90m" cap-str "\u001B[0m")
      "GREEN_BOLD" (str "\033[1;32m" cap-str "\u001B[0m")
      "YELLOW_BOLD" (str "\033[1;33m" cap-str "\u001B[0m")
      "GRAY_BOLD" (str "\033[1;37m" cap-str "\u001B[0m")
      (str cap-str))))

(defn clear-screen
  "Clears screen and moves the cursor to the top left"
  []
  (print (str (char 27) "[2J")) ; clear screen
  (print (str (char 27) "[;H"))) ; move cursor to the top left corner of the screen

(defn get-random-word
  "Get a random word from the wordlist"
  [word-list]
  (rand-nth word-list))

(defn get-printed-len
  "Get printed length of a string also accounting for the spaces used"
  [vector]
  (+ (count vector) (dec (count vector))))

; Debugging thread-last
(def spy #(do (println "DEBUG:" %) %))
