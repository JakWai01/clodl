(ns clodl.core
  (:require [clojure.string :as str]))

; Print words in caps
; Make sure that only words in the wordlist are accepted. This makes the game significantly harder. Do this as a flag
; Don't print the char as yellow if the char is already green. Also, if there is a double-letter in your guess,
; if it's actually contained once, just highlight it once.

(def keyboard-keys [{:q nil :w nil :e nil :r nil :t nil :y nil :u nil :i nil :o nil :p nil}
                    {:a nil :s nil :d nil :f nil :g nil :h nil :j nil :k nil :l nil}
                    {:z nil :x nil :c nil :v nil :b nil :n nil :m nil}])

; Try to clarify the error handling here
(defn get-term-columns
  "Get the width of the terminal"
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
  [s len]
  (let [slen (count s)
        lpad (int (/ (- len slen) 2))
        rpad (- len slen lpad)]
    (str (spaces lpad) s (spaces rpad))))

(defn print-centered
  "Print string to the center of the terminal"
  [string]
  (let [columns (get-term-columns)]
    (println (center string columns))))

(defn print-keyboard
  "Print a keyboard representing the already used characters"
  [keyboard-keys]
  (do (println "Test")
      (for [[[k v] & _] [{:a 1} {:b 2}]]
        (println "Found key" k "with value" v))))

(defn get-word-list-from-file
  "Reads the wordlist from the file"
  [path]
  (str/split (slurp path) #"\n"))

(defn valid?
  "Check that the guess is made out of valid chars and is exactly five characters long"
  [guess]
  (and
   (= (count guess) 5)
   (every? #(Character/isLetter %) guess)))

(defn colorize-string
  "Print the following text in the specified color"
  [text & [color]]
  (case color
    "GREEN" (str "\u001B[32m" text "\u001B[0m")
    "YELLOW" (str "\u001B[33m" text "\u001B[0m")
    (str text)))

(defn get-letter-matches
  "Charwise comparison and colorization"
  [guess target]
  (map
   #(cond
      (= %1 %2) (colorize-string %1 "GREEN")
      (not (nil? (str/index-of target %1))) (colorize-string %1 "YELLOW")
      :else (colorize-string %1)) guess target))

(defn evaluate-guess
  "Mark each char as either contained, contained and in the correct place or uncontained"
  [guess target]
  [(= guess target)
   (reduce str (get-letter-matches guess target))])

(defn print-past-guesses
  "Prints all the past guesses in order to be able to be able to infer the target word"
  [past-guesses]
  ; Normally, transform functions return a lazy sequence which can't produce side-effects.
  ; If the purpose of the function is to have side-effects, we need to eagerly evaluate the transformation.
  ; dorun -> returns nil (enough for us), doall -> returns the collection

  ; Use map-indexed here
  (dorun (map #(println (format "%s [%d/6]" %1 %2)) past-guesses (iterate inc 1))))

; Pass keyboard dict as well
(defn start-round
  "Take one guess and evaluate it's correctness"
  [target, past-guesses, round, won]
  (if (and (< round 6) (not won))
    (do
      (when (not (= 0 (count past-guesses)))
        (print-past-guesses past-guesses))
      (print-keyboard keyboard-keys)
      (print "Please enter a word: ")
      (flush)
      (let [guess (clojure.string/lower-case (read-line))]
        (if (valid? guess)
          (let [[won colorized-guess] (evaluate-guess guess target)]
            (start-round target (conj past-guesses colorized-guess) (inc round) won))
          (do (println "Invalid word, please try again. The word should have consist out of 5 letters.")
              (start-round target past-guesses round won)))))
    (if (boolean won)
      (println "Congratulations! You guessed the word!")
      (println (format "Game over! You were not able to guess the word '%s'", target)))))

(defn get-random-word
  "Get a random word from the wordlist"
  [word-list]
  (rand-nth word-list))

(defn start-game
  "Play a game of wordle. A game consists out of 6 rounds."
  [path]
  (let [word-list (get-word-list-from-file path)
        target (get-random-word word-list)]
    ; Remove the target word later
    ;; (println target)
    (println (dec (inc (get-term-columns))))
    (print-centered "Test")
    ; This snippet needs to work
    (for [[[k v] & _] [{:a 1} {:b 2}]]
      (println "Found key" k "with value" v))
    (start-round target [] 0 false)))

(defn -main
  "I don't do a whole lot."
  [& args]
  (case (first args)
    "play" (-> (last args)
               start-game)
    "help" (println (colorize-string "Usage:\nplay wordlist\nhelp" "YELLOW"))
    (println (colorize-string "Invalid commands, use help to display information." "YELLOW"))))