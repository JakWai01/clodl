(ns clodl.core (:require [clodl.util :as util]
                         [clojure.string :as str]))

; Make sure that only words in the wordlist are accepted. This makes the game significantly harder. Do this as a flag
; Don't print the char as yellow if the char is already green. Also, if there is a double-letter in your guess (only in the past guesses)
; if it's actually contained once, just highlight it once.
; Write a macro that reads in a chunk of the wordlist at compiletime -> That requires the easy mode (just a chunk + any word can be guessed)
; Make bold

(def keyboard-keys [[\Q \W \E \R \T \Y \U \I \O \P]
                    [\A \S \D \F \G \H \J \K \L]
                    [\Z \X \C \V \B \N \M]])

(def keyboard-key-colors {:A nil :B nil :C nil :D nil :E nil :F nil :G nil :H nil :I nil :J nil :K nil :L nil :M nil
                          :N nil :O nil :P nil :Q nil :R nil :S nil :T nil :U nil :V nil :W nil :X nil :Y nil :Z nil})

(defn in-wordlist?
  "Checks if the word is part of the wordlist"
  []
  ())

(defn colorize-string
  "Print the given string in the given color"
  [string & [color]]
  (case color
    "GREEN" (str "\u001B[32m" (util/upper string) "\u001B[0m")
    "YELLOW" (str "\u001B[33m" (util/upper string) "\u001B[0m")
    "GRAY" (str "\u001B[90m" (util/upper string) "\u001B[0m")
    (str (util/upper string))))

(defn get-letter-matches
  "Charwise comparison and colorization"
  [guess target]
  (map #(cond
          (= %1 %2) (colorize-string %1 "GREEN")
          (not (nil? (str/index-of target %1))) (colorize-string %1 "YELLOW")
          :else (colorize-string %1 "GRAY")) guess target))

(defn get-printed-len
  "Get printed length of a string also accounting for the spaces used"
  [vector]
  (+ (count vector) (dec (count vector))))

; Debugging e.g. thread-last
(def spy #(do (println "DEBUG:" %) %))

(defn print-keyboard [keyboard-keys colors]
  (doseq [key keyboard-keys]
    (as-> key x
      (map #(colorize-string % (get colors (keyword (str %)))) x)
      (clojure.string/join " " x)
      (util/print-centered x (get-printed-len key)))))

(defn evaluate-guess
  "Mark each char as either contained, contained and in the correct place or uncontained"
  [guess target]
  [(= guess target)
   (reduce str (get-letter-matches guess target))])

; Work on this next -> Show proper coloring (Also mark used but uncontained chars)
(defn create-guess-color-map
  "Create map containing updated values for one guess"
  [guess target]
  (reduce (fn [result [guess-char target-char]]
            (if (= guess-char target-char)
              (assoc result (keyword (str guess-char)) "GREEN")
              result))
          {}
          (map vector (util/upper guess) (util/upper target))))

(defn update-keyboard-key-colors
  "Update the colors of the keyboard keys"
  [keyboard-key-colors guess target]
  (merge keyboard-key-colors (create-guess-color-map guess target)))

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

(defn print-past-guesses
  "Prints all the past guesses in order to be able to be able to infer the target word"
  [past-guesses]
  ; Normally, transform functions return a lazy sequence which can't produce side-effects.
  ; If the purpose of the function is to have side-effects, we need to eagerly evaluate the transformation.
  ; dorun -> returns nil (enough for us), doall -> returns the collection

  ; Use map-indexed here
  ; Try to use the get-print-len function instead of 11 here
  (dorun (map #(util/print-centered (format "%s [%d/6]" %1 %2) 11) past-guesses (iterate inc 1))))

; Pass keyboard dict as well
(defn start-round
  "Take one guess and evaluate it's correctness"
  [target past-guesses round won colors]
  (if (and (< round 6) (not won))
    (do
      (when (not (= 0 (count past-guesses)))
        (print-past-guesses past-guesses))
      (println)
      (print-keyboard keyboard-keys colors)
      (print "Please enter a word: ")
      (flush)
      (let [guess (util/lower (read-line))]
        (if (valid? guess)
          (let [[won colorized-guess] (evaluate-guess guess target)]
            (start-round target (conj past-guesses colorized-guess) (inc round) won (update-keyboard-key-colors colors guess target)))
          (do (println "Invalid word, please try again. The word should have consist out of 5 letters.")
              (start-round target past-guesses round won colors)))))
    (if (boolean won)
      (do
        (print-past-guesses past-guesses)
        (println)
        (print-keyboard keyboard-keys keyboard-key-colors)
        (println)
        (util/print-centered "Congratulations! You guessed the word!"))
      (do (println)
          (util/print-centered (format "Game over! You were not able to guess the word '%s'", target))))))

(defn get-random-word
  "Get a random word from the wordlist"
  [word-list]
  (rand-nth word-list))

(defn start-game
  "Play a game of wordle. A game consists out of 6 rounds."
  [path]
  (let [word-list (get-word-list-from-file path)
        target (get-random-word word-list)
        test {:a 1 :b 2}]
    (println (get test :a))
    (println (create-guess-color-map "hallo" "maple"))
    (println target)
    (start-round target [] 0 false keyboard-key-colors)))

(defn -main
  "I don't do a whole lot."
  [& args]
  (case (first args)
    "play" (-> (last args)
               start-game)
    "help" (println (colorize-string "Usage:\nplay wordlist\nhelp" "YELLOW"))
    (println (colorize-string "Invalid commands, use help to display information." "YELLOW"))))