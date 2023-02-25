(ns clodl.core
  (:require [clojure.string :as str]))

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

(defn print-text-in-color
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
      (= %1 %2) (print-text-in-color %1 "GREEN")
      (not (nil? (str/index-of target %1))) (print-text-in-color %1 "YELLOW")
      :else (print-text-in-color %1)) guess target))

(defn evaluate-guess
  "Mark each char as either contained, contained and in the correct place or uncontained"
  [guess target]
  [(= guess target)
   (reduce str (get-letter-matches guess target))])

(defn start-round
  "Take one guess and evaluate it's correctness"
  [target, past-guesses, round, won]
  (if (and (< round 6) (not won))
    (do
      (print "Please enter a word: ")
      (flush)
      (let [guess (read-line)]
        (if (valid? guess)
          (let [[won colorized-guess] (evaluate-guess guess target)]
            (println colorized-guess)
            (start-round target (conj past-guesses colorized-guess) (inc round) won))
          (do (println "Invalid word, please try again. The word should have consist out of 5 letters.")
              (start-round target past-guesses round won)))))
    (if (boolean won)
      (println "Congratulations! You guessed the word!")
      (println "Game over! You were not able to guess the word 'INSERT WORD HERE'"))))

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
    (println target)
    (start-round target [] 0 false)))

(defn -main
  "I don't do a whole lot."
  [& args]
  (case (first args)
    "play" (-> (last args)
               start-game)
    "help" (println (print-text-in-color "Usage:\nplay wordlist\nhelp" "YELLOW"))
    (println (print-text-in-color "Invalid commands, use help to display information." "YELLOW"))))