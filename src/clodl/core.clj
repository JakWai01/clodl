(ns clodl.core
  (:require [clojure.string :as str]))

(defn get-word-list-from-file
  "Reads the wordlist from the file"
  [path]
  (str/split (slurp path) #"\n"))

(defn evaluate-guess
  "Mark each char as either contained, contained and in the correct place or uncontained"
  [chosen-word guess]
  (println (reduce str
                   (map
                    (fn [guess-char, chosen-word-char]
                      (if (= guess-char chosen-word-char)
                        (str "\u001B[32m" guess-char "\u001B[0m")
                        (if (not (nil? (str/index-of chosen-word guess-char)))
                          (str "\u001B[33m" guess-char "\u001B[0m")
                          (str guess-char))))
                    (seq guess)
                    (seq chosen-word))))
  (= guess chosen-word))

(defn valid?
  "Check that the guess is made out of valid chars and is exactly five characters long"
  [guess]
  (and
   (= (count guess) 5)
   (every? #(Character/isLetter %) guess)))

(defn start-round
  "Take one guess and evaluate it's correctness"
  [chosen-word]
  (print "Please enter a word: ")
  (flush)
  (let [guess (read-line)]
    (if (valid? guess)
      (evaluate-guess chosen-word guess)
      (start-round chosen-word))))

(defn print-color
  "Print the following text in the specified color"
  [color]
  ())

(defn start-game
  "Play a game of wordle. A game consists out of 6 rounds."
  [path]
  (let [word-list (get-word-list-from-file path)
        chosen-word (rand-nth word-list)]
    (println chosen-word)
    (loop [x 0]
      (when (< x 6)
        (if (= (start-round chosen-word) false)
          (recur (+ x 1))
          (println "You gessed the word!"))))))

(defn -main
  "I don't do a whole lot."
  [& args]
  (case (first args)
    "play" (-> (last args)
               start-game)
    "help" (println "\u001B[33mUsage:\ngenerate filename bitsize\nencrypt keyfile message\ndecrypt keyfile cipher\u001B[0m")
    (println "\u001B[33m invalid commands, use help to display information.\u001B[0m")))