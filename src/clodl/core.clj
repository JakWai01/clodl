(ns clodl.core
  (:require [clojure.string :as str]))

; TODO: Use thread-first thread-last
; TODO: Implement this using a macro so the list is loaded at compile-time
(defn get-word-list-from-file
  "Reads the wordlist from the file"
  [path]
  (str/split (slurp path) #"\n"))

(defn start-round
  "Take one guess and evaluate it's correctness"
  [chosen-word]
  (print "Please enter a word: ")
  (flush)
  (let [guess (read-line)]
    ; Call function to evaluate current distance here
    (= guess chosen-word)))

(defn evaluate-guess
  "Mark each char as either contained, contained and in the correct place or uncontained"
  [chosen-word guess])

(defn start-game
  "Play a game of wordle. A game consists out of 6 rounds."
  [path]
  (let [word-list (get-word-list-from-file path)
        chosen-word (rand-nth word-list)]
    (println chosen-word)
    (loop [x 0]
      (when (< x 6) (if (= (start-round chosen-word) false) (recur (+ x 1)) (println "You guessed the word!"))))))
     ; (if (= (start-round chosen-word) false) (when (< x 6) (recur (+ x 1))) (println "You gessed the word!")))))

; TODO: Do we really need to accept a list of arguments or are 2 sufficient?
; TODO: Implement wordle solver as well
(defn -main
  "I don't do a whole lot."
  [& args]
  (case (first args)
    "play" (-> (last args)
               start-game)
    "help" (println "\u001B[33mUsage:\ngenerate filename bitsize\nencrypt keyfile message\ndecrypt keyfile cipher\u001B[0m")
    (println "\u001B[33m invalid commands, use help to display information.\u001B[0m")))

; Wordle flow
; 1. Read wordlist at compiletime
; 2. Choose word at random
; 3. Prompt user for guess
; 4. Show guess
; 5. After 5-6 guesses, terminate
; 6. Allow to restart
; Store played game / stats to file

; For each character, check
; Check if word is correct
; if the character is contained but in the wrong position
; if the character is contained and in the correct position
; if the character is contained
; and mark those cases with a color