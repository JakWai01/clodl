(ns clodl.core (:require [clodl.util :as util]
                         [clodl.validator :as validator]
                         [clodl.keyboard :as keyboard]
                         [clojure.string :as str]))

(def ^:private ^:const keyboard-key-colors {:A nil :B nil :C nil :D nil :E nil :F nil :G nil :H nil :I nil :J nil :K nil :L nil :M nil
                          :N nil :O nil :P nil :Q nil :R nil :S nil :T nil :U nil :V nil :W nil :X nil :Y nil :Z nil})

(defn get-letter-matches
  "Charwise comparison and colorization"
  [guess target]
  (let [cap-guess (str/upper-case guess)
        cap-target (str/upper-case target)]
    (map #(cond
            (= %1 %2) (util/colorize-string %1 "GREEN_BOLD")
            (not (nil? (str/index-of cap-target %1))) (util/colorize-string %1 "YELLOW_BOLD")
            :else (util/colorize-string %1 "GRAY_BOLD")) cap-guess cap-target)))

(defn evaluate-guess
  "Mark each char as either contained, contained and in the correct place or uncontained"
  [guess target]
  [(= guess target)
   (reduce str
           (get-letter-matches guess target))])

(defn get-word-list-from-file
  "Reads the wordlist from the file"
  [path]
  (str/split (slurp path) #"\n"))

(defn print-past-guesses
  "Prints all the past guesses in order to be able to be able to infer the target word"
  [past-guesses]
  (dorun
   (map-indexed
    #(util/print-centered (format "%s [%d/6]" %2 (inc %1)) 11)
    past-guesses)))

(defn print-board-state
  "Print the current board state"
  [past-guesses colors]
  (util/clear-screen)
  (when
   (not
    (= 0 (count past-guesses)))
    (print-past-guesses past-guesses))
  (println)
  (keyboard/print-keyboard colors)
  (println))

(defn start-round
  "Take one guess and evaluate it's correctness"
  [target past-guesses round won colors word-list]
  (print-board-state past-guesses colors)
  (if (and (< round 6) (not won))
    (do
      (print "Please enter a word: ")
      (flush)
      (let [guess (str/lower-case (read-line))]
        (try
          (validator/bind (validator/valid? guess word-list)
            (fn [result]
              (let [[won colorized-guess] (evaluate-guess result target)]
                (start-round target (conj past-guesses colorized-guess) (inc round) won (keyboard/update-keyboard-key-colors colors guess target) word-list))))
          (catch Exception _
            (println "Invalid word, please try again. The word should consist out of exactly 5 letters.")
            (start-round target past-guesses round won colors word-list))))
    )
    (if (boolean won)
      (util/print-centered "Congratulations! You guessed the word!\n")
      (do (util/print-centered (format "Game over! You were not able to guess the word '%s'\n", target))
          (util/print-centered "Better luck next time!")))))

(defn start-game
  "Play a game of wordle. A game consists out of 6 rounds."
  [path]
  (let [word-list (get-word-list-from-file path)
        target (util/get-random-word word-list)]
    (start-round target [] 0 false keyboard-key-colors word-list)))

(defn print-help
  "Todo"
  [accent-color]
  (println (util/colorize-string "Clodl - Wordle in the terminal\n" accent-color))
  (println "It is required to provide the COLUMNS shell variable\nin order to provide a seamless user experience.\n")
  (println "COLUMNS=$COLUMNS clodl [COMMAND] ... [WORDLIST]\n")
  (println "Usage: \n")
  (println "\t" (util/colorize-string "play" accent-color) "\t Play a game of clodl")
  (println "\t" (util/colorize-string "help" accent-color) "\t Display this very page\n")
  (println "Examples:\n")
  (println "\tCOLUMNS=$COLUMNS clodl play words.txt")
  (println "\tclodl help"))

(defn -main
  "I don't do a whole lot."
  [& args]
  (case (first args)
    "play" (-> (last args)
               start-game)
    "help" (print-help "GREEN")
    (println (util/colorize-string "Invalid commands, use help to display information." "YELLOW"))))
